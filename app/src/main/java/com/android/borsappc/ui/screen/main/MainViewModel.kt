package com.android.borsappc.ui.screen.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.android.borsappc.data.model.API_DATE_FORMAT
import com.android.borsappc.data.repository.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.time.format.DateTimeFormatter.ofPattern
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
   private val authRepository: AuthRepository) : ViewModel() {

    private val _uiState = MutableStateFlow(MainUiState())
    private val _events = MutableSharedFlow<MainScreenEvent>()
    val uiState = _uiState.stateIn(
        viewModelScope, SharingStarted.WhileSubscribed(5000L), MainUiState())
    val events = _events.shareIn(
        viewModelScope, SharingStarted.WhileSubscribed(5000L))

    fun onEvent(event: MainUIEvent) {
        when (event) {
            is MainUIEvent.StartDateChanged -> {
                val date = event.date.format(ofPattern(API_DATE_FORMAT))
                _uiState.update {
                    it.copy(
                        workQuery =
                        it.workQuery.copy(startDate = date)
                    )
                }
            }
            is MainUIEvent.EndDateChanged -> {
                val date = event.date.format(ofPattern(API_DATE_FORMAT))
                _uiState.update {
                    it.copy(
                        workQuery =
                        it.workQuery.copy(endDate = date)
                    )
                }
            }
            is MainUIEvent.SortKeyChanged -> {
                _uiState.update {
                    it.copy(
                        workQuery =
                        it.workQuery.copy(sortBy = event.key)
                    )
                }
            }
            is MainUIEvent.SortDirectionChanged -> {
                _uiState.update {
                    it.copy(
                        workQuery =
                        it.workQuery.copy(sortDirection = event.direction)
                    )
                }
            }
        }
    }

    fun setCurrentScreen(route: String) {
        _uiState.update {
            it.copy(currentScreen = route)
        }
    }

    fun navigateTo(destination: String) {
        viewModelScope.launch {
            _events.emit(MainScreenEvent.NavigateTo(destination))
        }
    }

     fun signOut() {
        viewModelScope.launch {
            _uiState.update {
                it.copy(isSigningOut = true)
            }
            authRepository.getSignInData()
                .onSuccess { user ->
                    authRepository.signOut(user.username)
                        .onSuccess {
                            authRepository.clearSignInData()
                                .onSuccess {
                                    _uiState.update {
                                        it.copy(isSigningOut = false)
                                    }
                                    _events.emit(MainScreenEvent.SignOut)
                                }
                                .onFailure { error ->
                                    _uiState.update {
                                        it.copy(isSigningOut = false)
                                    }
                                    error.localizedMessage?.let {
                                        _events.emit(MainScreenEvent.ShowSnackbar(it))
                                    }
                                }
                        }
                        .onFailure { error ->
                            error.localizedMessage?.let {
                                _events.emit(MainScreenEvent.ShowSnackbar(it))
                            }
                        }

                }
                .onFailure { error ->
                    error.localizedMessage?.let {
                        _events.emit(MainScreenEvent.ShowSnackbar(it))
                    }
                }
        }
    }
}