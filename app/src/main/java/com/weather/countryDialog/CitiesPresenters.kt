package com.weather.countryDialog

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.weather.model.City
import com.weather.model.Country
import com.weather.utils.RequestHelper

import org.json.JSONObject

import java.lang.reflect.Type
import java.util.ArrayList
import java.util.HashMap

import io.reactivex.Observable

object CitiesPresenters {
    private val user = "babo"
    private val geoNames = "geonames"

    private val city_geonameId = "city_geonameId"
    private val URL_CITY = "http://api.geonames.org/childrenJSON?geonameId=$city_geonameId&username=$user"

    private val citiesMap = HashMap<Country, List<City>>()

    fun getCities(country: Country): Observable<List<City>> {

        if (citiesMap.containsKey(country) && citiesMap[country] != null)
            return Observable.just(citiesMap[country]!!)

        val requestHelper = RequestHelper()

        return requestHelper.request(URL_CITY.replace(city_geonameId, country.geonameId))
                .map { s ->
                    val jsonObject = JSONObject(s)
                    val json = jsonObject.getString(geoNames)
                    val listType = object : TypeToken<ArrayList<City>>() {

                    }.type
                    val list = Gson().fromJson<List<City>>(json, listType)
                    citiesMap[country] = list
                    list
                }.toObservable()
    }

}
