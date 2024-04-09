package com.example.compose

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.paddingFromBaseline
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyHorizontalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Spa
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationRail
import androidx.compose.material3.NavigationRailItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewDynamicColors
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.tooling.preview.PreviewScreenSizes
import androidx.compose.ui.unit.dp
import com.example.compose.ui.theme.AndroidPlaygroundTheme

class CodeLabComposeBasicLayoutActivity : ComponentActivity() {

    @OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AndroidPlaygroundTheme {
                val windowSizeClass = calculateWindowSizeClass(activity = this)
                MySootheApp(windowSizeClass)
            }
        }
    }
}

@Composable
fun MySootheApp(windowSize: WindowSizeClass) {
    when (windowSize.widthSizeClass) {
        WindowWidthSizeClass.Compact -> {
            MySootheAppPortrait()
        }
        WindowWidthSizeClass.Expanded -> {
            MySootheAppLandscape()
        }
    }
}

@PreviewScreenSizes
@PreviewLightDark
@PreviewDynamicColors
@Composable
private fun MySoothePreviewAll() {
    AndroidPlaygroundTheme {
        MySootheAppPortrait()
    }
}

@Preview(widthDp = 360, heightDp = 640)
@Composable
fun MySoothePreview() {
    AndroidPlaygroundTheme {
        MySootheAppPortrait()
    }
}

@Composable
fun MySootheAppPortrait(
    modifier: Modifier = Modifier
) {
    Scaffold(
        modifier = modifier,
        bottomBar = {
            SootheBottomNavigation()
        }
    ) { padding ->
        padding.calculateTopPadding()
        HomeScreen(
            modifier = Modifier.padding(padding)
        )
    }
}

@Preview(heightDp = 360, widthDp = 640)
@Composable
fun MySootheAppLandscapePreview() {
    AndroidPlaygroundTheme {
        MySootheAppLandscape()
    }
}

@Composable
fun MySootheAppLandscape(
    modifier: Modifier = Modifier
) {
    Surface(
        color = MaterialTheme.colorScheme.background,
        modifier = modifier,
    ) {
        Row {
            SootheNavigationRail()
            HomeScreen()
        }
    }
}

@Preview(
    showBackground = true,
    backgroundColor = 0xFFF0EAE2,
    heightDp = 150
)
@Composable
fun ScreenContentPreview() {
    AndroidPlaygroundTheme {
        HomeScreen(
            modifier = Modifier.padding(8.dp)
        )
    }
}

@Composable
fun HomeScreen(
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.verticalScroll(rememberScrollState()),
    ) {
        Spacer(modifier = Modifier.height(16.dp))
        SearchBar(
            modifier = Modifier.padding(horizontal = 16.dp)
        )
        HomeSection(title = "Align your body") {
            AlignYourBodyRow()
        }
        HomeSection(title = "Favorite Collections") {
            FavoriteCollectionsGrid()
        }
        Spacer(modifier = Modifier.height(16.dp))
    }
}

@Preview(
    showBackground = true,
    backgroundColor = 0xFFF0EAE2,
)
@Composable
fun SearchBarPreview() {
    AndroidPlaygroundTheme {
        SearchBar(modifier = Modifier.padding(8.dp))
    }
}

@Composable
fun SearchBar(
    modifier: Modifier = Modifier
) {
    TextField(
        value = "",
        onValueChange = {},
        leadingIcon = {
            Icon(Icons.Default.Search, contentDescription = null)
        },
        placeholder = {
            Text(text = "Search")
        },
        colors = TextFieldDefaults.colors(
            unfocusedContainerColor = MaterialTheme.colorScheme.surface,
            focusedContainerColor = MaterialTheme.colorScheme.surface,
        ),
        modifier = modifier
            .heightIn(min = 56.dp)
            .fillMaxWidth(),
    )
}

@Preview(
    showBackground = true,
    backgroundColor = 0xFFF0EAE2,
)
@Composable
fun AlignYourBodyElementPreview() {
    AndroidPlaygroundTheme {
        AlignYourBodyElement(
            drawable = R.drawable.ab1_inversions,
            text = "Inversions",
            modifier = Modifier.padding(8.dp)
        )
    }
}

@Composable
fun AlignYourBodyElement(
    @DrawableRes drawable: Int,
    text: String,
    modifier: Modifier = Modifier
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier,
    ) {
        Image(
            painterResource(id = drawable),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .size(88.dp)
                .clip(CircleShape)
        )
        Text(
            text = text,
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.paddingFromBaseline(top = 24.dp, bottom = 8.dp)
        )
    }
}

@Preview(
    showBackground = true,
    backgroundColor = 0xFFF0EAE2,
)
@Composable
fun FavoriteCollectionCardPreview() {
    AndroidPlaygroundTheme {
        FavoriteCollectionCard(
            drawable = R.drawable.fc2_nature_meditations,
            text = "Natural meditations",
            modifier = Modifier.padding(8.dp)
        )
    }
}

@Composable
fun FavoriteCollectionCard(
    @DrawableRes drawable: Int,
    text: String,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier,
        shape = MaterialTheme.shapes.small,
    ) {
        Row(
            modifier = Modifier.width(255.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Image(
                painterResource(id = drawable),
                contentDescription = null,
                modifier = Modifier.size(80.dp),
                contentScale = ContentScale.Crop,
            )
            Text(
                text = text,
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.padding(horizontal = 16.dp)
            )
        }
    }
}

@Preview(
    showBackground = true,
    backgroundColor = 0xFFF5F0EE
)
@Composable
fun AlignYourBodyRowPreview() {
    AndroidPlaygroundTheme {
        AlignYourBodyRow()
    }
}

@Composable
fun AlignYourBodyRow(
    modifier: Modifier = Modifier
) {
    LazyRow(
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        contentPadding = PaddingValues(horizontal = 16.dp),
        modifier = modifier,
    ) {
        items(alignYourBodyData) { item ->
            AlignYourBodyElement(
                drawable = item.drawable,
                text = item.text
            )
        }
    }
}

@Preview(
    showBackground = true,
    backgroundColor = 0xFFF5F0EE,
)
@Composable
fun FavoriteCollectionsGridPreview() {
    AndroidPlaygroundTheme {
        FavoriteCollectionsGrid(Modifier.padding(8.dp))
    }
}

@Composable
fun FavoriteCollectionsGrid(
    modifier: Modifier = Modifier
) {

    LazyHorizontalGrid(
        rows = GridCells.Fixed(2),
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        contentPadding = PaddingValues(horizontal = 16.dp),
        modifier = modifier.height(168.dp),
    ) {
        items(favoriteCollectionsData) { item ->
            FavoriteCollectionCard(
                drawable = item.drawable,
                text = item.text,
                modifier = Modifier.height(80.dp)
            )
        }
    }
}

@Preview(
    showBackground = true,
    backgroundColor = 0xFFF5F0EE,
)
@Composable
fun HomeSectionPreview() {
    AndroidPlaygroundTheme {
        HomeSection(
            title = "Align your body",
        ) {
            AlignYourBodyRow()
        }
    }
}

@Composable
fun HomeSection(
    title: String,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {

    Column(
        modifier = modifier
    ) {
        Text(
            text = title.uppercase(),
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier
                .paddingFromBaseline(top = 40.dp, bottom = 16.dp)
                .padding(horizontal = 16.dp)
        )
        content()
    }
}

@Preview(
    showBackground = true,
    backgroundColor = 0xFFF5F0EE,
)
@Composable
fun SootheBottomNavigationPreview() {
    AndroidPlaygroundTheme {
        SootheBottomNavigation(Modifier.padding(8.dp))
    }
}

@Composable
fun SootheBottomNavigation(
    modifier: Modifier = Modifier
) {
    NavigationBar(
        containerColor = MaterialTheme.colorScheme.surfaceVariant,
        modifier = modifier
    ) {
        NavigationBarItem(
            selected = true,
            onClick = { /*TODO*/ },
            icon = {
                Icon(Icons.Default.Spa, contentDescription = null)
            },
            label = {
                Text(text = "Home")
            }
        )
        NavigationBarItem(
            selected = false,
            onClick = { /*TODO*/ },
            icon = {
                Icon(Icons.Default.AccountCircle, contentDescription = null)
            },
            label = {
                Text(text = "Profile")
            }
        )
    }
}

@Preview
@Composable
fun SootheNavigationRailPreview() {
    AndroidPlaygroundTheme {
        SootheNavigationRail()
    }
}

@Composable
fun SootheNavigationRail(
    modifier: Modifier = Modifier
) {
    NavigationRail(
        containerColor = MaterialTheme.colorScheme.onBackground,
        modifier = modifier,
    ) {
        Column(
            modifier = Modifier
                .padding(horizontal = 8.dp)
                .fillMaxHeight(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            NavigationRailItem(
                selected = true,
                onClick = { /*TODO*/ },
                icon = {
                    Icon(Icons.Default.Spa, contentDescription = null)
                },
                label = {
                    Text(text = "Home")
                }
            )
            Spacer(modifier = Modifier.padding(8.dp))
            NavigationRailItem(
                selected = false,
                onClick = { /*TODO*/ },
                icon = {
                    Icon(Icons.Default.AccountCircle, contentDescription = null)
                },
                label = {
                    Text(text = "Profile")
                }
            )
        }
    }
}

private val alignYourBodyData = listOf(
    R.drawable.ab1_inversions to "Inversions",
    R.drawable.ab2_quick_yoga to "Quick yoga",
    R.drawable.ab3_stretching to "Stretching",
    R.drawable.ab4_tabata to "Tabata",
    R.drawable.ab5_hiit to "HIIT",
    R.drawable.ab6_pre_natal_yoga to "Pre-natal yoga"
).map { DrawableStringPair(it.first, it.second) }

private val favoriteCollectionsData = listOf(
    R.drawable.fc1_short_mantras to "Short mantras",
    R.drawable.fc2_nature_meditations to "Nature meditations",
    R.drawable.fc3_stress_and_anxiety to "Stress and anxiety",
    R.drawable.fc4_self_massage to "Self massage",
    R.drawable.fc5_overwhelmed to "Overwhelmed",
    R.drawable.fc6_nightly_wind_down to "Nightly wind down"
).map { DrawableStringPair(it.first, it.second) }

private data class DrawableStringPair(
    @DrawableRes val drawable: Int,
    val text: String
)
