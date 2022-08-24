package com.android.borsappc.ui.screen.auth

import com.android.borsappc.data.model.User

data class AuthUiState(
    var user: User? = null,
    var isFetchingUser: Boolean = false,
    var isPasswordVisible: Boolean = false,
)

const val USERNAME = "username"
const val PASSWORD = "password"
const val FOCUSED_TEXt_FIELD = "focusedTextField"
data class Message(val id: Int
?, val message: String)
enum class FocusedTextFieldKey {
    USERNAME, PASSWORD, NONE
}

