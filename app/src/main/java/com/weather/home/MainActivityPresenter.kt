package com.weather.home

import com.weather.CountriesPresenter
import com.weather.utils.rxError
import com.weather.model.Country
import io.reactivex.disposables.SerialDisposable
import io.reactivex.functions.Consumer

class MainActivityPresenter {
    private val countriesDisposable: SerialDisposable = SerialDisposable()
    lateinit var countries: List<Country>

    fun start() {
        getCountries()
    }

    private fun getCountries() {
        countriesDisposable.set(CountriesPresenter().countries.subscribe(Consumer {
            countries = it
        }, rxError("failed to get countries")))
    }
}