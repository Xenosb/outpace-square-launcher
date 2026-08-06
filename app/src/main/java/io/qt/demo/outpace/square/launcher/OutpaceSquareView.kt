package io.qt.demo.outpace.square.launcher

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.content.pm.PackageManager
import android.graphics.PixelFormat
import android.os.Build
import android.os.IBinder
import android.util.Log
import android.view.Surface
import android.view.SurfaceHolder
import android.view.SurfaceView
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInteropFilter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import io.qt.demo.outpace.square.IRenderingService

private const val TAG = "OutpaceSquareView"
private const val TARGET_PACKAGE = "io.qt.demo.outpace.square"
private const val SERVICE_ACTION = "io.qt.demo.outpace.square.IRenderingService"
private const val CAR_VIEW_ID = "carView"

/**
 * Binds to the Outpace Square Qt app's RenderingService and renders its 3D
 * car view into a SurfaceView, forwarding touch input back to Qt.
 */
@OptIn(ExperimentalComposeUiApi::class)
@Suppress("DEPRECATION")
@Composable
fun OutpaceSquareView(
    modifier: Modifier = Modifier,
    onRenderingServiceChanged: (IRenderingService?) -> Unit = {},
) {
    val context = LocalContext.current
    val appContext = remember { context.applicationContext }

    var renderingService by remember { mutableStateOf<IRenderingService?>(null) }
    var currentSurface by remember { mutableStateOf<Surface?>(null) }

    val connection = remember {
        object : ServiceConnection {
            override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
                Log.d(TAG, "Service connected: $name")
                val svc = IRenderingService.Stub.asInterface(service)
                renderingService = svc
                onRenderingServiceChanged(svc)
                currentSurface?.let { surface ->
                    try {
                        svc.setSurface(surface, CAR_VIEW_ID)
                    } catch (e: Exception) {
                        Log.e(TAG, "Error setting surface", e)
                    }
                }
            }

            override fun onServiceDisconnected(name: ComponentName?) {
                Log.d(TAG, "Service disconnected: $name")
                renderingService = null
                onRenderingServiceChanged(null)
            }
        }
    }

    DisposableEffect(appContext) {
        val bindFlags = Context.BIND_AUTO_CREATE or 0x00001000 // BIND_INCLUDE_CAPABILITIES (API 34+)
        val intent = Intent(SERVICE_ACTION).apply { setPackage(TARGET_PACKAGE) }

        try {
            val bound = appContext.bindService(intent, connection, bindFlags)
            Log.d(TAG, "bindService result=$bound")
            if (!bound) {
                Log.e(TAG, "bindService returned false — is Outpace Square installed and running?")
            }
        } catch (e: PackageManager.NameNotFoundException) {
            Log.e(TAG, "Outpace Square package not found", e)
        } catch (e: Exception) {
            Log.e(TAG, "Failed to bind to Outpace Square rendering service", e)
        }

        onDispose {
            try {
                appContext.unbindService(connection)
            } catch (_: Exception) {
            }
        }
    }

    Box(modifier = modifier.fillMaxSize()) {
        AndroidView(
            factory = { ctx ->
                SurfaceView(ctx).apply {
                    setZOrderOnTop(false)
                    setZOrderMediaOverlay(false)
                    holder.setFormat(PixelFormat.OPAQUE)

                    holder.addCallback(object : SurfaceHolder.Callback {
                        override fun surfaceCreated(holder: SurfaceHolder) {
                            currentSurface = holder.surface
                            renderingService?.let { service ->
                                try {
                                    service.setSurface(holder.surface, CAR_VIEW_ID)
                                } catch (e: Exception) {
                                    Log.e(TAG, "Error setting surface", e)
                                }
                            }
                        }

                        override fun surfaceChanged(holder: SurfaceHolder, format: Int, width: Int, height: Int) {}

                        override fun surfaceDestroyed(holder: SurfaceHolder) {
                            renderingService?.let { service ->
                                try {
                                    service.unsetSurface(CAR_VIEW_ID)
                                } catch (_: Exception) {
                                }
                            }
                            currentSurface = null
                        }
                    })
                }
            },
            modifier = Modifier
                .fillMaxSize()
                .pointerInteropFilter { event ->
                    renderingService?.let { service ->
                        try {
                            service.motionEvent(event, CAR_VIEW_ID)
                        } catch (e: Exception) {
                            Log.e(TAG, "Error forwarding motion event", e)
                        }
                    }
                    true
                }
        )
    }
}
