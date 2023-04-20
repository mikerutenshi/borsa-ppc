package com.android.borsappc.ui.screen.auth

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Button
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Scaffold
import androidx.compose.material.SnackbarDuration
import androidx.compose.material.SnackbarResult
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.ExperimentalLifecycleComposeApi
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.flowWithLifecycle
import cafe.adriel.voyager.androidx.AndroidScreen
import cafe.adriel.voyager.hilt.getViewModel
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.android.borsappc.R
import com.android.borsappc.toast
import com.android.borsappc.ui.CustomTextField
import com.android.borsappc.ui.White
import com.android.borsappc.ui.screen.main.MainScreen
import timber.log.Timber

object AuthScreen : AndroidScreen() {
    @Composable
    override fun Content() {
        val viewModel = getViewModel<AuthViewModel>()
        AuthScreenContent(viewModel)
    }

}

@OptIn(ExperimentalComposeUiApi::class, ExperimentalLifecycleComposeApi::class)
@Composable
fun AuthScreenContent(viewModel: AuthViewModel) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    val focusManager = LocalFocusManager.current
    val keyboardController = LocalSoftwareKeyboardController.current
    val usernameFocusRequester = remember { FocusRequester() }
    val passwordFocusRequester = remember { FocusRequester() }
    val scaffoldState = rememberScaffoldState()
    val navigator = LocalNavigator.currentOrThrow

    val events = remember(viewModel.events, lifecycleOwner) {
        viewModel.events.flowWithLifecycle(
            lifecycleOwner.lifecycle,
            Lifecycle.State.STARTED)
    }

    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val username by viewModel.username.collectAsStateWithLifecycle()
    val password by viewModel.password.collectAsStateWithLifecycle()
    val areInputValid by viewModel.areInputValid.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        Timber.d("launchedEffect triggered")
        events.collect { event ->
            when (event) {
                is AuthScreenEvent.ShowToast -> context.toast(event.messageId, event.param)
                is AuthScreenEvent.UpdateKeyboard -> {
                    if (event.show) keyboardController?.show() else keyboardController?.hide()
                }
                is AuthScreenEvent.ClearFocus -> focusManager.clearFocus()
                is AuthScreenEvent.RequestFocus -> {
                    Timber.d("requestFocus triggered")
                    when (event.textFieldKey) {
                        FocusedTextFieldKey.USERNAME -> usernameFocusRequester.requestFocus()
                        FocusedTextFieldKey.PASSWORD -> passwordFocusRequester.requestFocus()
                        FocusedTextFieldKey.NONE -> focusManager.clearFocus()
                    }
                }
                is AuthScreenEvent.MoveFocus -> focusManager.moveFocus(event.direction)
                is AuthScreenEvent.ShowSnackbar -> {

                    val result
                    = scaffoldState.snackbarHostState.showSnackbar(
                        event.message,
                        "Dismiss",
                        SnackbarDuration.Indefinite)
                    when (result) {
                        SnackbarResult.Dismissed -> {}
                        SnackbarResult.ActionPerformed ->
                            scaffoldState.snackbarHostState.currentSnackbarData?.dismiss()
                    }
                }
                is AuthScreenEvent.NavigateToMain -> {
                    navigator.replaceAll(MainScreen(event.user))
                }
            }
        }
    }

    DisposableEffect(key1 = viewModel) {
        Timber.d("disposableEffect triggered")
        viewModel.focusOnLastSelectedField()
        onDispose {  }
    }

    Scaffold(scaffoldState = scaffoldState) { paddingValues ->
        Column(
            modifier = Modifier.fillMaxSize().padding(paddingValues),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            CustomTextField(
                modifier = Modifier
                    .focusRequester(usernameFocusRequester)
                    .onFocusChanged { focusState ->
                        viewModel.onTextFieldFocusChanged(
                            key = FocusedTextFieldKey.USERNAME,
                            isFocused = focusState.isFocused
                        )
                    },
                labelResId = R.string.placeholder_username,
                keyboardOptions = remember {
                    KeyboardOptions(
                        keyboardType = KeyboardType.Text,
                        imeAction = ImeAction.Next
                    )
                },
                inputWrapper = username,
                onValueChange = viewModel::onUsernameEntered,
                onImeKeyAction = viewModel::onUsernameImeActionClick
            )
            Spacer(Modifier.height(16.dp))
            CustomTextField(
                modifier = Modifier
                    .focusRequester(passwordFocusRequester)
                    .onFocusChanged { focusState ->
                        viewModel.onTextFieldFocusChanged(
                            key = FocusedTextFieldKey.PASSWORD,
                            isFocused = focusState.isFocused
                        )
                    },
                labelResId = R.string.placeholder_password,
                keyboardOptions = remember {
                    KeyboardOptions(
                        keyboardType = KeyboardType.Password,
                        imeAction = ImeAction.Done
                    )
                },
                visualTransformation = if (uiState.isPasswordVisible) VisualTransformation.None
                else PasswordVisualTransformation(),
                trailingIcon = {
                    IconButton(onClick = viewModel::onTrailingIconClick) {
                        val image = if (uiState.isPasswordVisible) Icons.Filled.Visibility
                        else Icons.Filled.VisibilityOff
                        val desc = if (uiState.isPasswordVisible) "Hide password"
                        else "Show password"
                        Icon(imageVector = image, contentDescription = desc)
                    }
                },
                inputWrapper = password,
                onValueChange = viewModel::onPasswordEntered,
                onImeKeyAction = viewModel::onContinueClick
            )
            Spacer(Modifier.height(32.dp))
            Button(onClick = viewModel::onContinueClick, enabled = areInputValid) {
                Row(
                    modifier = Modifier.padding(16.dp), horizontalArrangement =
                    Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    if (uiState.isFetchingUser) {
                        Box(modifier = Modifier.size(24.dp)) {
                            CircularProgressIndicator(
                                modifier = Modifier.fillMaxSize(),
                                color = White,
                                strokeWidth = Dp(value = 4F)
                            )
                        }
                        Spacer(modifier = Modifier.width(16.dp))
                    }
                    Text(text = stringResource(id = R.string.btn_continue))
                }
            }
        }
    }
}