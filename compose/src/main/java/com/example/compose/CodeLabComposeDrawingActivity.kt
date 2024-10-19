package com.example.compose

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Fill
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.clipRect
import androidx.compose.ui.graphics.drawscope.inset
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.graphics.drawscope.scale
import androidx.compose.ui.graphics.drawscope.translate
import androidx.compose.ui.graphics.drawscope.withTransform
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.datasource.LoremIpsum
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.unit.toSize
import com.example.compose.ui.theme.AndroidPlaygroundTheme
import com.example.compose.ui.theme.Purple40

class CodeLabComposeDrawingActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            AndroidPlaygroundTheme {
                Column {
                    SimpleDrawSpacer(Modifier.size(150.dp))
                    SimpleDrawWithContent()
                    SimpleCanvasDraw()
                }
            }
        }
    }
}

@Preview(showSystemUi = true)
@Composable
private fun TextMeasurerExample() {
    AndroidPlaygroundTheme {
        MeasuredTextExample()
    }
}

@Composable
private fun MeasuredTextExample() {
    val textMeasurer = rememberTextMeasurer()
    Spacer(
        modifier = Modifier
            .drawWithCache {

                val measuredText = textMeasurer.measure(
                    LoremIpsum(40).values.first(),
                    constraints = Constraints.fixed(
                        width = (size.width * 2 / 3).toInt(),
                        height = (size.width * 2 / 3).toInt(),
                    ),
                    style = TextStyle(fontSize = 18.sp),
                    overflow = TextOverflow.Ellipsis,
                )

                onDrawBehind {
                    drawRect(Color.Magenta, size = measuredText.size.toSize())
                    drawText(measuredText)
                }
            }
            .fillMaxSize()
    )
}

@Preview(showBackground = true)
@Composable
private fun SimpleDrawSpacerPreview() {
    AndroidPlaygroundTheme {
        SimpleDrawSpacer(
            Modifier.fillMaxSize()
        )
    }
}

@Composable
fun SimpleDrawSpacer(
    modifier: Modifier = Modifier
) {
    Spacer(
        modifier = modifier
            .drawBehind {
                drawCircle(Color.Magenta)
            }
    )
}

@Preview(showBackground = true)
@Composable
private fun SimpleDrawWithContentPreview() {
    AndroidPlaygroundTheme {
        SimpleDrawWithContent()
    }
}

@Composable
fun SimpleDrawWithContent() {
    Column(
        modifier = Modifier.drawWithContent {
            rotate(45f) {
                // Helps to choose when the content should be drawn
                drawCircle(Color.Green)
                this@drawWithContent.drawContent()
            }
        }
    ) {
        Text(
            text = "Hello",
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.padding(16.dp)
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun SimpleCanvasDrawPreview() {
    AndroidPlaygroundTheme {
        SimpleCanvasDraw(Modifier.fillMaxSize())
    }
}

@Composable
fun SimpleCanvasDraw(
    modifier: Modifier = Modifier
) {
    Canvas(
        modifier = modifier,
        onDraw = {
            drawCircle(
                color = Color.Magenta,
                center = Offset(
                    20.dp.toPx(),
                    100.dp.toPx()
                ),
                radius = 60.dp.toPx()
            )
        }
    )
}

@Preview(showBackground = true)
@Composable
private fun ScaledCanvasPreview() {
    AndroidPlaygroundTheme {
        ScaledCanvas()
    }
}

@Composable
fun ScaledCanvas() {
    Canvas(modifier = Modifier.fillMaxSize()) {
        scale(scaleX = 10f, scaleY = 15f) {
            drawCircle(
                color = Color.Blue,
                radius = 20.dp.toPx()
            )
        }
        translate(
            left = 100f,
            top = -300f
        ) {
            drawCircle(
                color = Color.Red,
                radius = 200.dp.toPx()
            )
        }
        rotate(degrees = 45f) {
            drawRect(
                color = Color.Green,
                topLeft = Offset(
                    x = size.width / 3f,
                    y = size.height / 3f,
                ),
                size = size / 3f
            )
        }
        val quadrantSize = size / 2f
        inset(
            horizontal = 50f,
            vertical = 30f,
        ) {
            drawRect(
                color = Color.Yellow,
                size = quadrantSize,
            )
        }
        withTransform(
            transformBlock = {
                translate(left = size.width / 50f, top = size.height / 2)
                rotate(degrees = 45f)
            },
            drawBlock = {
                drawRect(
                    color = Color.Cyan,
                    size = size / 3f,
                )
            }
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun GraphLinePreview() {
    AndroidPlaygroundTheme {
        GraphLine(
            Modifier.fillMaxSize()
        )
    }
}

fun generatePath(size: Size): Path {
    val path = Path()
    path.moveTo(0f, size.height)
    path.lineTo(100f, size.height - 50)
    path.lineTo(150f, size.height - 20)
    path.lineTo(170f, size.height - 30)
    path.lineTo(size.width / 3, size.height - 300)
    path.lineTo(size.width / 2, size.height / 2)
    path.lineTo(3 * (size.width / 4), 3 * (size.height / 4))
    path.lineTo(size.width, 0f)
    return path
}

@Composable
fun GraphLine(
    modifier: Modifier = Modifier
) {
    Box(
        modifier.background(Purple40)
    ) {

        val animationProgress = remember {
            Animatable(0f)
        }

        LaunchedEffect(key1 = List(8) { "$it" }) {
            animationProgress.animateTo(1f, tween(3000))
        }

        Canvas(
            modifier = Modifier
                .padding(8.dp)
                .aspectRatio(3 / 2f)
                .fillMaxSize()
                .drawWithCache {
                    onDrawBehind {

                        val path = generatePath(size)

                        val filledPath = Path().apply {
                            addPath(path)
                            lineTo(size.width, size.height)
                            lineTo(0f, size.height)
                            close()
                        }

                        val brush = Brush.verticalGradient(
                            listOf(
                                Color.Green.copy(alpha = 0.4f),
                                Color.Transparent
                            )
                        )

                        onDrawBehind {
                            clipRect(
                                right = size.width * animationProgress.value
                            ) {
                                drawPath(path, Color.Green, style = Stroke(width = 2.dp.toPx()))
                                drawPath(filledPath, brush, style = Fill)
                            }
                        }
                    }
                }
        ) {
            val barWidthPx = 1.dp.toPx()
            drawRect(
                Color.LightGray,
                style = Stroke(width = barWidthPx)
            )

            // Draw vertical lines
            val verticalLines = 4
            val verticalSize = size.width / (verticalLines + 1)
            repeat(verticalLines) { i ->
                val startX = verticalSize * (i + 1)
                drawLine(
                    Color.LightGray,
                    start = Offset(startX, 0f),
                    end = Offset(startX, size.height),
                    strokeWidth = barWidthPx
                )
            }

            // Draw horizontal lines
            val horizontalLines = 3
            val horizontalSize = size.height / (horizontalLines + 1)
            repeat(verticalLines) { i ->
                val startY = horizontalSize * (i + 1)
                drawLine(
                    Color.LightGray,
                    start = Offset(0f, startY),
                    end = Offset(size.width, startY),
                    strokeWidth = barWidthPx
                )
            }
        }
    }
}
