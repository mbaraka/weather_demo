package com.weather.model

import androidx.annotation.NonNull
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.weather.database.DataTypeConverter
import java.io.Serializable

@Entity
class OpenWeather(
        @ColumnInfo(name = "autoId") @PrimaryKey(autoGenerate = true) val autoId: Long = 1,
        val base: String,
        @TypeConverters(DataTypeConverter::class) val clouds: Clouds,
        val cod: Int,
        @TypeConverters(DataTypeConverter::class) val coord: Coord,
        val dt: Int,
        val dt_txt: String?,
        @NonNull val id: Int,
        @TypeConverters(DataTypeConverter::class) val main: Main,
        val name: String,
        @TypeConverters(DataTypeConverter::class) val sys: Sys,
        val visibility: Int,
        @TypeConverters(DataTypeConverter::class) val weather: MutableList<Weather>,
        @TypeConverters(DataTypeConverter::class) val wind: Wind,
        val ignore: Boolean = false
) : Serializable {

    fun getIconUrl(): String {
        return "http://openweathermap.org/img/w/${weather[0].icon}.png"
    }

    companion object {
        fun createNotValid(name: String): OpenWeather {
            return OpenWeather(
                    base = "",
                    clouds = Clouds(all = 0),
                    cod = -1,
                    dt = -1,
                    dt_txt = "",
                    main = Main(humidity = -1, pressure = 0.0, temp = 0.0, temp_max = 0.0, temp_min = 0.0),
                    name = name,
                    sys = Sys(country = "", message = 0.0, sunrise = 0, sunset = 0, type = 0, id = 0),
                    visibility = -1,
                    weather = ArrayList(0),
                    wind = Wind(deg = 0.0, speed = 0.0),
                    ignore = true,
                    coord = Coord(lat = 0.0, lon = 0.0),
                    id = 0
            )
        }
    }
}

data class Main(
        @ColumnInfo(name = "idm") @PrimaryKey(autoGenerate = true) val idm: Long = 1,
        val humidity: Int,
        val pressure: Double,
        val temp: Double,
        val temp_max: Double,
        val temp_min: Double
) : Serializable

data class Wind(
        @ColumnInfo(name = "idwi") @PrimaryKey(autoGenerate = true) val idwi: Long = 1,
        val deg: Double,
        val speed: Double
) : Serializable

data class Weather(
        val description: String,
        val icon: String,
        @ColumnInfo(name = "idwe") @PrimaryKey @NonNull val idwe: Int,
        val main: String
) : Serializable

data class Sys(
        val country: String,
        @ColumnInfo(name = "ids") @PrimaryKey @NonNull val id: Int,
        val message: Double,
        val sunrise: Int,
        val sunset: Int,
        val type: Int
) : Serializable

data class Clouds(
        @ColumnInfo(name = "idcl") @PrimaryKey(autoGenerate = true) val idc: Long = 1,
        val all: Int
) : Serializable

data class Coord(
        @ColumnInfo(name = "idco") @PrimaryKey(autoGenerate = true) val idco: Long = 1,
        val lat: Double,
        val lon: Double
) : Serializable