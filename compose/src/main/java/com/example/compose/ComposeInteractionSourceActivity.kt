package com.example.compose

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.IndicationNodeFactory
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.DragInteraction
import androidx.compose.foundation.interaction.Interaction
import androidx.compose.foundation.interaction.InteractionSource
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.PressInteraction
import androidx.compose.foundation.interaction.collectIsHoveredAsState
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.ripple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.geometry.center
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Brush.Companion.linearGradient
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.drawOutline
import androidx.compose.ui.graphics.drawscope.ContentDrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.scale
import androidx.compose.ui.node.DelegatableNode
import androidx.compose.ui.node.DrawModifierNode
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancelAndJoin
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlin.math.abs
import kotlin.math.sign

class ComposeInteractionSourceActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Column {
                SimpleInteractionLayout()
                InteractionLayoutWithCollectAsPressed()
                InteractionLayout()
                HoveredInteractionButton()
                ScaledIndicationButton()
                ScaledIndicationBox()
                NeonIndicationButton()
            }
        }
    }
}

@Composable
private fun SimpleInteractionLayout() {
    val interactionSource = remember { MutableInteractionSource() }
    Box(
        modifier = Modifier
            .size(100.dp)
            .background(Color.Magenta)
            .clickable(
                onClick = {},
                interactionSource = interactionSource,
                indication = ripple()
            ),
        contentAlignment = Alignment.Center,
    ) {
        Text("Hello")
    }
}

@Composable
private fun InteractionLayoutWithCollectAsPressed() {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()
    Button(
        onClick = {},
        interactionSource = interactionSource
    ) {
        Text(
            modifier = Modifier.animateContentSize(
                animationSpec = spring(
                    dampingRatio = Spring.DampingRatioLowBouncy,
                    stiffness = Spring.StiffnessLow,
                )
            ),
            text = if (isPressed) "Pressed" else "Not Pressed"
        )
    }
}

@Composable
fun InteractionLayout() {
    val interactionSource = remember { MutableInteractionSource() }
    val interactions = remember { mutableStateListOf<Interaction>() }
    LaunchedEffect(interactionSource) {
        interactionSource.interactions.collect { interaction ->
            when (interaction) {
                is PressInteraction.Press -> {
                    interactions.add(interaction)
                }

                is PressInteraction.Release -> {
                    interactions.remove(interaction.press)
                }

                is PressInteraction.Cancel -> {
                    interactions.remove(interaction.press)
                }

                is DragInteraction.Start -> {
                    interactions.add(interaction)
                }

                is DragInteraction.Stop -> {
                    interactions.remove(interaction.start)
                }

                is DragInteraction.Cancel -> {
                    interactions.remove(interaction.start)
                }
            }
        }
    }
    val isDraggedOrPressed = interactions.isNotEmpty()
    Button(
        onClick = {},
        interactionSource = interactionSource
    ) {
        Text(
            modifier = Modifier.animateContentSize(
                animationSpec = spring(
                    dampingRatio = Spring.DampingRatioLowBouncy,
                    stiffness = Spring.StiffnessLow,
                )
            ),
            text = if (isDraggedOrPressed) "Dragged Or Pressed" else "Idle"
        )
    }
}

@Composable
private fun HoveredInteractionButton() {
    val interactionSource = remember { MutableInteractionSource() }
    val isHovered by interactionSource.collectIsHoveredAsState()
    Button(onClick = {}, interactionSource = interactionSource) {
        AnimatedVisibility(visible = isHovered) {
            Icon(Icons.Filled.ShoppingCart, contentDescription = null)
            Spacer(Modifier.size(ButtonDefaults.IconSpacing))
        }
        Text("Add to cart")
    }
}

@Composable
private fun ScaledIndicationButton() {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()
    val scale by animateFloatAsState(targetValue = if (isPressed) 0.9f else 1f, label = "scale")
    Button(
        modifier = Modifier.scale(scale),
        onClick = {},
        interactionSource = interactionSource,
    ) {
        Text(text = "Scaled button when pressed")
    }
}

private class ScaleIndicationInstance(
    private val interactionSource: InteractionSource
) : Modifier.Node(), DrawModifierNode {

    var currentPressedPosition: Offset = Offset.Zero
    val animatedScalePercent = Animatable(1f)

    suspend fun animateToPressed(pressPosition: Offset) {
        currentPressedPosition = pressPosition
        animatedScalePercent.animateTo(0.5f, spring())
    }

    suspend fun animateToResting() {
        animatedScalePercent.animateTo(1f, spring())
    }

    override fun onAttach() {
        super.onAttach()
        coroutineScope.launch {
            interactionSource.interactions.collectLatest { interaction ->
                when (interaction) {
                    is PressInteraction.Press -> {
                        animateToPressed(interaction.pressPosition)
                    }

                    is PressInteraction.Release -> {
                        animateToResting()
                    }

                    is PressInteraction.Cancel -> {
                        animateToResting()
                    }
                }
            }
        }
    }

    override fun ContentDrawScope.draw() {
        scale(
            scale = animatedScalePercent.value,
            pivot = currentPressedPosition
        ) {
            this@draw.drawContent()
        }
    }
}

private object ScaleIndication : IndicationNodeFactory {
    override fun create(interactionSource: InteractionSource): DelegatableNode {
        return ScaleIndicationInstance(interactionSource)
    }

    override fun hashCode(): Int = -1

    override fun equals(other: Any?) = other === this
}

@Composable
private fun ScaledIndicationBox() {
    Box(
        modifier = Modifier
            .size(200.dp)
            .clickable(
                onClick = {},
                indication = ScaleIndication,
                interactionSource = remember { MutableInteractionSource() },
            )
            .background(Color.Blue)
    ) {
        Text("Scaled Indication", color = Color.White)
    }
}

@Composable
private fun NeonIndicationButton() {
    Box(
        modifier = Modifier
            .size(200.dp)
            .clickable(
                onClick = {},
                indication = NeonIndication(RoundedCornerShape(4.dp), 4.dp),
                interactionSource = remember { MutableInteractionSource() },
            )
            .background(Color.Blue)
    ) {
        Row(Modifier.padding(16.dp)) {
            Icon(Icons.Filled.ShoppingCart, contentDescription = null)
            Spacer(Modifier.size(ButtonDefaults.IconSpacing))
            Text("Add to cart")
        }
    }
}

private class NeonIndication(
    private val shape: Shape,
    private val borderWidth: Dp,
) : IndicationNodeFactory {

    override fun create(interactionSource: InteractionSource): DelegatableNode {
        return NeonIndicationInstance(shape, borderWidth * 2, interactionSource)
    }

    override fun hashCode(): Int = -1

    override fun equals(other: Any?) = other === this
}

private class NeonIndicationInstance(
    private val shape: Shape,
    private val borderWidth: Dp,
    private val interactionSource: InteractionSource,
) : Modifier.Node(), DrawModifierNode {

    var currentPressPosition: Offset = Offset.Zero
    val animatedProgress = Animatable(0f)
    val animatedPressAlpha = Animatable(1f)

    var pressedAnimation: Job? = null
    var restingAnimation: Job? = null

    fun animateToPressed(pressPosition: Offset, scope: CoroutineScope) {
        val currentPressedAnimation = pressedAnimation
        pressedAnimation = scope.launch {
            // Finish any existing animations, in case of a new press while we are still showing
            // an animation for a previous one
            restingAnimation?.cancelAndJoin()
            currentPressedAnimation?.cancelAndJoin()
            currentPressPosition = pressPosition
            animatedPressAlpha.snapTo(1f)
            animatedProgress.snapTo(0f)
            animatedProgress.animateTo(1f, tween(450))
        }
    }

    fun animateToResting(scope: CoroutineScope) {
        restingAnimation = scope.launch {
            // Wait for the existing press animation to finish if it is still ongoing
            pressedAnimation?.cancelAndJoin()
            animatedPressAlpha.animateTo(0f, tween(250))
            animatedProgress.snapTo(0f)
        }
    }

    override fun onAttach() {
        super.onAttach()
        coroutineScope.launch {
            interactionSource.interactions.collectLatest { interaction ->
                when (interaction) {
                    is PressInteraction.Press -> animateToPressed(
                        interaction.pressPosition,
                        this
                    )

                    is PressInteraction.Release -> animateToResting(this)
                    is PressInteraction.Cancel -> animateToResting(this)
                }
            }
        }
    }

    override fun ContentDrawScope.draw() {
        val (startPosition, endPosition) = calculateGradientStartAndEndFromPressPosition(
            currentPressPosition, size
        )
        val brush = animateBrush(
            startPosition = startPosition,
            endPosition = endPosition,
            progress = animatedProgress.value
        )
        val alpha = animatedPressAlpha.value

        drawContent()

        val outline = shape.createOutline(size, layoutDirection, this)
        // Draw overlay on top of content
        drawOutline(
            outline = outline,
            brush = brush,
            alpha = alpha * 0.1f
        )
        // Draw border on top of overlay
        drawOutline(
            outline = outline,
            brush = brush,
            alpha = alpha,
            style = Stroke(width = borderWidth.toPx())
        )
    }

    /**
     * Calculates a gradient start / end where start is the point on the bounding rectangle of
     * size [size] that intercepts with the line drawn from the center to [pressPosition],
     * and end is the intercept on the opposite end of that line.
     */
    private fun calculateGradientStartAndEndFromPressPosition(
        pressPosition: Offset,
        size: Size
    ): Pair<Offset, Offset> {
        // Convert to offset from the center
        val offset = pressPosition - size.center
        // y = mx + c, c is 0, so just test for x and y to see where the intercept is
        val gradient = offset.y / offset.x
        // We are starting from the center, so halve the width and height - convert the sign
        // to match the offset
        val width = (size.width / 2f) * sign(offset.x)
        val height = (size.height / 2f) * sign(offset.y)
        val x = height / gradient
        val y = gradient * width

        // Figure out which intercept lies within bounds
        val intercept = if (abs(y) <= abs(height)) {
            Offset(width, y)
        } else {
            Offset(x, height)
        }

        // Convert back to offsets from 0,0
        val start = intercept + size.center
        val end = Offset(size.width - start.x, size.height - start.y)
        return start to end
    }

    private fun animateBrush(
        startPosition: Offset,
        endPosition: Offset,
        progress: Float
    ): Brush {
        if (progress == 0f) return TransparentBrush

        // This is *expensive* - we are doing a lot of allocations on each animation frame. To
        // recreate a similar effect in a performant way, it would be better to create one large
        // gradient and translate it on each frame, instead of creating a whole new gradient
        // and shader. The current approach will be janky!
        val colorStops = buildList {
            when {
                progress < 1 / 6f -> {
                    val adjustedProgress = progress * 6f
                    add(0f to Blue)
                    add(adjustedProgress to Color.Transparent)
                }

                progress < 2 / 6f -> {
                    val adjustedProgress = (progress - 1 / 6f) * 6f
                    add(0f to Purple)
                    add(adjustedProgress * MaxBlueStop to Blue)
                    add(adjustedProgress to Blue)
                    add(1f to Color.Transparent)
                }

                progress < 3 / 6f -> {
                    val adjustedProgress = (progress - 2 / 6f) * 6f
                    add(0f to Pink)
                    add(adjustedProgress * MaxPurpleStop to Purple)
                    add(MaxBlueStop to Blue)
                    add(1f to Blue)
                }

                progress < 4 / 6f -> {
                    val adjustedProgress = (progress - 3 / 6f) * 6f
                    add(0f to Orange)
                    add(adjustedProgress * MaxPinkStop to Pink)
                    add(MaxPurpleStop to Purple)
                    add(MaxBlueStop to Blue)
                    add(1f to Blue)
                }

                progress < 5 / 6f -> {
                    val adjustedProgress = (progress - 4 / 6f) * 6f
                    add(0f to Yellow)
                    add(adjustedProgress * MaxOrangeStop to Orange)
                    add(MaxPinkStop to Pink)
                    add(MaxPurpleStop to Purple)
                    add(MaxBlueStop to Blue)
                    add(1f to Blue)
                }

                else -> {
                    val adjustedProgress = (progress - 5 / 6f) * 6f
                    add(0f to Yellow)
                    add(adjustedProgress * MaxYellowStop to Yellow)
                    add(MaxOrangeStop to Orange)
                    add(MaxPinkStop to Pink)
                    add(MaxPurpleStop to Purple)
                    add(MaxBlueStop to Blue)
                    add(1f to Blue)
                }
            }
        }

        return linearGradient(
            colorStops = colorStops.toTypedArray(),
            start = startPosition,
            end = endPosition
        )
    }

    companion object {
        val TransparentBrush = SolidColor(Color.Transparent)
        val Blue = Color(0xFF30C0D8)
        val Purple = Color(0xFF7848A8)
        val Pink = Color(0xFFF03078)
        val Orange = Color(0xFFF07800)
        val Yellow = Color(0xFFF0D800)
        const val MaxYellowStop = 0.16f
        const val MaxOrangeStop = 0.33f
        const val MaxPinkStop = 0.5f
        const val MaxPurpleStop = 0.67f
        const val MaxBlueStop = 0.83f
    }
}
