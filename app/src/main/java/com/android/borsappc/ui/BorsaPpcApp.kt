package com.android.borsappc.ui

import androidx.compose.runtime.Composable
import androidx.navigation.compose.rememberNavController
import cafe.adriel.voyager.androidx.AndroidScreen
import cafe.adriel.voyager.hilt.getViewModel
import com.android.borsappc.ui.main.DrawerScreens
import com.android.borsappc.ui.main.MainViewModel

object BorsaPpcAppScreen : AndroidScreen() {

    @Composable
    override fun Content() {
        BorsaPpcTheme() {
            BorsaPpcAPP()
        }
    }
}

@Composable
fun BorsaPpcAPP() {
}
