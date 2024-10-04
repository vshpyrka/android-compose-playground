package com.example.compose

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.Animatable
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.EaseIn
import androidx.compose.animation.core.EaseOut
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateDp
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.animateIntOffsetAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.core.updateTransition
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBox
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.datasource.LoremIpsum
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import com.example.compose.ui.theme.AndroidPlaygroundTheme

class CodeLabSimpleAnimationsActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            AndroidPlaygroundTheme {
                AnimateOffsetExample()
//                Column {
//                    BubbleMessage()
//                    AnimatedLongText()
//                    AnimatedContentExample()
//                    ProgressIndicatorAnimationExample()
//                    AnimatedImageBorder()
//                }
            }
        }
    }
}

@Preview
@Composable
private fun BubbleMessagePreview() {
    AndroidPlaygroundTheme {
        BubbleMessage()
    }
}

@Composable
fun BubbleMessage(
    modifier: Modifier = Modifier
) {
    Surface(
        shape = MaterialTheme.shapes.medium,
        color = MaterialTheme.colorScheme.surface,
    ) {
        var showDate by remember {
            mutableStateOf(false)
        }
        Column(
            verticalArrangement = Arrangement.spacedBy(8.dp),
            modifier = modifier
                .padding(16.dp)
                .clickable { showDate = !showDate }
        ) {
            Image(
                painter = painterResource(id = R.drawable.ic_parrot),
                contentDescription = null
            )
            AnimatedVisibility(visible = showDate) {
                Text(
                    text = "8:07 AM",
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun AnimatedLongTextPreview() {
    AndroidPlaygroundTheme {
        AnimatedLongText()
    }
}

@Composable
fun AnimatedLongText(
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
    ) {
        var isExpanded by remember {
            mutableStateOf(false)
        }
        Text(
            maxLines = if (isExpanded) Int.MAX_VALUE else 3,
            overflow = TextOverflow.Ellipsis,
            text = LoremIpsum(100).values.joinToString(),
            modifier = Modifier
                .background(MaterialTheme.colorScheme.inverseSurface)
                .animateContentSize(
                    animationSpec = spring(
                        dampingRatio = Spring.DampingRatioLowBouncy,
                        stiffness = Spring.StiffnessLow,
                    )
                )
                .padding(16.dp)
                .clickable { isExpanded = !isExpanded }
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun AnimatedContentPreview() {
    AndroidPlaygroundTheme {
        AnimatedContentExample()
    }
}

enum class SelectedState {
    HOME, ACCOUNTS, SETTINGS
}

@Composable
fun AnimatedContentExample(
    modifier: Modifier = Modifier
) {

    var selectedState by remember {
        mutableStateOf(SelectedState.HOME)
    }

    Column(modifier) {
        Row {
            Icon(
                imageVector = Icons.Default.Home,
                contentDescription = null,
                modifier = Modifier
                    .size(56.dp)
                    .clickable { selectedState = SelectedState.HOME }
            )
            Icon(
                imageVector = Icons.Default.AccountBox,
                contentDescription = null,
                modifier = Modifier
                    .size(56.dp)
                    .clickable { selectedState = SelectedState.ACCOUNTS }
            )
            Icon(
                imageVector = Icons.Default.Settings,
                contentDescription = null,
                modifier = Modifier
                    .size(56.dp)
                    .clickable { selectedState = SelectedState.SETTINGS }
            )
        }

        Surface(
            shape = MaterialTheme.shapes.medium,
            color = MaterialTheme.colorScheme.surfaceVariant,
        ) {
            AnimatedContent(
                targetState = selectedState,
                label = "Animated content example",
                transitionSpec = {
                    slideIntoContainer(
                        animationSpec = tween(300, easing = EaseIn),
                        towards = AnimatedContentTransitionScope.SlideDirection.Up
                    ).togetherWith(
                        slideOutOfContainer(
                            animationSpec = tween(300, easing = EaseOut),
                            towards = AnimatedContentTransitionScope.SlideDirection.Down,
                        )
                    )
                }
            ) { targetState ->
                when (targetState) {
                    SelectedState.HOME -> AnimatedContentHome(
                        Modifier.padding(16.dp)
                    )

                    SelectedState.ACCOUNTS -> AnimatedContentSettings(
                        Modifier.padding(16.dp)
                    )

                    SelectedState.SETTINGS -> AnimatedContentAccounts(
                        Modifier.padding(16.dp)
                    )
                }
            }
        }
    }
}

@Composable
fun AnimatedContentHome(
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
    ) {
        Text(
            text = LoremIpsum(20).values.joinToString(),
        )
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Default.Home,
                contentDescription = null,
                modifier = Modifier.size(56.dp)
            )
            Text(text = "Home")
        }
    }
}

@Composable
fun AnimatedContentSettings(
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Default.Settings,
                contentDescription = null,
                modifier = Modifier.size(56.dp)
            )
            Text(text = "Settings")
        }
        Text(
            text = LoremIpsum(20).values.joinToString(),
        )
    }
}

@Composable
fun AnimatedContentAccounts(
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
    ) {
        Text(
            text = LoremIpsum(20).values.joinToString(),
        )
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Default.AccountBox,
                contentDescription = null,
                modifier = Modifier.size(56.dp)
            )
            Text(text = "Accounts")
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun ProgressIndicatorAnimationExamplePreview() {
    AndroidPlaygroundTheme {
        ProgressIndicatorAnimationExample()
    }
}

@Composable
fun ProgressIndicatorAnimationExample(
    modifier: Modifier = Modifier
) {
    var progress by remember {
        mutableFloatStateOf(0f)
    }

    val targetValueFloat = progress
    val animatedProgress by animateFloatAsState(
        targetValue = targetValueFloat,
        label = "Animated progress"
    )

    Column(
        modifier = modifier,
    ) {
        Icon(
            imageVector = Icons.Default.Settings,
            contentDescription = null,
            modifier = Modifier
                .size(56.dp)
                .clickable {
                    progress += 0.1f
                }
        )
        LinearProgressIndicator(
            progress = {
                animatedProgress
            },
            modifier = Modifier.padding(vertical = 16.dp, horizontal = 8.dp)
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun AnimatedImageBorderPreview() {
    AndroidPlaygroundTheme {
        AnimatedImageBorder()
    }
}

@Composable
fun AnimatedImageBorder(
    modifier: Modifier = Modifier
) {

    val infiniteTransition = rememberInfiniteTransition(label = "Gradient transition")
    val rotationAnimation = infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(tween(1000, easing = LinearEasing)),
        label = "Rotate animation"
    )

    Image(
        painter = painterResource(id = R.drawable.fc3_stress_and_anxiety),
        contentDescription = null,
        contentScale = ContentScale.Crop,
        modifier = Modifier
            .size(150.dp)
            .padding(8.dp)
            .drawBehind {
                rotate(rotationAnimation.value) {
                    drawCircle(
                        Brush.sweepGradient(
                            0.0f to Color.Red,
                            0.3f to Color.Green,
                            0.7f to Color.Blue,
                            1.0f to Color.Red,
                        ),
                        style = Stroke(width = 8.dp.toPx())
                    )
                }
            }
            .clip(CircleShape)
    )
}

data class Hero(val title: String, val message: String)

@Preview(showBackground = true)
@Composable
private fun HeroesPreview() {
    AndroidPlaygroundTheme {
        HeroesList(
            List(5) {
                Hero(
                    LoremIpsum((2..5).random()).values.joinToString(),
                    LoremIpsum((3..20).random()).values.joinToString()
                )
            }
        )
    }
}

@OptIn(ExperimentalAnimationApi::class, ExperimentalFoundationApi::class)
@Composable
fun HeroesList(
    heroes: List<Hero>,
) {
    Box(Modifier.fillMaxSize()) {
        AnimatedVisibility(
            visible = heroes.isNotEmpty(),
            enter = fadeIn(),
            exit = fadeOut(),
        ) {
            LazyColumn {
                itemsIndexed(heroes) { index, hero ->
                    HeroContainer(
                        modifier = Modifier
                            .padding(horizontal = 16.dp, vertical = 4.dp)
                            .animateEnterExit(
                                enter = slideInVertically(
                                    animationSpec = spring(dampingRatio = 0.8f)
                                ) {
                                    it * (index + 1)
                                }
                            ),
                        hero = hero
                    )
                }
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
private fun HeroContainerPreview() {
    AndroidPlaygroundTheme {
        HeroContainer(
            hero = Hero(
                LoremIpsum((2..5).random()).values.joinToString(),
                LoremIpsum((10..20).random()).values.joinToString()
            )
        )
    }
}

@Composable
fun HeroContainer(
    modifier: Modifier = Modifier,
    hero: Hero,
) {
    Surface(
        color = MaterialTheme.colorScheme.secondaryContainer,
        shape = MaterialTheme.shapes.large,
        modifier = modifier
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {

            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = hero.title,
                    style = MaterialTheme.typography.headlineSmall
                )
                Text(
                    text = hero.message,
                    style = MaterialTheme.typography.bodyMedium
                )
            }
            Spacer(modifier = Modifier.width(16.dp))
            Box(
                modifier = Modifier
                    .size(74.dp)
                    .clip(RoundedCornerShape(8.dp))
            ) {
                Image(
                    painter = painterResource(id = R.drawable.fc5_overwhelmed),
                    contentDescription = null,
                    contentScale = ContentScale.Crop
                )
            }
        }
    }
}

@Composable
private fun AnimateOffsetExample() {
    var clicked by remember { mutableStateOf(false) }
    val offset by animateIntOffsetAsState(
        targetValue = if (clicked) {
            IntOffset(100, 100)
        } else {
            IntOffset.Zero
        },
        label = "offset_anim"
    )
    Box(
        modifier = Modifier
            .size(150.dp)
            .offset {
                offset
            }
            .clickable {
                clicked = !clicked
            }
            .background(Color.Magenta)
    )
}

@Preview(showSystemUi = true)
@Composable
private fun AnimateOffsetExamplePreview() {
    AndroidPlaygroundTheme {
        AnimateOffsetExample()
    }
}

enum class BoxState {
    Collapsed,
    Expanded,
}

@Composable
private fun TransitionAnimationExample() {
    var currentState by remember { mutableStateOf(BoxState.Collapsed) }
    val transition = updateTransition(
        targetState = currentState,
        label = "transition"
    )
    val size by transition.animateDp(label = "rect") { state ->
        when (state) {
            BoxState.Collapsed -> 100.dp
            BoxState.Expanded -> 300.dp
        }
    }
    val rotate by transition.animateFloat(label = "rotate") { state ->
        when (state) {
            BoxState.Collapsed -> 0f
            BoxState.Expanded -> 45f
        }
    }
    Box(
        modifier = Modifier
            .size(size)
            .graphicsLayer {
                rotationZ = rotate
            }
            .background(Color.Magenta)
            .clickable {
                currentState = BoxState.Expanded
            }
    )
}

@Preview(showSystemUi = true)
@Composable
private fun TransitionAnimationExamplePreview() {
    AndroidPlaygroundTheme {
        Box(Modifier.fillMaxSize()) {
            TransitionAnimationExample()
        }
    }
}

@Composable
private fun AnimateOnLaunchExample() {
    val color = remember { Animatable(Color.Gray) }
    var open by remember { mutableStateOf(false) }
    LaunchedEffect(open) {
        color.animateTo(if (open) Color.Green else Color.Red)
    }
    Box(
        Modifier
            .fillMaxSize()
            .background(color.value)
            .clickable {
                open = true
            }
    )
}

@Preview(showSystemUi = true)
@Composable
private fun AnimateOnLaunchExamplePreview() {
    AndroidPlaygroundTheme {
        AnimateOnLaunchExample()
    }
}
