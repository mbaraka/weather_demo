package com.weather

import android.annotation.SuppressLint
import android.net.Uri
import android.util.Log
import com.google.gson.Gson
import com.weather.database.DBHelper
import com.weather.model.City
import com.weather.model.OpenWeather
import com.weather.utils.RequestHelper
import com.weather.utils.RxHelper
import com.weather.utils.rxError
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.functions.Action
import io.reactivex.functions.Consumer
import io.reactivex.internal.observers.EmptyCompletableObserver
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.PublishSubject

object WeatherCitiesPresenter {
    private val cityString = "CITY"
    private val countryCode = "countryCode"
    private val baseURL = "http://api.openweathermap.org/data/2.5/weather"
    private val APP_ID = "6871f74220dc203e86b498f2ab03ca3f"

    private val url = "http://api.openweathermap.org/data/2.5/weather?q=$cityString,$countryCode&APPID=$APP_ID"

    private var citiyList = ArrayList<City>()
    private val openWeatherEmitter: PublishSubject<OpenWeather> = PublishSubject.create()
    private val requestHelper: RequestHelper = RequestHelper()

    private val compositeDisposable = CompositeDisposable()

    fun start() {
        Observable.fromCallable {
            val openWeather = DBHelper.database.openWeatherDao().getAll()
            openWeather.forEach {
                openWeatherEmitter.onNext(it)
            }
        }
                .compose(RxHelper.asyncToUiObservable())
                .subscribe()
    }

    fun listenToCities(): Observable<OpenWeather> {
        return openWeatherEmitter.observeOn(AndroidSchedulers.mainThread())
    }

    fun addCities(cities: List<City>) {
        citiyList = cities as ArrayList<City>
        requestDataForCities()
    }

    private fun requestDataForCities() {
        compositeDisposable.add(Observable.fromIterable(citiyList)
                .flatMap {
                    requestData(it)
                }
                .subscribeOn(Schedulers.io())
                .subscribe(Consumer { openWeather ->
                    saveInDb(openWeather)
                    openWeatherEmitter.onNext(openWeather)
                }, rxError("failed to get data from open weather")))
    }

    @SuppressLint("CheckResult")
    private fun saveInDb(openWeather: OpenWeather) {
        Completable.fromAction {
            DBHelper.database.openWeatherDao().insertAll(openWeather)
        }
                .compose(RxHelper.asyncToUiCompletable())
                .subscribe(Action {
                    Log.i("", "action is done")
                }, rxError("Error while removing the content from the device"))


    }

    private fun requestData(city: City): Observable<OpenWeather> {
        val uri = Uri.parse(baseURL)
                .buildUpon()
                .appendQueryParameter("q", "${city.name},${city.countryCode}")
                .appendQueryParameter("APPID", APP_ID)
                .build().toString()

        return requestHelper.request(uri)
                .map { response ->
                    Gson().fromJson(response, OpenWeather::class.java)
                }
                .toObservable()
    }

    fun stop() {
        compositeDisposable.clear()
    }
}