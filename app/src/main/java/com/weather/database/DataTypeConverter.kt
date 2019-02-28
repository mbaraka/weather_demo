package com.weather.database

import androidx.room.TypeConverter
import com.google.gson.reflect.TypeToken
import java.util.Collections.emptyList
import com.google.gson.Gson
import com.weather.model.*
import java.util.*
import android.R.attr.data
import java.util.Collections.emptyList




class DataTypeConverter {
    companion object {
        private val gson = Gson()

        @TypeConverter
        @JvmStatic
        fun toMain(data: String?): Main {
            val listType = object : TypeToken<Main>() {}.type
            return gson.fromJson<Main>(data, listType)
        }

        @TypeConverter
        @JvmStatic
        fun fromMain(someObjects: Main): String {
            return gson.toJson(someObjects)
        }

        //
        @TypeConverter
        @JvmStatic
        fun toWind(data: String?): Wind {
            val listType = object : TypeToken<Wind>() {}.type
            return gson.fromJson<Wind>(data, listType)
        }

        @TypeConverter
        @JvmStatic
        fun fromWind(someObjects: Wind): String {
            return gson.toJson(someObjects)
        }

        //
        @TypeConverter
        @JvmStatic
        fun stringToWeatherList(data: String?): MutableList<Weather> {
            if (data == null) {
                return Collections.emptyList()
            }

            val listType = object : TypeToken<MutableList<Weather>>() {

            }.type

            return gson.fromJson(data, listType)
        }

        @TypeConverter
        @JvmStatic
        fun weatherListToString(someObjects: MutableList<Weather>): String {
            return gson.toJson(someObjects)
        }

        //
        @TypeConverter
        @JvmStatic
        fun toSys(data: String?): Sys {
            val listType = object : TypeToken<Sys>() {}.type
            return gson.fromJson<Sys>(data, listType)
        }

        @TypeConverter
        @JvmStatic
        fun fromSys(someObjects: Sys): String {
            return gson.toJson(someObjects)
        }


        //
        @TypeConverter
        @JvmStatic
        fun toClouds(data: String?): Clouds {
            val listType = object : TypeToken<Clouds>() {}.type
            return gson.fromJson<Clouds>(data, listType)
        }

        @TypeConverter
        @JvmStatic
        fun fromClouds(someObjects: Clouds): String {
            return gson.toJson(someObjects)
        }

        //
        @TypeConverter
        @JvmStatic
        fun toCoord(data: String?): Coord {
            val listType = object : TypeToken<Coord>() {}.type
            return gson.fromJson<Coord>(data, listType)
        }

        @TypeConverter
        @JvmStatic
        fun fromCoord(someObjects: Coord): String {
            return gson.toJson(someObjects)
        }
    }

}