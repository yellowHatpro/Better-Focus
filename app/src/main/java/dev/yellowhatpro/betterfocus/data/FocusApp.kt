package dev.yellowhatpro.betterfocus.data

import kotlin.random.Random

data class FocusApp(
    val packageName: String,
    val hours: Int,
    val minutes: Int,
    val id: Long = Random(100L).nextLong()
){
    companion object{
        val emptyFocusApp = FocusApp(
        "",
            0,0, 0L
        )
    }
}