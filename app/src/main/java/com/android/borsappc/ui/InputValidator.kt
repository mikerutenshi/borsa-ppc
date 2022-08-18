package com.android.borsappc.ui

import com.android.borsappc.R
import java.util.regex.Pattern

const val PASSWORD_PATTERN = "^(?=.*[0-9])(?=.*[A-Z])(?=.*[@#$%^&+=!])(?=\\S+$).{8,}$"

object InputValidator {
    fun getUsernameErrorIdOrNull(input: String): Int? {
        return when {
            input.length < 3 -> R.string.error_username_too_short
            else -> null
        }
    }

    fun getPasswordErrorIdOrNull(input: String): Int? {
        val pattern = Pattern.compile(PASSWORD_PATTERN)

        return when {
            input.length < 8 -> R.string.error_password_too_short
//            !pattern.matcher(input).matches() -> R.string.error_password_regex
            else -> null
        }
    }
}