package com.example.compose

import android.os.Bundle
import android.text.format.DateUtils
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Addchart
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.PlayCircle
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.datasource.LoremIpsum
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import com.example.compose.ui.theme.AndroidPlaygroundTheme
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.graphicsLayer

class NestedScrollingModifierActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            PodcastScreen()
        }
    }
}

@Composable
private fun PodcastScreen() {
    val minImageSize = 300f
    val maxImageSize = 500f
    val minAlpha = 0.3f
    val maxAlpha = 1f
    val maxOffset = 300f
    var currentImageSize by remember { mutableFloatStateOf(maxImageSize) }
    var currentImageAlpha by remember { mutableFloatStateOf(maxAlpha) }
    var currentFabOffset by remember { mutableIntStateOf(0) }
    val nestedScrollConnection = remember {
        object : NestedScrollConnection {
            override fun onPreScroll(available: Offset, source: NestedScrollSource): Offset {
                val delta = available.y.toInt()
                val newImageSize = currentImageSize + delta
                val previousImgSize = currentImageSize
                currentImageSize = newImageSize.coerceIn(minImageSize, maxImageSize)
                val consumed = currentImageSize - previousImgSize

                val sizeDelta = (currentImageSize - minImageSize) / (maxImageSize - minImageSize)
                currentImageAlpha = sizeDelta.coerceIn(minAlpha, maxAlpha)

                currentFabOffset = (sizeDelta * maxOffset).toInt()

                return Offset(0f, consumed)
            }
        }
    }

    Box(
        modifier = Modifier.nestedScroll(nestedScrollConnection)
    ) {
        Image(
            modifier = Modifier
                .align(Alignment.TopCenter)
                .size(currentImageSize.toInt().pxToDp())
                .graphicsLayer {
                    alpha = currentImageAlpha
                },
            painter = painterResource(R.drawable.ab4_tabata),
            contentDescription = null,
        )
        PodcastList(
            modifier = Modifier.offset {
                IntOffset(0, currentImageSize.toInt())
            }
        )
        PodcastFab(
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(16.dp)
                .offset {
                    IntOffset(currentFabOffset, 0)
                }
        )
    }
}

@Composable
private fun PodcastList(
    modifier: Modifier = Modifier,
) {
    LazyColumn(
        contentPadding = PaddingValues(vertical = 16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        modifier = modifier.padding(horizontal = 16.dp)
    ) {
        val strings = List(10) {
            LoremIpsum((5..20).random()).values.joinToString()
        }
        items(strings) {
            PodcastItem(it)
        }
    }
}

@Composable
private fun PodcastItem(
    text: String
) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = MaterialTheme.shapes.medium,
        color = Color(251, 236, 224),
    ) {
        Column(
            modifier = Modifier.padding(8.dp),
        ) {
            Text(
                text = text
            )

            Row {
                Icon(
                    modifier = Modifier.padding(8.dp),
                    imageVector = Icons.Default.PlayCircle,
                    contentDescription = null,
                )
                Text(
                    modifier = Modifier
                        .padding(8.dp)
                        .weight(1f),
                    text = DateUtils.getRelativeTimeSpanString(
                        System.currentTimeMillis()
                    ).toString()
                )
                Icon(
                    modifier = Modifier.padding(8.dp),
                    imageVector = Icons.Default.Addchart,
                    contentDescription = null,
                )
                Icon(
                    modifier = Modifier.padding(8.dp),
                    imageVector = Icons.Default.MoreVert,
                    contentDescription = null,
                )
            }
        }
    }
}

@Composable
private fun PodcastFab(
    modifier: Modifier = Modifier
) {
    FloatingActionButton(
        modifier = modifier,
        onClick = {}
    ) {
        Icon(
            Icons.Default.FavoriteBorder,
            contentDescription = null,
        )
    }
}


@Composable
fun Dp.dpToPx() = with(LocalDensity.current) { this@dpToPx.toPx() }

@Composable
fun Int.pxToDp() = with(LocalDensity.current) { this@pxToDp.toDp() }

@Preview(showBackground = true)
@Composable
private fun PodcastScreenPreview() {
    AndroidPlaygroundTheme {
        PodcastScreen()
    }
}
