package dev.yellowhatpro.betterfocus.utils

import com.chargemap.compose.numberpicker.Hours
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.lang.reflect.Type

object TypeConverter {

    fun listToJSON(list: List<Pair<String, Hours>>) = Gson().toJson(list)!!

    fun listFromJSON(listJSON: String): List<Pair<String, Hours>> {
        val type: Type = object : TypeToken<List<Pair<String, Pair<Int,Int>>>>() {}.type
        return Gson().fromJson(
            listJSON,
            type
        )
    }
}