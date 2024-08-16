package com.example.compose

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PageSize
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.dp
import androidx.compose.ui.util.lerp
import com.example.compose.ui.theme.AndroidPlaygroundTheme
import kotlinx.coroutines.launch
import kotlin.math.absoluteValue

class ComposePagerActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AndroidPlaygroundTheme {
                PhotoPager(modifier = Modifier.fillMaxSize())
            }
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun PhotoPager(modifier: Modifier = Modifier) {
    val photos = listOf(
        R.drawable.ab1_inversions,
        R.drawable.ab2_quick_yoga,
        R.drawable.ab3_stretching,
        R.drawable.ab4_tabata,
        R.drawable.ab5_hiit,
        R.drawable.ab6_pre_natal_yoga,
        R.drawable.fc1_short_mantras,
        R.drawable.fc2_nature_meditations,
        R.drawable.fc3_stress_and_anxiety,
        R.drawable.fc4_self_massage,
        R.drawable.fc5_overwhelmed,
        R.drawable.fc6_nightly_wind_down,
    )
    val state = rememberPagerState {
        photos.size
    }
    val pageSize = object : PageSize {
        override fun Density.calculateMainAxisPageSize(
            availableSpace: Int,
            pageSpacing: Int
        ): Int {
            return (availableSpace - 2 * pageSpacing) / 3
        }

    }
    Column(
        modifier = modifier,
    ) {
        HorizontalPager(
            state = state,
//            pageSize = pageSize,
            contentPadding = PaddingValues(horizontal = 36.dp)
        ) { page ->
            Card(
                modifier = Modifier.graphicsLayer {
                    val pageOffset =
                        ((state.currentPage - page) + state.currentPageOffsetFraction).absoluteValue
                    val fraction = 1f - pageOffset.coerceIn(0f, 1f)
                    alpha = lerp(
                        start = 0.5f,
                        stop = 1.0f,
                        fraction = fraction,
                    )
                    scaleX = lerp(
                        start = 0.9f,
                        stop = 1.0f,
                        fraction = fraction,
                    )
                    scaleY = lerp(
                        start = 0.9f,
                        stop = 1.0f,
                        fraction = fraction,
                    )
                }
            ) {
                Image(
                    modifier = Modifier.aspectRatio(1f),
                    painter = painterResource(photos[page]),
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                )
            }
        }
        Row(
            Modifier
                .wrapContentHeight()
                .fillMaxWidth()
                .padding(8.dp),
            horizontalArrangement = Arrangement.Center
        ) {
            repeat(photos.size) { iteration ->
                val circleModifier = Modifier
                    .clip(CircleShape)
                    .size(16.dp)
                    .background(
                        if (state.currentPage == iteration) {
                            Color.DarkGray
                        } else {
                            Color.LightGray
                        }
                    )
                Box(circleModifier)
            }
        }
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ) {
            val scope = rememberCoroutineScope()
            Button(
                onClick = {
                    scope.launch {
                        state.animateScrollToPage(0)
                    }
                }
            ) {
                Text("Start")
            }
            Button(
                onClick = {
                    scope.launch {
                        state.animateScrollToPage(photos.size)
                    }
                }
            ) {
                Text("End")
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun PhotoPagerPreview() {
    AndroidPlaygroundTheme {
        PhotoPager(modifier = Modifier.fillMaxSize())
    }
}
