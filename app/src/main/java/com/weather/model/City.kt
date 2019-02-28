package com.weather.model

import androidx.annotation.NonNull
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "city")
class City {

    @PrimaryKey
    @NonNull
    @ColumnInfo(name = "geoname_id")
    lateinit var geonameId: String

    @ColumnInfo(name = "name")
    lateinit var name: String

    lateinit var countryCode: String

    override fun toString(): String {
        return name
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as City

        if (geonameId != other.geonameId) return false

        return true
    }

    override fun hashCode(): Int {
        return geonameId.hashCode()
    }


}