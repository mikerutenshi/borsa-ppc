package com.android.borsappc

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class BorsaPPCApplication  : Application() {

    override fun onCreate() {
        super.onCreate()
    }
}