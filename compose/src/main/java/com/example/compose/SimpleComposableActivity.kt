@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.compose

import android.content.res.Configuration
import android.os.Bundle
import android.view.Gravity
import android.view.WindowManager
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColor
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.exponentialDecay
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.AnchoredDraggableState
import androidx.compose.foundation.gestures.DraggableAnchors
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.anchoredDraggable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredWidthIn
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.selection.toggleable
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ContactMail
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.stateDescription
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.compose.ui.window.DialogWindowProvider
import com.example.compose.ui.theme.AndroidPlaygroundTheme
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch
import kotlin.math.roundToInt
import kotlin.random.Random

class SimpleComposableActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        enableEdgeToEdge()
        setContent {
            ScreenContent()
        }
//        val keyboardHeight = WindowInsets.ime.getBottom(LocalDensity.current)
//        val isVisible = WindowInsets.isImeVisible

//        setContent {
//            AndroidPlaygroundTheme {
//                // A surface container using the 'background' color from the theme
//                Surface(
//                    modifier = Modifier.fillMaxSize(),
//                    color = MaterialTheme.colorScheme.background
//                ) {
//                    Greeting("Android")
//                }
//            }
//        }
    }
}

@Composable
fun CustomBottomSheetPreview() {
    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        Spacer(modifier = Modifier.weight(1F))

        CustomBottomSheet {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
                    .background(Color.Magenta)
            )
        }
    }
}

enum class DragAnchors {
    Start,
    End
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun CustomBottomSheet(
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit,
) {
    val density = LocalDensity.current

    val state = remember {
        AnchoredDraggableState(
            initialValue = DragAnchors.Start,
            positionalThreshold = { distance: Float -> distance * 0.5f },
            velocityThreshold = { with(density) { 60.dp.toPx() } },
            snapAnimationSpec = spring(),
            decayAnimationSpec = exponentialDecay(),
        )
    }
    LaunchedEffect(state) {
        snapshotFlow { state.currentValue }
            .distinctUntilChanged()
            .collect { stage ->
                if (stage == DragAnchors.End) {
                    println("AAA Closed!")
                }
            }
    }
    Box(
        modifier = modifier
            .offset {
                IntOffset(
                    x = 0,
                    y = state
                        .requireOffset()
                        .roundToInt()
                )
            }
            .anchoredDraggable(
                state = state,
                orientation = Orientation.Vertical
            )
            .onSizeChanged { sheetSize ->
                state.updateAnchors(
                    DraggableAnchors {
                        DragAnchors.Start at 0f
                        DragAnchors.End at sheetSize.height.toFloat()
                    }
                )
            }
    ) {
        content()
    }
}

@Composable
private fun AutoFocusingText(
    modifier: Modifier = Modifier,
    value: Float,
) {
    val focusRequester = remember { FocusRequester() }
    var text by remember { mutableStateOf(value.toString()) }

    LaunchedEffect(Unit) {
        focusRequester.requestFocus()
    }

    TextField(
        value = text,
        onValueChange = { text = it },
        modifier = modifier.focusRequester(focusRequester),
        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
    )

}

data class Message(val author: String, val body: String, val isMe: Boolean = false)

@Preview(
    name = "Light Mode",
    showBackground = true,
)
@Preview(
    name = "Dark Mode",
    showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_YES
)
@Composable
fun ScreenContent() {
    AndroidPlaygroundTheme {
        Surface(modifier = Modifier.fillMaxSize()) {
            Conversation(SampleData.conversationSample)
        }
    }
}

@Composable
fun Conversation(messages: List<Message>) {
    LazyColumn {
        items(messages) { message ->
            MessageCard(message)
        }
    }
}

@Composable
private fun MessageCard(message: Message) {

    // Items arranged horizontally
    // Add padding around our message
    Row(modifier = Modifier.padding(all = 8.dp)) {

        Image(
            painter = painterResource(R.drawable.ic_parrot),
            contentDescription = "Contact profile picture",
            modifier = Modifier
                // Set image size to 40 dp
                .size(40.dp)
                // Clip image to be shaped as a circle
                .clip(CircleShape)
                .border(1.5.dp, MaterialTheme.colorScheme.primary, CircleShape)
        )

        // Add a horizontal space between the image and the column
        Spacer(modifier = Modifier.width(8.dp))


        // We keep track if the message is expanded or not in this
        // variable
        var isExpanded by remember { mutableStateOf(false) }

        // surfaceColor will be updated gradually from one color to the other
        val surfaceColor by animateColorAsState(
            targetValue = if (isExpanded) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surface,
            label = "Some Label"
        )

        // Items arranged vertically
        Column(modifier = Modifier.clickable { isExpanded = !isExpanded }) {
            Text(
                text = message.author,
                color = MaterialTheme.colorScheme.secondary,
                style = MaterialTheme.typography.titleSmall,
            )
            // Add a vertical space between the author and message texts
            Spacer(modifier = Modifier.height(4.dp))

            Surface(
                shape = MaterialTheme.shapes.medium,
                shadowElevation = 1.dp,
                // surfaceColor color will be changing gradually from primary to surface
                color = surfaceColor,
                modifier = Modifier
                    .animateContentSize()
                    .padding(1.dp)
            ) {
                Text(
                    text = message.body,
                    modifier = Modifier.padding(all = 4.dp),
                    maxLines = if (isExpanded) Int.MAX_VALUE else 1,
                    style = MaterialTheme.typography.bodyMedium,
                )
            }
        }
    }

    // Items are stacked
//    Box {
//        Text(text = message.author)
//        Text(text = message.body)
//    }
}

/**
 * SampleData for Jetpack Compose Tutorial
 */
object SampleData {
    // Sample conversation data
    val conversationSample = listOf(
        Message(
            "Lexi",
            "Test...Test...Test..."
        ),
        Message(
            "Lexi",
            """List of Android versions:
            |Android KitKat (API 19)
            |Android Lollipop (API 21)
            |Android Marshmallow (API 23)
            |Android Nougat (API 24)
            |Android Oreo (API 26)
            |Android Pie (API 28)
            |Android 10 (API 29)
            |Android 11 (API 30)
            |Android 12 (API 31)""".trim()
        ),
        Message(
            "Lexi",
            """I think Kotlin is my favorite programming language.
            |It's so much fun!""".trim()
        ),
        Message(
            "Lexi",
            "Searching for alternatives to XML layouts..."
        ),
        Message(
            "Lexi",
            """Hey, take a look at Jetpack Compose, it's great!
            |It's the Android's modern toolkit for building native UI.
            |It simplifies and accelerates UI development on Android.
            |Less code, powerful tools, and intuitive Kotlin APIs :)""".trim()
        ),
        Message(
            "Lexi",
            "It's available from API 21+ :)"
        ),
        Message(
            "Lexi",
            "Writing Kotlin for UI seems so natural, Compose where have you been all my life?"
        ),
        Message(
            "Lexi",
            "Android Studio next version's name is Arctic Fox"
        ),
        Message(
            "Lexi",
            "Android Studio Arctic Fox tooling for Compose is top notch ^_^"
        ),
        Message(
            "Lexi",
            "I didn't know you can now run the emulator directly from Android Studio"
        ),
        Message(
            "Lexi",
            "Compose Previews are great to check quickly how a composable layout looks like"
        ),
        Message(
            "Lexi",
            "Previews are also interactive after enabling the experimental setting"
        ),
        Message(
            "Lexi",
            "Have you tried writing build.gradle with KTS?"
        ),
    )
}

@Preview
@Composable
private fun ChatListPreview() {
    ChatList(
        modifier = Modifier.fillMaxSize()
    )
}

@Composable
fun ChatList(modifier: Modifier = Modifier) {
    AndroidPlaygroundTheme {
        Box(modifier = modifier) {
            val messages = SampleData.conversationSample.map {
                it.copy(isMe = Random.nextBoolean())
            }

            val listState = rememberLazyListState()
            LazyColumn(
                state = listState,
                contentPadding = PaddingValues(bottom = 80.dp)
            ) {
                items(messages) {
                    Box(
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        ChatMessage(
                            message = it,
                            modifier = Modifier
                                .padding(horizontal = 16.dp, vertical = 4.dp)
                                .requiredWidthIn(max = 280.dp)
                                .align(
                                    if (it.isMe) Alignment.CenterEnd
                                    else Alignment.CenterStart
                                )
                        )
                    }
                }
            }

            val showButton by remember {
                derivedStateOf {
                    listState.firstVisibleItemIndex > 0
                }
            }

            val scope = rememberCoroutineScope()

//            if (showButton) {
            AnimatedVisibility(
                visible = showButton,
                enter = fadeIn(),
                exit = fadeOut(),
                modifier = Modifier.align(Alignment.BottomCenter)
            ) {
                Button(
                    onClick = {
                        scope.launch {
                            listState.scrollToItem(0)
                        }
                    },
                    modifier = Modifier
                        .height(80.dp)
                ) {
                    Text(text = "Scroll to top")
                }
            }
        }
    }
}

@Composable
fun ChatMessage(
    modifier: Modifier = Modifier,
    message: Message,
) {
    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors().copy(
            contentColor = if (message.isMe) MaterialTheme.colorScheme.surface
            else MaterialTheme.colorScheme.secondary
        )
    ) {
        Text(
            text = message.body,
            modifier = Modifier.padding(12.dp)
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun BorderExampleComposablePreview() {
    AndroidPlaygroundTheme {
        BorderExampleComposable(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 200.dp)
        )
    }
}

@Composable
fun BorderExampleComposable(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
    ) {
        val isShaped by remember {
            mutableStateOf(false)
        }
        Image(
            painter = painterResource(id = R.drawable.fc5_overwhelmed),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .size(150.dp)
                .clip(CircleShape)
                .shadow(16.dp, RoundedCornerShape(100))
                .clickable {
                    if (isShaped) {

                    } else {

                    }
                }
        )
    }
}

@Composable
fun ToggleableItem(modifier: Modifier = Modifier) {
    var selected by remember {
        mutableStateOf(false)
    }
    val stateEnabled = "Enabled"
    val stateDisabled = "Disabled"
    Row(
        modifier = modifier
            .semantics {
                stateDescription = if (selected) {
                    stateEnabled
                } else {
                    stateDisabled
                }
            }
            .toggleable(
                value = selected,
                onValueChange = {
                    selected = !selected
                },
                role = Role.Checkbox,
            )
            .padding(horizontal = 16.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {

        Icon(
            imageVector = Icons.Default.ContactMail,
            contentDescription = null,
            modifier = Modifier.size(56.dp)
        )
        Text(
            text = "Hello World Hello World Hello World Hello World",
            modifier = Modifier
                .padding(8.dp)
                .weight(1F)
        )
        Checkbox(checked = selected, onCheckedChange = null)
    }
}

@Preview(showBackground = true)
@Composable
private fun ToggleableItemPreview() {
    AndroidPlaygroundTheme {
        ToggleableItem(
            Modifier.fillMaxWidth()
        )
    }
}

@Composable
fun AnimatedBackground() {
    val infiniteTransition = rememberInfiniteTransition(label = "infiniteAnimation")
    // Creates a Color animation as a part of the [InfiniteTransition].
    val color by infiniteTransition.animateColor(
        initialValue = Color.Red,
        targetValue = Color(0xff800000), // Dark Red
        animationSpec = infiniteRepeatable(
            // Linearly interpolate between initialValue and targetValue every 1000ms.
            animation = tween(1000, easing = LinearEasing),
            // Once [TargetValue] is reached, starts the next iteration in reverse (i.e. from
            // TargetValue to InitialValue). Then again from InitialValue to TargetValue. This
            // [RepeatMode] ensures that the animation value is *always continuous*.
            repeatMode = RepeatMode.Reverse
        ), label = "colorAnimation"
    )

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(100.dp)
            .drawBehind {
                // Change color only during Draw phase, without causing recomposition
                drawRect(color)
            }
    )
}

@Preview
@Composable
private fun AnimatedBackgroundPreview() {
    AndroidPlaygroundTheme {
        AnimatedBackground()
    }
}

@Composable
fun CustomBottomGravityDialog(
    modifier: Modifier = Modifier
) {
    AndroidPlaygroundTheme {
        var showDialog by remember {
            mutableStateOf(true)
        }

        if (showDialog) {
            Dialog(
                properties = DialogProperties(
                    usePlatformDefaultWidth = false,
                ),
                onDismissRequest = { showDialog = false }
            ) {
                val dialogWindowProvider =
                    LocalView.current.parent as DialogWindowProvider
                dialogWindowProvider.window.setGravity(Gravity.BOTTOM)
                dialogWindowProvider.window.clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND)
                AutoFocusingText(
                    modifier = Modifier.fillMaxWidth(),
                    value = 20f
                )
            }
        }
    }
}
