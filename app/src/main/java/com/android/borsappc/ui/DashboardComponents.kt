package com.android.borsappc.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavHostController
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.android.borsappc.R
import com.android.borsappc.data.model.User
import com.android.borsappc.ui.auth.AuthScreen
import com.android.borsappc.ui.main.Drawer
import com.android.borsappc.ui.main.DrawerScreens
import com.android.borsappc.ui.main.MainViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun DashboardScaffold(
    viewModel: MainViewModel,
    drawerState: BottomDrawerState,
    scope: CoroutineScope,
    scaffoldState: ScaffoldState,
    user: User,
    scaffoldContent: @Composable (paddingValues: PaddingValues) -> Unit
) {
    BottomDrawer(
        drawerState = drawerState,
        drawerContent = {
            Drawer(user = user) { route ->
                scope.launch {
                    drawerState.close()
                }

//                if (route == DrawerScreens.Auth.route) {
//                    navHostController.navigate(route) {
//                        navHostController.graph.startDestinationRoute?.let { popUpTo(it) }
//                        launchSingleTop = true
//                    }
//                } else {
//                    viewModel.changeScaffoldContent(screenRoute = route)
//                }
                when (route) {
                    DrawerScreens.Auth.route -> viewModel.signOut()
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
            scaffoldContent(padding)
        }
    }
}

