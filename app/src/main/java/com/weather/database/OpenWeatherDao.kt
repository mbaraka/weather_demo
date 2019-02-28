package com.weather.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.weather.model.OpenWeather

@Dao
interface OpenWeatherDao {

    @Query("SELECT * FROM openweather")
    fun getAll(): List<OpenWeather>

    @Insert
    fun insertAll(vararg openWeather: OpenWeather)

    @Delete
    fun delete(openWeather: OpenWeather)
}