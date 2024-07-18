package com.example.insta.mvvm.utils

import android.content.Context
import android.app.Activity
import android.content.Intent

fun Context.newScreen(c: Class<*>, isFinish: Boolean = false, clearStack: Boolean = false) {
    val intent = Intent(this, c)
    if (clearStack) {
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
    }
    startActivity(intent)
    if (isFinish && this is Activity) {
        finish()
    }
}
