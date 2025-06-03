package com.example.compose

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.SemanticsActions
import androidx.compose.ui.semantics.SemanticsProperties
import androidx.compose.ui.semantics.SemanticsPropertyKey
import androidx.compose.ui.semantics.SemanticsPropertyReceiver
import androidx.compose.ui.semantics.getOrNull
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.test.SemanticsMatcher
import androidx.compose.ui.test.assert
import androidx.compose.ui.test.assertIsOff
import androidx.compose.ui.test.hasClickAction
import androidx.compose.ui.test.hasTestTag
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.junit4.accessibility.enableAccessibilityChecks
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.onRoot
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.printToLog
import androidx.compose.ui.test.tryPerformAccessibilityChecks
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.em
import androidx.compose.ui.unit.sp
import com.example.compose.ui.theme.AndroidPlaygroundTheme
import com.google.android.apps.common.testing.accessibility.framework.AccessibilityCheckResult
import com.google.android.apps.common.testing.accessibility.framework.integrations.espresso.AccessibilityValidator
import org.junit.Rule
import org.junit.Test

class ComposeAccessibilityTest {

    @get:Rule
    val rule = createComposeRule()

    val accessibilityValidator = AccessibilityValidator().apply {
        setThrowExceptionFor(AccessibilityCheckResult.AccessibilityCheckResultType.ERROR)
    }

    @Test
    fun testSimpleComposable() {
        rule.setContent {
            AndroidPlaygroundTheme {
                Column {
                    Text(text = "Hello")
                    Text(
                        text = "World",
                        modifier = Modifier.clickable { },
                        fontSize = 30.sp,
                        fontStyle = FontStyle.Italic,
                        style = TextStyle(
                            color = Color.Red,
                            fontSize = 16.sp,
                            fontFamily = FontFamily.Monospace,
                            fontWeight = FontWeight.W800,
                            fontStyle = FontStyle.Italic,
                            letterSpacing = 0.5.em,
                            background = Color.LightGray,
                            textDecoration = TextDecoration.Underline
                        )
                    )
                    Button(onClick = { /*TODO*/ }) {
                        Text(text = "Button", Modifier.testTag("clickableButton"))
                    }
                }
            }
        }

        rule.onRoot().printToLog("AAA")

        rule.onNodeWithText("Hello").assertExists()

        rule.onNode(
            hasText("World")
                    and
                    hasClickAction()
        ).assertExists()

        rule.onNode(
            hasTestTag("clickableButton"),
            useUnmergedTree = true
        ).assertExists()

        rule.enableAccessibilityChecks(accessibilityValidator)
        rule.onRoot().tryPerformAccessibilityChecks()

        // Wait until something appears
//        rule.waitUntil {
//                .fetchSemanticsNodes()
//                .isNotEmpty()
//        }

    }

    @Test
    fun testSwitch() {
        rule.setContent {
            AndroidPlaygroundTheme {
                var isChecked by remember { mutableStateOf(true) }
                Switch(isChecked, onCheckedChange = { isChecked = !isChecked })
            }
        }
        val testSwitch = SemanticsMatcher.expectValue(
            SemanticsProperties.Role, Role.Switch
        )
        rule.onNode(testSwitch)
            .performClick()
            .assertIsOff()
    }

    @Test
    fun testMergedSemanticsTree() {
        rule.setContent {
            AndroidPlaygroundTheme {
                Button(onClick = { /*TODO*/ }) {
                    Icon(
                        imageVector = Icons.Filled.Favorite,
                        contentDescription = null
                    )
                    Spacer(Modifier.size(ButtonDefaults.IconSpacing))
                    Text("Like")
                }
            }
        }
        rule.onRoot().printToLog("AAA")
        /*
            printToLog:
            Printing with useUnmergedTree = 'false'
            Node #1 at (l=0.0, t=242.0, r=278.0, b=368.0)px
             |-Node #2 at (l=0.0, t=253.0, r=278.0, b=358.0)px
               Focused = 'false'
               Role = 'Button'
               Text = '[Like]'
               Actions = [OnClick, RequestFocus, SetTextSubstitution, ShowTextSubstitution, ClearTextSubstitution, GetTextLayoutResult]
               MergeDescendants = 'true'
         */
    }

    @Test
    fun testUnMergedSemanticsTree() {
        rule.setContent {
            AndroidPlaygroundTheme {
                Button(onClick = { /*TODO*/ }) {
                    Icon(
                        imageVector = Icons.Filled.Favorite,
                        contentDescription = null
                    )
                    Spacer(Modifier.size(ButtonDefaults.IconSpacing))
                    Text("Like")
                }
            }
        }
        rule.onRoot(useUnmergedTree = true).printToLog("AAA")
        /*
        printToLog:
        Printing with useUnmergedTree = 'true'
        Node #1 at (l=0.0, t=242.0, r=278.0, b=368.0)px
         |-Node #2 at (l=0.0, t=253.0, r=278.0, b=358.0)px
           Focused = 'false'
           Role = 'Button'
           Actions = [OnClick, RequestFocus]
           MergeDescendants = 'true'
            |-Node #6 at (l=147.0, t=279.0, r=215.0, b=332.0)px
              Text = '[Like]'
              Actions = [SetTextSubstitution, ShowTextSubstitution, ClearTextSubstitution, GetTextLayoutResult]
         */
    }

    @Test
    fun testAccessibilityLabel() {
        rule.setContent {
            AndroidPlaygroundTheme {
                Column {
                    Text(
                        modifier = Modifier.clickable(
                            onClickLabel = "Click",
                            onClick = {},
                        ),
                        text = "Hello World"
                    )
                }
            }
        }
        rule.onNodeWithText("Hello World").assert(
            SemanticsMatcher("onClickLabel is set correctly") {
                it.config.getOrNull(SemanticsActions.OnClick)?.label == "Click"
            }
        )
    }

    val PickedDateKey = SemanticsPropertyKey<Long>("PickedDate")
    var SemanticsPropertyReceiver.pickedDate by PickedDateKey

    @Test
    fun testCustomSemanticsKey() {
        rule.setContent {
            AndroidPlaygroundTheme {
                Box(
                    modifier = Modifier
                        .semantics {
                            pickedDate = 1234567890
                        }
                ) {
                    Text("Hello World")
                }
            }
        }
        rule
            .onNode(SemanticsMatcher.expectValue(PickedDateKey, 1234567890))
            .assertExists()
    }
}
