package com.android.borsappc.ui

import androidx.compose.runtime.Composable
import androidx.navigation.compose.rememberNavController
import com.android.borsappc.ui.main.BorsaPPCNavHost
import com.android.borsappc.ui.main.DrawerScreens

@Composable
fun BorsaPPCAPP() {
    val navController = rememberNavController()
    val isLoggedIn = true

    BorsaPPCNavHost(
        navController = navController,
        initialScreen = if (isLoggedIn) DrawerScreens.Main.route else
            DrawerScreens.Auth.route
    )
}
