package com.android.borsappc.ui.auth

import androidx.compose.ui.focus.FocusDirection
import com.android.borsappc.data.model.User

sealed class AuthScreenEvent {
    class ShowToast(val messageId: Int, val param: String?) : AuthScreenEvent()
    class ShowSnackbar(val message: String) : AuthScreenEvent()
    class UpdateKeyboard(val show: Boolean) : AuthScreenEvent()
    class MoveFocus(val direction: FocusDirection = FocusDirection.Down) : AuthScreenEvent()
    class RequestFocus(val textFieldKey: FocusedTextFieldKey) : AuthScreenEvent()
    object ClearFocus : AuthScreenEvent()
    class NavigateToMain(val user: User) : AuthScreenEvent()
}