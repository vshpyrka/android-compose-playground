package com.example.compose

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.material3.Text
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import com.example.compose.ui.theme.AndroidPlaygroundTheme
import kotlinx.coroutines.delay
import org.junit.Rule
import org.junit.Test

class ComposeAnimationTest {

    @get:Rule
    val rule = createComposeRule()

    @Test
    fun advanceTimeExampleTest() {
        rule.mainClock.autoAdvance = false
        rule.setContent {
            AndroidPlaygroundTheme {
                var isVisible by remember { mutableStateOf(false) }
                LaunchedEffect(Unit) {
                    delay(500)
                    isVisible = true
                }
                AnimatedVisibility(isVisible) {
                    Text("Hello World")
                }
            }
        }
        rule.mainClock.advanceTimeBy(1000)
        // or go frame by frame
        rule.mainClock.advanceTimeByFrame()

        rule.onNodeWithText("Hello World").assertIsDisplayed()
    }
}
