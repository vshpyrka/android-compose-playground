package com.example.compose.stylus

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.compose.stylus.StylusVisualization.drawOrientation
import com.example.compose.stylus.StylusVisualization.drawPressure
import com.example.compose.stylus.StylusVisualization.drawTilt

@Composable
fun StylusDrawComponent(
    viewModel: StylusViewModel = viewModel()
) {

    val fastRendering = remember {
        FastRenderer(viewModel)
    }

    val state by viewModel.stylusState.collectAsStateWithLifecycle()

    // A surface container using the 'background' color from the theme
    Surface(
        modifier = Modifier
            .fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Column {
            StylusVisualization(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(100.dp),
                stylusState = state,
            )
            Divider(
                thickness = 1.dp,
                color = Color.Black,
            )
            DrawAreaLowLatency(fastRendering = fastRendering)
        }
    }
}

@Composable
fun StylusVisualization(
    stylusState: StylusState,
    modifier: Modifier = Modifier,
) {
    Canvas(
        modifier = modifier
    ) {
        with(stylusState) {
            drawOrientation(this.orientation)
            drawTilt(this.tilt)
            drawPressure(this.pressure)
        }
    }
}

@Composable
fun DrawAreaLowLatency(
    modifier: Modifier = Modifier,
    fastRendering: FastRenderer,
) {
    AndroidView(factory = { context ->
        LowLatencySurfaceView(context, fastRenderer = fastRendering)
    }, modifier = modifier)
}
