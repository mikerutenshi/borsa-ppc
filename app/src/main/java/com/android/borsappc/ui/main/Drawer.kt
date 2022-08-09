package com.android.borsappc.ui.main

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Divider
import androidx.compose.material.MaterialTheme.typography
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

sealed class DrawerScreens(val title: String, val route: String) {
    object Auth : DrawerScreens("Autentikasi", "auth")
    object Work : DrawerScreens("Pekerjaan", "work")
    object Worker : DrawerScreens("Tukang", "worker")
    object Product : DrawerScreens( "Sepatu", "product")
    object Report : DrawerScreens( "Laporan", "report")
}

private val screens = listOf(
    DrawerScreens.Work,
    DrawerScreens.Worker,
    DrawerScreens.Product,
    DrawerScreens.Report
)

@Composable
fun Drawer(modifier: Modifier = Modifier,
    onDestinationClicked: (route: String) -> Unit) {
    Column(modifier = Modifier.padding(24.dp)) {
        Text("Navigation",
            style = typography.h4)
        Spacer(modifier = Modifier.height(24.dp))
        Divider()
        screens.forEach { screen ->
            Spacer(modifier = Modifier.height(24.dp))
            Text(
                text = screen.title,
                style = typography.h6,
                modifier = Modifier.clickable {
                    onDestinationClicked(screen.route)
                }
            )
        }
    }
}