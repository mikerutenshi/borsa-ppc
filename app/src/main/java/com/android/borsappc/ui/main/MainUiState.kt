package com.android.borsappc.ui.main

data class MainUiState(
    var errorMessage: String? = null,
    val currentScreen: String = DrawerScreens.Work.route
)
