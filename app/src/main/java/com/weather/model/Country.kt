package com.weather.model

import androidx.annotation.NonNull
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "country")
class Country {

    @PrimaryKey
    @ColumnInfo(name = "geoname_id")
    @NonNull
    lateinit var geonameId: String

    @ColumnInfo(name = "name")
    lateinit var countryName: String

    override fun toString(): String {
        return countryName
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Country

        if (geonameId != other.geonameId) return false

        return true
    }

    override fun hashCode(): Int {
        return geonameId.hashCode()
    }

}