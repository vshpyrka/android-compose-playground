package com.example.compose

import android.app.Activity
import android.content.ClipData
import android.content.ClipDescription
import android.graphics.DashPathEffect
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.PersistableBundle
import android.view.KeyEvent
import android.view.KeyboardShortcutGroup
import android.view.KeyboardShortcutInfo
import android.view.Menu
import android.view.MotionEvent
import android.view.View
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.core.animateDp
import androidx.compose.animation.core.updateTransition
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.content.MediaType
import androidx.compose.foundation.content.ReceiveContentListener
import androidx.compose.foundation.content.TransferableContent
import androidx.compose.foundation.content.consume
import androidx.compose.foundation.content.contentReceiver
import androidx.compose.foundation.content.hasMediaType
import androidx.compose.foundation.draganddrop.dragAndDropSource
import androidx.compose.foundation.draganddrop.dragAndDropTarget
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
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.handwriting.handwritingDetector
import androidx.compose.foundation.text.handwriting.handwritingHandler
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Done
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draganddrop.DragAndDropEvent
import androidx.compose.ui.draganddrop.DragAndDropTarget
import androidx.compose.ui.draganddrop.DragAndDropTransferData
import androidx.compose.ui.draganddrop.mimeTypes
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusProperties
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.toComposePathEffect
import androidx.compose.ui.graphics.vector.PathParser
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
import androidx.compose.ui.input.pointer.pointerInteropFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.ClipEntry
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalWindowInfo
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.datasource.LoremIpsum
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.em
import androidx.compose.ui.unit.sp
import androidx.compose.ui.util.fastAny
import androidx.compose.ui.window.Dialog
import coil.compose.AsyncImage
import com.example.compose.ui.theme.AndroidPlaygroundTheme
import kotlinx.coroutines.launch
import kotlin.math.roundToInt

class TouchInputActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Scaffold { paddings ->
                Column(Modifier.padding(paddings)) {
                    RichContentClipboardExample()
                }
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
                    detectTapGestures(
                        onDoubleTap = { println("AAA onDoubleTap $it") },
                        onLongPress = { println("AAA onLongPress $it") },
                        onPress = { println("AAA onPress $it") },
                        onTap = { println("AAA onTap $it") },
                    )
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

@OptIn(ExperimentalFoundationApi::class)
@Preview
@Composable
private fun DragAndDropExample() {

    val label = remember { "Drag me" }

    val color = Color.Blue

    Box(modifier = Modifier.fillMaxSize()) {
        Box(
            modifier = Modifier
                .size(150.dp)
                .align(Alignment.TopStart)
                .dragAndDropSource(
//                    drawDragDecoration = {
//                        drawCircle(color, 150.dp.toPx())
//                    },
                    block = {
                        detectTapGestures(
                            onLongPress = {
                                startTransfer(
                                    DragAndDropTransferData(
                                        clipData = ClipData.newPlainText(
                                            label,
                                            label
                                        ),
                                        flags = View.DRAG_FLAG_GLOBAL,
                                    )
                                )
                            }
                        )
                    }
                )
                .background(color)

        ) {
            Button(onClick = {}) {
                Text("Hello World")
            }
        }

        val callback = remember {
            object : DragAndDropTarget {
                override fun onStarted(event: DragAndDropEvent) {
                    // When the drag event starts
                    println("AAA onStarted $event")
                }

                override fun onEntered(event: DragAndDropEvent) {
                    // When the dragged object enters the target surface
                    println("AAA onEntered $event")
                }

                override fun onEnded(event: DragAndDropEvent) {
                    // When the drag event stops
                    println("AAA onEnded $event")
                }

                override fun onExited(event: DragAndDropEvent) {
                    // When the dragged object exits the target surface
                    println("AAA onExited $event")
                }

                override fun onDrop(event: DragAndDropEvent): Boolean {
                    // Parse received data
                    println("AAA onDrop $event")
                    return true
                }
            }
        }

        Box(
            modifier = Modifier
                .size(300.dp)
                .align(Alignment.BottomEnd)
                .background(Color.Green)
                .dragAndDropTarget(
                    shouldStartDragAndDrop = { event ->
                        event.mimeTypes().contains(ClipDescription.MIMETYPE_TEXT_PLAIN)
                    },
                    callback,
                )
        )
    }
}

@Composable
fun HandwritingDetectorSample() {
    var openDialog by remember { mutableStateOf(false) }
    val focusRequester = remember { FocusRequester() }

    Column(
        Modifier
            .imePadding()
            .requiredWidth(300.dp)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            "This is not an actual text field, but it is a handwriting detector so you can use " +
                    "a stylus to write here."
        )
        Spacer(Modifier.size(16.dp))
        Text(
            "Fake text field",
            Modifier
                .fillMaxWidth()
                .handwritingDetector { openDialog = !openDialog }
                .padding(4.dp)
                .border(
                    1.dp,
                    MaterialTheme.colorScheme.onSurface.copy(alpha = 0.3f),
                    RoundedCornerShape(4.dp)
                )
                .padding(16.dp),
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.3f)
        )
    }

    if (openDialog) {
        Dialog(onDismissRequest = { openDialog = false }) {
            Card(modifier = Modifier.width(300.dp), shape = RoundedCornerShape(16.dp)) {
                Column(
                    modifier = Modifier.padding(24.dp),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text("This text field is a handwriting handler.")
                    Spacer(Modifier.size(16.dp))
                    val state = remember { TextFieldState() }
                    BasicTextField(
                        state = state,
                        modifier =
                        Modifier
                            .fillMaxWidth()
                            .focusRequester(focusRequester)
                            .handwritingHandler(),
                        decorator = { innerTextField ->
                            Box(
                                Modifier
                                    .padding(4.dp)
                                    .border(
                                        1.dp,
                                        MaterialTheme.colorScheme.onSurface,
                                        RoundedCornerShape(4.dp)
                                    )
                                    .padding(16.dp)
                            ) {
                                innerTextField()
                            }
                        }
                    )
                }
            }

            val windowInfo = LocalWindowInfo.current
            LaunchedEffect(windowInfo) {
                snapshotFlow { windowInfo.isWindowFocused }
                    .collect { isWindowFocused ->
                        if (isWindowFocused) {
                            focusRequester.requestFocus()
                        }
                    }
            }
        }
    }
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
private fun StylusMotionEvent() {

    Box {
        var path by remember {
            mutableStateOf(Path())
        }

        var hoverOffset by remember {
            mutableStateOf(Offset.Unspecified)
        }

        var isStylus by remember { mutableStateOf(false) }
        var stylusAxisX by remember { mutableFloatStateOf(0f) }
        var stylusAxisY by remember { mutableFloatStateOf(0f) }
        var stylusPressure by remember { mutableFloatStateOf(0f) }
        var stylusOrientation by remember { mutableFloatStateOf(0f) }
        var stylusTilt by remember { mutableFloatStateOf(0f) }
        var stylusDistance by remember { mutableFloatStateOf(0f) }

        Canvas(
            modifier = Modifier
                .fillMaxSize()
                .pointerInteropFilter { event ->
                    isStylus = event.getToolType(0) == MotionEvent.TOOL_TYPE_STYLUS
                    stylusAxisX = event.getAxisValue(MotionEvent.AXIS_X)
                    stylusAxisY = event.getAxisValue(MotionEvent.AXIS_Y)
                    stylusPressure = event.getAxisValue(MotionEvent.AXIS_PRESSURE)
                    stylusOrientation = event.getAxisValue(MotionEvent.AXIS_ORIENTATION)
                    stylusTilt = event.getAxisValue(MotionEvent.AXIS_TILT)
                    stylusDistance = event.getAxisValue(MotionEvent.AXIS_DISTANCE)
//                    println("AAA x=$x y=$y pressure=$pressure orientation=$orientation tilt=$tilt distance=$distance")

                    println("AAA ${event.action}")
                    when (event.action) {
                        MotionEvent.ACTION_DOWN,
                        MotionEvent.ACTION_POINTER_DOWN -> {
                            println("AAA Down")
                            path.moveTo(event.x, event.y)
                        }

                        MotionEvent.ACTION_MOVE -> {
                            println("AAA Move pointerCount=${event.pointerCount}")
                            path.lineTo(event.x, event.y)
                        }

                        MotionEvent.ACTION_UP,
                        MotionEvent.ACTION_POINTER_UP -> {
                            println("AAA Up")
                            path = Path().apply { reset(); addPath(path) }
                        }

                        MotionEvent.ACTION_CANCEL -> {
                            println("AAA action cancel")
                        }

                        MotionEvent.FLAG_CANCELED -> {
                            println("AAA flag cancelled")
                        }

                        MotionEvent.ACTION_HOVER_ENTER,
                        MotionEvent.ACTION_HOVER_MOVE -> {
                            hoverOffset = Offset(event.x, event.y)
                        }

                        MotionEvent.ACTION_HOVER_EXIT -> {
                            hoverOffset = Offset.Unspecified
                        }
                    }

                    true
                }
//            .pointerInput(Unit) {
//                awaitEachGesture {
//                    while(true) {
//                        val event = awaitPointerEvent()
//                        event.changes.forEach {
//                            println("AAA $it")
//                        }
//                    }
//                }
//            }
        ) {
            println("AAA Draw")
            drawPath(
                path,
                brush = SolidColor(Color.Magenta),
                style = Stroke(width = 4.dp.toPx(), cap = StrokeCap.Round, join = StrokeJoin.Round)
            )

            if (hoverOffset != Offset.Unspecified) {
                drawCircle(
                    color = Color.Blue,
                    center = hoverOffset,
                    radius = 30.dp.toPx()
                )
            }
        }

        Column(
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(30.dp)
        ) {
            val text = "Stylus\n" +
                    "isStylus=$isStylus\n" +
                    "axisX=$stylusAxisX\n" +
                    "axisY=$stylusAxisY\n" +
                    "stylusPressure=$stylusPressure\n" +
                    "stylusOrientation=$stylusOrientation\n" +
                    "stylusTilt=$stylusTilt\n" +
                    "stylusDistance=$stylusDistance\n"
            Text(
                text = text,
                style = TextStyle(fontSize = 4.em)
            )
        }
    }
}

@Preview
@Composable
private fun TestPreview() {
    AndroidPlaygroundTheme {
        Canvas(
            Modifier.fillMaxSize()
        ) {
            val path = PathParser().parsePathString("M 0,0 500,0 500,500 0,500 z").toPath()
            drawPath(
                path,
                color = Color.Magenta,
                style = Stroke(
                    width = 10f,
                    pathEffect = DashPathEffect(
                        floatArrayOf(5f, 5f),
                        0f
                    ).toComposePathEffect()
                )
            )
        }
    }
}


@Composable
private fun SelectableTextContainer() {
    Card {
        Column {
            SelectionContainer {
                Text(
                    modifier = Modifier.padding(16.dp),
                    text = LoremIpsum(40).values.first()
                )
            }

            Text(
                modifier = Modifier.padding(16.dp),
                text = LoremIpsum(40).values.first()
            )
        }
    }
}

@Composable
private fun ClipboardExample() {
    val clipboardManager = LocalClipboardManager.current

    Button(
        onClick = {
            clipboardManager.setText(
                buildAnnotatedString {
                    append("Copied text")
                }
            )
        }
    ) {
        Text("Click to copy text to ClipboardManager")
    }
}

@Composable
private fun ClipboardEntryExample() {
    val clipboardManager = LocalClipboardManager.current

    Column {

        var text by remember { mutableStateOf("Default text") }

        Button(
            onClick = {
                val clipData = ClipData.newPlainText("text label", "Copied text")
                val clip = ClipEntry(clipData)
                clipboardManager.setClip(clip)
            }
        ) {
            Text("Click to copy text to ClipboardManager")
        }

        Button(
            onClick = {
                text = clipboardManager.getText().toString()
            }
        ) {
            Text("Paste copied text")
        }

        TextField(
            value = text,
            onValueChange = {}
        )
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun RichContentClipboardExample() {

    val clipboardManager = LocalClipboardManager.current
    val context = LocalContext.current

    var images by remember { mutableStateOf<List<Uri>>(emptyList()) }

    var dragging by remember { mutableStateOf(false) }
    var hovering by remember { mutableStateOf(false) }

    val scrollState = rememberScrollState()
    val coroutineScope = rememberCoroutineScope()

    val receiveContentListener = remember {
        object : ReceiveContentListener {

            override fun onDragStart() {
                super.onDragStart()
                dragging = true
            }

            override fun onDragEnd() {
                super.onDragEnd()
                hovering = false
                dragging = false
            }

            override fun onDragEnter() {
                super.onDragEnter()
                hovering = true
            }

            override fun onDragExit() {
                super.onDragExit()
                hovering = false
            }

            override fun onReceive(transferableContent: TransferableContent): TransferableContent? {
                return when {
                    transferableContent.hasMediaType(MediaType.Image) -> {
                        transferableContent.consume { item ->
                            val uri = item.uri
                            if (uri != null) {
                                images += uri

                                coroutineScope.launch {
                                    scrollState.animateScrollTo(scrollState.maxValue)
                                }
                            }
                            uri != null
                        }
                    }

                    else -> transferableContent
                }
            }
        }
    }

    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        Button(
            onClick = {
                val clipData = ClipData.newUri(
                    context.contentResolver,
                    "label",
                    Uri.parse("https://developer.android.com/static/images/design/ui/large-screens-banner_2880.png")
                )
                clipData.addItem(ClipData.Item(Uri.parse("https://developer.android.com/static/images/design/ui/promo-widgets_2880.png")))
                val clip = ClipEntry(clipData)
                clipboardManager.setClip(clip)
            }
        ) {
            Text("Click to copy text to ClipboardManager")
        }

        Button(
            onClick = {
                val clipData = ClipData
                    .newPlainText("password", "1234567890")
                    .apply {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                            description.extras = PersistableBundle().apply {
                                putBoolean(ClipDescription.EXTRA_IS_SENSITIVE, true)
                            }
                        } else {
                            description.extras = PersistableBundle().apply {
                                putBoolean("android.content.extra.IS_SENSITIVE", true)
                            }
                        }
                    }
                val clipEntry = ClipEntry(clipData)
                clipboardManager.setClip(clipEntry)
            }
        ) {
            Text("Copy password")
        }

        Row(
            modifier = Modifier
                .padding(bottom = 8.dp)
                .fillMaxWidth()
                .horizontalScroll(scrollState),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            images.forEach { uri ->
                AsyncImage(
                    model = uri,
                    contentDescription = null,
                    modifier = Modifier
                        .size(100.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .border(1.dp, Color.Gray, RoundedCornerShape(8.dp)),
                    contentScale = ContentScale.Crop
                )
            }
        }
        val state = rememberTextFieldState("")

        BasicTextField(
            modifier = Modifier
                .contentReceiver(receiveContentListener)
                .fillMaxWidth()
                .height(60.dp),
            state = state,
            textStyle = TextStyle(color = Color.Black, fontSize = 18.sp),
        )
    }
}
