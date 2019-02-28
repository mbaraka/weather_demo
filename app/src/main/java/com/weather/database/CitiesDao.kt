package com.weather.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.weather.model.City
import com.weather.model.Country

@Dao
interface CitiesDao {

    @Query("SELECT * FROM city")
    fun getAll(): List<Country>

    @Insert
    fun insertAll(vararg cities: City)

    @Delete
    fun delete(user: City)
}