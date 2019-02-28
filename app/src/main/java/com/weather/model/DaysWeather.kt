package com.weather.model

data class DaysWeather(
    val city: City,
    val cnt: Int,
    val cod: String,
    val list: List<OpenWeather>,
    val message: Double
)