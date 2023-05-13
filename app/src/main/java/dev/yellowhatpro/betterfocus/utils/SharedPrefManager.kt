package dev.yellowhatpro.betterfocus.utils

import android.content.SharedPreferences
import android.content.pm.ApplicationInfo
import android.preference.PreferenceManager
import com.chargemap.compose.numberpicker.Hours
import dev.yellowhatpro.betterfocus.App
import dev.yellowhatpro.betterfocus.data.FocusApp

object SharedPrefManager {
    val pref: SharedPreferences
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
        set(value) {
            value?.let {
                setBoolean("IS_OB_DONE", value)
            }
        }
    var isUsageStatsPermissionGranted: Boolean?
        get() = pref.getBoolean("IS_USPG", false)
        set(value) {
            value?.let {
                setBoolean("IS_USPG", value)
            }
        }

    var allApps: List<String>?
        get() = pref.getString("ALL_APPS", "")?.let {
            if (it.isBlank()) null else
                TypeConverter.listOfStringFromJSON(it)
        }
        set(value) {
            value?.let {
                setString("ALL_APPS", TypeConverter.listOfStringToJSON(it))
            }
        }

    var focusList: List<FocusApp>?
        get() = pref.getString("TIME_LIMIT_OF_APPS", "")?.let {
            if (it.isBlank()) null else
                TypeConverter.listFromJSON(it)
        }
        set(value) {
            value?.let {
                setString("TIME_LIMIT_OF_APPS", TypeConverter.listToJSON(it))
            }
        }
    var allApplicationInfo: List<ApplicationInfo?>?
        get() = pref.getString("APPLICATION_INFO", "")?.let {
            if (it.isBlank()) null else
                TypeConverter.listOfAppFromJSON(it)
        }
        set(value) {
            value?.let {
                setString("APPLICATION_INFO", TypeConverter.listOfAppToJSON(it))
            }
        }

}