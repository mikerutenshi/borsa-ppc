package com.android.borsappc

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.datastore.core.DataStore
import androidx.lifecycle.lifecycleScope
import cafe.adriel.voyager.navigator.Navigator
import com.android.borsappc.data.model.User
import com.android.borsappc.data.repository.AuthRepository
import com.android.borsappc.ui.BorsaPpcTheme
import com.android.borsappc.ui.auth.AuthScreen
import com.android.borsappc.ui.main.MainScreen
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.last
import kotlinx.coroutines.flow.map
import timber.log.Timber
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject lateinit var authRepository: AuthRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        startScreen()
    }

    // "fix" android Q activity leak on back-pressed
    override fun onBackPressed() {
        if (onBackPressedDispatcher.hasEnabledCallbacks()) {
            super.onBackPressed()
        } else {
            finishAfterTransition()
        }
    }

    private fun startScreen() {
        lifecycleScope.launch {
            authRepository.getSignInData()
                .onSuccess { user ->
                    val screen = if (user.username.isNotEmpty()) MainScreen(user) else AuthScreen
                    setContent {
                        BorsaPpcTheme() {
                            Navigator(screen)
                        }
                    }
                }
                .onFailure { error ->
                    Timber.d("MainActivity - startScreen ${error.localizedMessage}") }
        }
    }
}