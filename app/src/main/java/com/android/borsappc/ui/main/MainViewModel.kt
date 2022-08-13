package com.android.borsappc.ui.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.*
import timber.log.Timber
import javax.inject.Inject

class MainViewModel @Inject constructor() : ViewModel() {
    private val _uiState = MutableStateFlow(MainUiState())
    val uiState = _uiState.stateIn(
        viewModelScope, SharingStarted.WhileSubscribed(5000L), MainUiState())

    fun clearErrorMessage() {
        _uiState.update { it.copy(errorMessage = null) }
    }

    fun setErrorMessage(error: String) {
        _uiState.update { it.copy(errorMessage = error) }
        Timber.d("setErrorMessage: ${_uiState.value}")
    }

    fun changeScaffoldContent(screenRoute: String) {
        _uiState.update { it.copy(currentScreen = screenRoute)}
    }
}