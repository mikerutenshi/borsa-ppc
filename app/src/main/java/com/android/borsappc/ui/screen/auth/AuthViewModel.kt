package com.android.borsappc.ui.screen.auth

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.android.borsappc.data.model.UserSignIn
import com.android.borsappc.data.repository.AuthRepository
import com.android.borsappc.ui.InputValidator
import com.android.borsappc.ui.InputWrapper
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.*
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
) : ViewModel() {
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
        viewModelScope, SharingStarted.WhileSubscribed(5_000), AuthUiState())

    private val _events = MutableSharedFlow<AuthScreenEvent>()
    val events = _events.asSharedFlow().shareIn(
        viewModelScope, SharingStarted.WhileSubscribed(5_000))
    private val _inputEvents = MutableSharedFlow<UserInputEvent>()

    init {
        observeUserInputEvents()
    }

    fun onUsernameEntered(input: String) {
//        val errorId = InputValidator.getUsernameErrorIdOrNull(input)
//        handle[USERNAME] = username.value.copy(value = input, errorId = null)
        handle[USERNAME] = username.value.copy(errorMessage = null)
        viewModelScope.launch(Dispatchers.Default) {
            _inputEvents.emit(UserInputEvent.Username(input))
        }
    }

    fun onPasswordEntered(input: String) {
//        val errorId = InputValidator.getPasswordErrorIdOrNull(input)
//        handle[PASSWORD] = password.value.copy(value = input, errorId = null)
        handle[PASSWORD] = password.value.copy(errorMessage = null)
        viewModelScope.launch(Dispatchers.Default) {
            _inputEvents.emit(UserInputEvent.Password(input))
        }
    }

    fun onUsernameImeActionClick() {
        viewModelScope.launch(Dispatchers.Default) {
            _events.emit(AuthScreenEvent.MoveFocus())
        }
    }

    fun onTrailingIconClick() {
        _uiState.update { it.copy(isPasswordVisible = !_uiState.value.isPasswordVisible) }
    }

    fun onTextFieldFocusChanged(key: FocusedTextFieldKey, isFocused: Boolean) {
        focusedTextField = if (isFocused) key else focusedTextField
    }

    fun onContinueClick() {
        val handler = CoroutineExceptionHandler { coroutineContext, throwable ->
            Timber.d("Exception caught: $coroutineContext - ${throwable.localizedMessage}")
        }
        viewModelScope.launch(Dispatchers.Default + handler) {

            when (val inputErrors = getInputErrorsOrNull()) {
                null -> {
                    clearFocusAndHideKeyboard()
                    _uiState.update {
                        it.copy(isFetchingUser = true)
                    }
                    authRepository.signIn(UserSignIn(
                        username.value.value.lowercase(), password.value.value))
                        .onSuccess { user ->
                            _uiState.update {
                                it.copy(isFetchingUser = false)
                            }

                            handle[USERNAME] = username.value.copy(errorMessage = null)
                            handle[PASSWORD] = password.value.copy(errorMessage = null)

                            authRepository.storeSignInData(user)
                                .onSuccess {
                                    _events.emit(AuthScreenEvent.NavigateToMain(user))
                                }
                                .onFailure { error ->
                                    error.message?.let {
                                        _events.emit(AuthScreenEvent.ShowSnackbar(it))
                                    }
                                }
                        }

                        .onFailure {
                            _uiState.update {
                                it.copy(isFetchingUser = false)
                            }
                            val error = it.localizedMessage
                            if (error != null) {
                                when {
                                    error.lowercase().contains("username") -> {
                                        focusedTextField = FocusedTextFieldKey.USERNAME
                                        _events.emit(AuthScreenEvent.RequestFocus(focusedTextField))
                                        handle[USERNAME] = username.value.copy(errorMessage = error)
                                    }
                                    error.lowercase().contains("password") -> {
                                        focusedTextField = FocusedTextFieldKey.PASSWORD
                                        _events.emit(AuthScreenEvent.RequestFocus(focusedTextField))
                                        handle[PASSWORD] = password.value.copy(errorMessage = error)
                                    }
                                    else -> {
                                        _events.emit(AuthScreenEvent.ShowSnackbar(error))
                                    }
                                }
                            }
                        }
                }
                else -> displayInputErrors(inputErrors)
            }
        }
    }

    fun focusOnLastSelectedField() {
        viewModelScope.launch(Dispatchers.Default) {
            Timber.d("focusOnLastSelectedField triggered %s", focusedTextField)
            delay(1000L)
            _events.emit(AuthScreenEvent.RequestFocus(focusedTextField))
            _events.emit(AuthScreenEvent.UpdateKeyboard(true))
        }
    }

    @OptIn(FlowPreview::class)
    private fun observeUserInputEvents() {
        viewModelScope.launch(Dispatchers.Default) {
            _inputEvents.onEach { event ->
                when (event) {
                    is UserInputEvent.Username -> handle[USERNAME] =
                        username.value.copy(value = event.input)
                    is UserInputEvent.Password -> handle[PASSWORD] =
                        password.value.copy(value = event.input)
                }
            }.debounce(1000L).collect { event ->
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
//                .onEach { event ->
//                    when (event) {
//                        is UserInputEvent.Username -> {
////                            when (InputValidator.getUsernameErrorIdOrNull(event.input)) {
////                                null -> {
//                                    handle[USERNAME] = username.value.copy(value = event.input)
////                                }
////                                else -> {
////                                    handle[USERNAME] =
////                                        _uiState.updateAndGet { it.copy(username = it.username.copy(value = event.input)) }.username
////                                }
////                            }
//                        }
//                        is UserInputEvent.Password -> {
////                            when (InputValidator.getPasswordErrorIdOrNull(event.input)) {
////                                null -> {
//                                    handle[PASSWORD] = password.value.copy(value = event.input)
////                                }
////                                else -> {
////                                    handle[PASSWORD] =
////                                        _uiState.updateAndGet { it.copy(password = it.password.copy(value = event.input)) }.password
////                                }
////                            }
//                        }
//                    }
//                }
//                .debounce(650)
//                .collect { event ->
//                    when (event) {
//                        is UserInputEvent.Username -> {
//                            val errorId = InputValidator.getUsernameErrorIdOrNull(event.input)
//                            handle[USERNAME] = username.value.copy(value = event.input, errorId = errorId)
//                        }
//                        is UserInputEvent.Password -> {
//                            val errorId = InputValidator.getPasswordErrorIdOrNull(event.input)
//                            handle[PASSWORD] = password.value.copy(value = event.input, errorId = errorId)
//                        }
//                    }
//                }
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
        _events.emit(AuthScreenEvent.ClearFocus)
        _events.emit(AuthScreenEvent.UpdateKeyboard(false))
        focusedTextField = FocusedTextFieldKey.NONE
    }
}