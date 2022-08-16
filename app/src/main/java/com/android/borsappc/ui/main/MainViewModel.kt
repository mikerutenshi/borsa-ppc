package com.android.borsappc.ui.main

import android.content.SharedPreferences
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.datastore.core.DataStore
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.android.borsappc.SignInPrefs
import com.android.borsappc.UserPreferences
import com.android.borsappc.data.repository.AuthRepository
import com.android.borsappc.ui.ScreenEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
   private val authRepository: AuthRepository,
   private val userPreferences: DataStore<UserPreferences>
   ) : ViewModel() {
    private val _uiState = MutableStateFlow(MainUiState())
    private val _events = MutableSharedFlow<ScreenEvent>()
    val uiState = _uiState.stateIn(
        viewModelScope, SharingStarted.WhileSubscribed(5000L), MainUiState())
    val events = _events.shareIn(
        viewModelScope, SharingStarted.WhileSubscribed(5000L))

    fun signOut() {
        viewModelScope.launch {
            authRepository.getSignInData()
                .onSuccess { user ->
                    authRepository.signOut(user.username)
                        .onSuccess {
                            authRepository.clearSignInData()
                                .onSuccess {
                                    _events.emit(ScreenEvent.SignOut)
                                }
                                .onFailure { error ->
                                    error.localizedMessage?.let {
                                        _events.emit(ScreenEvent.ShowSnackbar(it))
                                    }
                                }
                        }
                        .onFailure { error ->
                            error.localizedMessage?.let {
                                _events.emit(ScreenEvent.ShowSnackbar(it))
                            }
                        }

                }
                .onFailure { error ->
                    error.localizedMessage?.let {
                    _events.emit(ScreenEvent.ShowSnackbar(it))
                    }
                }
        }
    }
}