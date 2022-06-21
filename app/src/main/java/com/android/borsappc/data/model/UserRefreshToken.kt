package com.android.borsappc.data.model

import com.google.gson.annotations.SerializedName

data class UserRefreshToken(
    @SerializedName("username")
    private val username: String,
    @SerializedName("refresh_token")
    private val refreshToken: String
)
