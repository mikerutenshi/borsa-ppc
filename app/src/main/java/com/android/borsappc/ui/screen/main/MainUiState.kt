package com.android.borsappc.ui.screen.main

import com.android.borsappc.data.model.WorkQuery

data class MainUiState(
    val currentScreen: String = DrawerScreens.Work.route,
    var isSigningOut: Boolean = false,
    val workQuery: WorkQuery = WorkQuery()
)

const val CURRENT_SCREEN = "current_screen"
