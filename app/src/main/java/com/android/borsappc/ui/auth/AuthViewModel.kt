package com.android.borsappc.ui.auth

import androidx.lifecycle.ViewModel
import com.android.borsappc.data.repository.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : ViewModel() {
}