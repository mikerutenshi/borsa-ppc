package com.android.borsappc.ui.main

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
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.fontResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.flowWithLifecycle
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.android.borsappc.R
import com.android.borsappc.ui.BorsaPPCTheme
import com.android.borsappc.ui.Red
import com.android.borsappc.ui.auth.AuthScreen
import com.android.borsappc.ui.auth.AuthViewModel
import com.android.borsappc.ui.product.ProductScreen
import com.android.borsappc.ui.work.WorkScreen
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import timber.log.Timber

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun MainScreen(
    viewModel: MainViewModel,
    authViewModel: AuthViewModel
) {
    val scope = rememberCoroutineScope()
    val lifecycleOwner = LocalLifecycleOwner.current
    val scaffoldState = rememberScaffoldState()
    val drawerState = rememberBottomDrawerState(initialValue = BottomDrawerValue.Closed)
    val navController = rememberNavController()
    val uiState = remember(viewModel.uiState, lifecycleOwner) {
        viewModel.uiState.flowWithLifecycle(lifecycleOwner.lifecycle, Lifecycle.State.STARTED)
    }

    LaunchedEffect(Unit) {
        uiState.collect { uiState ->
            val error = uiState.errorMessage
            if (error != null) {
                Timber.d("errorMessage.collect: $error")
                val result = scaffoldState.snackbarHostState.showSnackbar(
                    error,
                    "Dismiss",
                    SnackbarDuration.Indefinite)
                when (result) {
                    SnackbarResult.Dismissed -> viewModel.clearErrorMessage()
                    SnackbarResult.ActionPerformed -> viewModel.clearErrorMessage()
                }
            }
        }
    }

    BottomDrawer(
        drawerState = drawerState,
        drawerContent = {
            Drawer() { route ->
                scope.launch {
                    drawerState.close()
                }
                navController.navigate(route) {
                    navController.graph.startDestinationRoute?.let { popUpTo(it) }
                    launchSingleTop = true
                }
            }
        }
    ) {
        Scaffold(
            modifier = Modifier,
            scaffoldState = scaffoldState,
            floatingActionButtonPosition = FabPosition.Center,
            snackbarHost = {
                SnackbarHost(it) { data ->
                    Snackbar(
                        backgroundColor = Red,
                        snackbarData = data
                    )
                }
            },
            floatingActionButton = {
                FloatingActionButton(onClick = { /* ... */ },
                ) {
                    /* FAB content */
                    Icon(Icons.Filled.Add,
                        contentDescription = stringResource(id = R.string.ic_desc_plus))
                }
            },
            // Defaults to false
            isFloatingActionButtonDocked = true,
            bottomBar = {
                BottomAppBar {
                    // Leading icons should typically have a high content alpha
                    CompositionLocalProvider(LocalContentAlpha provides ContentAlpha.high) {
                        IconButton(onClick = {
                            scope.launch {
                                drawerState.open()
                            }
                        }) {
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
        ) { padding ->
            BorsaPPCNavHost(
                navController = navController,
                authViewModel = authViewModel
            )
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFFF0EAE2)
@Composable
fun HomeScreenPreview() {
}