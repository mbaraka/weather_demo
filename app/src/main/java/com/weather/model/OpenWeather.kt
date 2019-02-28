package com.weather.model

import androidx.annotation.NonNull
import androidx.room.*
import com.weather.database.DataTypeConverter
import java.io.Serializable

@Entity
class OpenWeather(
        val base: String,
        @TypeConverters(DataTypeConverter::class) val clouds: Clouds,
        val cod: Int,
        @TypeConverters(DataTypeConverter::class) val coord: Coord,
        val dt: Int,
        @PrimaryKey @NonNull val id: Int,
        @TypeConverters(DataTypeConverter::class) val main: Main,
        val name: String,
        @TypeConverters(DataTypeConverter::class) val sys: Sys,
        val visibility: Int,
        @TypeConverters(DataTypeConverter::class) val weather: MutableList<Weather>,
        @TypeConverters(DataTypeConverter::class) val wind: Wind
) : Serializable

data class Main(
        @ColumnInfo(name = "idm") @PrimaryKey(autoGenerate = true) val idm: Long = 1,
        val humidity: Int,
        val pressure: Int,
        val temp: Double,
        val temp_max: Double,
        val temp_min: Double
)

data class Wind(
        @ColumnInfo(name = "idwi") @PrimaryKey(autoGenerate = true) val idwi: Long = 1,
        val deg: Double,
        val speed: Double
)

data class Weather(
        val description: String,
        val icon: String,
        @ColumnInfo(name = "idwe") @PrimaryKey @NonNull val idwe: Int,
        val main: String
)

data class Sys(
        val country: String,
        @ColumnInfo(name = "ids") @PrimaryKey @NonNull val id: Int,
        val message: Double,
        val sunrise: Int,
        val sunset: Int,
        val type: Int
)

data class Clouds(
        @ColumnInfo(name = "idcl") @PrimaryKey(autoGenerate = true) val idc: Long = 1,
        val all: Int
)

data class Coord(
        @ColumnInfo(name = "idco") @PrimaryKey(autoGenerate = true) val idco: Long = 1,
        val lat: Double,
        val lon: Double
)