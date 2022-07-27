package com.android.borsappc.data.repository

import com.android.borsappc.data.model.User
import com.android.borsappc.data.model.UserSignIn
import com.android.borsappc.data.net.datasource.AuthDataSource
import com.android.borsappc.data.net.datasource.AuthRemoteDataSource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.withContext
import javax.inject.Inject

class AuthRepository @Inject constructor(
    private val authRemoteDataSource: AuthRemoteDataSource
) {
    suspend fun signInFlow(signIn: UserSignIn) = flow<User> {
        val user = authRemoteDataSource.signIn(signIn).data
        emit(user)
    }.flowOn(Dispatchers.IO)

    suspend fun signIn(signIn: UserSignIn) =
        authRemoteDataSource.signIn(signIn).data
}