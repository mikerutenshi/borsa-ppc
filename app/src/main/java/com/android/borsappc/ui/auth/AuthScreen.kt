package com.android.borsappc.ui.auth

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.runtime.*
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
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.ExperimentalLifecycleComposeApi
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.flowWithLifecycle
import androidx.navigation.NavHostController
import com.android.borsappc.R
import com.android.borsappc.toast
import com.android.borsappc.ui.*
import com.android.borsappc.ui.main.DrawerScreens
import timber.log.Timber

@OptIn(ExperimentalComposeUiApi::class, ExperimentalLifecycleComposeApi::class)
@Composable
fun AuthScreen(navController: NavHostController) {
    val viewModel = hiltViewModel<AuthViewModel>()
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    val focusManager = LocalFocusManager.current
    val keyboardController = LocalSoftwareKeyboardController.current
    val usernameFocusRequester = remember { FocusRequester() }
    val passwordFocusRequester = remember { FocusRequester() }
    val scaffoldState = rememberScaffoldState()

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
                is ScreenEvent.ShowToast -> context.toast(event.messageId, event.param)
                is ScreenEvent.UpdateKeyboard -> {
                    if (event.show) keyboardController?.show() else keyboardController?.hide()
                }
                is ScreenEvent.ClearFocus -> focusManager.clearFocus()
                is ScreenEvent.RequestFocus -> {
                    Timber.d("requestFocus triggered")
                    when (event.textFieldKey) {
                        FocusedTextFieldKey.USERNAME -> usernameFocusRequester.requestFocus()
                        FocusedTextFieldKey.PASSWORD -> passwordFocusRequester.requestFocus()
                        FocusedTextFieldKey.NONE -> focusManager.clearFocus()
                    }
                }
                is ScreenEvent.MoveFocus -> focusManager.moveFocus(event.direction)
                is ScreenEvent.ShowSnackbar -> {
                    val result = scaffoldState.snackbarHostState.showSnackbar(
                        event.message,
                        "Dismiss",
                        SnackbarDuration.Indefinite)
                    when (result) {
                        SnackbarResult.Dismissed -> Unit
                        SnackbarResult.ActionPerformed ->
                            scaffoldState.snackbarHostState.currentSnackbarData?.dismiss()
                    }
                }
                is ScreenEvent.SignIn -> {
                    val dashboardRoute = DrawerScreens.Main.route
                    navController.navigate(dashboardRoute) {
                        popUpTo(dashboardRoute)
                        launchSingleTop = true
                    }
                }
            }
        }
    }

    DisposableEffect(key1 = viewModel) {
        Timber.d("disposableEffect triggered")
        viewModel.focusOnLastSelectedField()
        onDispose {  }
    }

    Scaffold(scaffoldState = scaffoldState) {
        Column(
            modifier = Modifier.fillMaxSize(),
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
                    Text(text = stringResource(id = R.string.signin_continue))
                }
            }
        }
    }
}