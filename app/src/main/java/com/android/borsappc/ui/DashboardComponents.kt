package com.android.borsappc.ui

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.FilterAlt
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.DisposableEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.android.borsappc.R
import com.android.borsappc.data.model.User
import com.android.borsappc.ui.screen.main.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun DashboardScaffold(
    viewModel: MainViewModel,
    drawerState: BottomDrawerState,
    scope: CoroutineScope,
    scaffoldState: ScaffoldState,
    bottomSheetState: ModalBottomSheetState,
    user: User,
    uiState: MainUiState,
    scaffoldContent: @Composable (paddingValues: PaddingValues) -> Unit
) {
    if (bottomSheetState.currentValue != ModalBottomSheetValue.Hidden) {
        DisposableEffect(key1 = Unit) {
            onDispose {
                viewModel.onEvent(MainUIEvent.FilterScreenClosed)
            }
        }
    }

    BottomDrawer(
        drawerState = drawerState,
        gesturesEnabled = drawerState.isOpen,
        drawerContent = {
            Drawer(
                user = user,
                uiState = uiState
            ) { route ->
                if (route == DrawerScreens.Auth.route) {
                    viewModel.signOut()
                } else {
                    scope.launch {
                        drawerState.close()
                    }

                    viewModel.navigateTo(route)
                }
            }
        }
    ) {
        ModalBottomSheetLayout(sheetContent = { FilterBottomSheet(viewModel) },
            sheetState = bottomSheetState
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
                    FloatingActionButton(
                        onClick = { /* ... */ },
                    ) {
                        /* FAB content */
                        Icon(
                            Icons.Filled.Add,
                            contentDescription = stringResource(id = R.string.ic_desc_plus)
                        )
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
                                Icon(
                                    Icons.Filled.Menu,
                                    contentDescription = "Localized description"
                                )
                            }
                        }
                        // The actions should be at the end of the BottomAppBar. They use the default medium
                        // content alpha provided by BottomAppBar
                        Spacer(Modifier.weight(1f, true))
                        IconButton(onClick = { /* doSomething() */ }) {
                            Icon(Icons.Filled.Search, contentDescription = "Localized description")
                        }
                        IconButton(onClick = {
                            scope.launch {
                                bottomSheetState.show()
                            }
                        }) {
                            Icon(
                                Icons.Filled.FilterAlt,
                                contentDescription = "Localized description"
                            )
                        }
                    }
                }
            ) { padding ->
                scaffoldContent(padding)
            }
        }
    }
}

