package com.example.compose

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle

class ComposeLauncherActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val items = listOf(
            "Simple Composable" to SimpleComposableActivity::class,
            "CodeLab Composable" to ComposableCodeLabActivity::class,
            "Surface Composable" to ComposeSurfaceActivity::class,
            "CodeLab Basic Layout Composable" to CodeLabComposeBasicLayoutActivity::class,
            "CodeLab State In Compose" to CodeLabComposeStateActivity::class,
            "CodeLab Compose Lazy Layouts" to CodeLabComposeLazyLayoutsActivity::class,
            "CodeLab Compose Styling Text" to CodeLabStylingTextActivity::class,
            "CodeLab Compose Simple Animations" to CodeLabSimpleAnimationsActivity::class,
            "CodeLab Compose Simple Drawing" to CodeLabComposeDrawingActivity::class,
            "CodeLab Compose Advanced Layouts" to CodeLabAdvancedLayoutsActivity::class,
            "Compose Insets" to ComposeInsetsActivity::class,
            "Compose Photo Grid" to ComposePhotoGridActivity::class,
            "Compose Navigation" to ComposeNavigationActivity::class,
            "Compose Interaction Source" to ComposeInteractionSourceActivity::class,
            "Nested scrolling" to NestedScrollingModifierActivity::class,
            "Constraints Modifiers" to ConstraintsModifiersActivity::class,
            "Compose Pager" to ComposePagerActivity::class,
            "Compose Flow" to ComposeFlowActivity::class,
            "Compose Brush" to ComposeBrushActivity::class,
        )
        setContent {
            val context = LocalContext.current
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState()),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
            ) {
                items.forEach {
                    val (title, klass) = it
                    Button(
                        onClick = {
                            context.startActivity(Intent(context, klass.java))
                        }
                    ) {
                        Text(
                            text = title,
                            style = TextStyle()
                        )
                    }
                }
            }
        }
    }
}
