package com.example.compose

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.runtime.toMutableStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.compose.ui.theme.AndroidPlaygroundTheme

class CodeLabComposeStateActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AndroidPlaygroundTheme {
                MainContent()
            }
        }
    }
}

@Preview(
    showBackground = true
)
@Composable
fun MainContent(
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background,
    ) {
        WellnessScreen()
    }
}

@Composable
fun WellnessScreen(
    modifier: Modifier = Modifier
) {
    StatefulCounter(modifier)
}

@Composable
fun StatefulCounter(
    modifier: Modifier = Modifier
) {
    var count by rememberSaveable { mutableIntStateOf(0) }
    StatelessCounter(
        count = count,
        onIncrement = {
            count++
        },
        modifier = modifier,
    )
}

@Composable
private fun StatelessCounter(
    count: Int,
    onIncrement: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.padding(16.dp)
    ) {
        if (count > 0) {
            Text(
                text = "You've had $count glasses.",
                modifier = modifier.padding(16.dp)
            )
        }
        Button(
            onClick = onIncrement,
            enabled = count < 10,
            modifier = Modifier.padding(top = 8.dp)
        ) {
            Text("Add one")
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun WellnessTaskItem(
    modifier: Modifier = Modifier
) {
    val list = List(30) { "$it" }
    list.toMutableStateList()
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "Item",
            modifier = Modifier
                .weight(1f)
                .padding(start = 8.dp),
        )
        Checkbox(
            checked = false,
            onCheckedChange = {},
        )
        IconButton(onClick = { /*TODO*/ }) {
            Icon(Icons.Default.Close, contentDescription = null)
        }
    }
}
