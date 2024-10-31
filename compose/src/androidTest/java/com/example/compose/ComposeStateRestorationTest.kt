package com.example.compose

import androidx.compose.material3.Text
import androidx.compose.ui.test.junit4.StateRestorationTester
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import org.junit.Rule
import org.junit.Test

class ComposeStateRestorationTest {

    @get:Rule
    val rule = createComposeRule()

    @Test
    fun restoreState() {
        val restorationTester = StateRestorationTester(rule)

        restorationTester.setContent {
            Text("Hello")
        }

        // Trigger a recreation
        restorationTester.emulateSavedInstanceStateRestore()

        // Verify next items

        rule.onNodeWithText("Hello")
            .assertExists()
    }
}
