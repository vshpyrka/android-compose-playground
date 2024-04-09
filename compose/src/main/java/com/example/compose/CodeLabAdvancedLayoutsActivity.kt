package com.example.compose

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.FloatRange
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.GenericShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.IntrinsicMeasurable
import androidx.compose.ui.layout.IntrinsicMeasureScope
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.layout.Measurable
import androidx.compose.ui.layout.MeasurePolicy
import androidx.compose.ui.layout.MeasureResult
import androidx.compose.ui.layout.MeasureScope
import androidx.compose.ui.layout.layout
import androidx.compose.ui.layout.layoutId
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.unit.dp
import com.example.compose.ui.theme.AndroidPlaygroundTheme

class CodeLabAdvancedLayoutsActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            AndroidPlaygroundTheme {
                ImageTest()
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
            animationProgress =  { 1f }
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
    ) {measurables, constraints ->

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


