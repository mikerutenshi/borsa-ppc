package com.android.borsappc.ui.screen.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.android.borsappc.data.model.API_DATE_FORMAT
import com.android.borsappc.data.model.Sort
import com.android.borsappc.data.model.WorkQuery
import com.android.borsappc.data.repository.AuthRepository
import com.android.borsappc.data.repository.WorkRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.format.DateTimeFormatter.ofPattern
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val workRepository: WorkRepository
) : ViewModel() {

    private val _uiState: MutableStateFlow<MainUiState> = MutableStateFlow(MainUiState())
    private val _events = MutableSharedFlow<MainScreenEvent>()
    val uiState = _uiState.stateIn(
        viewModelScope, SharingStarted.WhileSubscribed(5000L), MainUiState())
    val events = _events.shareIn(
        viewModelScope, SharingStarted.WhileSubscribed(5000L))

    init {
        initUIState()
    }

    fun initUIState() {
        viewModelScope.launch {
            workRepository.getWorkFilterData().collectLatest {
                val initialState = MainUiState(workQuery = WorkQuery(
                    startDate = it.date.startDate.ifEmpty {
                        LocalDate.now().minusWeeks(1L).format(
                            ofPattern(API_DATE_FORMAT)
                        )
                    },
                    endDate = it.date.endDate.ifEmpty {
                        LocalDate.now().format(
                            ofPattern(API_DATE_FORMAT)
                        )
                    },
                    sortBy = it.sort.sortBy.ifEmpty { Sort.BY_SPK_NO },
                    sortDirection = it.sort.sortDirection.ifEmpty { Sort.DIRECTION_ASC }
                ))
                _uiState.update { initialState }
            }
        }
    }

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
            MainUIEvent.FilterScreenClosed -> {
                viewModelScope.launch {
                    workRepository.storeWorkFilterData(_uiState.value.workQuery)
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