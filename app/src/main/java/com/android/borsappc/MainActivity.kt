package com.android.borsappc

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.android.borsappc.ui.BorsaPPCTheme
import com.android.borsappc.ui.auth.AuthViewModel
import com.android.borsappc.ui.main.MainScreen
import com.android.borsappc.ui.main.MainViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private val authViewModel: AuthViewModel by viewModels()
    private val mainViewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            BorsaPPCTheme {
                MainScreen(
                    mainViewModel,
                    authViewModel
                )
            }
        }
    }
}