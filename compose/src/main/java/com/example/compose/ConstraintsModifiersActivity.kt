package com.example.compose

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.compose.ui.theme.AndroidPlaygroundTheme

class ConstraintsModifiersActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AndroidPlaygroundTheme {
                ConstraintsModifiers()
            }
        }
    }
}

@Composable
private fun ConstraintsModifiers() {
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        ConstraintsModifiers1()
        ConstraintsModifiers2()
        ConstraintsModifiers3()
    }
}

@Composable
private fun ConstraintsModifiers1() {
    // minW = 0, minH = 0, maxW = 300, maxH = 200
    Box(
        Modifier
            .size(300.dp, 200.dp)
            .background(Color.Red)
    ) {
        Box(
            Modifier
                .size(400.dp)
                .background(Color.Green)
        )
    }
}

@Composable
private fun ConstraintsModifiers2() {
    // minW = 0, minH = 0, maxW = 300, maxH = 200
    Box(
        Modifier
            .size(300.dp, 200.dp)
            .background(Color.Red)
    ) {
        Box(
            Modifier
                .fillMaxSize()
                // minW = 300, minH = 200, maxW = 300, maxH = 200
                .size(100.dp) // Ignored, sized to minW/minH
                .background(Color.Green)
        )
    }
}

@Composable
private fun ConstraintsModifiers3() {
    // minW = 0, minH = 0, maxW = 300, maxH = 200
    Box(
        Modifier
            .size(300.dp, 200.dp)
            .background(Color.Red)
    ) {
        Box(
            Modifier
                // minW = 100, minH = 100, maxW = 300, maxH = 200
                .size(100.dp)
                .size(50.dp) // Ignored, sized to minW/minH
                .background(Color.Green)
        )
    }
}

@Preview
@Composable
private fun ConstraintsModifiersPreview() {
    AndroidPlaygroundTheme {
        ConstraintsModifiers()
    }
}

