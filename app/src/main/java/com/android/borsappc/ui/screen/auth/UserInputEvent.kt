package com.android.borsappc.ui.screen.auth

sealed class UserInputEvent {
    class Username(val input: String) : UserInputEvent()
    class Password(val input: String) : UserInputEvent()
}