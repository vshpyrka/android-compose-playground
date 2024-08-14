package com.example.compose

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.ime
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeContent
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.safeGestures
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.systemGestures
import androidx.compose.foundation.layout.windowInsetsBottomHeight
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.layout.windowInsetsTopHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import kotlin.random.Random

class ComposeInsetsActivity : ComponentActivity() {

    enum class State {
        Default,
        SafeContent,
        SafeGestures,
        SafeDrawing,
        StatusBars,
        NavigationBars,
        SystemBars,
        Ime,
        SystemGestures,
        WindowInsetTopHeight,
        WindowInsetBottomHeightLazyColumn,
        LazyListContentPadding,
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {

            var state by remember {
                mutableStateOf(State.Default)
            }

            when (state) {
                State.Default -> {
                    Default(
                        onSafeDrawingClick = {
                            state = State.SafeDrawing
                        },
                        onSafeContentClick = {
                            state = State.SafeContent
                        },
                        onSafeGesturesClick = {
                            state = State.SafeGestures
                        },
                        onStatusBarsClick = {
                            state = State.StatusBars
                        },
                        onNavigationBarsClick = {
                            state = State.NavigationBars
                        },
                        onSystemBarsClick = {
                            state = State.SystemBars
                        },
                        onImeClick = {
                            state = State.Ime
                        },
                        onSystemGesturesClick = {
                            state = State.SystemGestures
                        },
                        onWindowInsetTopHeightClick = {
                            state = State.WindowInsetTopHeight
                        },
                        onWindowInsetBottomHeightImeClick = {
                            state = State.WindowInsetBottomHeightLazyColumn
                        },
                        onLazyListWithContentPaddingClick = {
                            state = State.LazyListContentPadding
                        },
                    )
                }

                State.SafeContent -> {
                    Container(
                        onClick = {
                            state = State.Default
                        },
                        modifier = Modifier.windowInsetsPadding(WindowInsets.safeContent)
                        // .safeContentPadding()
                    )
                }

                State.SafeGestures -> {
                    Container(
                        onClick = {
                            state = State.Default
                        },
                        modifier = Modifier.windowInsetsPadding(WindowInsets.safeGestures)
                        // .safeGesturesPadding()
                    )
                }

                State.SafeDrawing -> {
                    Container(
                        onClick = {
                            state = State.Default
                        },
                        modifier = Modifier.windowInsetsPadding(WindowInsets.safeDrawing)
                        // .safeDrawingPadding()
                    )
                }

                State.StatusBars -> {
                    Container(
                        onClick = {
                            state = State.Default
                        },
                        modifier = Modifier.windowInsetsPadding(WindowInsets.statusBars)
                    )
                }

                State.NavigationBars -> {
                    Container(
                        onClick = {
                            state = State.Default
                        },
                        modifier = Modifier.windowInsetsPadding(WindowInsets.navigationBars)
                    )
                }

                State.SystemBars -> {
                    Container(
                        onClick = {
                            state = State.Default
                        },
                        modifier = Modifier.windowInsetsPadding(WindowInsets.systemBars)
                    )
                }

                State.Ime -> {
                    Container(
                        onClick = {
                            state = State.Default
                        },
                        modifier = Modifier.windowInsetsPadding(WindowInsets.ime)
                    )
                }

                State.SystemGestures -> {
                    Container(
                        onClick = {
                            state = State.Default
                        },
                        modifier = Modifier.windowInsetsPadding(WindowInsets.systemGestures)
                    )
                }

                State.WindowInsetTopHeight -> {
                    Column {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .windowInsetsTopHeight(WindowInsets.statusBars)
                                .background(Color.Red)
                        )
                        Container(
                            onClick = {
                                state = State.Default
                            }
                        )
                    }
                }

                State.WindowInsetBottomHeightLazyColumn -> {
                    LazyColumn(modifier = Modifier.imePadding()) {
                        items(3) {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(200.dp)
                                    .background(Color.Gray)
                                    .padding(16.dp)
                            )
                        }
                        item {
                            TextField("Hello", onValueChange = {})
                        }
                        item {
                            Box(
                                modifier = Modifier
                                    .windowInsetsBottomHeight(WindowInsets.statusBars)
                                    .background(Color.Red)
                            )
                        }
                    }
                }

                State.LazyListContentPadding -> {
                    LazyColumn(
                        contentPadding = WindowInsets.systemBars.asPaddingValues()
                    ) {
                        items(3) {
                            fun getRandomColor(): Color {
                                val random = Random
                                val red = random.nextInt(256)
                                val green = random.nextInt(256)
                                val blue = random.nextInt(256)
                                return Color(red, green, blue)
                            }
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(500.dp)
                                    .background(getRandomColor())
                                    .padding(16.dp)
                            )
                        }
                    }
                }
            }
        }
    }

    @Composable
    fun Default(
        onSafeDrawingClick: () -> Unit,
        onSafeContentClick: () -> Unit,
        onSafeGesturesClick: () -> Unit,
        onStatusBarsClick: () -> Unit,
        onNavigationBarsClick: () -> Unit,
        onSystemBarsClick: () -> Unit,
        onImeClick: () -> Unit,
        onSystemGesturesClick: () -> Unit,
        onWindowInsetTopHeightClick: () -> Unit,
        onWindowInsetBottomHeightImeClick: () -> Unit,
        onLazyListWithContentPaddingClick: () -> Unit,
    ) {
        Column(
            Modifier
                .background(Color.Magenta)
                .fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Button(onClick = onSafeDrawingClick) {
                Text("Safe Drawing")
            }
            Button(onClick = onSafeContentClick) {
                Text("Safe Content")
            }
            Button(onClick = onSafeGesturesClick) {
                Text("Safe Gestures")
            }
            Button(onClick = onStatusBarsClick) {
                Text("Status Bars")
            }
            Button(onClick = onNavigationBarsClick) {
                Text("Navigation Bars")
            }
            Button(onClick = onSystemBarsClick) {
                Text("System Bars")
            }
            Button(onClick = onImeClick) {
                Text("Ime")
            }
            Button(onClick = onSystemGesturesClick) {
                Text("System Gestures")
            }
            Button(onClick = onWindowInsetTopHeightClick) {
                Text("Window Inset Top Height")
            }
            Button(onClick = onWindowInsetBottomHeightImeClick) {
                Text("Window Inset Bottom Height Ime")
            }
            Button(onClick = onLazyListWithContentPaddingClick) {
                Text("Lazy List with content padding")
            }
        }
    }

    @Composable
    fun Container(
        onClick: () -> Unit,
        modifier: Modifier = Modifier,
    ) {
        Box(
            modifier = modifier
                .fillMaxSize()
                .background(Color.Blue), contentAlignment = Alignment.Center
        ) {
            Button(onClick) {
                Text("Back")
            }
        }
    }
}
