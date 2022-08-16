package com.android.borsappc.ui

import androidx.compose.ui.focus.FocusDirection
import com.android.borsappc.data.model.User
import com.android.borsappc.ui.auth.FocusedTextFieldKey

sealed class ScreenEvent {
    class ShowToast(val messageId: Int, val param: String?) : ScreenEvent()
    class ShowSnackbar(val message: String) : ScreenEvent()
    class UpdateKeyboard(val show: Boolean) : ScreenEvent()
    class MoveFocus(val direction: FocusDirection = FocusDirection.Down) : ScreenEvent()
    class RequestFocus(val textFieldKey: FocusedTextFieldKey) : ScreenEvent()
    object ClearFocus : ScreenEvent()
    class NavigateToMain(val user: User) : ScreenEvent()
    object SignOut : ScreenEvent()
}