package dev.yellowhatpro.betterfocus.utils

import com.google.gson.Gson
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
}