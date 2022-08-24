package com.android.borsappc.ui

import androidx.compose.runtime.Composable
import cafe.adriel.voyager.androidx.AndroidScreen

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
