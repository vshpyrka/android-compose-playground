package com.example.compose.anim

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.compose.ui.theme.AndroidPlaygroundTheme

class GoogleAnimSamples : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Box(Modifier.fillMaxSize()) {
                NestedSharedBoundsSample()
            }
        }
    }
}

@Preview(showSystemUi = true)
@Composable
private fun GoogleAnimSamplesPreview() {
    AndroidPlaygroundTheme {
        Box(Modifier.fillMaxSize()) {
            AnimatedContentTransitionSpecSample()
        }
    }
}