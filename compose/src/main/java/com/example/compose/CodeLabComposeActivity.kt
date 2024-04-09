package com.example.compose

import android.content.res.Configuration
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.compose.ui.theme.AndroidPlaygroundTheme

class ComposableCodeLabActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AndroidPlaygroundTheme {
                CodeLabScreenContent(modifier = Modifier.fillMaxSize())
            }
        }
    }
}

@Preview(
    showBackground = true
)
@Composable
fun CodeLabScreenContent(
    modifier: Modifier = Modifier,
) {
    var shouldShowOnboarding by rememberSaveable { mutableStateOf(true) }

    Surface(modifier = modifier) {
        if (shouldShowOnboarding) {
            OnboardingScreen(modifier) {
                shouldShowOnboarding = false
            }
        } else {
            Greetings(
                modifier,
                names = List(1000) { "$it" }
            )
        }
    }
}

@Preview(
    showBackground = true,
    widthDp = 320,
    heightDp = 320,
)
@Composable
fun OnboardingScreenPreview() {
    AndroidPlaygroundTheme {
        OnboardingScreen(onContinueClicked = {})
    }
}

@Composable
fun OnboardingScreen(
    modifier: Modifier = Modifier,
    onContinueClicked: () -> Unit,
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text("Welcome to the Basics Codelab!")
        Button(
            modifier = Modifier.padding(vertical = 24.dp),
            onClick = onContinueClicked
        ) {
            Text(text = "Continue")
        }
    }
}

@Preview(
    showBackground = true,
    name = "Greetings Light"
)
@Preview(
    showBackground = true,
    name = "Greetings Dark",
    uiMode = Configuration.UI_MODE_NIGHT_YES
)
@Composable
fun GreetingPreview() {
    AndroidPlaygroundTheme {
        GreetingCard(name = "Hello")
    }
}

@Composable
fun Greetings(
    modifier: Modifier,
    names: List<String> = listOf("John Doe", "Jane Doe"),
) {
    LazyColumn(
        modifier = modifier.padding(4.dp)
    ) {
        items(names) { name ->
            GreetingCard(name = name)
        }
    }
}

@Composable
fun GreetingCard(
    name: String,
    modifier: Modifier = Modifier,
) {
    Card(
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primary,
        ),
        modifier = modifier.padding(vertical = 4.dp, horizontal = 8.dp),
    ) {
        Greeting(name = name)
    }
}

@Composable
fun Greeting(
    name: String,
) {
    var expanded by rememberSaveable { mutableStateOf(false) }
//        val extraPadding = if (expanded) 48.dp else 0.dp
    val extraPadding by animateDpAsState(
        targetValue = if (expanded) 48.dp else 0.dp,
        label = "Padding animation",
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessLow,
        )
    )

    Row(
        modifier = Modifier
            .padding(24.dp)
            .animateContentSize(
                animationSpec = spring(
                    dampingRatio = Spring.DampingRatioMediumBouncy,
                    stiffness = Spring.StiffnessLow,
                )
            )
    ) {
        Column(
            modifier = Modifier
                .weight(1f)
//                .padding(bottom = extraPadding.coerceAtLeast(0.0.dp))
        ) {
            Text("Hello")
            Text(
                text = name,
                style = MaterialTheme.typography.headlineMedium
                    .copy(fontWeight = FontWeight.ExtraBold),
            )
            if (expanded) {
                Text(
                    text = ("Composem ipsum color sit lazy, " +
                            "padding theme elit, sed do bouncy. ").repeat(4),
                )
            }
        }

//        ElevatedButton(onClick = {}) {
//            Text(text = "Hello")
//        }

        IconButton(onClick = { expanded = !expanded }) {
            Icon(
                imageVector = if (expanded) Icons.Filled.ExpandLess else Icons.Filled.ExpandMore,
                contentDescription = null,
            )
        }
    }
}
