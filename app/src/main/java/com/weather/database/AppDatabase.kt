package com.weather.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.weather.model.City
import com.weather.model.Country
import com.weather.model.OpenWeather

@Database(entities = [Country::class, City::class, OpenWeather::class],
        version = 1, exportSchema = false)
@TypeConverters(DataTypeConverter::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun countryDao(): CountriesDao
    abstract fun citiesDao(): CitiesDao
    abstract fun openWeatherDao(): OpenWeatherDao
}
