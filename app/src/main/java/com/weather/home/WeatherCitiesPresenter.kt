package com.weather.home

import android.annotation.SuppressLint
import android.net.Uri
import com.google.gson.Gson
import com.weather.database.DBHelper
import com.weather.model.City
import com.weather.model.OpenWeather
import com.weather.utils.*
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.PublishSubject

object WeatherCitiesPresenter {
    private const val baseURL = "http://api.openweathermap.org/data/2.5/weather"
    private const val APP_ID = "6871f74220dc203e86b498f2ab03ca3f"
    private const val metric = "metric"

    private var cityList = ArrayList<City>()
    private val openWeatherEmitter: PublishSubject<OpenWeather> = PublishSubject.create()
    private val errorEmitter: PublishSubject<String> = PublishSubject.create()
    private val requestHelper: RequestHelper = RequestHelper()

    private val compositeDisposable = CompositeDisposable()

    fun loadSaveCities() {
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

    fun listenToErrors(): Observable<String> {
        return errorEmitter.observeOn(AndroidSchedulers.mainThread())
    }

    fun addCities(cities: List<City>) {
        cityList = cities as ArrayList<City>
        requestDataForCities()
    }

    private fun requestDataForCities() {
        compositeDisposable.add(Observable.fromIterable(cityList)
                .flatMap {
                    requestData(it)
                            .onErrorResumeNext(Observable.just(OpenWeather.createNotValid(it.name)))
                }
                .subscribeOn(Schedulers.io())
                .subscribe({ openWeather ->
                    if (!openWeather.ignore) {
                        saveInDb(openWeather)
                        openWeatherEmitter.onNext(openWeather)
                    } else {
                        errorEmitter.onNext("Failed to get the data for city: ${openWeather.name}")
                        loge("Failed to get the data for a city")
                    }
                }, {
                    errorEmitter.onNext("Failed to get the data")
                    loge("failed to get the data")
                    it.printStackTrace()
                }))
    }

    @SuppressLint("CheckResult")
    private fun saveInDb(openWeather: OpenWeather) {
        Completable.fromAction {
            DBHelper.database.openWeatherDao().insertAll(openWeather)
        }
                .compose(RxHelper.asyncToUiCompletable())
                .subscribe(emptyAction("Saving OpenWeather is done"), rxError("Error while removing the content from the device"))


    }

    private fun requestData(city: City): Observable<OpenWeather> {
        val uri = Uri.parse(baseURL)
                .buildUpon()
                .appendQueryParameter("q", "${city.name},${city.countryCode}")
                .appendQueryParameter("APPID", APP_ID)
                .appendQueryParameter("units", metric)
                .build().toString()

        return requestHelper.request(uri)
                .map { response -> Gson().fromJson(response, OpenWeather::class.java) }
                .toObservable()
    }

    fun stop() {
        compositeDisposable.clear()
    }
}