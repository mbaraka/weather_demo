package com.weather

import android.app.Application
import com.weather.database.DBHelper

open class Application : Application() {

    override fun onCreate() {
        super.onCreate()
        DBHelper.start(this)
    }

}