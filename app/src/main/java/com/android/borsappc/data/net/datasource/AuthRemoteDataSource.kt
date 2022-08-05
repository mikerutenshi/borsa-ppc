package com.android.borsappc.data.net.datasource

import com.android.borsappc.data.model.*
import com.android.borsappc.data.net.response.GenericResponse
import com.android.borsappc.data.net.service.AuthService
import com.android.borsappc.di.RetrofitWithoutAuth
import retrofit2.Retrofit
import timber.log.Timber
import javax.inject.Inject

class AuthRemoteDataSource @Inject constructor(
    @RetrofitWithoutAuth private val retrofit : Retrofit) : AuthDataSource {

    override suspend fun signIn(signIn: UserSignIn): GenericResponse<User> {
        return retrofit.create(AuthService::class.java).signIn(signIn)
    }

    override suspend fun register(user: User): GenericResponse<Unit> {
        return retrofit.create(AuthService::class.java).register(user)
    }

    override fun refreshToken(refreshToken: UserRefreshToken):
            GenericResponse<UserAccessToken> {
        return retrofit.create(AuthService::class.java).refreshToken(refreshToken)
    }

    override suspend fun signOut(userName: String): GenericResponse<Unit> {
        return retrofit.create(AuthService::class.java).signOut(Username(userName))
    }
}