package com.android.borsappc.ui

import androidx.compose.runtime.Composable

typealias OnValueChange = (value: String) -> Unit
typealias OnImeKeyAction = () -> Unit
typealias TrailingIcon = @Composable () -> Unit
typealias OnErrorMessage = (error: String) -> Unit
