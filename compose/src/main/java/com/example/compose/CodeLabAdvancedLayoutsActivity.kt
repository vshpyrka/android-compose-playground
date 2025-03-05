package com.example.compose

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.GenericShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.IntrinsicMeasurable
import androidx.compose.ui.layout.IntrinsicMeasureScope
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.layout.Measurable
import androidx.compose.ui.layout.MeasurePolicy
import androidx.compose.ui.layout.MeasureResult
import androidx.compose.ui.layout.MeasureScope
import androidx.compose.ui.layout.Placeable
import androidx.compose.ui.layout.layout
import androidx.compose.ui.layout.layoutId
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextLayoutResult
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.round
import com.example.compose.ui.theme.AndroidPlaygroundTheme
import kotlin.math.max
import kotlin.math.roundToInt
import kotlin.text.Typography.ellipsis

class CodeLabAdvancedLayoutsActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            AndroidPlaygroundTheme {
                AdaptiveButtonsEnd()
            }
        }
    }
}

@Preview
@Composable
private fun ImageTestPreview() {
    AndroidPlaygroundTheme {
        ImageTest(
            Modifier.size(300.dp, 200.dp)
        )
    }
}

@Composable
fun ImageTest(
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier.background(Color.Cyan),
    ) {
        Image(
            painterResource(id = R.drawable.ic_parrot),
            contentDescription = null,
            Modifier
                .background(Color.Magenta)
                .clip(CircleShape)
                .size(100.dp)
                .padding(10.dp)
        )
    }
}


@Preview(showBackground = true)
@Composable
private fun CustomLayoutPreview() {
    AndroidPlaygroundTheme {
        CustomLayout(
            modifier = Modifier.fillMaxSize()
        ) {
            Text(text = "Hello")
            Text(text = "Android")
            Text(text = "World")
        }
    }
}

@Composable
fun CustomLayout(
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    Layout(
        content = content,
        measurePolicy = { measurables, constraints ->
            // Measurement step
            // Determine sizes of components

            val placebles = measurables.map { // measure only once
                it.measure(
                    constraints.copy(
                        minWidth = constraints.minWidth + 10,
                        maxWidth = constraints.maxWidth + 10,
                    )
                )
            }

            layout(
                placebles.maxOf { it.width },
                placebles.sumOf { it.height }
            ) {
                // Placement step
                // Determine positions of components

                var y = 0
                placebles.forEach {
                    //  it.placeRelative() - will place relatively to locale
                    it.placeRelative(x = 0, y = y)
                    y += it.height
                }
            }
        }
    )
}

@Preview(showBackground = true)
@Composable
private fun VerticalGridPreview() {
    AndroidPlaygroundTheme {
        VerticalGrid(
            modifier = Modifier.fillMaxSize()
        ) {
            Text(text = "Hello")
            Text(text = "Android")
            Text(text = "World")
        }
    }
}

@Composable
fun VerticalGrid(
    modifier: Modifier = Modifier,
    columns: Int = 2,
    content: @Composable () -> Unit
) {
    Layout(
        content = content,
        measurePolicy = { measurables, constraints ->
            val itemWidth = constraints.maxWidth / columns
            // Keep given height constraints, but set an exact width
            val itemConstraints = constraints.copy(
                minWidth = itemWidth,
                maxWidth = itemWidth,
            )
            // measure each item with these constraints
            val placebles = measurables.map { it.measure(itemConstraints) }

            layout(
                placebles.maxOf { it.width },
                placebles.sumOf { it.height }
            ) {

                // Some logic for placement

                var y = 0
                placebles.forEachIndexed { index, placeble ->
                    //  it.placeRelative() - will place relatively to locale
                    placeble.placeRelative(x = 0, y = y)
                    y += placeble.height
                }
            }
        }
    )
}

@Preview(showBackground = true)
@Composable
private fun ButtonNavItemPreview() {
    AndroidPlaygroundTheme {
        BottomNavItem(
            icon = {
                Icon(imageVector = Icons.Default.Search, contentDescription = null)
            },
            text = {
                Text(text = "Hello")
            },
            animationProgress = { 1f }
        )
    }
}

@Composable
fun BottomNavItem(
    modifier: Modifier = Modifier,
    icon: @Composable BoxScope.() -> Unit,
    text: @Composable BoxScope.() -> Unit,
//    @FloatRange(from = 0.0, to = 1.0) animationProgress: Float
    animationProgress: () -> Float,
) {
    Layout(
        modifier = modifier,
        content = {
            Box(
                modifier = Modifier.layoutId("icon"),
                content = icon
            )
            Box(
                modifier = Modifier.layoutId("text"),
                content = text
            )
        }
    ) { measurables, constraints ->

        val iconPlaceble = measurables.first { it.layoutId == "icon" }.measure(constraints)
        val textPlaceble = measurables.first { it.layoutId == "text" }.measure(constraints)

        val maxWidth = constraints.maxWidth
        val maxHeight = constraints.maxHeight

        val iconY = (maxHeight - iconPlaceble.height) / 2
        val textY = (maxHeight - textPlaceble.height) / 2

        val progress = animationProgress()
        val textWidth = textPlaceble.width * progress
        val iconX = (maxWidth - textWidth - iconPlaceble.width) / 2
        val textX = iconX + iconPlaceble.width

        layout(maxWidth, maxHeight) {
            iconPlaceble.placeRelative(iconX.toInt(), iconY)
            if (progress != 0f) {
                textPlaceble.placeRelative(textX.toInt(), textY)
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun LayoutModifierExamplePreview() {
    AndroidPlaygroundTheme {
        LayoutModifierExample()
    }
}

@Composable
fun LayoutModifierExample() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.LightGray)
            .padding(40.dp)
    ) {
        Button(onClick = { /*TODO*/ }, modifier = Modifier.fillMaxWidth()) { Text(text = "Hello") }

        Button(
            onClick = { /*TODO*/ },
            modifier = Modifier
                .layout { measurable, constraints ->

                    val placeable = measurable.measure(
                        constraints.copy(
                            maxWidth = constraints.maxWidth + 80.dp.roundToPx()
                        )
                    )

                    layout(placeable.width, placeable.height) {
                        placeable.place(0, 0)
                    }
                }
        ) {
            Text(text = "Hello")
        }

        Button(
            onClick = { /*TODO*/ },
            modifier = Modifier.requiredWidth(1000.dp) // Override parent layout constraints
        ) {
            Text(text = "Hello")
        }
        Button(onClick = { /*TODO*/ }, modifier = Modifier.fillMaxWidth()) { Text(text = "Hello") }


        BoxWithConstraints {
            if (maxHeight < 300.dp) {
                // BigImage()
            } else {
                // SmallImage()
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun IntrinsicExamplePreview() {
    AndroidPlaygroundTheme {
        Box(Modifier.fillMaxSize()) {
            IntrinsicExample()
        }
    }
}

@Composable
fun IntrinsicExample() {
    Column(
        modifier = Modifier
            .background(Color.LightGray)
            .width(IntrinsicSize.Max)
    ) {
        Text(
            text = "Mad",
            Modifier
                .background(Color.Gray)
                .fillMaxWidth()
        )
        Text(
            text = "Skills",
            Modifier
                .background(Color.Gray)
                .fillMaxWidth()
        )
        Text(
            text = "Layouts",
            Modifier
                .background(Color.Gray)
                .fillMaxWidth()
        )
        Text(
            text = "And Modifiers",
            Modifier
                .background(Color.Gray)
                .fillMaxWidth()
        )
    }
}

@Composable
fun MyCustomLayout(
    content: @Composable () -> Unit,
    modifier: Modifier = Modifier
) {
    Layout(
        measurePolicy = object : MeasurePolicy {

            override fun MeasureScope.measure(
                measurables: List<Measurable>,
                constraints: Constraints
            ): MeasureResult {
                TODO("Not yet implemented")
            }

            override fun IntrinsicMeasureScope.maxIntrinsicHeight(
                measurables: List<IntrinsicMeasurable>,
                width: Int
            ): Int {
                TODO("Not yet implemented")
            }
        }
    )
}

@Preview(showBackground = true)
@Composable
private fun HexagonPreview() {
    AndroidPlaygroundTheme {
        HexagonList(
            Modifier
                .fillMaxSize()
                .background(Color.Yellow)
        )
    }
}

@Composable
fun HexagonList(
    modifier: Modifier = Modifier
) {
    LazyColumn(
        verticalArrangement = Arrangement.spacedBy((-92).dp),
        contentPadding = PaddingValues(vertical = 16.dp, horizontal = 8.dp),
        modifier = modifier
    ) {
        items(50) {
            val isEven = it % 2 == 0
            Box(
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                if (isEven) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Start,
                    ) {
                        Hexagon(it)
                    }
                } else {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.End,
                    ) {
                        Hexagon(it)
                    }
                }
            }
        }
    }
}

@Composable
fun Hexagon(position: Int) {
    Box(
        modifier = Modifier
            .fillMaxWidth(0.55f)
            .height(200.dp)
            .clip(
                GenericShape { size, layoutDirection ->
                    val width = size.width
                    val height = size.height
                    moveTo(width / 4, 0f)
                    lineTo((width / 4) * 3, 0f)
                    lineTo(width, height / 2)
                    lineTo((width / 4) * 3, height)
                    lineTo(width / 4, height)
                    lineTo(0f, height / 2)
                    close()
                }
            )
            .background(MaterialTheme.colorScheme.primary),
        contentAlignment = Alignment.Center,
    ) {
        Text(text = "Item $position", style = MaterialTheme.typography.headlineLarge)
    }
}

@Composable
fun AdaptiveButtonsEnd(
    modifier: Modifier = Modifier
) {
    Layout(
        modifier = modifier,
        content = {
            Button(
                modifier = Modifier.layoutId("primary"),
                onClick = {}
            ) {
                Text("Primary")
            }
            Button(
                modifier = Modifier.layoutId("secondary"),
                onClick = {}
            ) {
                Text("Secondary")
            }
        },
        measurePolicy = { measurables, constraints ->
            val primaryButtonMeasurable = measurables.find { it.layoutId == "primary" }!!
            val secondaryButtonMeasurable = measurables.find { it.layoutId == "secondary" }!!

            val primaryButtonMinIntrinsicWidth = primaryButtonMeasurable
                .minIntrinsicWidth(constraints.maxHeight)
            val secondaryButtonMinIntrinsicWidth = secondaryButtonMeasurable
                .minIntrinsicWidth(constraints.maxHeight)

            val showHorizontally = primaryButtonMinIntrinsicWidth <= constraints.maxWidth / 2 &&
                    secondaryButtonMinIntrinsicWidth <= constraints.maxWidth / 2

            val width = constraints.minWidth
            val height: Int
            val primaryButtonPlaceable: Placeable
            val secondaryButtonPlaceable: Placeable

            if (showHorizontally) {
                val halfWidthConstraints = constraints.copy(
                    minWidth = constraints.maxWidth / 2,
                    maxWidth = constraints.maxWidth / 2,
                )
                primaryButtonPlaceable = primaryButtonMeasurable.measure(halfWidthConstraints)
                secondaryButtonPlaceable = secondaryButtonMeasurable.measure(halfWidthConstraints)
                height = max(primaryButtonPlaceable.height, secondaryButtonPlaceable.height)
            } else {
                val fullWidthConstraints = constraints.copy(
                    minWidth = constraints.maxWidth
                )
                primaryButtonPlaceable = primaryButtonMeasurable.measure(fullWidthConstraints)
                secondaryButtonPlaceable = secondaryButtonMeasurable.measure(fullWidthConstraints)
                height = primaryButtonPlaceable.height + secondaryButtonPlaceable.height
            }
            layout(width, height) {
                if (showHorizontally) {
                    primaryButtonPlaceable.placeRelative(
                        width / 2,
                        (height - primaryButtonPlaceable.height) / 2,
                    )
                    secondaryButtonPlaceable.placeRelative(
                        0,
                        (height - secondaryButtonPlaceable.height) / 2,
                    )
                } else {
                    primaryButtonPlaceable.placeRelative(
                        0,
                        0,
                    )
                    secondaryButtonPlaceable.placeRelative(
                        0,
                        primaryButtonPlaceable.height,
                    )
                }
            }
        }
    )
}

@Preview(widthDp = 200, heightDp = 300)
@Preview(widthDp = 300, heightDp = 300)
@Composable
private fun AdaptiveButtonsEndPreview() {
    AndroidPlaygroundTheme {
        Box {
            AdaptiveButtonsEnd(Modifier.fillMaxWidth())
        }
    }
}

@Preview(widthDp = 200, heightDp = 200)
@Preview(widthDp = 300, heightDp = 200)
@Preview(widthDp = 300, heightDp = 400)
@Preview(widthDp = 300, heightDp = 400, locale = "ar")
@Composable
fun TextWidthBadge() {
    val text =
        "Lorem Ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua."
    Surface(
        modifier = Modifier.wrapContentSize()
    ) {
        var textLayoutResult: TextLayoutResult? by remember { mutableStateOf(null) }
        Layout(
            content = {
                Text(
                    modifier = Modifier.layoutId("text"),
                    text = text,
                    onTextLayout = {
                        textLayoutResult = it
                    }
                )
                Icon(
                    Icons.Default.CheckCircle,
                    contentDescription = "Received",
                    modifier = Modifier
                        .layoutId("badge")
                        .size(24.dp)
                )
            },
            measurePolicy = { measurables, constraints ->
                val textPlaceable =
                    measurables.find { it.layoutId == "text" }!!.measure(constraints)
                val badgePlaceable =
                    measurables.find { it.layoutId == "badge" }!!.measure(constraints)
                val badgeWidth = badgePlaceable.width
                val badgeHeight = badgePlaceable.height

                val newTextLayoutResult = textLayoutResult!!

                val lastLineEndBoundingBox = newTextLayoutResult.getBoundingBox(
                    newTextLayoutResult.getLineEnd(
                        lineIndex = newTextLayoutResult.lineCount - 1,
                        visibleEnd = true,
                    ) - 1
                )

                val lastLineRelativeOffset = IntOffset(
                    when (layoutDirection) {
                        LayoutDirection.Ltr -> lastLineEndBoundingBox.right
                        LayoutDirection.Rtl -> newTextLayoutResult.size.width - lastLineEndBoundingBox.left
                    }.roundToInt(),
                    lastLineEndBoundingBox.top.roundToInt(),
                )

                val displayBadgeInLastLine =
                    constraints.maxWidth - lastLineRelativeOffset.x >= badgeWidth
                val width: Int
                val height: Int
                val badgeX: Int
                val badgeY: Int

                if (displayBadgeInLastLine) {
                    width = max(
                        newTextLayoutResult.size.width,
                        lastLineRelativeOffset.x + badgeWidth
                    )
                    height = max(
                        newTextLayoutResult.size.height,
                        lastLineRelativeOffset.y + badgeHeight
                    )
                    badgeX = lastLineRelativeOffset.x
                    badgeY = lastLineRelativeOffset.y
                } else {
                    width = max(newTextLayoutResult.size.width, badgeWidth)
                    height = newTextLayoutResult.size.height + badgeHeight
                    badgeX = max(0, newTextLayoutResult.size.width - badgeWidth)
                    badgeY = newTextLayoutResult.size.height
                }
                layout(width, height) {
                    textPlaceable.placeRelative(0, 0)
                    badgePlaceable.placeRelative(badgeX, badgeY)
                }
            }
        )
    }
}

@Preview(widthDp = 200, heightDp = 300)
@Preview(widthDp = 300, heightDp = 300)
@Preview(widthDp = 400, heightDp = 300)
@Preview(widthDp = 500, heightDp = 300)
@Preview(widthDp = 600, heightDp = 300)
@Composable
fun MoreButtonTextEnd() {
    val maxLines = 5
    val text =
        "Lorem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry's standard dummy text ever since the 1500s, when an unknown printer took a galley of type and scrambled it to make a type specimen book."

    val textMeasurer = rememberTextMeasurer()
    var fullTextLayoutResult: TextLayoutResult? by remember { mutableStateOf(null) }
    var showButton by remember { mutableStateOf(false) }

    var ellipsizedLayoutResult: TextLayoutResult? by remember { mutableStateOf(null) }

    Surface(
        modifier = Modifier.wrapContentSize()
    ) {
        val contentColor = LocalContentColor.current
        val style = LocalTextStyle.current

        Layout(
            content =  {
                Text(
                    modifier = Modifier
                        .layoutId("text")
                        .drawBehind {
                            drawText(
                                if (showButton) {
                                    ellipsizedLayoutResult!!
                                } else {
                                    fullTextLayoutResult!!
                                },
                                contentColor,
                            )
                        },
                    text = text,
                    style = style,
                    overflow = TextOverflow.Ellipsis,
                    maxLines = maxLines,
                    onTextLayout = {
                        fullTextLayoutResult = it
                    },
                    // Text will be rendered in the modifier, but keep the full text for semantics
                    color = Color.Transparent
                )

                Button(
                    modifier = Modifier.layoutId("button"),
                    onClick = {},
                ) {
                    Text("More")
                }
            },
            measurePolicy = { measurable, constraints ->
                val textMeasurable = measurable.find { it.layoutId == "text" }
                val textPlaceable = textMeasurable!!.measure(constraints)

                // The full text layout is now available from the above measure
                val newFullTextLayoutResult = fullTextLayoutResult!!

                val buttonMeasurable = measurable.find { it.layoutId == "button" }
                val buttonPlaceable = buttonMeasurable!!.measure(constraints)

                // We need to show the button if the last line was ellipsized
                showButton = newFullTextLayoutResult.isLineEllipsized(newFullTextLayoutResult.lineCount - 1)

                if (showButton) {
                    val ellipsisTextLayoutResult = textMeasurer.measure(
                        text = ellipsis.toString(),
                        style = style,
                    )

                    // Determine how much space is available for the last line of text without
                    // the ellipsis
                    val lastLineTextWidth = constraints.maxWidth -
                            buttonPlaceable.width -
                            ellipsisTextLayoutResult.size.width
                    val lastCharOffset = newFullTextLayoutResult.getOffsetForPosition(
                        Offset(
                            lastLineTextWidth.toFloat(),
                            newFullTextLayoutResult.getLineBottom(maxLines - 1)
                        )
                    )

                    val newEllipsizedLayoutResult = textMeasurer.measure(
                        text.substring(0, lastCharOffset) + ellipsis,
                        style = style,
                        constraints = constraints,
                    )
                    ellipsizedLayoutResult = newEllipsizedLayoutResult

                    val lastLineEndBoundingBox = newEllipsizedLayoutResult.getBoundingBox(
                        newEllipsizedLayoutResult.getLineEnd(
                            newEllipsizedLayoutResult.lineCount - 1,
                            visibleEnd = true,
                        ) - 1
                    )

                    val lastLineRelativeOffset = Offset(
                        when (layoutDirection) {
                            LayoutDirection.Ltr -> lastLineEndBoundingBox.right
                            LayoutDirection.Rtl -> newEllipsizedLayoutResult.size.width - lastLineEndBoundingBox.left
                        },
                        lastLineEndBoundingBox.top
                    ).round()

                    val width = constraints.maxWidth
                    val height = max(
                        newFullTextLayoutResult.size.height,
                        lastLineRelativeOffset.y + buttonPlaceable.height
                    )
                    val buttonX = constraints.maxWidth - buttonPlaceable.width
                    val buttonY = lastLineRelativeOffset.y

                    layout(width, height) {
                        textPlaceable.placeRelative(0, 0)
                        buttonPlaceable.placeRelative(buttonX, buttonY)
                    }
                } else {
                    layout(
                        width = newFullTextLayoutResult.size.width,
                        height = newFullTextLayoutResult.size.height
                    ) {
                        textPlaceable.placeRelative(0, 0)
                    }
                }
            }
        )
    }
}