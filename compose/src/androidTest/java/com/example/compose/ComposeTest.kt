package com.example.compose

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.test.assert
import androidx.compose.ui.test.hasClickAction
import androidx.compose.ui.test.hasTestTag
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.onRoot
import androidx.compose.ui.test.printToLog
import org.junit.Rule
import org.junit.Test

class ComposeTest {

    @get:Rule
    val rule = createComposeRule()

    @Test
    fun testSimpleComposable() {
        rule.setContent {
            TestComposable()
        }

        // Print semantics
        rule.onRoot().printToLog("AAA")

        rule.onNodeWithText("Hello")
            .assertExists()
        rule.onNode(
            hasText("World")
            and
            hasClickAction()
        ).assertExists()
        rule.onNode(
            hasTestTag("clickableButton")
        ).assertExists()
    }
}

@Composable
fun TestComposable(modifier: Modifier = Modifier) {
    Column(modifier = modifier) {
        Text(text = "Hello")
        Text(text = "World", modifier = Modifier.clickable {  })
        Button(onClick = { /*TODO*/ }) {
            Text(text = "Button", Modifier.testTag("clickableButton"))
        }
    }
}
