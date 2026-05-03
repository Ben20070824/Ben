package com.example.ben

import android.app.Application
import android.content.Context

class MyApplication : Application() {
    companion object{
        lateinit var globalContext: Context
            private set
    }

    override fun onCreate() {
        super.onCreate()
        globalContext = applicationContext
    }
}