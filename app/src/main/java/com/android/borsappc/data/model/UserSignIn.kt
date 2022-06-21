package com.android.borsappc.data.model

import com.google.gson.annotations.SerializedName

data class UserSignIn(
    @SerializedName("username")
    private val username: String,
    @SerializedName("password")
    private val password: String
)
