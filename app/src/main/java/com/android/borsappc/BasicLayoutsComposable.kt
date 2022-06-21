package com.android.borsappc

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyHorizontalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.material.MaterialTheme.colors
import androidx.compose.material.MaterialTheme.typography
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.fontResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.android.borsappc.ui.BorsaPPCTheme
import org.intellij.lang.annotations.JdkConstants


data class ItemAlignBody(val drawable: Int, val string: Int)

@Composable
fun HomeSection(
    @StringRes title: Int,
    content: @Composable () -> Unit
) {
    Column() {
        Text(stringResource(title).uppercase(),
            modifier = Modifier
                .padding(horizontal = 8.dp)
                .paddingFromBaseline(
                    top = 40.dp,
                    bottom = 8.dp
                ),
        )
        content()
    }
}

@Composable
fun HomeScreen() {
    Column {
        Spacer(modifier = Modifier.height(16.dp))
        SearchBar()
        HomeSection(title = R.string.inversion) {
            AlignYourBodyScrollable()
        }
        Spacer(modifier = Modifier.height(16.dp))
    }
}

@Composable
fun SearchBar() {
    TextField(
        value = "",
        onValueChange = {},
        modifier = Modifier
            .padding(horizontal = 16.dp)
            .fillMaxWidth()
            .heightIn(min = 56.dp),
        leadingIcon = { (Icon(imageVector = Icons.Default.Search, contentDescription = null)) },
        placeholder = { Text(stringResource(R.string.placeholder_search)) },
        colors = TextFieldDefaults.textFieldColors(backgroundColor = MaterialTheme.colors.surface)
    )
}

@Composable
fun AlignYourBodyScrollable() {
    val elements = mutableListOf<ItemAlignBody>(
        ItemAlignBody(R.drawable.ab_exercise, R.string.inversion),
        ItemAlignBody(R.drawable.ab_exercise, R.string.show_more),
        ItemAlignBody(R.drawable.ab_exercise, R.string.show_less),
        ItemAlignBody(R.drawable.ab_exercise, R.string.app_name),
        ItemAlignBody(R.drawable.ab_exercise, R.string.inversion),
        ItemAlignBody(R.drawable.ab_exercise, R.string.show_more),
        ItemAlignBody(R.drawable.ab_exercise, R.string.show_less),
        ItemAlignBody(R.drawable.ab_exercise, R.string.app_name),
        ItemAlignBody(R.drawable.ab_exercise, R.string.inversion),
        ItemAlignBody(R.drawable.ab_exercise, R.string.show_more),
        ItemAlignBody(R.drawable.ab_exercise, R.string.show_less),
        ItemAlignBody(R.drawable.ab_exercise, R.string.app_name),
    )
    LazyHorizontalGrid(
        modifier = Modifier.heightIn(max = 300.dp),
        rows = GridCells.Fixed(2),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp)
    ) {
        items(items = elements) { element ->
            AlignYourBodyElement(element,
                Modifier.wrapContentHeight(Alignment.CenterVertically)
            )
        }
    }
}

@Composable
fun AlignYourBodyElement(
    alignBody: ItemAlignBody,
    modifier: Modifier = Modifier
) {
    Column(horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier) {
        Image(painter = painterResource(alignBody.drawable),
            contentDescription = null,
            modifier = Modifier
                .size(88.dp)
                .clip(CircleShape),
            contentScale = ContentScale.Crop
        )
        Text(
            stringResource(id = alignBody.string),
            modifier = Modifier.paddingFromBaseline(top = 24.dp, bottom = 8.dp),
            style = MaterialTheme.typography.subtitle2
            )
    }
}

@Composable
fun NewHomeScreen() {
    Scaffold(
        floatingActionButtonPosition = FabPosition.Center,
        floatingActionButton = {
            FloatingActionButton(onClick = { /* ... */ },
            ) {
                /* FAB content */
                Icon(Icons.Filled.Add, contentDescription = "Localized description")
            }
        },
        // Defaults to false
        isFloatingActionButtonDocked = true,
        bottomBar = {
            BottomAppBar {
                // Leading icons should typically have a high content alpha
                CompositionLocalProvider(LocalContentAlpha provides ContentAlpha.high) {
                    IconButton(onClick = { /* doSomething() */ }) {
                        Icon(Icons.Filled.Menu, contentDescription = "Localized description")
                    }
                }
                // The actions should be at the end of the BottomAppBar. They use the default medium
                // content alpha provided by BottomAppBar
                Spacer(Modifier.weight(1f, true))
                IconButton(onClick = { /* doSomething() */ }) {
                    Icon(Icons.Filled.Search, contentDescription = "Localized description")
                }
                IconButton(onClick = { /* doSomething() */ }) {
                    Icon(Icons.Filled.MoreVert, contentDescription = "Localized description")
                }
            }
        }
    ) {
        HomeScreen()
    }
}

@Preview(showBackground = true, backgroundColor = 0xFFF0EAE2)
@Composable
fun HomeScreenPreview() {
    NewHomeScreen()
}

@Preview(showBackground = true, name = "BasicLayoutPreview")
@Composable
fun BasicLayoutPreview() {
    BorsaPPCTheme {
        SearchBar()
    }
}

@Preview(showBackground = true, name = "AlignYourBodyPreview")
@Composable
fun AlignYourBodyPreview() {
    BorsaPPCTheme {
        AlignYourBodyElement(ItemAlignBody(R.drawable.ab_exercise, R.string.inversion), Modifier.padding(8.dp))
    }
}


@Preview(showBackground = true, name = "AlignYourBodyScrollablePreview")
@Composable
fun AlignYourBodyScrollablePreview() {
    AlignYourBodyScrollable()
}
