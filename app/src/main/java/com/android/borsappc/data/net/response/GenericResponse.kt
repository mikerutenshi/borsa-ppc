package com.android.borsappc.data.net.response

import com.android.borsappc.data.model.Meta
import com.google.gson.annotations.SerializedName

data class GenericResponse<T>(
    @SerializedName("status")
    val status: String,
    @SerializedName("data")
    val data: T,
    @SerializedName("message")
    val message: String,
    @SerializedName("meta")
    val meta: Meta
)