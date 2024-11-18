package com.example.compose

import android.app.Activity
import android.os.Bundle
import android.view.KeyEvent
import android.view.KeyboardShortcutGroup
import android.view.KeyboardShortcutInfo
import android.view.Menu
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.core.animateDp
import androidx.compose.animation.core.updateTransition
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.focusGroup
import androidx.compose.foundation.focusable
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.awaitEachGesture
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.gestures.calculateCentroid
import androidx.compose.foundation.gestures.calculateCentroidSize
import androidx.compose.foundation.gestures.calculateRotation
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.gestures.draggable
import androidx.compose.foundation.gestures.rememberDraggableState
import androidx.compose.foundation.gestures.rememberScrollableState
import androidx.compose.foundation.gestures.rememberTransformableState
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.gestures.transformable
import androidx.compose.foundation.gestures.waitForUpOrCancellation
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Done
import androidx.compose.material3.Button
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusProperties
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.KeyEventType
import androidx.compose.ui.input.key.isAltPressed
import androidx.compose.ui.input.key.isCtrlPressed
import androidx.compose.ui.input.key.isMetaPressed
import androidx.compose.ui.input.key.isShiftPressed
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.onKeyEvent
import androidx.compose.ui.input.key.onPreviewKeyEvent
import androidx.compose.ui.input.key.type
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.util.fastAny
import com.example.compose.ui.theme.AndroidPlaygroundTheme
import kotlin.math.roundToInt

class TouchInputActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Column {
                ChangeFocusAdvancingDirection()
            }
        }
    }

    override fun onProvideKeyboardShortcuts(
        data: MutableList<KeyboardShortcutGroup>?,
        menu: Menu?,
        deviceId: Int
    ) {
        super.onProvideKeyboardShortcuts(data, menu, deviceId)
        val cursorMovement = KeyboardShortcutGroup(
            "Cursor movement",
            listOf(
                KeyboardShortcutInfo("Up", KeyEvent.KEYCODE_P, KeyEvent.META_CTRL_ON),
                KeyboardShortcutInfo("Down", KeyEvent.KEYCODE_N, KeyEvent.META_CTRL_ON),
                KeyboardShortcutInfo("Forward", KeyEvent.KEYCODE_F, KeyEvent.META_CTRL_ON),
                KeyboardShortcutInfo("Backward", KeyEvent.KEYCODE_B, KeyEvent.META_CTRL_ON),
            )
        )

        val messageEdit = KeyboardShortcutGroup(
            "Message editing",
            listOf(
                KeyboardShortcutInfo("Select All", KeyEvent.KEYCODE_A, KeyEvent.META_CTRL_ON),
                KeyboardShortcutInfo(
                    "Send a message",
                    KeyEvent.KEYCODE_ENTER,
                    KeyEvent.META_SHIFT_ON
                )
            )
        )

        data?.add(cursorMovement)
        data?.add(messageEdit)
    }

}

@Composable
private fun ChangeFocusAdvancingDirection() {
    var text by remember { mutableStateOf("") }
    val focusManager = LocalFocusManager.current

    TextField(
        value = text,
        onValueChange = { text = it },
        modifier = Modifier
            .onPreviewKeyEvent {
                if (it.type == KeyEventType.KeyUp && it.key == Key.Tab) {
                    focusManager.moveFocus(FocusDirection.Next)
                    true
                } else {
                    false
                }
            }
    )

    TextField(
        value = text,
        onValueChange = { text = it }
    )
}

enum class FilterChip(val text: String) {
    NONE(""),
    ALL("All"),
    CANDIES("Candies"),
    CHOCOLATE("Chocolate"),
    PASTRIES("Pastries"),
}

@Composable
private fun FocusBehaviorExample() {
    val photos = listOf(
        R.drawable.fc1_short_mantras,
        R.drawable.fc2_nature_meditations,
        R.drawable.fc3_stress_and_anxiety,
        R.drawable.fc4_self_massage,
        R.drawable.fc5_overwhelmed,
        R.drawable.fc6_nightly_wind_down,
    )
    var selected by remember { mutableStateOf(FilterChip.NONE) }

    LazyVerticalGrid(
        columns = GridCells.Fixed(4),
        state = rememberLazyGridState(),
        verticalArrangement = Arrangement.spacedBy(3.dp),
        horizontalArrangement = Arrangement.spacedBy(3.dp),
    ) {
        item(
            span = { GridItemSpan(maxLineSpan) }
        ) {
            Row(
                modifier = Modifier
                    .focusGroup()
            ) {
                for (i in FilterChip.entries.drop(1)) {
                    TopFilterChip(
                        text = i.text,
                        selected = selected == i,
                        onSelectionChange = {
                            selected = i
                        }
                    )
                }
            }
        }
        items(photos, key = { it }) {

            var focused by remember { mutableStateOf(false) }
            val transition = updateTransition(focused, label = "selected")
            val padding by transition.animateDp(label = "padding") { isFocused ->
                if (isFocused) 10.dp else 0.dp
            }
            val roundedCornerShape by transition.animateDp(label = "corner") { isFocused ->
                if (isFocused) 16.dp else 0.dp
            }

            Image(
                modifier = Modifier
                    .fillMaxSize()
                    .aspectRatio(1f)
                    .padding(padding)
                    .clip(RoundedCornerShape(roundedCornerShape))
                    .onFocusChanged { focusState -> focused = focusState.isFocused }
                    .focusable(),
                painter = painterResource(it),
                contentDescription = null,
                contentScale = ContentScale.Crop
            )
        }
    }
}

@Composable
private fun TopFilterChip(
    text: String,
    selected: Boolean,
    onSelectionChange: () -> Unit,
) {
    FilterChip(
        modifier = Modifier.widthIn(150.dp),
        onClick = {
            onSelectionChange()
        },
        label = {
            Text(text)
        },
        border = null,
        selected = selected,
        leadingIcon = if (selected) {
            {
                Icon(
                    imageVector = Icons.Filled.Done,
                    contentDescription = "Done icon",
                    modifier = Modifier.size(FilterChipDefaults.IconSize)
                )
            }
        } else {
            null
        },
    )
}

@Composable
private fun OneDimensionalTraversalFocusOrder() {
    val (first, second, third, fourth) = remember { FocusRequester.createRefs() }

    Column {
        Row {
            TextButton(
                {},
                Modifier
                    .focusRequester(first)
                    .focusProperties { next = second }
            ) {
                Text("First field")
            }
            TextButton(
                {},
                Modifier
                    .focusRequester(third)
                    .focusProperties { next = fourth }
            ) {
                Text("Third field")
            }
        }

        Row {
            TextButton(
                {},
                Modifier
                    .focusRequester(second)
                    .focusProperties { next = third }
            ) {
                Text("Second field")
            }
            TextButton(
                {},
                Modifier
                    .focusRequester(fourth)
                    .focusProperties {
                        next = first
                    }
            ) {
                Text("Fourth field")
            }
        }
    }
}

@Composable
private fun KeyboardShortcutsExample() {
    val activity = LocalContext.current as Activity
    Button(
        onClick = {
            activity.requestShowKeyboardShortcuts()
        }
    ) {
        Text(text = "Show keyboard shortcuts")
    }
}

@Composable
private fun KeyboardInputExample() {
    Column(
        modifier = Modifier
            .onPreviewKeyEvent {
                val isAltPressed = it.isAltPressed
                val isCtrlPressed = it.isCtrlPressed
                val isMetaPressed = it.isMetaPressed
                val isShiftPressed = it.isShiftPressed
                println(
                    "AAA isAltPressed=$isAltPressed " +
                            "isCtrlPressed=$isCtrlPressed " +
                            "isMetaPressed=$isMetaPressed " +
                            "isShiftPressed=$isShiftPressed"
                )

                if (it.key == Key.S) {
                    println("AAA parent handled preview key event")
                    true
                } else {
                    false
                }
            }
            .onKeyEvent {
                println("AAA parent received key event first")
                false
            }
    ) {
        Box(
            modifier = Modifier
                .focusable()
                .onPreviewKeyEvent {
                    println("AAA child received preview key event")
                    false
                }
                .onKeyEvent {
                    println("AAA child handled key event after parent")
                    true
                }
        ) {
            Text("Press any key")
        }
    }
}

@Composable
private fun TransformableModifierGesture() {
    var scale by remember { mutableFloatStateOf(1f) }
    var rotation by remember { mutableFloatStateOf(0f) }
    var offset by remember { mutableStateOf(Offset.Zero) }
    val state = rememberTransformableState { zoomChange, panChange, rotationChange ->
        scale *= zoomChange
        rotation += rotationChange
        offset += panChange
    }
    Box(
        modifier = Modifier
            .graphicsLayer {
                scaleX = scale
                scaleY = scale
                rotationZ = rotation
                translationX = offset.x
                translationY = offset.y
            }
            .transformable(state)
            .background(Color.Magenta)
            .fillMaxSize()
    )
}

@Composable
private fun DraggableGesture() {
    Box(Modifier.fillMaxSize()) {
        var offsetX by remember { mutableFloatStateOf(0f) }
        var offsetY by remember { mutableFloatStateOf(0f) }
        Box(
            modifier = Modifier
                .offset {
                    IntOffset(
                        offsetX.roundToInt(),
                        offsetY.roundToInt(),
                    )
                }
                .size(150.dp)
                .background(Color.Magenta)
                .pointerInput("Draggable") {
                    detectDragGestures { change, dragAmount ->
                        change.consume()
                        offsetX += dragAmount.x
                        offsetY += dragAmount.y
                    }
                }
        )
    }
}

@Composable
private fun DraggableModifierExample() {
    var offsetX by remember { mutableFloatStateOf(0f) }
    Box(
        modifier = Modifier
            .offset { IntOffset(offsetX.roundToInt(), 0) }
            .size(150.dp)
            .background(Color.Magenta)
            .draggable(
                state = rememberDraggableState { delta ->
                    offsetX += delta
                },
                orientation = Orientation.Horizontal
            ),
    )
}

@Preview
@Composable
private fun TouchInputPreview() {
    AndroidPlaygroundTheme {
        ScrollableSample()
    }
}

@Composable
private fun ScrollableSample() {
    // actual composable state
    var offset by remember { mutableFloatStateOf(0f) }
    Box(
        Modifier
            .size(150.dp)
            .scrollable(
                orientation = Orientation.Vertical,
                // Scrollable state: describes how to consume
                // scrolling delta and update offset
                state = rememberScrollableState { delta ->
                    offset += delta
                    delta
                }
            )
            .background(Color.LightGray),
        contentAlignment = Alignment.Center
    ) {
        Text(offset.toString())
    }
}


@Composable
private fun DrawCircleInCenterGesture() {
    var centroidSize by remember { mutableFloatStateOf(0f) }
    var position by remember { mutableStateOf(Offset.Zero) }
    Box(
        Modifier
            .drawBehind {
                // Draw a circle where the gesture is
                drawCircle(Color.Blue, centroidSize, center = position)
            }
            .pointerInput(Unit) {
                awaitEachGesture {
                    awaitFirstDown().also {
                        position = it.position
                    }
                    do {
                        val event = awaitPointerEvent()
                        val size = event.calculateCentroidSize()
                        if (size != 0f) {
                            centroidSize = event.calculateCentroidSize()
                        }
                        val centroid = event.calculateCentroid()
                        if (centroid != Offset.Unspecified) {
                            position = centroid
                        }
                    } while (event.changes.any { it.pressed })
                }
            }
            .fillMaxSize()
    )
}

@Composable
private fun RotationGesture() {
    var angle by remember { mutableFloatStateOf(30f) }
    Column {
        Box(
            modifier = Modifier
                .graphicsLayer {
                    rotationZ = angle
                }
                .size(200.dp)
                .background(Color.Magenta)
                .pointerInput("input") {
                    awaitEachGesture {
                        awaitFirstDown()
                        do {
                            val event = awaitPointerEvent()
                            val rotation = event.calculateRotation()
                            angle += rotation
                        } while (event.changes.fastAny { it.pressed })
                    }
                }
        )
    }
}

@Composable
private fun MultiTouchDetection() {
    Column {
        Box(
            modifier = Modifier
                .size(200.dp)
                .background(Color.Magenta)
                .pointerInput("input") {
                    awaitEachGesture {
                        val pointerChange = awaitFirstDown(requireUnconsumed = false)
                        println("AAA pointerChange = $pointerChange")
                        do {
                            val event = awaitPointerEvent()
                            val canceled = event.changes.fastAny { it.isConsumed }

                            println("AAA type ${event.type}")
                            println("AAA size ${event.changes.size}")
                            event.changes.forEach { change ->
                                println("AAA $change")
                                // Tell composable parents that event was already consumed
                                change.consume()
                            }
                        } while (!canceled && event.changes.fastAny { it.pressed })
                    }
                }
        )
    }
}

@Composable
private fun AllFingersUpDetection() {
    Column {
        Box(
            modifier = Modifier
                .size(200.dp)
                .background(Color.Magenta)
                .pointerInput("input") {
                    awaitPointerEventScope {
                        while (true) {
                            // Wait for all fingers to go up
                            val up = waitForUpOrCancellation()
                            println("AAA $up")
                        }
                    }
                }
        )
    }
}

@Composable
private fun PointerDownDetection() {
    Column {
        Box(
            modifier = Modifier
                .size(200.dp)
                .background(Color.Magenta)
                .pointerInput("input") {
                    awaitEachGesture {
                        val down = awaitFirstDown()
                        down.consume()
                        println("AAA $down")
                        val up = waitForUpOrCancellation()
                        println("AAA $up")
                        if (up != null) {
                            up.consume()
                        }
                    }
                }
        )
    }
}

@Composable
private fun MultipleGesturesDetection() {
    Column {
        Box(
            modifier = Modifier
                .size(200.dp)
                .background(Color.Magenta)
                .pointerInput(Unit) {
                    detectTapGestures {
                        println("AAA Tap")
                    }
                }
                .pointerInput(Unit) {
                    detectDragGestures { change, amount ->
                        println("AAA dragging $change $amount")
                    }
                }
        )
    }
}

@Composable
private fun FullGesturesDetection() {
    Column {
        Box(
            modifier = Modifier
                .size(200.dp)
                .background(Color.Magenta)
                .pointerInput("input") {
                    detectTapGestures {
                        println("AAA Tap $it")
                    }
                    // detectDragGestures()
                    // detectTransformGestures()
                    // detectDragGesturesAfterLongPress()
                    // detectHorizontalDragGestures()
                    // detectVerticalDragGestures()
                }
        )
    }
}

@Composable
private fun LogPointerEvents() {
    Column {
        Box(
            modifier = Modifier
                .size(200.dp)
                .pointerInput("input") {
                    awaitPointerEventScope {
                        while (true) {
                            val event = awaitPointerEvent()
                            println("AAA type = ${event.type}")
                            println("AAA changes size = ${event.changes.size}")
                            event.changes.forEach {
                                println("AAA change = $it")
                            }
                        }
                    }
                }
                .background(Color.Magenta)
        )
    }
}
