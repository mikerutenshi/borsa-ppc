package com.android.borsappc.ui.auth

import com.android.borsappc.data.model.User
import com.android.borsappc.ui.InputWrapper

data class AuthUiState(
    var user: User? = null,
    val messages: MutableList<Message> = mutableListOf(),
    var isFetchingUser: Boolean = false,
//    val username: InputWrapper = InputWrapper(),
//    val password: InputWrapper = InputWrapper(),
    var isPasswordVisible: Boolean = false,
//    var focusedTextField: FocusedTextFieldKey = FocusedTextFieldKey.USERNAME
)

//val AuthUiState.areInputsValid: Boolean get() =
//    username.value.isNotEmpty() && password.value.isNotEmpty() &&
//            username.errorId == null && password.errorId == null

const val USERNAME = "username"
const val PASSWORD = "password"
const val IS_PASSWORD_VISIBLE = "isPasswordVisible"
const val FOCUSED_TEXt_FIELD = "focusedTextField"
data class Message(val id: Int
?, val message: String)
enum class FocusedTextFieldKey {
    USERNAME, PASSWORD, NONE
}

