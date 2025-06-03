package com.example.compose

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.selection.toggleable
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FabPosition
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.CollectionInfo
import androidx.compose.ui.semantics.CollectionItemInfo
import androidx.compose.ui.semantics.CustomAccessibilityAction
import androidx.compose.ui.semantics.ProgressBarRangeInfo
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.clearAndSetSemantics
import androidx.compose.ui.semantics.collectionInfo
import androidx.compose.ui.semantics.collectionItemInfo
import androidx.compose.ui.semantics.customActions
import androidx.compose.ui.semantics.heading
import androidx.compose.ui.semantics.hideFromAccessibility
import androidx.compose.ui.semantics.isTraversalGroup
import androidx.compose.ui.semantics.onClick
import androidx.compose.ui.semantics.progressBarRangeInfo
import androidx.compose.ui.semantics.role
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.stateDescription
import androidx.compose.ui.semantics.toggleableState
import androidx.compose.ui.semantics.traversalIndex
import androidx.compose.ui.state.ToggleableState
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.compose.ui.theme.AndroidPlaygroundTheme

class ComposeAccessibilityActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TalkBackTraverseOrder()
        }
    }
}

@Composable
private fun ClickableBox() {
    Column(
        verticalArrangement = Arrangement.spacedBy(4.dp),
        modifier = Modifier
            .verticalScroll(rememberScrollState())
    ) {
        Box(
            Modifier
                .clickable(
                    onClick = {},
                    onClickLabel = "Clickable box"
                )
                .size(150.dp)
                .background(Color.Magenta)
        )
        Box(
            Modifier
                .semantics {
                    onClick(
                        label = "Clickable area",
                        action = { true },
                    )
                }
                .size(150.dp)
                .background(Color.Magenta)
        )

        Box(Modifier.clickable { /* TODO */ }) { Text("Click me!") }

        IconButton(onClick = {}) {
            Icon(
                imageVector = Icons.Filled.Share,
                contentDescription = "Share image resource"
            )
        }

        // Article Accessibility in Compose. Double tap to open this article.
        Row(
            modifier = Modifier
                .clickable(
                    onClickLabel = "Open this article",
                    onClick = {}
                )
        ) {
            Icon(
                imageVector = Icons.Filled.Share,
                contentDescription = "Article"
            )
            Text("Accessibility in Compose")
        }

        // Double tap and hold to bookmark this article.
        Row(
            modifier = Modifier
                .combinedClickable(
                    onClick = { /* Handle click */ },
                    onLongClick = { /* Handle long click */ },
                    onClickLabel = "Open this article",
                    onLongClickLabel = "Bookmark this article"
                )
        ) {}

        Row(
            modifier = Modifier.semantics(
                mergeDescendants = true,
                properties = {},
            )
        ) {
            Image(
                imageVector = Icons.Filled.AccountCircle,
                contentDescription = null // decorative
            )
            Column {
                Text("John Doe")
                Text("April 02 • 30 min read")
            }
        }

        var bookmarked by remember { mutableStateOf(false) }

        Row(
            modifier = Modifier
                .clickable {
                    bookmarked = !bookmarked
                }
                .semantics {
                    stateDescription = if (bookmarked) "Bookmarked" else "Not bookmarked"
                    customActions = listOf(
                        CustomAccessibilityAction(
                            label = if (bookmarked) "Unbookmark" else "Bookmark",
                            action = {
                                bookmarked = !bookmarked
                                true
                            }
                        )
                    )
                },
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                imageVector = Icons.Filled.AccountCircle,
                contentDescription = null
            )
            Column {
                Text("A little thing about Android module paths")
                Text("Romeo Montague - 1 min read")
            }
            Checkbox(
                checked = bookmarked,
                onCheckedChange = { bookmarked = !bookmarked },
                modifier = Modifier.clearAndSetSemantics { }
            )
        }

        var topicChecked by remember { mutableStateOf(false) }

        Row(
            modifier = Modifier
                .semantics {
                    stateDescription = if (topicChecked) "Subscribed" else "Not subscribed"
                }
                .toggleable(
                    value = topicChecked,
                    onValueChange = {
                        topicChecked = !topicChecked
                    },
                ),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                imageVector = Icons.Filled.AccountCircle,
                contentDescription = null
            )
            Column {
                Text("A little thing about Android module paths")
                Text("Romeo Montague - 1 min read")
            }
            Checkbox(
                checked = topicChecked,
                onCheckedChange = { topicChecked = !topicChecked },
                modifier = Modifier.clearAndSetSemantics { }
            )
        }

        var favoriteChecked by remember { mutableStateOf(false) }
        Row(
            modifier = Modifier
                .toggleable(
                    value = favoriteChecked,
                    onValueChange = {
                        favoriteChecked = it
                    },
                )
                .clearAndSetSemantics {
                    stateDescription = if (favoriteChecked) "Favorited" else "Not favorited"
                    toggleableState = ToggleableState(favoriteChecked)
                    role = Role.Switch
                },
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Filled.Favorite,
                contentDescription = null // decorative
            )
            Text("Favorite?")
        }

        Box(
            modifier = Modifier.semantics {
                hideFromAccessibility() // This box will be ignored by TalkBack
            }
        )

        // Error: Please add both email and password
        Box(
            modifier = Modifier.semantics {
                error("Please add both email and password")
            }
        ) {
            Text("Fields cannot be empty")
        }

        CircularProgressIndicator(
            modifier = Modifier.semantics {
                progressBarRangeInfo = ProgressBarRangeInfo(
                    current = 0.5f, // 50% progress
                    range = 0f..1f,
                )
            }
        )

        // In list, 3 items
        LazyColumn(
            modifier = Modifier.semantics {
                collectionInfo = CollectionInfo(
                    rowCount = 3, // Number of rows in the list
                    columnCount = 1, // Number of columns in the list
                )
            }
        ) {
            item {
                Text(
                    modifier = Modifier
                        .padding(16.dp)
                        .semantics {
                            collectionItemInfo = CollectionItemInfo(
                                rowIndex = 0,
                                rowSpan = 0,
                                columnIndex = 0,
                                columnSpan = 0,
                            )
                        },
                    text = "Item 1",
                )
            }
        }

        Text(
            modifier = Modifier.semantics {
                heading()
            },
            text = "Collections and sequences in Kotlin"
        )
        Text("Working with collections is a common task and the Kotlin Standard library offers many great utility functions.")
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun TalkBackTraverseOrder() {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text("Top App Bar")
                }
            )
        },
        floatingActionButtonPosition = FabPosition.End,
        floatingActionButton = {
            Box(
                modifier = Modifier.semantics { isTraversalGroup = true; traversalIndex = -1f }
            ) {
                FloatingActionButton(onClick = {}) {
                    Icon(
                        imageVector = Icons.Filled.Favorite,
                        contentDescription = "Mark as favorite"
                    )
                }
            }
        },
        content = { paddingValues ->
            Column(modifier = Modifier.padding(paddingValues)) {
                Text("One")
                Text("Two")
                Text("Three")
                Text("Four")
            }
        },
        bottomBar = {
            BottomAppBar {
                Text("Bottom App Bar")
            }
        }
    )
}

@Preview(showBackground = true)
@Composable
private fun ClickableBoxPreview() {
    AndroidPlaygroundTheme {
        ClickableBox()
    }
}
