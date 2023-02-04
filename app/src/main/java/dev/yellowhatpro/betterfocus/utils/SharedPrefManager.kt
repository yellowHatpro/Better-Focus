package dev.yellowhatpro.betterfocus.utils

import android.content.SharedPreferences
import android.preference.PreferenceManager
import dev.yellowhatpro.betterfocus.App

object SharedPrefManager {
    val pref : SharedPreferences
        get() = PreferenceManager.getDefaultSharedPreferences(App.context!!)

    fun setString(
        key: String?,
        value: String?
    ) {
        val editor = pref.edit()
        editor.putString(key, value)
        editor.apply()
    }

    fun setInteger(key: String?, value: Int) {
        val editor = pref.edit()
        editor.putInt(key, value)
        editor.apply()
    }

    fun setLong(key: String?, value: Long) {
        val editor = pref.edit()
        editor.putLong(key, value)
        editor.apply()
    }

    fun setBoolean(key: String?, value: Boolean) {
        val editor = pref.edit()
        editor.putBoolean(key, value)
        editor.apply()
    }

    var isOnboardingCompleted: Boolean?
        get() = pref.getBoolean("IS_OB_DONE", false)
        set(value){
            value?.let {
                setBoolean("IS_OB_DONE",value)
            }
        }
}