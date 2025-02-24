package com.example.compose.widget.simple

import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.glance.LocalContext
import androidx.glance.appwidget.testing.unit.runGlanceAppWidgetUnitTest
import androidx.glance.testing.unit.hasText
import com.example.compose.widget.tiny.GlanceComposeAppWidgetContent
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.RuntimeEnvironment
import org.robolectric.annotation.Config
import org.robolectric.annotation.LooperMode

@RunWith(RobolectricTestRunner::class)
@Config(manifest=Config.NONE)
@LooperMode(LooperMode.Mode.PAUSED)
class GlanceWidgetTest {

    @Test
    fun testGlanceWidget() = runGlanceAppWidgetUnitTest{
        setAppWidgetSize(DpSize(100.dp, 100.dp))
        provideComposable {
            //  Override the LocalContext with the Robolectric context to access resources via Context class
            CompositionLocalProvider(LocalContext provides RuntimeEnvironment.getApplication()) {
                GlanceComposeAppWidgetContent()
            }
        }
        onNode(
            hasText("Glance App Widget")
        ).assertExists()
    }
}
