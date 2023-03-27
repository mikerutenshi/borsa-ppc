package com.android.borsappc.data.repository.datasource

import com.android.borsappc.data.model.*
import com.android.borsappc.data.model.ResponseBody
import com.android.borsappc.data.net.service.AuthService
import com.android.borsappc.di.RetrofitWithoutAuth
import retrofit2.Retrofit
import javax.inject.Inject

class AuthRemoteDataSource @Inject constructor(
    @RetrofitWithoutAuth private val retrofit : Retrofit) : AuthDataSource {

    override suspend fun signIn(signIn: UserSignIn): ResponseBody<User> {
        return retrofit.create(AuthService::class.java).signIn(signIn)
    }

    override suspend fun register(user: User): ResponseBody<Unit> {
        return retrofit.create(AuthService::class.java).register(user)
    }

    override suspend fun refreshToken(refreshToken: UserRefreshToken):
            ResponseBody<UserAccessToken> {
        return retrofit.create(AuthService::class.java).refreshToken(refreshToken)
    }

    override suspend fun signOut(userName: String): ResponseBody<Unit> {
        return retrofit.create(AuthService::class.java).signOut(Username(userName))
    }
}