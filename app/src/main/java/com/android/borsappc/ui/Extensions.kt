package com.android.borsappc.ui

import android.content.Context
import android.widget.Toast

fun Context.toast(messageId: Int, param: String?) {
    Toast.makeText(this, getString(messageId, param), Toast.LENGTH_SHORT).show()
}