package io.qt.demo.outpace.square.launcher

import android.os.Bundle

import java.util.Locale

import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview

import io.qt.demo.outpace.square.launcher.data.AirConditioningProperties
import io.qt.demo.outpace.square.launcher.ui.dock.DockBar
import io.qt.demo.outpace.square.launcher.ui.theme.MyApplicationTheme

import io.qt.demo.outpace.square.IRenderingService

private const val TemperatureStepCelsius = 0.5f

private fun formatTemperature(value: Float): String =
    String.format(Locale.US, "%.1f °", value)

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyApplicationTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    var airConditioningProperties by remember {
                        mutableStateOf(
                            AirConditioningProperties(
                                driverTemperature = 21.0f,
                                passengerTemperature = 21.0f
                            )
                        )
                    }

                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(Color.Black)
                            .padding(innerPadding)
                    ) {
                        OutpaceSquareView(modifier = Modifier.fillMaxSize())

                        DockBar(
                            driverTemperature = formatTemperature(airConditioningProperties.driverTemperature),
                            passengerTemperature = formatTemperature(airConditioningProperties.passengerTemperature),
                            onDecreaseDriverTemperature = {
                                airConditioningProperties = airConditioningProperties.copy(
                                    driverTemperature = airConditioningProperties.driverTemperature - TemperatureStepCelsius
                                )
                            },
                            onIncreaseDriverTemperature = {
                                airConditioningProperties = airConditioningProperties.copy(
                                    driverTemperature = airConditioningProperties.driverTemperature + TemperatureStepCelsius
                                )
                            },
                            onDecreasePassengerTemperature = {
                                airConditioningProperties = airConditioningProperties.copy(
                                    passengerTemperature = airConditioningProperties.passengerTemperature - TemperatureStepCelsius
                                )
                            },
                            onIncreasePassengerTemperature = {
                                airConditioningProperties = airConditioningProperties.copy(
                                    passengerTemperature = airConditioningProperties.passengerTemperature + TemperatureStepCelsius
                                )
                            },
                            modifier = Modifier
                                .align(Alignment.BottomCenter)
                                .navigationBarsPadding()
                        )
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF000000, widthDp = 1280, heightDp = 800)
@Composable
fun MainActivityPreview() {
    MyApplicationTheme {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black)
        ) {
            DockBar(modifier = Modifier.align(Alignment.BottomCenter))
        }
    }
}