package com.android.borsappc.data.net.service

import com.android.borsappc.data.model.*
import com.android.borsappc.data.net.response.GenericResponse
import io.reactivex.Single
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthService {
    @POST("v1/users/authenticate")
    suspend fun signIn(@Body signInModel: UserSignIn): GenericResponse<User>

    @POST("v1/users")
    suspend fun register(@Body userModel: User): GenericResponse<Unit>

    @POST("v1/users/token")
    fun refreshToken(@Body refreshTokenModel: UserRefreshToken): GenericResponse<UserAccessToken>?

    @POST("v1/users/signout")
    suspend fun signOut(@Body userNameModel: Username): GenericResponse<Unit>?
}