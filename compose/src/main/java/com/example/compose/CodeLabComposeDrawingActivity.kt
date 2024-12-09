package com.example.compose

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.geometry.center
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.ColorMatrix
import androidx.compose.ui.graphics.CompositingStrategy
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Fill
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.clipPath
import androidx.compose.ui.graphics.drawscope.clipRect
import androidx.compose.ui.graphics.drawscope.inset
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.graphics.drawscope.scale
import androidx.compose.ui.graphics.drawscope.translate
import androidx.compose.ui.graphics.drawscope.withTransform
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.layer.drawLayer
import androidx.compose.ui.graphics.rememberGraphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.datasource.LoremIpsum
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.unit.toSize
import com.example.compose.ui.theme.AndroidPlaygroundTheme
import com.example.compose.ui.theme.Purple40
import kotlinx.coroutines.launch

class CodeLabComposeDrawingActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            AndroidPlaygroundTheme {
                FadingEdgeDemo(Modifier) {
                    AvatarsExample()
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

@Composable
private fun FlashLightKeyholeEffect() {
    var pointerOffset by remember {
        mutableStateOf(
            Offset(0f, 0f)
        )
    }
    Column(
        Modifier
            .fillMaxSize()
            .pointerInput("dragging") {
                detectDragGestures { change, dragAmount ->
                    pointerOffset += dragAmount
                }
            }
            .onSizeChanged {
                pointerOffset = Offset(it.width / 2f, it.height / 2f)
            }
            .drawWithContent {
                drawContent()
                drawRect(
                    Brush.radialGradient(
                        listOf(Color.Transparent, Color.Black),
                        center = pointerOffset,
                        radius = 100.dp.toPx()
                    )
                )
            }
    ) {
        Image(
            modifier = Modifier.fillMaxSize(),
            painter = painterResource(R.drawable.fc3_stress_and_anxiety),
            contentDescription = null,
            contentScale = ContentScale.Crop,
        )
    }
}

@Preview
@Composable
private fun FlashLightKeyholeEffectPreview() {
    AndroidPlaygroundTheme {
        FlashLightKeyholeEffect()
    }
}

@Composable
private fun DrawBlendMode() {
    Image(
        painter = painterResource(R.drawable.fc1_short_mantras),
        contentDescription = "Water",
        contentScale = ContentScale.Crop,
        modifier = Modifier
            .size(120.dp)
            .aspectRatio(1f)
            .background(
                Brush.linearGradient(
                    listOf(
                        Color(0xFFC5E1A5),
                        Color(0xFF80DEEA),
                    )
                ),
            )
            .padding(8.dp)
            .graphicsLayer {
                compositingStrategy = CompositingStrategy.Offscreen
            }
            .drawWithCache {
                val path = Path()
                path.addOval(
                    Rect(
                        topLeft = Offset.Zero,
                        bottomRight = Offset(size.width, size.height)
                    )
                )
                onDrawWithContent {
                    clipPath(path) {
                        this@onDrawWithContent.drawContent()
                    }
                    val dotSize = size.width / 8f
                    drawCircle(
                        color = Color.Black,
                        radius = dotSize,
                        center = Offset(
                            x = size.width - dotSize,
                            y = size.height - dotSize,
                        ),
                        blendMode = BlendMode.Clear,
                    )
                    drawCircle(
                        color = Color(0xFFEF5350),
                        radius = dotSize * 0.8f,
                        center = Offset(
                            x = size.width - dotSize,
                            y = size.height - dotSize,
                        )
                    )
                }
            }
    )
}

@Preview
@Composable
private fun DrawBlendModePreview() {
    AndroidPlaygroundTheme {
        DrawBlendMode()
    }
}

@Composable
fun CompositingStrategyExamples() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .wrapContentSize(Alignment.Center)
    ) {
        // Does not clip content even with a graphics layer usage here. By default, graphicsLayer
        // does not allocate + rasterize content into a separate layer but instead is used
        // for isolation. That is draw invalidations made outside of this graphicsLayer will not
        // re-record the drawing instructions in this composable as they have not changed
        Canvas(
            modifier = Modifier
                .graphicsLayer()
                .size(100.dp) // Note size of 100 dp here
                .border(2.dp, color = Color.Blue)
        ) {
            // ... and drawing a size of 200 dp here outside the bounds
            drawRect(color = Color.Magenta, size = Size(200.dp.toPx(), 200.dp.toPx()))
        }

        Spacer(modifier = Modifier.size(300.dp))

        /* Clips content as alpha usage here creates an offscreen buffer to rasterize content
        into first then draws to the original destination */
        Canvas(
            modifier = Modifier
                // force to an offscreen buffer
                .graphicsLayer(compositingStrategy = CompositingStrategy.Offscreen)
                .size(100.dp) // Note size of 100 dp here
                .border(2.dp, color = Color.Blue)
        ) {
            /* ... and drawing a size of 200 dp. However, because of the CompositingStrategy.Offscreen usage above, the
            content gets clipped */
            drawRect(color = Color.Red, size = Size(200.dp.toPx(), 200.dp.toPx()))
        }
    }
}

@Preview
@Composable
private fun CompositingStrategyExamplesPreview() {
    AndroidPlaygroundTheme {
        CompositingStrategyExamples()
    }
}

@Composable
private fun CompositingStrategyModulateAlpha() {
    val Purple = Color(0xFF7E57C2)
    val Yellow = Color(0xFFFFCA28)
    val Red = Color(0xFFEF5350)

    fun DrawScope.drawSquares() {

        val size = Size(100.dp.toPx(), 100.dp.toPx())
        drawRect(color = Red, size = size)
        drawRect(
            color = Purple, size = size,
            topLeft = Offset(size.width / 4f, size.height / 4f)
        )
        drawRect(
            color = Yellow, size = size,
            topLeft = Offset(size.width / 4f * 2f, size.height / 4f * 2f)
        )
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(32.dp)
    ) {
        // Base drawing, no alpha applied
        Canvas(
            modifier = Modifier.size(200.dp)
        ) {
            drawSquares()
        }

        Spacer(modifier = Modifier.size(36.dp))

        // Alpha 0.5f applied to whole composable
        Canvas(
            modifier = Modifier
                .size(200.dp)
                .graphicsLayer {
                    alpha = 0.5f
                }
        ) {
            drawSquares()
        }
        Spacer(modifier = Modifier.size(36.dp))

        // 0.75f alpha applied to each draw call when using ModulateAlpha
        Canvas(
            modifier = Modifier
                .size(200.dp)
                .graphicsLayer {
                    compositingStrategy = CompositingStrategy.ModulateAlpha
                    alpha = 0.75f
                }
        ) {
            drawSquares()
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun CompositingStrategyModulateAlphaPreview() {
    AndroidPlaygroundTheme {
        CompositingStrategyModulateAlpha()
    }
}

@Composable
private fun LayerToBitmap() {
    val coroutineScope = rememberCoroutineScope()
    val graphicsLayer = rememberGraphicsLayer()
    Box(
        modifier = Modifier
            .drawWithContent {
                graphicsLayer.record {
                    this@drawWithContent.drawContent()
                }
                drawLayer(graphicsLayer)
            }
            .clickable {
                coroutineScope.launch {
                    val bitmap = graphicsLayer.toImageBitmap()
                    println("AAA $bitmap")
                }
            }
    ) {
        Image(
            painter = painterResource(R.drawable.fc3_stress_and_anxiety),
            contentDescription = null,
            contentScale = ContentScale.Crop,
        )
    }
}

@Preview
@Composable
private fun LayerToBitmapPreview() {
    AndroidPlaygroundTheme {
        LayerToBitmap()
    }
}

private fun Modifier.blendMode(blendMode: BlendMode): Modifier {
    return this.drawWithCache {
        val graphicsLayer = obtainGraphicsLayer()
        graphicsLayer.apply {
            record {
                drawContent()
            }
            this.blendMode = blendMode
        }
        onDrawWithContent {
            drawLayer(graphicsLayer)
        }
    }
}

private fun Modifier.colorFilter(colorFilter: ColorFilter): Modifier {
    return this.drawWithCache {
        val graphicsLayer = obtainGraphicsLayer()
        graphicsLayer.apply {
            record {
                drawContent()
            }
            this.colorFilter = colorFilter
        }
        onDrawWithContent {
            drawLayer(graphicsLayer)
        }
    }
}

@Composable
private fun GraphicsLayerExample() {
    Box(
        modifier = Modifier.background(Color.Magenta),
        contentAlignment = Alignment.Center
    ) {

        Image(
            modifier = Modifier.fillMaxSize(),
            painter = painterResource(R.drawable.fc2_nature_meditations),
            contentDescription = null,
        )

        Text(
            color = Color.White,
            style = MaterialTheme.typography.headlineLarge,
            modifier = Modifier
                .blendMode(BlendMode.Clear),
            textAlign = TextAlign.Center,
            text = "Hello World",
        )
    }
}

@Composable
private fun GraphicsLayerExample2() {
    Box(
        contentAlignment = Alignment.Center
    ) {
        Image(
            modifier = Modifier
                .fillMaxSize()
                .colorFilter(
                    ColorFilter.colorMatrix(
                        ColorMatrix()
                            .apply {
                                setToSaturation(0f)
                            }
                    )
                ),
            painter = painterResource(R.drawable.fc2_nature_meditations),
            contentDescription = null,
        )

        Text(
            color = Color.White,
            style = MaterialTheme.typography.headlineLarge,
            modifier = Modifier
                .blendMode(BlendMode.Difference),
            textAlign = TextAlign.Center,
            text = "Hello World",
        )
    }
}

@Preview
@Composable
private fun GraphicsLayerExamplePreview() {
    AndroidPlaygroundTheme {
        GraphicsLayerExample()
    }
}

@Preview
@Composable
private fun GraphicsLayerExample2Preview() {
    AndroidPlaygroundTheme {
        GraphicsLayerExample2()
    }
}

@Composable
private fun Avatar(
    strokeWidth: Float,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit,
) {
    val stroke = remember(strokeWidth) {
        Stroke(width = strokeWidth)
    }
    Box(
        modifier = modifier
            .drawWithContent {
                drawContent()
                drawCircle(
                    color = Color.Black,
                    size.minDimension / 2,
                    size.center,
                    style = stroke,
                    blendMode = BlendMode.Clear,
                )
            }
            .clip(CircleShape)
    ) {
        content()
    }
}

@Composable
private fun AvatarsExample() {
    val avatars = listOf(
        R.drawable.fc1_short_mantras,
        R.drawable.fc2_nature_meditations,
        R.drawable.fc3_stress_and_anxiety,
        R.drawable.fc4_self_massage,
        R.drawable.fc5_overwhelmed,
        R.drawable.fc6_nightly_wind_down,
    )
    val size = 150.dp
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.Magenta)
            .graphicsLayer {
                compositingStrategy = CompositingStrategy.Offscreen
            }
    ) {
        var offset = 0.dp
        for (avatar in avatars) {
            Avatar(
                strokeWidth = 10.0f,
                modifier = Modifier
                    .size(150.dp)
                    .offset(offset)
            ) {
                Image(
                    painter = painterResource(id = avatar),
                    contentScale = ContentScale.Crop,
                    contentDescription = null,
                )
            }
            offset += size / 2
        }
    }
}

@Composable
private fun FadingEdgeDemo(
    modifier: Modifier,
    content: @Composable () -> Unit,
) {
    Box(
        modifier = modifier
            .graphicsLayer {
                compositingStrategy = CompositingStrategy.Offscreen
            }
            .drawWithContent {
                drawContent()
                drawRect(
                    brush = Brush.verticalGradient(
                        listOf(Color.Black, Color.Transparent),
                    ),
                    blendMode = BlendMode.DstIn
                )
            }
    ) {
        content()
    }
}

@Preview
@Composable
private fun AvatarExamplePreview() {
    AndroidPlaygroundTheme {
        AvatarsExample()
    }
}