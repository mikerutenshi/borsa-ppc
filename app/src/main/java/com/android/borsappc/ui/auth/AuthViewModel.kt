package com.android.borsappc.ui.auth

import androidx.lifecycle.*
import com.android.borsappc.R
import com.android.borsappc.data.repository.AuthRepository
import com.android.borsappc.ui.InputValidator
import com.android.borsappc.ui.InputWrapper
import com.android.borsappc.ui.ScreenEvent
import com.android.borsappc.ui.UserInputEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*
import timber.log.Timber
import javax.inject.Inject

class InputErrors(
    val usernameErrorId: Int?,
    val passwordErrorId: Int?
)

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val handle: SavedStateHandle
) : ViewModel()/*, LifecycleObserver */ {

    val username = handle.getStateFlow(USERNAME, InputWrapper())
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000L), InputWrapper())
    val password = handle.getStateFlow(PASSWORD, InputWrapper())
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000L), InputWrapper())
    val areInputValid = combine(username, password) { u, p ->
        u.value.isNotEmpty()
                && u.errorId == null
                && p.value.isNotEmpty()
                && p.errorId == null
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000L), false)
    private var focusedTextField = handle[FOCUSED_TEXt_FIELD] ?: FocusedTextFieldKey.USERNAME
        set(value) {
            field = value
            handle[FOCUSED_TEXt_FIELD] = value
        }

    private val _uiState = MutableStateFlow(AuthUiState())
    val uiState: StateFlow<AuthUiState> = _uiState.asStateFlow().stateIn(
        viewModelScope, SharingStarted.WhileSubscribed(5000L), AuthUiState())
    private val _events = MutableSharedFlow<ScreenEvent>()
    val events = _events.asSharedFlow().shareIn(
        viewModelScope, SharingStarted.WhileSubscribed(5000L))

    private val inputEvents = Channel<UserInputEvent>(Channel.CONFLATED)

    init {
        observeUserInputEvents()
    }

    fun onUsernameEntered(input: String) {
//        val errorId = InputValidator.getUsernameErrorIdOrNull(input)
//        handle[USERNAME] = username.value.copy(value = input, errorId = null)
        viewModelScope.launch(Dispatchers.Default) {
            inputEvents.send(UserInputEvent.Username(input))
        }
    }

    fun onPasswordEntered(input: String) {
//        val errorId = InputValidator.getPasswordErrorIdOrNull(input)
//        handle[PASSWORD] = password.value.copy(value = input, errorId = null)
        viewModelScope.launch(Dispatchers.Default) {
            inputEvents.send(UserInputEvent.Password(input))
        }
    }

    fun onUsernameImeActionClick() {
        viewModelScope.launch(Dispatchers.Default) {
            _events.emit(ScreenEvent.MoveFocus())
        }
    }

    fun onTrailingIconClick() {
        _uiState.update { it.copy(isPasswordVisible = !_uiState.value.isPasswordVisible) }
    }

    fun onTextFieldFocusChanged(key: FocusedTextFieldKey, isFocused: Boolean) {
        focusedTextField = if (isFocused) key else focusedTextField
        Timber.d("onFocusChanged triggered %s %s", key, focusedTextField)
    }

    fun onContinueClick() {
        Timber.d("onContinueClick triggered")
        viewModelScope.launch(Dispatchers.Default) {
//            val resId = if (areInputsValid.value) R.string.validation_success else
//                R.string.validation_fail
//            _events.send(ScreenEvent.ShowToast(resId))
            when (val inputErrors = getInputErrorsOrNull()) {
                null -> {
                    clearFocusAndHideKeyboard()
                    Timber.d("onContinueClickEmit triggered")
                    _events.emit(ScreenEvent.ShowToast(R.string.validation_success))

                }
                else -> displayInputErrors(inputErrors)
            }
        }
    }

    fun focusOnLastSelectedField() {
        viewModelScope.launch(Dispatchers.Default) {
            Timber.d("focusOnLastSelectedField triggered %s", focusedTextField)
            delay(1000L)
            _events.emit(ScreenEvent.RequestFocus(focusedTextField))
            _events.emit(ScreenEvent.UpdateKeyboard(true))
        }
    }

    @OptIn(FlowPreview::class)
    private fun observeUserInputEvents() {
        viewModelScope.launch(Dispatchers.Default) {
            inputEvents.receiveAsFlow()
                .onEach { event ->
                    when (event) {
                        is UserInputEvent.Username -> {
//                            when (InputValidator.getUsernameErrorIdOrNull(event.input)) {
//                                null -> {
                                    handle[USERNAME] = username.value.copy(value = event.input)
//                                }
//                                else -> {
//                                    handle[USERNAME] =
//                                        _uiState.updateAndGet { it.copy(username = it.username.copy(value = event.input)) }.username
//                                }
//                            }
                        }
                        is UserInputEvent.Password -> {
//                            when (InputValidator.getPasswordErrorIdOrNull(event.input)) {
//                                null -> {
                                    handle[PASSWORD] = password.value.copy(value = event.input)
//                                }
//                                else -> {
//                                    handle[PASSWORD] =
//                                        _uiState.updateAndGet { it.copy(password = it.password.copy(value = event.input)) }.password
//                                }
//                            }
                        }
                    }
                }
                .debounce(650)
                .collect { event ->
                    when (event) {
                        is UserInputEvent.Username -> {
                            val errorId = InputValidator.getUsernameErrorIdOrNull(event.input)
                            handle[USERNAME] = username.value.copy(value = event.input, errorId = errorId)
                        }
                        is UserInputEvent.Password -> {
                            val errorId = InputValidator.getPasswordErrorIdOrNull(event.input)
                            handle[PASSWORD] = password.value.copy(value = event.input, errorId = errorId)
                        }
                    }
                }
        }
    }
     private fun getInputErrorsOrNull(): InputErrors? {
        val usernameErrorId = InputValidator.getUsernameErrorIdOrNull(username.value.value)
        val passwordErrorId = InputValidator.getPasswordErrorIdOrNull(password.value.value)
        return if (usernameErrorId == null && passwordErrorId == null) {
            null
        } else {
            InputErrors(usernameErrorId, passwordErrorId)
        }
    }

    private fun displayInputErrors(inputErrors: InputErrors) {
        handle[USERNAME] = username.value.copy(errorId = inputErrors.usernameErrorId)
        handle[PASSWORD] = password.value.copy(errorId = inputErrors.passwordErrorId)
    }

    private suspend fun clearFocusAndHideKeyboard() {
        _events.emit(ScreenEvent.ClearFocus)
        _events.emit(ScreenEvent.UpdateKeyboard(false))
        focusedTextField = FocusedTextFieldKey.NONE
    }
}