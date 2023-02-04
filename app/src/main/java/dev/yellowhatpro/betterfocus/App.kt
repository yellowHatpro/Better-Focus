package dev.yellowhatpro.betterfocus

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class App : Application() {
    override fun onCreate() {
        super.onCreate()
        context = this
    }
    companion object {
        var context: App? = null
    }
}