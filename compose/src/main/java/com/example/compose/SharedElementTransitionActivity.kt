package com.example.compose

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedContentScope
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.EnterExitState
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionLayout
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.animation.core.animateDp
import androidx.compose.animation.core.spring
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
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
                            sharedContentState = rememberSharedContentState(key = "key_image"),
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
                        sharedContentState = rememberSharedContentState(key = "key_image"),
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

data class Snack(val name: String, val image: Int)

private val listSnacks = listOf(
    Snack("Cupcake", R.drawable.fc1_short_mantras),
    Snack("Donut", R.drawable.fc2_nature_meditations),
    Snack("Eclair", R.drawable.fc3_stress_and_anxiety),
    Snack("Froyo", R.drawable.fc4_self_massage),
    Snack("Gingerbread", R.drawable.fc5_overwhelmed),
    Snack("Honeycomb", R.drawable.fc6_nightly_wind_down),
)

private val shapeForSharedElement = RoundedCornerShape(16.dp)

@OptIn(ExperimentalSharedTransitionApi::class)
@Preview
@Composable
private fun AnimatedVisibilitySharedElementShortenedExample() {
    var selectedSnack by remember { mutableStateOf<Snack?>(null) }

    SharedTransitionLayout(modifier = Modifier.fillMaxSize()) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.LightGray.copy(alpha = 0.5f))
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(listSnacks) { snack ->
                AnimatedVisibility(
                    visible = snack != selectedSnack,
                    enter = fadeIn() + scaleIn(),
                    exit = fadeOut() + scaleOut(),
                    modifier = Modifier.animateItem()
                ) {
                    Box(
                        modifier = Modifier
                            .sharedBounds(
                                sharedContentState = rememberSharedContentState(key = "${snack.name}-bounds"),
                                animatedVisibilityScope = this,
                                clipInOverlayDuringTransition = OverlayClip(shapeForSharedElement)
                            )
                            .background(Color.White, shapeForSharedElement)
                            .clip(shapeForSharedElement)
                    ) {
                        SnackContents(
                            snack = snack,
                            modifier = Modifier.sharedElement(
                                sharedContentState = rememberSharedContentState(key = snack.name),
                                animatedVisibilityScope = this@AnimatedVisibility
                            ),
                            onClick = {
                                selectedSnack = snack
                            }
                        )
                    }
                }
            }
        }
        SnackEditDetails(
            snack = selectedSnack,
            onConfirmClick = {
                selectedSnack = null
            }
        )
    }
}

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun SharedTransitionScope.SnackEditDetails(
    snack: Snack?,
    modifier: Modifier = Modifier,
    onConfirmClick: () -> Unit
) {
    AnimatedContent(
        modifier = modifier,
        targetState = snack,
        transitionSpec = {
            fadeIn() togetherWith fadeOut()
        },
        label = "SnackEditDetails"
    ) { targetSnack ->
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            if (targetSnack != null) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .clickable {
                            onConfirmClick()
                        }
                        .background(Color.Black.copy(alpha = 0.5f))
                )
                Column(
                    modifier = Modifier
                        .padding(horizontal = 16.dp)
                        .sharedBounds(
                            sharedContentState = rememberSharedContentState(key = "${targetSnack.name}-bounds"),
                            animatedVisibilityScope = this@AnimatedContent,
                            clipInOverlayDuringTransition = OverlayClip(shapeForSharedElement)
                        )
                        .background(Color.White, shapeForSharedElement)
                        .clip(shapeForSharedElement)
                ) {

                    SnackContents(
                        snack = targetSnack,
                        modifier = Modifier.sharedElement(
                            sharedContentState = rememberSharedContentState(key = targetSnack.name),
                            animatedVisibilityScope = this@AnimatedContent,
                        ),
                        onClick = {
                            onConfirmClick()
                        }
                    )
                    Row(
                        Modifier
                            .fillMaxWidth()
                            .padding(bottom = 8.dp, end = 8.dp),
                        horizontalArrangement = Arrangement.End
                    ) {
                        TextButton(onClick = { onConfirmClick() }) {
                            Text(text = "Save changes")
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun SnackContents(
    snack: Snack,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Column(
        modifier = modifier
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null
            ) {
                onClick()
            }
    ) {
        Image(
            painter = painterResource(id = snack.image),
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(20f / 9f),
            contentScale = ContentScale.Crop,
            contentDescription = null
        )
        Text(
            text = snack.name,
            modifier = Modifier
                .wrapContentWidth()
                .padding(8.dp),
            style = MaterialTheme.typography.titleSmall
        )
    }
}