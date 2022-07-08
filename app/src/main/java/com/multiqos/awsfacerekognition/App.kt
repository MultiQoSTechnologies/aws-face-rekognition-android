package com.multiqos.awsfacerekognition

import android.app.Application

/**Application class*/
class App : Application() {

    override fun onCreate() {
        super.onCreate()
        context = this
    }
    companion object {
        lateinit var context: App
            private set
    }
}