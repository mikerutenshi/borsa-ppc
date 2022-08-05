package com.android.borsappc.ui.auth

import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.toLowerCase
import androidx.lifecycle.*
import com.android.borsappc.R
import com.android.borsappc.data.model.UserSignIn
import com.android.borsappc.data.repository.AuthRepository
import com.android.borsappc.ui.InputValidator
import com.android.borsappc.ui.InputWrapper
import com.android.borsappc.ui.ScreenEvent
import com.android.borsappc.ui.UserInputEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import retrofit2.HttpException
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
        viewModelScope, SharingStarted.WhileSubscribed(5000L), AuthUiState())

    private val _events = MutableSharedFlow<ScreenEvent>()
    val events = _events.asSharedFlow().shareIn(
        viewModelScope, SharingStarted.WhileSubscribed(5000L))
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
        val handler = CoroutineExceptionHandler { coroutineContext, throwable ->
            Timber.d("Exception caught: $coroutineContext - ${throwable.localizedMessage}")
        }
        viewModelScope.launch(Dispatchers.Default + handler) {
//            val resId = if (areInputsValid.value) R.string.validation_success else
//                R.string.validation_fail
//            _events.send(ScreenEvent.ShowToast(resId))
            when (val inputErrors = getInputErrorsOrNull()) {
                null -> {
                    clearFocusAndHideKeyboard()
                    Timber.d("onContinueClickEmit triggered")
//                    _events.emit(ScreenEvent.ShowToast(R.string.validation_success))
//                    authRepository.signIn(UserSignIn(username.value.value,
//                        password.value.value))
//                        .onStart {
//                            Timber.d("onStart triggered")
//                            /*todo show progress bar*/
//                        }
//                        .catch { exception ->
//                            _uiState.update {
//                                val errorMessage = if (exception is HttpException) {
//                                    Message(exception.code(), exception.message())
//                                } else {
//                                    exception.localizedMessage?.let { message ->
//                                        Message(null, message) } ?:
//                                    Message(null, "terdapat error")
//                                }
//                                val messages = _uiState.value.messages
//                                messages.add(errorMessage)
//                                it.copy(messages = messages)
//                            }
//                        }
//                        .collect { user ->
//                            Timber.d("${user.username} is collected")
//                            _events.emit(ScreenEvent.ShowToast(R.string.user_logged_in, user.username))
//                        }
                    _uiState.update {
                        it.copy(isFetchingUser = true)
                    }
                    launch() {
                        authRepository.signIn(UserSignIn(
                            username.value.value.lowercase(), password.value.value))
                            .onSuccess { user ->
                                _uiState.update {
                                    it.copy(isFetchingUser = false)
                                }
                                _events.emit(ScreenEvent.ShowToast(
                                        R.string.user_logged_in, user.username))
                               handle[USERNAME] = username.value.copy(errorMessage = null)
                                handle[PASSWORD] = password.value.copy(errorMessage = null)
                            }
                            .onFailure {
                                _uiState.update {
                                    it.copy(isFetchingUser = false)
                                }
                                val error = it.localizedMessage
                                if (error != null) {
//                                    when {
//                                        error.lowercase().contains("username") -> {
//                                            focusedTextField = FocusedTextFieldKey.USERNAME
//                                            _events.emit(ScreenEvent.RequestFocus(focusedTextField))
//                                            handle[USERNAME] = password.value.copy(errorMessage = error)
//                                        }
//                                        error.lowercase().contains("password") -> {
//                                            focusedTextField = FocusedTextFieldKey.PASSWORD
//                                            _events.emit(ScreenEvent.RequestFocus(focusedTextField))
//                                            handle[PASSWORD] = password.value.copy(errorMessage = error)
//                                        }
//                                        else -> {
                                            _uiState.update { state ->
                                                state.messages.add(Message(null, error))
                                                state.copy(messages = state.messages)
                                            }
                                            Timber.d("Error Message ${_uiState.value.messages[0]}")
//                                        }
//                                    }
                                }
//                                _events.emit(ScreenEvent.ShowToast(
//                                    R.string.user_logged_in, it.localizedMessage))
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
            _events.emit(ScreenEvent.RequestFocus(focusedTextField))
            _events.emit(ScreenEvent.UpdateKeyboard(true))
        }
    }

    fun clearUserMessage(message: Message) {
        val messages = _uiState.value.messages
        messages.remove(message)
        _uiState.update { it.copy(messages = messages) }
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
        _events.emit(ScreenEvent.ClearFocus)
        _events.emit(ScreenEvent.UpdateKeyboard(false))
        focusedTextField = FocusedTextFieldKey.NONE
    }
}