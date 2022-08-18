package com.android.borsappc.ui.main

import com.android.borsappc.data.model.User

sealed class MainScreenEvent  {
    class ShowSnackbar(val message: String) : MainScreenEvent()
    object SignOut : MainScreenEvent()
    class NavigateTo(val destination: String) : MainScreenEvent()
}
