package com.weather.CityDetails

import android.net.Uri
import com.google.gson.Gson
import com.weather.model.DaysWeather
import com.weather.utils.RequestHelper
import com.weather.utils.RxHelper
import io.reactivex.Observable

class CityDetailsPresenter(private val cityId: Int) {
    private val baseURL = "http://api.openweathermap.org/data/2.5/forecast"
    private val APP_ID = "6871f74220dc203e86b498f2ab03ca3f"
    private val metric = "metric"

    private val requestHelper: RequestHelper = RequestHelper()

    fun requestDataForCities(): Observable<DaysWeather> {
        val uri = Uri.parse(baseURL)
                .buildUpon()
                .appendQueryParameter("id", cityId.toString())
                .appendQueryParameter("APPID", APP_ID)
                .appendQueryParameter("units", metric)
                .build().toString()

       return requestHelper.request(uri)
                .map { response ->
                    Gson().fromJson(response, DaysWeather::class.java)
                }
                .toObservable()
                .compose(RxHelper.asyncToUiObservable())
    }
}