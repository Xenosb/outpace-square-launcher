package io.qt.demo.outpace.square.launcher.ui.dock

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import io.qt.demo.outpace.square.launcher.R
import io.qt.demo.outpace.square.launcher.ui.theme.DockIconPressed
import io.qt.demo.outpace.square.launcher.ui.theme.DockTemperatureDecrease
import io.qt.demo.outpace.square.launcher.ui.theme.DockTemperatureIncrease
import io.qt.demo.outpace.square.launcher.ui.theme.MyApplicationTheme

private val MenuButtonSize = 46.21.dp
private val MenuButtonGap = 37.dp

private val TemperatureControlWidth = 175.6.dp
private val TemperatureControlHeight = 46.21.dp
private val TemperatureArrowTouchSize = 32.35.dp
private val TemperatureArrowIconSize = DpSize(11.4.dp, 18.48.dp)
private val TemperatureArrowInset = 2.31.dp

@Composable
fun DockBar(
    driverTemperature: String = "21.0 °",
    passengerTemperature: String = "21.0 °",
    onCarSettingsClick: () -> Unit = {},
    onHomeClick: () -> Unit = {},
    onAppsClick: () -> Unit = {},
    onDecreaseDriverTemperature: () -> Unit = {},
    onIncreaseDriverTemperature: () -> Unit = {},
    onDecreasePassengerTemperature: () -> Unit = {},
    onIncreasePassengerTemperature: () -> Unit = {},
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .wrapContentHeight()
    ) {
        TemperatureControl(
            temperature = driverTemperature,
            onDecrease = onDecreaseDriverTemperature,
            onIncrease = onIncreaseDriverTemperature,
            modifier = Modifier
                .align(Alignment.CenterStart)
                .padding(start = 13.dp)
        )

        Row(
            horizontalArrangement = Arrangement.spacedBy(MenuButtonGap),
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.align(Alignment.Center)
        ) {
            DockMenuButton(
                painter = painterResource(R.drawable.ic_dock_car_settings),
                contentDescription = stringResource(R.string.dock_car_settings),
                iconSize = DpSize(34.24.dp, 21.95.dp),
                onClick = onCarSettingsClick
            )
            DockMenuButton(
                painter = painterResource(R.drawable.ic_dock_home),
                contentDescription = stringResource(R.string.dock_home),
                iconSize = DpSize(26.57.dp, 27.15.dp),
                onClick = onHomeClick
            )
            DockMenuButton(
                painter = painterResource(R.drawable.ic_dock_apps),
                contentDescription = stringResource(R.string.dock_apps),
                iconSize = DpSize(27.73.dp, 27.73.dp),
                onClick = onAppsClick
            )
        }

        TemperatureControl(
            temperature = passengerTemperature,
            onDecrease = onDecreasePassengerTemperature,
            onIncrease = onIncreasePassengerTemperature,
            modifier = Modifier
                .align(Alignment.CenterEnd)
                .padding(end = 18.4.dp)
        )
    }
}

@Composable
private fun DockMenuButton(
    painter: Painter,
    contentDescription: String,
    iconSize: DpSize,
    onClick: () -> Unit,
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()

    Box(
        modifier = Modifier
            .size(MenuButtonSize)
            .clickable(
                interactionSource = interactionSource,
                indication = null,
                onClick = onClick
            ),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            painter = painter,
            contentDescription = contentDescription,
            tint = if (isPressed) DockIconPressed else Color.White,
            modifier = Modifier.size(iconSize)
        )
    }
}

@Composable
private fun TemperatureControl(
    temperature: String,
    onDecrease: () -> Unit,
    onIncrease: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier.size(TemperatureControlWidth, TemperatureControlHeight)
    ) {
        TemperatureArrow(
            painter = painterResource(R.drawable.ic_dock_chevron),
            contentDescription = stringResource(R.string.dock_decrease_temperature),
            tint = DockTemperatureDecrease,
            onClick = onDecrease,
            mirrored = false,
            modifier = Modifier
                .align(Alignment.CenterStart)
                .offset(x = TemperatureArrowInset)
        )

        Text(
            text = temperature,
            color = Color.White,
            fontSize = 27.73.sp,
            fontWeight = FontWeight.Medium,
            letterSpacing = 4.sp,
            modifier = Modifier.align(Alignment.Center)
        )

        TemperatureArrow(
            painter = painterResource(R.drawable.ic_dock_chevron),
            contentDescription = stringResource(R.string.dock_increase_temperature),
            tint = DockTemperatureIncrease,
            onClick = onIncrease,
            mirrored = true,
            modifier = Modifier
                .align(Alignment.CenterEnd)
                .offset(x = -TemperatureArrowInset)
        )
    }
}

@Composable
private fun TemperatureArrow(
    painter: Painter,
    contentDescription: String,
    tint: Color,
    onClick: () -> Unit,
    mirrored: Boolean,
    modifier: Modifier = Modifier,
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()

    Box(
        modifier = modifier
            .size(TemperatureArrowTouchSize)
            .clickable(
                interactionSource = interactionSource,
                indication = null,
                onClick = onClick
            ),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            painter = painter,
            contentDescription = contentDescription,
            tint = if (isPressed) tint.copy(alpha = 0.6f) else tint,
            modifier = Modifier
                .size(TemperatureArrowIconSize)
                .scale(scaleX = if (mirrored) -1f else 1f, scaleY = 1f)
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF000000, widthDp = 1280, heightDp = 800)
@Composable
private fun DockBarPreview() {
    MyApplicationTheme {
        Box(modifier = Modifier.fillMaxSize()) {
            DockBar(modifier = Modifier.align(Alignment.BottomCenter))
        }
    }
}
