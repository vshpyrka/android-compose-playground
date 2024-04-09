package com.example.compose

import android.content.res.Configuration
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredWidthIn
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.compose.ui.theme.AndroidPlaygroundTheme
import kotlinx.coroutines.launch
import kotlin.math.max
import kotlin.random.Random

class SimpleComposableActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        enableEdgeToEdge()
        setContent {
            ScreenContent()
        }

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
