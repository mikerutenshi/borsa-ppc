package com.android.borsappc.ui.main

import androidx.activity.compose.BackHandler
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.ExperimentalLifecycleComposeApi
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.flowWithLifecycle
import androidx.navigation.NavHostController
import com.android.borsappc.getActivity
import com.android.borsappc.ui.DashboardScaffold
import com.android.borsappc.ui.product.ProductScreen
import com.android.borsappc.ui.report.ReportScreen
import com.android.borsappc.ui.work.WorkScreen
import com.android.borsappc.ui.worker.WorkerScreen
import timber.log.Timber

@OptIn(ExperimentalMaterialApi::class, ExperimentalLifecycleComposeApi::class)
@Composable
fun MainScreen(navController: NavHostController) {
    val mainViewModel = hiltViewModel<MainViewModel>()

    val scope = rememberCoroutineScope()
    val lifecycleOwner = LocalLifecycleOwner.current
    val scaffoldState = rememberScaffoldState()
    val drawerState = rememberBottomDrawerState(initialValue = BottomDrawerValue.Closed)
    val uiStateLifecycleAware = remember(mainViewModel.uiState, lifecycleOwner) {
        mainViewModel.uiState.flowWithLifecycle(lifecycleOwner.lifecycle, Lifecycle.State.STARTED)
    }
    val uiState by mainViewModel.uiState.collectAsStateWithLifecycle()
    val activity = LocalContext.current.getActivity()

    BackHandler() {
        activity?.finish()
    }

    LaunchedEffect(Unit) {
        uiStateLifecycleAware.collect { uiState ->
            val error = uiState.errorMessage
            if (error != null) {
                Timber.d("errorMessage.collect: $error")
                val result = scaffoldState.snackbarHostState.showSnackbar(
                    error,
                    "Dismiss",
                    SnackbarDuration.Indefinite)
                when (result) {
                    SnackbarResult.Dismissed -> mainViewModel.clearErrorMessage()
                    SnackbarResult.ActionPerformed -> mainViewModel.clearErrorMessage()
                }
            }
        }
    }

    DisposableEffect(key1 = mainViewModel) {
        Timber.d("disposableEffect triggered")

        onDispose {  }
    }

    DashboardScaffold(drawerState = drawerState, viewModel = mainViewModel, navHostController = navController, scope = scope, scaffoldState = scaffoldState) {
        when (uiState.currentScreen) {
            DrawerScreens.Work.route -> WorkScreen()
            DrawerScreens.Worker.route -> WorkerScreen()
            DrawerScreens.Product.route -> ProductScreen()
            DrawerScreens.Report.route -> ReportScreen()
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFFF0EAE2)
@Composable
fun HomeScreenPreview() {
}