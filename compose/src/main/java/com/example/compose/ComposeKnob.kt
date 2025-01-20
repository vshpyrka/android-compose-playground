package com.example.compose

import android.view.MotionEvent
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.input.pointer.pointerInteropFilter
import androidx.compose.ui.layout.boundsInWindow
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.compose.ui.theme.AndroidPlaygroundTheme
import kotlin.math.PI
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.sin

@Stable
class KnobState(defaultAngle: Float) {
    var rotation by mutableFloatStateOf(defaultAngle)
    var touchOffset by mutableStateOf(Offset(0f, 0f))
    var center by mutableStateOf(Offset(0f, 0f))
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun ComposeKnob(
    onValueChange: (Float) -> Unit,
    modifier: Modifier = Modifier,
    defaultAngle: Float = 0f,
) {
    val state = remember {
        KnobState(defaultAngle)
    }
    Image(
        modifier = modifier
            .fillMaxSize()
            .onGloballyPositioned {
                state.center = it.boundsInWindow().center
            }
            .drawWithContent {
                drawContent()
                drawLine(
                    color = Color.Magenta,
                    start = state.center,
                    end = state.touchOffset,
                    strokeWidth = 5.dp.toPx(),
                )
            }
            .pointerInteropFilter { event ->
                state.touchOffset = Offset(event.x, event.y)

                // Convert from radians to degrees
                val angle = -atan2(
                    x = state.center.x - event.x,
                    y = state.center.y - event.y,
                ) * (180 / PI) // == Math.toDegrees()
                    .toFloat()

                println("AAA $angle")

//                when (event.action) {
//                    MotionEvent.ACTION_DOWN,
//                    MotionEvent.ACTION_MOVE -> {
//                        if (angle !in -defaultAngle..defaultAngle) {
//                                val fixedAngle = if (angle in -180f..-defaultAngle) {
//                                    360f + angle
//                                } else {
//                                    angle
//                                }
//                            state.rotation = fixedAngle
//
//                            val percent = (fixedAngle - defaultAngle) / (360f - 2 * defaultAngle)
//                            onValueChange(percent)
//                            true
//                        } else false
//                    }
//                    else -> false
//                }
                true
            }
//            .rotate(state.rotation)
            .graphicsLayer {
                rotationZ = state.rotation
            },
        imageVector = ImageVector.vectorResource(R.drawable.ic_arrow),
        contentDescription = null,
    )
}

@Preview(showBackground = true)
@Composable
private fun ComposeKnobPreview() {
    var value by remember { mutableFloatStateOf(0f) }
    AndroidPlaygroundTheme {
        ComposeKnob(
            onValueChange = {
                value = it
            }
        )
    }
}

@Preview(showBackground = true)
@Composable
fun KnobDemo() {
    var knobValue by remember { mutableStateOf(0.5f) } // initial value

    Knob(
        value = knobValue,
        onValueChange = { newValue ->
            knobValue = newValue
        },
        modifier = Modifier.size(150.dp)
    )
}

@Composable
fun Knob(
    modifier: Modifier = Modifier,
    size: Dp = 100.dp,
    value: Float = 0f, // normalized value between 0 and 1
    onValueChange: (Float) -> Unit
) {
    var angle by remember { mutableStateOf(value * 270f - 135f) } // map value to angle (-135 to 135)

    Box(
        modifier = modifier
            .size(size)
            .pointerInput(Unit) {
                detectDragGestures { change, dragAmount ->
                    change.consume()
                    // Calculate the new angle based on touch movement
                    val newAngle = angle + dragAmount.x
                    angle = newAngle.coerceIn(-135f, 135f)
                    // Map the angle back to a normalized value
                    onValueChange((angle + 135f) / 270f)
                }
            }
    ) {
        Canvas(modifier = Modifier.size(size)) {
            drawKnobBase()
            drawKnobMarker(angle)
        }
    }
}

fun DrawScope.drawKnobBase() {
    drawCircle(
        color = Color.Gray,
        radius = size.minDimension / 2,
        style = Stroke(width = 8f)
    )
}

fun DrawScope.drawKnobMarker(angle: Float) {
    val radius = size.minDimension / 2 - 20f
    val radians = Math.toRadians(angle.toDouble())
    val markerX = center.x + radius * cos(radians).toFloat()
    val markerY = center.y + radius * sin(radians).toFloat()

    drawLine(
        color = Color.Black,
        start = center,
        end = Offset(markerX, markerY),
        strokeWidth = 8f
    )
}


@Composable
fun Knob(
    value: Float,
    onValueChange: (Float) -> Unit,
    modifier: Modifier = Modifier
) {
    var angle by remember { mutableStateOf(value * 360f - 180f) } // Map value to degrees

    val sweepAngle = 300f // Angle for the knob's sweep

    Canvas(
        modifier = modifier.pointerInput(Unit) {
            detectDragGestures { change, dragOffset ->
                val newAngle = angle + dragOffset.y * 0.1f
                angle = newAngle.coerceIn(0f, 360f)
                onValueChange(angle / 360f + 0.5f)
            }
        }
    ) {
        // Draw the background circle
        drawCircle(
            color = Color.Gray,
            radius = size.minDimension / 2f
        )

        // Draw the indicator line
        drawLine(
            start = center,
            end = center + Offset(
                cos(angle * PI / 180f).toFloat() * size.minDimension / 2f,
                sin(angle * PI / 180f).toFloat() * size.minDimension / 2f
            ),
            color = Color.Red,
            strokeWidth = 2f
        )

        // Draw the knob handle
        drawCircle(
            color = Color.Blue,
            radius = size.minDimension / 6f,
            center = center + Offset(
                cos(angle * PI / 180f).toFloat() * size.minDimension * 0.8f,
                sin(angle * PI / 180f).toFloat() * size.minDimension * 0.8f
            )
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun KnobExample2() {
    Knob(
        modifier = Modifier.size(300.dp),
        value = 0.5f,
        onValueChange = {}
    )
}