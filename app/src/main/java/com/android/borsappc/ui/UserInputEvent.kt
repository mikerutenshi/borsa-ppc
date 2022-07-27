package com.android.borsappc.ui

sealed class UserInputEvent {
    class Username(val input: String) : UserInputEvent()
    class Password(val input: String) : UserInputEvent()
}