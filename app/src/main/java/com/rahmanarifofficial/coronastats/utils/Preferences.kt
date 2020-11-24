package com.rahmanarifofficial.coronastats.utils

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit

class Preferences(val context: Context) {
    private val PREFS_NAME = "CoronaStats"
    private val IS_LOGIN = "IS_LOGIN"
    val prefs: SharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

    var isLogin: Boolean
        get() = prefs.getBoolean(IS_LOGIN, false)
        set(value) {
            prefs.edit {
                putBoolean(IS_LOGIN, value)
            }
        }
}