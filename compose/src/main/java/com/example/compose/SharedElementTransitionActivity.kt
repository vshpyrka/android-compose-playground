package com.example.compose

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedContentScope
import androidx.compose.animation.EnterExitState
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionLayout
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.animation.core.animateDp
import androidx.compose.animation.core.spring
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.datasource.LoremIpsum
import androidx.compose.ui.unit.dp
import com.example.compose.ui.theme.AndroidPlaygroundTheme
import com.example.compose.ui.theme.Typography

class SharedElementTransitionActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            AndroidPlaygroundTheme {
                SharedElementTransitionExample()
            }
        }
    }
}

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
private fun SharedElementTransitionExample() {
    var showDetails by remember {
        mutableStateOf(false)
    }
    SharedTransitionLayout {
        // AnimatedContent
        // AnimatedVisibility
        // NavHost
        AnimatedContent(
            showDetails,
            label = "basic_transition"
        ) { targetState ->
            if (!targetState) {
                MainContent(
                    onShowDetails = {
                        showDetails = true
                    },
                    animatedVisibilityScope = this@AnimatedContent,
                    sharedTransitionScope = this@SharedTransitionLayout,
                )
            } else {
                DetailsContent(
                    onBack = {
                        showDetails = false
                    },
                    animatedVisibilityScope = this@AnimatedContent,
                    sharedTransitionScope = this@SharedTransitionLayout,
                )
            }
        }
    }
}

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
private fun MainContent(
    onShowDetails: () -> Unit,
    animatedVisibilityScope: AnimatedContentScope,
    sharedTransitionScope: SharedTransitionScope,
) {
    val roundedCornerAnimation by animatedVisibilityScope.transition
        .animateDp(label = "rounded_corners") {
            when (it) {
                EnterExitState.PreEnter -> 0.dp
                EnterExitState.Visible -> 16.dp
                EnterExitState.PostExit -> 16.dp
            }
        }
    with(sharedTransitionScope) {
        Card(
            colors = CardDefaults.cardColors().copy(
                containerColor = Color.Magenta,
            ),
            shape = RoundedCornerShape(16.dp),
            modifier = Modifier
                .sharedBounds(
                    sharedContentState = rememberSharedContentState(key = "bounds"),
                    animatedVisibilityScope = animatedVisibilityScope,
                    resizeMode = SharedTransitionScope.ResizeMode.RemeasureToBounds,
                    clipInOverlayDuringTransition = OverlayClip(
                        RoundedCornerShape(
                            roundedCornerAnimation
                        )
                    )
                )
                .clickable(onClick = onShowDetails)
        ) {
            Column(
                modifier = Modifier.padding(8.dp)
            ) {
                Image(
                    modifier = Modifier
                        .sharedElement(
                            state = rememberSharedContentState(key = "key_image"),
                            animatedVisibilityScope = animatedVisibilityScope,
                            boundsTransform = { initialRect, targetRect ->
                                spring(
                                    dampingRatio = 0.8f,
                                    stiffness = 380f,
                                )
                            }
                        )
                        .clip(CircleShape)
                        .size(100.dp),
                    painter = painterResource(R.drawable.fc3_stress_and_anxiety),
                    contentDescription = "Image",
                    contentScale = ContentScale.Crop,
                )
                Text(
                    modifier = Modifier.sharedBounds(
                        sharedContentState = rememberSharedContentState(key = "title"),
                        animatedVisibilityScope = animatedVisibilityScope,
                    ),
                    text = "Cupcake",
                    style = Typography.titleMedium.copy(Color.White),
                )
                Text(
                    modifier = Modifier.sharedBounds(
                        sharedContentState = rememberSharedContentState(key = "subtitle"),
                        animatedVisibilityScope = animatedVisibilityScope,
                    ),
                    text = "Text",
                    style = Typography.labelMedium.copy(Color.White),
                )
            }
        }
    }
}

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
private fun DetailsContent(
    onBack: () -> Unit,
    animatedVisibilityScope: AnimatedContentScope,
    sharedTransitionScope: SharedTransitionScope,
) {
    val roundedCornerAnimation by animatedVisibilityScope.transition
        .animateDp(label = "rounded_corners") {
            when (it) {
                EnterExitState.PreEnter -> 16.dp
                EnterExitState.Visible -> 0.dp
                EnterExitState.PostExit -> 0.dp
            }
        }
    with(sharedTransitionScope) {
        Column(
            modifier = Modifier
                .sharedBounds(
                    sharedContentState = rememberSharedContentState(
                        key = "bounds",
                    ),
                    animatedVisibilityScope = animatedVisibilityScope,
                    resizeMode = SharedTransitionScope.ResizeMode.RemeasureToBounds,
                    clipInOverlayDuringTransition = OverlayClip(
                        RoundedCornerShape(
                            roundedCornerAnimation
                        )
                    )
                )
                .fillMaxSize()
                .background(Color.Magenta)
                .clickable(onClick = onBack)
                .padding(16.dp),
        ) {
            Image(
                modifier = Modifier
                    .sharedElement(
                        state = rememberSharedContentState(key = "key_image"),
                        animatedVisibilityScope = animatedVisibilityScope,
                        boundsTransform = { initialRect, targetRect ->
                            spring(
                                dampingRatio = 0.8f,
                                stiffness = 380f,
                            )
                        }
                    )
                    .clip(CircleShape)
                    .size(300.dp),
                painter = painterResource(R.drawable.fc3_stress_and_anxiety),
                contentDescription = "Image",
                contentScale = ContentScale.Crop,
            )

            Text(
                modifier = Modifier.sharedBounds(
                    sharedContentState = rememberSharedContentState(key = "title"),
                    animatedVisibilityScope = animatedVisibilityScope,
                ),
                text = "Cupcake",
                style = Typography.titleMedium.copy(Color.White),
            )
            Text(
                modifier = Modifier.sharedBounds(
                    sharedContentState = rememberSharedContentState(key = "subtitle"),
                    animatedVisibilityScope = animatedVisibilityScope,
                ),
                text = "Text",
                style = Typography.bodyMedium.copy(Color.White),
            )

            Text(
                // Don't shrink text live while animating layout bounds
                modifier = Modifier.skipToLookaheadSize(),
                text = LoremIpsum(50).values.joinToString(),
                style = Typography.labelMedium.copy(Color.White),
            )
        }
    }
}

@Preview(showSystemUi = true)
@Composable
private fun SharedElementTransitionExamplePreview() {
    AndroidPlaygroundTheme {
        SharedElementTransitionExample()
    }
}
