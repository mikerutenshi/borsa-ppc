package com.android.borsappc

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import com.android.borsappc.ui.BorsaPPCAPP
import com.android.borsappc.ui.BorsaPPCTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            BorsaPPCTheme {
                BorsaPPCAPP()
            }
        }
    }
}