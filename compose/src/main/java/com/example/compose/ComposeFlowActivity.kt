package com.example.compose

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowColumn
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AssistChip
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.datasource.LoremIpsum
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.compose.ui.theme.AndroidPlaygroundTheme

class ComposeFlowActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AndroidPlaygroundTheme {
                Column {
                    FlowExample()
                    ContextualFlow()
                    WeightFlowExample()
                    RandomWeightFlowExample()
                }
            }
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun FlowExample() {
    FlowRow(
        modifier = Modifier.padding(8.dp),
        horizontalArrangement = Arrangement.SpaceAround,
        verticalArrangement = Arrangement.Center,
        maxItemsInEachRow = 3,
    ) {
        List(10) {
            LoremIpsum((1..3).random()).values.joinToString()
        }.forEach {
            AssistChip(
                modifier = Modifier.align(Alignment.CenterVertically),
                onClick = {},
                label = { Text(it) }
            )
        }
    }
}

@Composable
private fun ContextualFlow() {
//    val totalCount = 40
//    var maxLines by remember {
//        mutableStateOf(2)
//    }
//
//    val moreOrCollapseIndicator = @Composable { scope: ContextualFlowRowOverflowScope ->
//        val remainingItems = totalCount - scope.shownItemCount
//
//        AssistChip(
//            onClick = {
//                if (remainingItems == 0) {
//                    maxLines = 2
//                } else {
//                    maxLines += 5
//                }
//            },
//            label = { Text(
//                if (remainingItems == 0) "Less" else "+$remainingItems"
//            ) }
//        )
//    }
//
//    ContextualFlowRow(
//        modifier = Modifier
//            .safeDrawingPadding()
//            .fillMaxWidth(1f)
//            .padding(16.dp)
//            .wrapContentHeight(align = Alignment.Top)
//            .verticalScroll(rememberScrollState()),
//        verticalArrangement = Arrangement.spacedBy(4.dp),
//        horizontalArrangement = Arrangement.spacedBy(8.dp),
//        maxLines = maxLines,
//        overflow = ContextualFlowRowOverflow.expandOrCollapseIndicator(
//            minRowsToShowCollapse = 4,
//            expandIndicator = moreOrCollapseIndicator,
//            collapseIndicator = moreOrCollapseIndicator
//        ),
//        itemCount = totalCount
//    ) { index ->
//        AssistChip(
//            onClick = {},
//            label = { Text("Item $index") }
//        )
//    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun WeightFlowExample() {
    AndroidPlaygroundTheme {
        val rows = 3
        val columns = 3
        FlowRow(
            modifier = Modifier.padding(4.dp),
            horizontalArrangement = Arrangement.spacedBy(4.dp),
            maxItemsInEachRow = rows
        ) {
            val itemModifier = Modifier
                .padding(4.dp)
                .weight(1f)
                .aspectRatio(1f)
                .clip(RoundedCornerShape(8.dp))
                .background(Color.Magenta)
            repeat(rows * columns) {
                Spacer(modifier = itemModifier)
            }
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun RandomWeightFlowExample() {
    FlowRow(
        modifier = Modifier.padding(4.dp),
        horizontalArrangement = Arrangement.spacedBy(4.dp),
        maxItemsInEachRow = 2,
    ) {
        val itemModifier = Modifier
            .padding(4.dp)
            .height(80.dp)
            .clip(RoundedCornerShape(8.dp))
            .background(Color.Green)
        repeat(6) { item ->
            if ((item + 1) % 3 == 0) {
                Box(
                    modifier = itemModifier.fillMaxWidth()
                )
            } else {
                Box(
                    modifier = itemModifier.weight(0.5f)
                )
            }
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun FractionalExample() {
    FlowRow(
        modifier = Modifier.padding(4.dp),
        horizontalArrangement = Arrangement.spacedBy(4.dp),
        maxItemsInEachRow = 3,
    ) {
        val itemModifier = Modifier
            .clip(RoundedCornerShape(8.dp))
            .height(300.dp)
        Box(
            itemModifier
                .width(60.dp)
                .background(Color.Red)
        )
        Box(
            itemModifier
                .fillMaxWidth(0.7f)
                .background(Color.Green)
        )
        Box(
            itemModifier
                .weight(1f)
                .background(Color.Blue)
        )
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun FillMaxWidthFlowExample() {
    AndroidPlaygroundTheme {
        FlowColumn(
            Modifier
                .padding(20.dp)
                .fillMaxHeight()
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            maxItemsInEachColumn = 5,
        ) {
            List(10) {
                LoremIpsum((1..3).random()).values.joinToString()
            }.forEach {
                Box(
                    Modifier
//                        .fillMaxColumnWidth()
                        .border(1.dp, Color.DarkGray, RoundedCornerShape(8.dp))
                        .padding(8.dp)
                ) {

                    Text(
                        text = it,
                        fontSize = 18.sp,
                        modifier = Modifier.padding(3.dp)
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun FlowExamplePreview() {
    AndroidPlaygroundTheme {
        FlowExample()
    }
}

@Preview(showBackground = true)
@Composable
private fun ContextualFlowPreview() {
    AndroidPlaygroundTheme {
        ContextualFlow()
    }
}

@Preview(showBackground = true)
@Composable
private fun WeightFlowPreview() {
    AndroidPlaygroundTheme {
        WeightFlowExample()
    }
}

@Preview(showBackground = true)
@Composable
private fun RandomWeightFlowPreview() {
    AndroidPlaygroundTheme {
        RandomWeightFlowExample()
    }
}

@Preview(showBackground = true)
@Composable
private fun FractionalPreview() {
    AndroidPlaygroundTheme {
        FractionalExample()
    }
}


@Preview(showBackground = true)
@Composable
private fun FillMaxWidthFlowPreview() {
    AndroidPlaygroundTheme {
        FillMaxWidthFlowExample()
    }
}
