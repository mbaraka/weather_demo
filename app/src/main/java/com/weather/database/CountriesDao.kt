package com.weather.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.weather.model.Country

@Dao
interface CountriesDao {

    @Query("SELECT * FROM country")
    fun getAll(): List<Country>

    @Insert
    fun insertAll(vararg countries: Country)

    @Insert
    fun insertAll(countryList: List<Country>)
}