package com.android.borsappc.ui.main

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Divider
import androidx.compose.material.MaterialTheme.colors
import androidx.compose.material.MaterialTheme.typography
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.capitalize
import androidx.compose.ui.text.intl.Locale
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.uniqueScreenKey
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.android.borsappc.data.model.User
import java.util.*

sealed class DrawerScreens(val title: String, val route: String) {
    object Auth : DrawerScreens("Keluar", "auth")
    object Work : DrawerScreens("Pekerjaan", "work")
    object Worker : DrawerScreens("Tukang", "worker")
    object Product : DrawerScreens( "Sepatu", "product")
    object Report : DrawerScreens( "Laporan", "report")
}

private val screens = listOf(
    DrawerScreens.Work,
    DrawerScreens.Worker,
    DrawerScreens.Product,
    DrawerScreens.Report,
    DrawerScreens.Auth
)

@Composable
fun Drawer(modifier: Modifier = Modifier,
           user: User,
           uiState: MainUiState,
           onDestinationClicked: (route: String) -> Unit
    ) {
    val navigator = LocalNavigator.currentOrThrow

    Column(modifier = Modifier.padding(24.dp)) {
        if (uiState.isSigningOut) {
            Text("Sampai ketemu lagi, ${user.username.capitalize(Locale.current)} ...",
                style = typography.h4)
        } else {
            Text("Selamat datang, ${user.username.capitalize(Locale.current)}",
                style = typography.h4)
        }
        Spacer(modifier = Modifier.height(24.dp))
        Divider()
        screens.forEach { screen ->
            val textColor = if (screen.route == uiState.currentScreen) colors.primary else
                colors.onPrimary

            Spacer(modifier = Modifier.height(24.dp))
            Text(
                text = screen.title,
                color = textColor,
                style = typography.h6,
                modifier = Modifier.clickable {
                    onDestinationClicked(screen.route)
                }
            )
        }
    }
}