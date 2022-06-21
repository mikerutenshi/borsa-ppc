package com.android.borsappc.data.model

import com.google.gson.annotations.SerializedName

data class UserAccessToken(
    @SerializedName("access_token")
    val accessToken: String
)
