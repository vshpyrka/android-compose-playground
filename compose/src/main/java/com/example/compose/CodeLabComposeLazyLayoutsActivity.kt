package com.example.compose

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowCircleUp
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.dp
import com.example.compose.ui.theme.AndroidPlaygroundTheme
import kotlinx.coroutines.launch

class CodeLabComposeLazyLayoutsActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AndroidPlaygroundTheme {
                LazyRowComposable(
                    Modifier.fillMaxWidth()
                )
            }
        }
    }
}

@Preview(
    showBackground = true,
    heightDp = 600,
)

@Preview(
    showBackground = true,
    heightDp = 320,
    widthDp = 600,
)
@Composable
private fun LazyRowComposablePreview() {
    AndroidPlaygroundTheme {
        LazyRowComposable()
    }
}

data class LazyRowItem(val title: String)

@Composable
fun LazyRowComposable(
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier,
    ) {
        val items = List(30) { LazyRowItem("") }

        val state = rememberLazyListState()

        val showScrollToTopButton by remember {
            derivedStateOf { state.firstVisibleItemIndex > 0 }
        }

        /*
        LazyColumn(
            state = state,
            contentPadding = PaddingValues(vertical = 16.dp, horizontal = 8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxWidth(),
        ) {
            itemsIndexed(items) {index, item ->
                LazyRowItemComposable(index.toString())
            }
        }
        */

        /*
        LazyVerticalGrid(
            columns = GridCells.Adaptive(128.dp),
            contentPadding = PaddingValues(vertical = 16.dp, horizontal = 8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier.fillMaxWidth(),
        ) {
            itemsIndexed(items) { index, item ->
                LazyRowItemComposable(index = index.toString())
            }
        }
        */

        LazyVerticalGrid(
            columns = object : GridCells {
                override fun Density.calculateCrossAxisCellSizes(
                    availableSize: Int,
                    spacing: Int
                ): List<Int> {
                    val firstColumn = (availableSize - spacing) * 2 /3
                    val secondColumn = availableSize - spacing - firstColumn
                    return listOf(firstColumn, secondColumn)
                }
            },
            contentPadding = PaddingValues(vertical = 16.dp, horizontal = 8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier.fillMaxWidth(),
        ) {
            item(
                span = {
//                    maxLineSpan
//                    maxCurrentLineSpan
                    GridItemSpan(maxLineSpan)
                }
            ) {
                CategoryCard(title = "Fruits")
            }
            itemsIndexed(
                items,
            ) { index, item ->
                LazyRowItemComposable(index = index.toString())
            }
            item(
                span = {
//                    maxLineSpan
//                    maxCurrentLineSpan
                    GridItemSpan(maxLineSpan)
                }
            ) {
                CategoryCard(title = "Vegetables")
            }
            itemsIndexed(
                items,
                span = { index, item ->
                    GridItemSpan(if (index % 2 != 0) 2 else 1)
                }
            ) { index, item ->
                LazyRowItemComposable(index = index.toString())
            }
        }

        if (showScrollToTopButton) {
            val coroutineScope = rememberCoroutineScope()
            IconButton(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(8.dp),
                onClick = {
                    coroutineScope.launch {
                        state.animateScrollToItem(index = 0)
                    }
                }
            ) {
                Icon(
                    imageVector = Icons.Default.ArrowCircleUp,
                    contentDescription = null,
                )
            }
        }
    }
}

@Preview
@Composable
private fun CustomArrangementPreview() {
    val items = List(3) { "Item $it"}
    LazyColumn(
        modifier = Modifier.fillMaxHeight(),
        verticalArrangement = TopWithFooter
    ) {
        itemsIndexed(
            items,
        ) { index, item ->
            LazyRowItemComposable(index = index.toString())
        }
    }
}

object TopWithFooter : Arrangement.Vertical {
    override fun Density.arrange(totalSize: Int, sizes: IntArray, outPositions: IntArray) {
        var y = 0
        sizes.forEachIndexed { index, i ->
            outPositions[index] = y
            y += i
        }
        if (y < totalSize) {
            val lastIndex = outPositions.lastIndex
            outPositions[lastIndex] = totalSize - sizes.last()
        }
    }

}

@Composable
fun CategoryCard(
    title: String,
    modifier: Modifier = Modifier,
) {
    Surface(
        color = MaterialTheme.colorScheme.primary,
        shape = MaterialTheme.shapes.small,
        modifier = modifier
    ) {
        Text(text = title, Modifier.padding(8.dp))
    }
}

@Composable
fun LazyRowItemComposable(
    index: String,
    modifier: Modifier = Modifier
) {
    Surface(
        shape = MaterialTheme.shapes.small,
        color = MaterialTheme.colorScheme.surface,
        modifier = modifier
    ) {
        Column {
            Text(
                text = "Cupcake $index",
                style = MaterialTheme.typography.titleMedium
            )
            Text(
                text = "A tag line",
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}
