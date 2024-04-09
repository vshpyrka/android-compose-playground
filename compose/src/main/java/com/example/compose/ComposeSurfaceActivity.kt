package com.example.compose

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.compose.ui.theme.AndroidPlaygroundTheme

class ComposeSurfaceActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AndroidPlaygroundTheme {
                ComposableSurface()
            }
        }
    }
}

@Preview(
    showBackground = true
)
@Composable
fun ComposableSurface() {
    Surface(
        color = MaterialTheme.colorScheme.primary,
        shape = RoundedCornerShape(8.dp),
        border = BorderStroke(2.dp, MaterialTheme.colorScheme.surfaceVariant),
        tonalElevation = 8.dp,
        shadowElevation = 8.dp,
    ) {
        Text("Jetpack Compose", modifier = Modifier.padding(24.dp))
    }
}

@Preview(showBackground = true)
@Composable
fun BoxModifierComposable() {
    Box(
        modifier = Modifier.size(150.dp)
    ) {
        Text(
            "Jetpack Compose",
            modifier = Modifier.align(Alignment.BottomEnd)
        )
    }
}

@Preview(showBackground = true)
@Composable
fun SurveyAnswer() {
    Surface(
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline),
        shape = MaterialTheme.shapes.small,
        shadowElevation = 8.dp,
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxWidth()
                .padding(16.dp)
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
            Text(text = "Spark", Modifier.weight(1f))
            RadioButton(selected = false, onClick = { /*TODO*/ })
        }
    }
}
