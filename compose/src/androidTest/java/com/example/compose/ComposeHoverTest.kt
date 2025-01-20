package com.example.compose

import androidx.compose.foundation.background
import androidx.compose.foundation.hoverable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsHoveredAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performMouseInput
import androidx.compose.ui.unit.dp
import org.junit.Rule
import org.junit.Test

class ComposeHoverTest {

    @get:Rule
    val rule = createComposeRule()

    @OptIn(ExperimentalTestApi::class)
    @Test
    fun hoverTest() {
        var isHovered = false
        val interactionSource = MutableInteractionSource()
        val hoverTag = "hoverItem"

        rule.setContent {
            Box(
                modifier = Modifier
                    .size(250.dp)
                    .testTag(hoverTag)
            ) {

                Box(
                    modifier = Modifier
                        .size(128.dp)
                        .align(Alignment.Center)
                        .background(Color.Blue)
                        .hoverable(interactionSource)
                )
            }
            isHovered = interactionSource.collectIsHoveredAsState().value
        }

        // enter outside of the blue box
        rule.onNodeWithTag(hoverTag)
            .performMouseInput {
                enter(Offset(10.dp.toPx(), 10.dp.toPx()))
            }
        rule.waitForIdle()

        assert(!isHovered)

        // move over the blue box
        rule.onNodeWithTag(hoverTag).performMouseInput {
            moveTo(Offset(150.dp.toPx(), 150.dp.toPx()))
        }

        rule.waitForIdle()
        assert(isHovered)

        rule.onNodeWithTag(hoverTag).performMouseInput {
            moveTo(Offset(210.dp.toPx(), 210.dp.toPx()))
        }

        rule.waitForIdle()
        assert(!isHovered)
    }
}
