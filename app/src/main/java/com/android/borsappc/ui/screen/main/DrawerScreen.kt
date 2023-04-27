package com.android.borsappc.ui.screen.main

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.Divider
import androidx.compose.material.MaterialTheme.colors
import androidx.compose.material.MaterialTheme.typography
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.capitalize
import androidx.compose.ui.text.intl.Locale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.android.borsappc.R
import com.android.borsappc.data.model.User
import java.time.LocalTime

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
//    val navigator = LocalNavigator.currentOrThrow
    val role = when (user.role) {
        Role.SUPER_USER -> " / ${stringResource(id = R.string.role_superuser)}"
        Role.ADMIN_PRICE -> " / ${stringResource(id = R.string.role_admin_price)}"
        Role.ADMIN_WORK -> " / ${stringResource(id = R.string.role_admin_work)}"
        Role.ADMIN_QA -> " / ${stringResource(id = R.string.role_admin_qa)}"
        else -> ""
    }

    Column(modifier = Modifier.padding(24.dp)) {
        if (uiState.isSigningOut) {
            Text(stringResource(id = R.string.see_you_later, user.username.capitalize(Locale.current)),
                style = typography.h5)
        } else {
            val currentTime = LocalTime.now()
            val name = "${user.firstName} ${user.lastName ?: ""}$role"
            val greetings = when {
                currentTime.isAfter(LocalTime.of(17, 59)) ->
                        stringResource(id = R.string.good_evening_user, name)
                currentTime.isAfter(LocalTime.of(11, 59)) ->
                    stringResource(id = R.string.good_afternoon_user, name)
                currentTime.isAfter(LocalTime.of(0, 0)) ->
                    stringResource(id = R.string.good_morning_user, name)

                else -> stringResource(id = R.string.welcome_user, name)
            }
            Text(greetings,
                style = typography.h5)
        }
        Spacer(modifier = Modifier.height(24.dp))
        Divider()
        screens.forEach { screen ->
            val textColor = if (screen.route == uiState.currentScreen) colors.primary else
                colors.onPrimary

//            Spacer(modifier = Modifier.height(24.dp))
            Text(
                text = screen.title,
                color = textColor,
                style = typography.h6,
                modifier = Modifier
                    .clickable {
                        onDestinationClicked(screen.route)
                    }
                    .fillMaxWidth()
                    .padding(top = 12.dp, bottom = 12.dp),
                textAlign = TextAlign.Start
            )
        }
    }
}