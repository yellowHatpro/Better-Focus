package dev.yellowhatpro.betterfocus.utils

import android.content.pm.ApplicationInfo
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import dev.yellowhatpro.betterfocus.data.FocusApp
import java.lang.reflect.Type


object TypeConverter {

    fun listToJSON(list: List<FocusApp>) = Gson().toJson(list)!!

    fun listFromJSON(listJSON: String): List<FocusApp> {
        val type: Type = object : TypeToken<List<FocusApp>>() { }.type
        return Gson().fromJson(
            listJSON,
            type
        )
    }

    fun listOfStringToJSON(list: List<String>) = Gson().toJson(list)!!

    fun listOfStringFromJSON(listJSON: String): List<String> {
        val type: Type = object : TypeToken<List<String>>() { }.type
        return Gson().fromJson(
            listJSON,
            type
        )
    }

    fun listOfAppToJSON(list: List<ApplicationInfo?>) = Gson().toJson(list)!!

    fun listOfAppFromJSON(listJSON: String): List<ApplicationInfo?> {
        val type: Type = object : TypeToken<List<ApplicationInfo?>>() {}.type
        return GsonBuilder()
            .registerTypeAdapter(CharSequence::class.java, CharSequenceTypeAdapter())
            .create().fromJson(listJSON, type)
    }
}