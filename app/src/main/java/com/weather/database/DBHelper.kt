package com.weather.database

import android.content.Context
import androidx.room.Room

object DBHelper {
    lateinit var database: AppDatabase
        private set

    fun start(context: Context) {
        database = Room.databaseBuilder(
            context,
            AppDatabase::class.java, "weather_db"
        ).build()
    }



}