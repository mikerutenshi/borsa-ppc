package com.android.borsappc.ui

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class InputWrapper(
    val value: String = "",
    val errorId: Int? = null,
    val errorMessage: String? = null
) : Parcelable
