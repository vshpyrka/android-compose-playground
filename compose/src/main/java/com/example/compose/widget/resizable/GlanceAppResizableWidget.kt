package com.example.compose.widget.resizable

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.glance.ColorFilter
import androidx.glance.GlanceId
import androidx.glance.GlanceModifier
import androidx.glance.GlanceTheme
import androidx.glance.Image
import androidx.glance.ImageProvider
import androidx.glance.LocalSize
import androidx.glance.action.clickable
import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.SizeMode
import androidx.glance.appwidget.components.CircleIconButton
import androidx.glance.appwidget.components.Scaffold
import androidx.glance.appwidget.components.TitleBar
import androidx.glance.appwidget.cornerRadius
import androidx.glance.appwidget.lazy.GridCells
import androidx.glance.appwidget.lazy.LazyColumn
import androidx.glance.appwidget.lazy.LazyVerticalGrid
import androidx.glance.appwidget.lazy.items
import androidx.glance.appwidget.provideContent
import androidx.glance.background
import androidx.glance.layout.Alignment
import androidx.glance.layout.Box
import androidx.glance.layout.Column
import androidx.glance.layout.Row
import androidx.glance.layout.fillMaxSize
import androidx.glance.layout.fillMaxWidth
import androidx.glance.layout.padding
import androidx.glance.layout.size
import androidx.glance.preview.ExperimentalGlancePreviewApi
import androidx.glance.semantics.semantics
import androidx.glance.semantics.testTag
import androidx.glance.text.FontWeight
import androidx.glance.text.Text
import androidx.glance.text.TextDefaults
import androidx.glance.text.TextStyle
import androidx.glance.unit.ColorProvider
import com.example.compose.R

private enum class SizeBucket { Narrow, Normal, Wide }

class GlanceAppResizableWidget : GlanceAppWidget() {

    override val sizeMode: SizeMode
        get() = SizeMode.Exact

    override suspend fun provideGlance(context: Context, id: GlanceId) {
        provideContent {
            val state by HomeControllerRepository.state.collectAsState()
            ResizableWidgetContent(state)
        }
    }

    override suspend fun onDelete(context: Context, glanceId: GlanceId) {
        super.onDelete(context, glanceId)
        println("AAA GlanceAppTinyWidget is Deleted")
    }

}

@Composable
private fun calculateSizeBucket(): SizeBucket {
    val size: DpSize = LocalSize.current
    val width = size.width

    return when {
        width <= 200.dp -> SizeBucket.Narrow
        width > 300.dp -> SizeBucket.Wide
        else -> SizeBucket.Normal
    }
}

@Composable
fun ResizableWidgetContent(state: List<HomeDeviceStatus>) {
    GlanceTheme {
        val sizeBucket = calculateSizeBucket()
        when (sizeBucket) {
            SizeBucket.Narrow -> {
                WidgetUiNarrow(
                    state = state,
                    titleBar = {
                        WidgetTitleBar(
                            showPowerOffButton = false
                        )
                    },
                    onDeviceToggle = { device ->
                        HomeControllerRepository.toggleDevice(device)
                    }
                )
            }

            SizeBucket.Normal -> {
                WidgetUiNormal(
                    state = state,
                    titleBar = {
                        WidgetTitleBar(
                            showPowerOffButton = false
                        )
                    },
                    onDeviceToggle = { device ->
                        HomeControllerRepository.toggleDevice(device)
                    }
                )
            }

            SizeBucket.Wide -> {
                WidgetUiWide(
                    state = state,
                    titleBar = {
                        WidgetTitleBar(
                            showPowerOffButton = true
                        )
                    },
                    onDeviceToggle = { device ->
                        HomeControllerRepository.toggleDevice(device)
                    }
                )
            }
        }
    }
}

@Composable
private fun WidgetTitleBar(
    showPowerOffButton: Boolean,
    modifier: GlanceModifier = GlanceModifier,
) {
    TitleBar(
        modifier = modifier,
        startIcon = ImageProvider(R.drawable.ic_home),
        iconColor = GlanceTheme.colors.primary,
        title = "Home",
        textColor = GlanceTheme.colors.onSurface,
        actions = {
            Row {
                WidgetTitleBarActionButton(
                    modifier = GlanceModifier
                        .semantics {
                            testTag = "refresh"
                        },
                    icon = R.drawable.ic_refresh,
                    onClick = {}
                )
                if (showPowerOffButton) {
                    WidgetTitleBarActionButton(
                        modifier = GlanceModifier
                            .semantics {
                                testTag = "power"
                            },
                        icon = R.drawable.ic_power,
                        onClick = {}
                    )
                }
            }
        }
    )
}

@Composable
private fun WidgetTitleBarActionButton(
    icon: Int,
    onClick: () -> Unit,
    modifier: GlanceModifier = GlanceModifier,
) {
    Box(
        modifier = modifier
            .size(48.dp)
            .clickable { onClick() }
            .padding(start = 2.dp),
        contentAlignment = Alignment.Center
    ) {
        Image(
            modifier = GlanceModifier.size(24.dp),
            provider = ImageProvider(icon),
            contentDescription = "",
            colorFilter = ColorFilter.tint(GlanceTheme.colors.onSurface)
        )
    }
}

@Composable
private fun WidgetUiNarrow(
    state: List<HomeDeviceStatus>,
    titleBar: @Composable () -> Unit,
    onDeviceToggle: (HomeDevice) -> Unit,
    modifier: GlanceModifier = GlanceModifier,
) {
    Scaffold(
        modifier = modifier.fillMaxSize(),
        titleBar = titleBar,
        backgroundColor = GlanceTheme.colors.widgetBackground,
    ) {
        val itemModifier = GlanceModifier
            .fillMaxWidth()
            .cornerRadius(8.dp)
            .padding(vertical = 16.dp)
        LazyColumn {
            items(state) { deviceStatus ->
                DeviceStatusSmall(
                    modifier = itemModifier,
                    deviceStatus = deviceStatus,
                    onClick = {
                        onDeviceToggle(deviceStatus.device)
                    }
                )
            }
        }
    }
}

@Composable
fun WidgetUiNormal(
    state: List<HomeDeviceStatus>,
    titleBar: @Composable () -> Unit,
    onDeviceToggle: (HomeDevice) -> Unit,
    modifier: GlanceModifier = GlanceModifier,
) {
    Scaffold(
        modifier = modifier.fillMaxSize(),
        titleBar = titleBar,
        backgroundColor = GlanceTheme.colors.widgetBackground,
    ) {
        LazyColumn {
            items(state) { deviceStatus ->
                DeviceStatusNormal(
                    deviceStatus = deviceStatus,
                    onClick = {
                        onDeviceToggle(deviceStatus.device)
                    }
                )
            }
        }
    }
}

@Composable
fun WidgetUiWide(
    state: List<HomeDeviceStatus>,
    titleBar: @Composable () -> Unit,
    onDeviceToggle: (HomeDevice) -> Unit,
    modifier: GlanceModifier = GlanceModifier,
) {
    Scaffold(
        modifier = modifier.fillMaxSize(),
        titleBar = titleBar,
        backgroundColor = GlanceTheme.colors.widgetBackground,
    ) {
        LazyVerticalGrid(
            gridCells = GridCells.Fixed(2)
        ) {
            items(state) { deviceStatus ->
                DeviceStatusWide(
                    deviceStatus = deviceStatus,
                    onClick = {
                        onDeviceToggle(deviceStatus.device)
                    }
                )
            }
        }
    }
}

@Composable
fun DeviceStatusSmall(
    deviceStatus: HomeDeviceStatus,
    onClick: () -> Unit,
    modifier: GlanceModifier = GlanceModifier,
) {
    Row(
        modifier = modifier
            .clickable { onClick() },
        verticalAlignment = Alignment.CenterVertically,
    ) {
        CircleIconButton(
            modifier = GlanceModifier.semantics {
                testTag = "device_round_icon"
            },
            imageProvider = ImageProvider(deviceStatus.device.getDeviceIcon()),
            contentDescription = null,
            onClick = onClick,
            backgroundColor = if (deviceStatus.isOn) {
                GlanceTheme.colors.primary
            } else {
                GlanceTheme.colors.secondaryContainer
            },
            contentColor = if (deviceStatus.isOn) {
                GlanceTheme.colors.onPrimary
            } else {
                GlanceTheme.colors.onSecondaryContainer
            },
        )
        Column(
            modifier = GlanceModifier.padding(start = 8.dp)
        ) {
            DeviceTitle(deviceStatus.device)
            DeviceStatus(deviceStatus.isOn)
        }
    }
}

@Composable
fun DeviceStatusNormal(
    deviceStatus: HomeDeviceStatus,
    onClick: () -> Unit,
    modifier: GlanceModifier = GlanceModifier,
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
    ) {
        Row(
            modifier = GlanceModifier
                .semantics {
                    testTag = "clickable_box"
                }
                .fillMaxWidth()
                .cornerRadius(16.dp)
                .padding(8.dp)
                .background(
                    if (deviceStatus.isOn) {
                        GlanceTheme.colors.primary
                    } else {
                        GlanceTheme.colors.secondaryContainer
                    },
                )
                .clickable { onClick() },
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Image(
                modifier = GlanceModifier.padding(horizontal = 8.dp),
                provider = ImageProvider(deviceStatus.device.getDeviceIcon()),
                contentDescription = null,
                colorFilter = ColorFilter.tint(
                    if (deviceStatus.isOn) {
                        GlanceTheme.colors.onPrimary
                    } else {
                        GlanceTheme.colors.onSecondaryContainer
                    }
                )
            )
            Column(
                modifier = GlanceModifier
                    .padding(
                        start = 8.dp,
                        top = 8.dp,
                    )
            ) {
                DeviceTitle(
                    device = deviceStatus.device,
                    color = if (deviceStatus.isOn) {
                        GlanceTheme.colors.onPrimary
                    } else {
                        GlanceTheme.colors.onSecondaryContainer
                    },
                )
                DeviceStatus(
                    isOn = deviceStatus.isOn,
                    color = if (deviceStatus.isOn) {
                        GlanceTheme.colors.onPrimary
                    } else {
                        GlanceTheme.colors.onSecondaryContainer
                    },
                )
            }
        }
    }
}

@Composable
fun DeviceStatusWide(
    deviceStatus: HomeDeviceStatus,
    onClick: () -> Unit,
    modifier: GlanceModifier = GlanceModifier,
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .padding(4.dp)
    ) {
        Column(
            modifier = GlanceModifier
                .semantics {
                    testTag = "clickable_box"
                }
                .fillMaxWidth()
                .cornerRadius(16.dp)
                .padding(horizontal = 8.dp, vertical = 16.dp)
                .background(
                    if (deviceStatus.isOn) {
                        GlanceTheme.colors.primary
                    } else {
                        GlanceTheme.colors.secondaryContainer
                    },
                )
                .clickable { onClick() },
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Image(
                modifier = GlanceModifier.padding(horizontal = 8.dp),
                provider = ImageProvider(deviceStatus.device.getDeviceIcon()),
                contentDescription = null,
                colorFilter = ColorFilter.tint(
                    if (deviceStatus.isOn) {
                        GlanceTheme.colors.onPrimary
                    } else {
                        GlanceTheme.colors.onSecondaryContainer
                    }
                )
            )
            Column(
                modifier = GlanceModifier
                    .padding(
                        start = 8.dp,
                        top = 8.dp,
                    )
            ) {
                DeviceTitle(
                    device = deviceStatus.device,
                    color = if (deviceStatus.isOn) {
                        GlanceTheme.colors.onPrimary
                    } else {
                        GlanceTheme.colors.onSecondaryContainer
                    },
                )
                DeviceStatus(
                    isOn = deviceStatus.isOn,
                    color = if (deviceStatus.isOn) {
                        GlanceTheme.colors.onPrimary
                    } else {
                        GlanceTheme.colors.onSecondaryContainer
                    },
                )
            }
        }
    }
}

@Composable
fun DeviceTitle(
    device: HomeDevice,
    modifier: GlanceModifier = GlanceModifier,
    color: ColorProvider = TextDefaults.defaultTextColor,
) {
    Text(
        modifier = modifier
            .semantics {
                testTag = "device_title"
            },
        text = device.getTitle(),
        style = TextStyle(
            color = color,
            fontWeight = FontWeight.Medium,
            fontSize = 16.sp,
        )
    )
}

@Composable
fun DeviceStatus(
    isOn: Boolean,
    modifier: GlanceModifier = GlanceModifier,
    color: ColorProvider = TextDefaults.defaultTextColor,
) {
    Text(
        modifier = modifier
            .semantics {
                testTag = "device_status"
            },
        text = if (isOn) "On" else "Off",
        style = TextStyle(
            color = color,
        )
    )
}

fun HomeDevice.getDeviceIcon(): Int {
    return when (this) {
        HomeDevice.LivingRoom -> R.drawable.ic_lightbulb
        HomeDevice.NurseryTV -> R.drawable.ic_tv
        HomeDevice.HallwayLights -> R.drawable.ic_lightbulb
        HomeDevice.FrontDoor -> R.drawable.ic_door
    }
}

fun HomeDevice.getTitle(): String {
    return when (this) {
        HomeDevice.LivingRoom -> "Living Room"
        HomeDevice.NurseryTV -> "Nursery TV"
        HomeDevice.HallwayLights -> "Hallway Lights"
        HomeDevice.FrontDoor -> "Front Door"
    }
}

@OptIn(ExperimentalGlancePreviewApi::class)
@androidx.glance.preview.Preview(widthDp = 170, heightDp = 250)
@Composable
private fun ResizableWidgetContentSmallPreview() {
    val devices = buildList {
        HomeDevice.entries.forEach {
            add(HomeDeviceStatus(device = it, isOn = false))
        }
    }
    ResizableWidgetContent(devices)
}

@OptIn(ExperimentalGlancePreviewApi::class)
@androidx.glance.preview.Preview(widthDp = 250, heightDp = 250)
@Composable
private fun ResizableWidgetContentSmallNormal() {
    val devices = buildList {
        HomeDevice.entries.forEach {
            add(HomeDeviceStatus(device = it, isOn = false))
        }
    }
    ResizableWidgetContent(devices)
}

@OptIn(ExperimentalGlancePreviewApi::class)
@androidx.glance.preview.Preview(widthDp = 350, heightDp = 350)
@Composable
private fun ResizableWidgetContentSmallLarge() {
    val devices = buildList {
        HomeDevice.entries.forEach {
            add(HomeDeviceStatus(device = it, isOn = false))
        }
    }
    ResizableWidgetContent(devices)
}
