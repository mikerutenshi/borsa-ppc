package com.android.borsappc.data.repository

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class AuthRepository @Inject constructor() {
    suspend fun signIn() {
        withContext(Dispatchers.IO) {

        }
    }
}