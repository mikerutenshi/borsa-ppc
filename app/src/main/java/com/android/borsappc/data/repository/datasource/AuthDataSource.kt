package com.android.borsappc.data.repository.datasource

import com.android.borsappc.data.model.*

interface AuthDataSource {
    suspend fun signIn(signIn: UserSignIn): ResponseBody<User>
    suspend fun register(user: User): ResponseBody<Unit>
    suspend fun refreshToken(refreshToken: UserRefreshToken): ResponseBody<UserAccessToken>
    suspend fun signOut(userName: String): ResponseBody<Unit>
}