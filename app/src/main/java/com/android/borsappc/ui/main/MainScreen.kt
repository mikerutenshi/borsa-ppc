package com.android.borsappc.ui.main

import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.ExperimentalLifecycleComposeApi
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.flowWithLifecycle
import cafe.adriel.voyager.androidx.AndroidScreen
import cafe.adriel.voyager.hilt.getViewModel
import cafe.adriel.voyager.navigator.CurrentScreen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.Navigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.android.borsappc.data.model.User
import com.android.borsappc.ui.DashboardScaffold
import com.android.borsappc.ui.auth.AuthScreen
import com.android.borsappc.ui.product.ProductScreen
import com.android.borsappc.ui.report.ReportScreen
import com.android.borsappc.ui.work.WorkScreen
import com.android.borsappc.ui.worker.WorkerScreen
import timber.log.Timber

class MainScreen(private val user: User) : AndroidScreen() {

    @Composable
    override fun Content() {
        val viewModel = getViewModel<MainViewModel>()
        Navigator(WorkScreen()) {
            MainScreenContent(viewModel = viewModel, user = user)
        }
    }

}

@OptIn(ExperimentalMaterialApi::class, ExperimentalLifecycleComposeApi::class)
@Composable
fun MainScreenContent(viewModel: MainViewModel, user: User) {
    val scope = rememberCoroutineScope()
    val lifecycleOwner = LocalLifecycleOwner.current
    val scaffoldState = rememberScaffoldState()
    val drawerState = rememberBottomDrawerState(initialValue = BottomDrawerValue.Closed)
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val events = remember { viewModel.events }

    val navigator = LocalNavigator.currentOrThrow

    LaunchedEffect(Unit) {
        events.collect { event ->
            when (event) {
                is MainScreenEvent.ShowSnackbar -> {
                    val result = scaffoldState.snackbarHostState.showSnackbar(
                        event.message,
                        "Dismiss",
                        SnackbarDuration.Indefinite)
                    when (result) {
                        SnackbarResult.Dismissed -> {}
                        SnackbarResult.ActionPerformed ->
                            scaffoldState.snackbarHostState.currentSnackbarData?.dismiss()
                    }
                }
                is MainScreenEvent.SignOut -> navigator.parent?.replaceAll(AuthScreen)
                is MainScreenEvent.NavigateTo -> {
                    val screen = when (event.destination) {
                        DrawerScreens.Work.route -> WorkScreen()
                        DrawerScreens.Worker.route -> WorkerScreen()
                        DrawerScreens.Product.route -> ProductScreen()
                        DrawerScreens.Report.route -> ReportScreen()
                        else -> WorkScreen()
                    }
                    viewModel.setCurrentScreen(event.destination)
                    navigator.replace(screen)
                }
            }
        }
    }

    DisposableEffect(key1 = viewModel) {
        Timber.d("disposableEffect triggered")

        onDispose {  }
    }

    DashboardScaffold(drawerState = drawerState,
        viewModel = viewModel,
        scope = scope,
        scaffoldState = scaffoldState,
        user = user,
        uiState = uiState
    ) {
        CurrentScreen()
    }
}

@Preview(showBackground = true, backgroundColor = 0xFFF0EAE2)
@Composable
fun HomeScreenPreview() {
}