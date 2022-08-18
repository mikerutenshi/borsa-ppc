package com.android.borsappc.ui.main

data class MainUiState(
    val currentScreen: String = DrawerScreens.Work.route,
    var isSigningOut: Boolean = false
)

const val CURRENT_SCREEN = "current_screen"
