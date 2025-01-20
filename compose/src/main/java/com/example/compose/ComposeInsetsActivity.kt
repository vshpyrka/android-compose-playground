package com.example.compose

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.captionBar
import androidx.compose.foundation.layout.displayCutout
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.ime
import androidx.compose.foundation.layout.imeNestedScroll
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeContent
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.safeGestures
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.systemGestures
import androidx.compose.foundation.layout.tappableElement
import androidx.compose.foundation.layout.waterfall
import androidx.compose.foundation.layout.windowInsetsBottomHeight
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.layout.windowInsetsTopHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Button
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.asComposePath
import androidx.compose.ui.graphics.drawscope.scale
import androidx.compose.ui.graphics.drawscope.withTransform
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.unit.dp
import androidx.core.view.WindowCompat
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
        Caption,
        DisplayCutout,
        Waterfall,
        TappableElement,
        Ime,
        ImeNestedScroll,
        SystemGestures,
        WindowInsetTopHeight,
        WindowInsetBottomHeightLazyColumn,
        LazyListContentPadding,
    }

    @OptIn(ExperimentalLayoutApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        WindowCompat.setDecorFitsSystemWindows(window, false)

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
                        onCaptionClick = {
                            state = State.Caption
                        },
                        onDisplayCutout = {
                            state = State.DisplayCutout
                        },
                        onWaterfall = {
                            state = State.Waterfall
                        },
                        onTappableElement = {
                            state = State.TappableElement
                        },
                        onImeClick = {
                            state = State.Ime
                        },
                        onImeNestedScrollClick = {
                            state = State.ImeNestedScroll
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

                State.Caption -> {
                    Container(
                        onClick = {
                            state = State.Default
                        },
                        modifier = Modifier.windowInsetsPadding(WindowInsets.captionBar)
                    )
                }

                State.DisplayCutout -> {
                    val cutoutPath: Path = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                        LocalView.current.rootWindowInsets.displayCutout?.cutoutPath?.asComposePath()
                            ?: Path()
                    } else {
                        Path()
                    }
                    Box {
                        Container(
                            onClick = {
                                state = State.Default
                            },
                            modifier = Modifier.windowInsetsPadding(WindowInsets.displayCutout)
                        )
                        Spacer(
                            Modifier
                                .drawBehind {
                                    withTransform(
                                        {
                                            scale(1.5f)
                                        }
                                    ) {
                                        drawPath(cutoutPath, Color.Red)
                                    }
                                }
                        )
                    }
                }

                State.Waterfall -> {
                    Container(
                        onClick = {
                            state = State.Default
                        },
                        modifier = Modifier.windowInsetsPadding(WindowInsets.waterfall)
                    )
                }

                State.TappableElement -> {
                    Container(
                        onClick = {
                            state = State.Default
                        },
                        modifier = Modifier.windowInsetsPadding(WindowInsets.tappableElement)
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

                State.ImeNestedScroll -> {
                    Box {
                        LazyColumn(
                            modifier = Modifier
                                .fillMaxSize() // fill the entire window
                                .imePadding() // padding for the bottom for the IME
                                .imeNestedScroll(), // scroll IME at the bottom
                            content = {
                                items(20) {
                                    Text(
                                        modifier = Modifier.fillMaxWidth(),
                                        text = "Hello $it",
                                    )
                                }
                            }
                        )
                        FloatingActionButton(
                            modifier = Modifier
                                .align(Alignment.BottomEnd)
                                .padding(16.dp) // normal 16dp of padding for FABs
                                .navigationBarsPadding() // padding for navigation bar
                                .imePadding(), // padding for when IME appears
                            onClick = { }
                        ) {
                            Icon(imageVector = Icons.Filled.Add, contentDescription = "Add")
                        }
                    }
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
                        items(20) {
                            TextField(
                                modifier = Modifier.fillMaxWidth(),
                                value = "Hello $it",
                                onValueChange = {}
                            )
                        }
                        item {
                            Spacer(
                                modifier = Modifier
                                    .windowInsetsBottomHeight(WindowInsets.statusBars)
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
        onCaptionClick: () -> Unit,
        onDisplayCutout: () -> Unit,
        onWaterfall: () -> Unit,
        onTappableElement: () -> Unit,
        onImeClick: () -> Unit,
        onImeNestedScrollClick: () -> Unit,
        onSystemGesturesClick: () -> Unit,
        onWindowInsetTopHeightClick: () -> Unit,
        onWindowInsetBottomHeightImeClick: () -> Unit,
        onLazyListWithContentPaddingClick: () -> Unit,
    ) {
        Column(
            Modifier
                .background(Color.Magenta)
                .fillMaxSize()
                .verticalScroll(rememberScrollState()),
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
            Button(onClick = onCaptionClick) {
                Text("Caption")
            }
            Button(onClick = onDisplayCutout) {
                Text("Display Cutout")
            }
            Button(onClick = onWaterfall) {
                Text("Waterfall")
            }
            Button(onClick = onTappableElement) {
                Text("Tappable Element")
            }
            Button(onClick = onImeClick) {
                Text("Ime")
            }
            Button(onClick = onImeNestedScrollClick) {
                Text("ImeNestedScroll")
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
