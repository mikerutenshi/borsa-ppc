package com.android.borsappc.data.model

import com.google.gson.annotations.SerializedName

data class ResponseBody<T>(
    @SerializedName("status")
    val status: String,
    @SerializedName("message")
    val message: String,
    @SerializedName("data")
    val data: T,
    @SerializedName("meta")
    val meta: Meta?
)
data class Meta(
    @SerializedName("total_page")
    val totalPage: Int
)
