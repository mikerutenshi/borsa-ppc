package com.android.borsappc.ui.screen.main

sealed class MainScreenEvent  {
    class ShowSnackbar(val message: String) : MainScreenEvent()
    object SignOut : MainScreenEvent()
    class NavigateTo(val destination: String) : MainScreenEvent()
}
