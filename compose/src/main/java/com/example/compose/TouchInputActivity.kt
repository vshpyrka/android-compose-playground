package com.example.compose

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
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
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.TransformOrigin
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
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
            TransformableModifierGesture()
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
