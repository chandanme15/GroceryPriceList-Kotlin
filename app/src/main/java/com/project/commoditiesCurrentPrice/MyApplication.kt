package com.project.commoditiesCurrentPrice

import android.app.Application

class MyApplication : Application() {
    companion object {
        lateinit var instance: MyApplication
            private set
    }

    init {
        instance = this
    }
}