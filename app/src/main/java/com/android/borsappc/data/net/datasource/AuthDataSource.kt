package com.android.borsappc.data.net.datasource

import com.android.borsappc.data.model.User
import com.android.borsappc.data.model.UserAccessToken
import com.android.borsappc.data.model.UserRefreshToken
import com.android.borsappc.data.model.UserSignIn
import com.android.borsappc.data.net.response.GenericResponse

interface AuthDataSource {
    suspend fun signIn(signIn: UserSignIn): GenericResponse<User>
    suspend fun register(user: User): GenericResponse<Unit>
    suspend fun refreshToken(refreshToken: UserRefreshToken): GenericResponse<UserAccessToken>
    suspend fun signOut(userName: String): GenericResponse<Unit>
}