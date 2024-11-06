package com.example.compose

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.ui.Modifier
import androidx.compose.ui.test.DeviceConfigurationOverride
import androidx.compose.ui.test.FontScale
import androidx.compose.ui.test.FontWeightAdjustment
import androidx.compose.ui.test.ForcedSize
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.then
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import org.junit.Rule
import org.junit.Test

class DeviceConfigurationOverrideTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun forcedSize() {
        composeTestRule.setContent {
            DeviceConfigurationOverride(
                DeviceConfigurationOverride.ForcedSize(DpSize(1280.dp, 800.dp))
            ) {
                // Will be rendered in the space for 1280dp by 800dp without clipping.
                Box(Modifier.size(300.dp))
            }
        }
    }

    @Test
    fun then() {
        composeTestRule.setContent {
            DeviceConfigurationOverride(
                DeviceConfigurationOverride.FontScale(1.5f) then
                        DeviceConfigurationOverride.FontWeightAdjustment(200)
            ) {
                Text(text = "text with increased scale and weight")
            }
        }
    }
}