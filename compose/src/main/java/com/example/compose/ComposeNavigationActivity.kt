package com.example.compose

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController

class ComposeNavigationActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            NavigationApp()
        }
    }
}

@Composable
fun NavigationApp() {
    val navController = rememberNavController()
    NavHost(
        navController = navController,
        startDestination = "start",
    ) {
        composable("start") {
            Box(
                Modifier
                    .fillMaxSize()
                    .background(Color.Blue),
                contentAlignment = Alignment.Center,
            ) {
                Box(
                    Modifier
                        .size(100.dp)
                        .background(Color.Red)
                        .clickable(
                            onClick = {
                                navController.navigate("end")
                            }
                        )
                )
            }
        }
        composable("end") {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center,
            ) {
                Box(
                    Modifier
                        .size(400.dp)
                        .background(Color.Red)
                )
            }
        }
    }
}
