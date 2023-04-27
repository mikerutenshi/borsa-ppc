package com.android.borsappc.ui.screen.main

import com.android.borsappc.data.model.QueryProductList
import com.android.borsappc.data.model.WorkQuery

data class MainUiState(
    var currentScreen: String = DrawerScreens.Work.route,
    var isSigningOut: Boolean = false,
    var workQuery: WorkQuery = WorkQuery(),
    var queryProductList: QueryProductList = QueryProductList()
)

const val CURRENT_SCREEN = "current_screen"
