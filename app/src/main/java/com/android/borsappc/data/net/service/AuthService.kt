package com.android.borsappc.data.net.service

import com.android.borsappc.data.model.*
import com.android.borsappc.data.model.ResponseBody
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthService {
    @POST("v1/users/authenticate")
    suspend fun signIn(@Body signInModel: UserSignIn): ResponseBody<User>

    @POST("v1/users")
    suspend fun register(@Body userModel: User): ResponseBody<Unit>

    @POST("v1/users/token")
    suspend fun refreshToken(@Body refreshTokenModel: UserRefreshToken): ResponseBody<UserAccessToken>

    @POST("v1/users/signout")
    suspend fun signOut(@Body userNameModel: Username): ResponseBody<Unit>
}