package com.android.borsappc.data.repository

import com.android.borsappc.data.model.User
import com.android.borsappc.data.model.UserSignIn
import com.android.borsappc.data.net.datasource.AuthDataSource
import com.android.borsappc.data.net.datasource.AuthRemoteDataSource
import com.android.borsappc.util.resultOf
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.withContext
import timber.log.Timber
import javax.inject.Inject

class AuthRepository @Inject constructor(
    private val authRemoteDataSource: AuthRemoteDataSource
) {
//    fun signIn(signIn: UserSignIn): Flow<User> {
//        return flow<User> {
//            val data = authRemoteDataSource.signIn(signIn).data
////            val data = User(1, "Michael", "Password", "Michael", "Susanto", "Student",
////            "AccessToken", "RefreshToken")
//            Timber.d("data triggered ${data.username}")
//            emit(data)
//        }.flowOn(Dispatchers.IO)
//    }

    suspend fun signIn(signIn: UserSignIn): Result<User> =
        resultOf {
            withContext(Dispatchers.IO) {
                authRemoteDataSource.signIn(signIn).data
            }
        }
}