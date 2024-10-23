package com.example.compose

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.ColorMatrix
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.ImageShader
import androidx.compose.ui.graphics.LinearGradientShader
import androidx.compose.ui.graphics.Shader
import androidx.compose.ui.graphics.ShaderBrush
import androidx.compose.ui.graphics.TileMode
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.compose.ui.theme.AndroidPlaygroundTheme

class ComposeBrushActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AndroidPlaygroundTheme {
                Column(
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    ImageBrushExample()
                    BrushShapeExample()
                    AnimatedShaderBackground()
                }
            }
        }
    }
}

@Composable
private fun ImageBrushExample() {
    val imageBrush = ShaderBrush(
        ImageShader(
            ImageBitmap.imageResource(R.drawable.fc5_overwhelmed)
        )
    )
    Text(
        "Hello Android!",
        style = TextStyle(
            brush = imageBrush,
            fontWeight = FontWeight.ExtraBold,
            fontSize = 36.sp
        )
    )
}

@Composable
private fun ImageBrushBorderExample() {
    val rainbowColorsBrush = remember {
        Brush.sweepGradient(
            listOf(
                Color(0xFF9575CD),
                Color(0xFFBA68C8),
                Color(0xFFE57373),
                Color(0xFFFFB74D),
                Color(0xFFFFF176),
                Color(0xFFAED581),
                Color(0xFF4DD0E1),
                Color(0xFF9575CD)
            )
        )
    }
    val borderWidth = 4.dp
    Image(
        painter = painterResource(id = R.drawable.fc5_overwhelmed),
        contentDescription = null,
        contentScale = ContentScale.Crop,
        modifier = Modifier
            .size(150.dp)
            .border(
                BorderStroke(borderWidth, rainbowColorsBrush),
                CircleShape
            )
            .padding(borderWidth)
            .clip(CircleShape)
    )
}

@Composable
private fun BrushShapeExample() {
    val imageBrush = ShaderBrush(
        ImageShader(
            ImageBitmap.imageResource(R.drawable.fc4_self_massage)
        )
    )
    Canvas(
        modifier = Modifier.size(150.dp),
    ) {
        drawCircle(imageBrush)
    }
}

@Composable
private fun AnimatedShaderBackground() {
    val infiniteTransition = rememberInfiniteTransition(label = "background")
    val targetOffset = with(LocalDensity.current) {
        1000.dp.toPx()
    }
    val offset by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = targetOffset,
        animationSpec = infiniteRepeatable(
            tween(
                5000,
                easing = LinearEasing
            ),
            repeatMode = RepeatMode.Reverse
        ),
        label = "offset"
    )
    val brushColors = listOf(Color.Yellow, Color.Magenta)
    Spacer(
        modifier = Modifier
            .height(280.dp)
            .fillMaxWidth()
            .blur(40.dp)
            .drawWithCache {
                val brushSize = 400f
                val brush = Brush.linearGradient(
                    colors = brushColors,
                    start = Offset(offset, offset),
                    end = Offset(
                        offset + brushSize,
                        offset + brushSize,
                    ),
                    tileMode = TileMode.Mirror
                )
                onDrawBehind {
                    drawRect(brush)
                }
            }
    )
}

@Composable
private fun ImageSaturationExample() {
    val infiniteTransition = rememberInfiniteTransition(label = "Saturation transition")
    val rotationAnimation = infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            tween(2000, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse,
        ),
        label = "Rotate animation"
    )
    Image(
        painter = painterResource(id = R.drawable.fc5_overwhelmed),
        contentDescription = null,
        colorFilter = ColorFilter.colorMatrix(
            ColorMatrix().apply {
                setToSaturation(rotationAnimation.value)
            }
        )
    )
}

@Composable
private fun SizedBrushExample() {
    val listColors = listOf(Color.Yellow, Color.Red, Color.Blue)
    val customBrush = remember {
        object : ShaderBrush() {
            override fun createShader(size: Size): Shader {
                return LinearGradientShader(
                    colors = listColors,
                    from = Offset.Zero,
                    to = Offset(size.width / 4f, 0f),
                    tileMode = TileMode.Mirror,
                )
            }

        }
    }
    Box(
        Modifier
            .requiredSize(200.dp)
            .background(customBrush))
}

@Preview
@Composable
private fun ImageBrushExamplePreview() {
    AndroidPlaygroundTheme {
        ImageBrushExample()
    }
}

@Preview
@Composable
private fun ImageBrushBorderExamplePreview() {
    AndroidPlaygroundTheme {
        ImageBrushBorderExample()
    }
}

@Preview
@Composable
private fun BrushShapeExamplePreview() {
    AndroidPlaygroundTheme {
        BrushShapeExample()
    }
}

@Preview
@Composable
private fun AnimatedShaderBackgroundPreview() {
    AndroidPlaygroundTheme {
        AnimatedShaderBackground()
    }
}

@Preview
@Composable
private fun ImageSaturationExamplePreview() {
    AndroidPlaygroundTheme {
        ImageSaturationExample()
    }
}

@Preview
@Composable
private fun SizedBrushExamplePreview() {
    AndroidPlaygroundTheme {
        SizedBrushExample()
    }
}