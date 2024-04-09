package com.example.compose

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.CutCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.compose.ui.theme.AndroidPlaygroundTheme

class CodeLabStylingTextActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            AndroidPlaygroundTheme {
                Column(
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    TextBubble(
                        modifier = Modifier.width(220.dp)
                    )
                    CustomTextField()
                    FullBorderTextField()
                    BorderModifiedTextField()
                    CursorColorChangeTextField()
                    DecoratedTextField()
                }
            }
        }
    }
}

@Preview(
    widthDp = 220
)
@Composable
private fun TextBubblePreview() {
    AndroidPlaygroundTheme {
        TextBubble()
    }
}

@Composable
fun TextBubble(
    modifier: Modifier = Modifier
) {
    Surface(
        shape = MaterialTheme.shapes.medium,
        color = MaterialTheme.colorScheme.surface,
        modifier = modifier,
    ) {
        Column {
            var showButton by remember {
                mutableStateOf(false)
            }
            var isExpanded by remember {
                mutableStateOf(false)
            }
            val text = buildAnnotatedString {
                append("Android vitals helps you improve the stability and performance of ")
                val spanStyle = SpanStyle(
                    fontFamily = FontFamily.Monospace,
                    background = Color.Cyan,
                )
                withStyle(style = spanStyle) {
                    append("Google Play")
                }
                append(
                    " apps on Android devices. For the best end-user experience, we recommend monitoring and prioritizing your app vitals. For more information see Android vitals overview. Firebase Performance Monitoring is a service that helps you to gain insight into the performance characteristics of your app."
                )
            }
            Text(
                maxLines = if (isExpanded) Int.MAX_VALUE else 8,
                overflow = TextOverflow.Ellipsis,
                text = text,
                onTextLayout = {
                    if (it.hasVisualOverflow) {
                        showButton = true
                    }
                },
                modifier = Modifier
                    .animateContentSize()
                    .padding(horizontal = 8.dp)
                    .padding(top = 8.dp)
            )

            if (showButton) {
                Button(
                    modifier = Modifier
                        .align(Alignment.End)
                        .padding(16.dp),
                    onClick = { isExpanded = !isExpanded },
                ) {
                    Text(
                        text = if (isExpanded) "Less" else "More",
                        style = MaterialTheme.typography.bodyMedium
                            .copy(fontWeight = FontWeight.ExtraBold)
                    )
                }
            }
        }
    }
}

@Preview
@Composable
fun CustomTextFieldPreview() {
    AndroidPlaygroundTheme {
        CustomTextField()
    }
}

@Composable
fun CustomTextField(
    modifier: Modifier = Modifier
) {
    var text by remember { mutableStateOf("Hello World") }
    TextField(
        value = "Hello World",
        onValueChange = { text = it },
        label = {
            Text("Message #composers")
        },
        colors = TextFieldDefaults.colors(
            unfocusedContainerColor = Color.Yellow,
            focusedContainerColor = Color.Magenta,
            focusedIndicatorColor = Color.Yellow,
            unfocusedIndicatorColor = Color.Magenta,
        ),
        modifier = modifier,
    )
}

@Preview
@Composable
fun FullBorderTextFieldPreview() {
    AndroidPlaygroundTheme {
        FullBorderTextField()
    }
}

@Composable
fun FullBorderTextField(
    modifier: Modifier = Modifier
) {
    var text by remember { mutableStateOf("Hello World") }
    TextField(
        value = text,
        onValueChange = {
            text = it
        },
        label = {
            Text("Message #composers")
        },
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = Color.Green,
            unfocusedBorderColor = Color.Yellow,
        ),
        modifier = modifier,
    )
}

@Preview
@Composable
private fun BorderModifiedTextFieldPreview() {
    AndroidPlaygroundTheme {
        BorderModifiedTextField()
    }
}

@Composable
fun BorderModifiedTextField(
    modifier: Modifier = Modifier
) {
    var text by remember { mutableStateOf("Hello World") }
    TextField(
        value = text,
        onValueChange = { text = it },
        label = {
            Text("Message #composers")
        },
        modifier = modifier.border(
            border = BorderStroke(
                brush = Brush.linearGradient(
                    0.0f to Color.Red,
                    0.3f to Color.Green,
                    1.0f to Color.Blue,
                    start = Offset(0.0f, 50.0f),
                    end = Offset(0.0f, 100.0f)
                ),
                width = 2.dp
            ),
            shape = CutCornerShape(12.dp)
        ),
    )
}

@Preview
@Composable
private fun CursorColorChangeTextFieldPreview() {
    AndroidPlaygroundTheme {
        CursorColorChangeTextField()
    }
}

@Composable
fun CursorColorChangeTextField() {
    var text by remember { mutableStateOf("Hello World") }
    BasicTextField(
        value = text,
        onValueChange = { text = it },
        cursorBrush = Brush.linearGradient(
            0.0f to Color.Red,
            0.3f to Color.Green,
            1.0f to Color.Blue,
            start = Offset(0.0f, 50.0f),
            end = Offset(0.0f, 100.0f)
        )
    )
}

@Preview
@Composable
private fun DecoratedTextFieldPreview() {
    AndroidPlaygroundTheme {
        DecoratedTextField()
    }
}

@Composable
fun DecoratedTextField() {
    var text by remember { mutableStateOf("Hello World") }
    BasicTextField(
        value = text,
        onValueChange = { text = it },
        decorationBox = { innerTextField ->
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.background(
                    color = Color.Magenta,
                    shape = MaterialTheme.shapes.medium,
                )
            ) {
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
                innerTextField()
            }
        }
    )
}
