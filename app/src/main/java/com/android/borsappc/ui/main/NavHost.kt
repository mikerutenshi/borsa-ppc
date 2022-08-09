package com.android.borsappc.ui.main

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.android.borsappc.ui.auth.AuthScreen
import com.android.borsappc.ui.auth.AuthViewModel
import com.android.borsappc.ui.product.ProductScreen
import com.android.borsappc.ui.report.ReportScreen
import com.android.borsappc.ui.work.WorkScreen
import com.android.borsappc.ui.worker.WorkerScreen

@Composable
fun BorsaPPCNavHost(
    navController: NavHostController,
    modifier: Modifier = Modifier,
    authViewModel: AuthViewModel
) {
    NavHost(
        navController = navController,
        startDestination = DrawerScreens.Work.route,
        modifier = modifier
    ) {
        composable(route = DrawerScreens.Auth.route) {
            AuthScreen(authViewModel)
        }

        composable(route = DrawerScreens.Work.route) {
            WorkScreen()
        }

        composable(route = DrawerScreens.Worker.route) {
            WorkerScreen()
        }

        composable(route = DrawerScreens.Product.route) {
            ProductScreen()
        }
        composable(route = DrawerScreens.Report.route) {
            ReportScreen()
        }
    }
}