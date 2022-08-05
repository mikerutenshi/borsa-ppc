package com.android.borsappc.data.model

import com.google.gson.annotations.SerializedName

data class User(
    @SerializedName("id")
    private val id: Int,
    @SerializedName("username")
    val username: String,
    @SerializedName("password")
    private val password: String,
    @SerializedName("first_name")
    private val firstName: String,
    @SerializedName("last_name")
    private val lastName: String,
    @SerializedName("role")
    private val role: String,
    @SerializedName("access_token")
    private val accessToken: String,
    @SerializedName("refresh_token")
    private val refreshToken: String
)
