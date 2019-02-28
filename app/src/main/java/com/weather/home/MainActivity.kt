package com.weather.home

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.weather.R
import com.weather.WeatherCitiesPresenter
import com.weather.countryDialog.CountryDialog
import com.weather.utils.rxError
import io.reactivex.functions.Consumer
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*

class MainActivity : AppCompatActivity() {
    private val mainActivityPresenter = MainActivityPresenter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)
        mainActivityPresenter.start()

        fab.setOnClickListener { view ->
            CountryDialog().show(this)
        }

        adjustRecyclerItems()
    }

    @SuppressLint("CheckResult")
    private fun adjustRecyclerItems() {
        val citiesRecyclerAdapter = CitiesRecyclerAdapter()
        activityMain_recycler.layoutManager = LinearLayoutManager(this)
        activityMain_recycler.setHasFixedSize(true)
        activityMain_recycler.adapter = citiesRecyclerAdapter

        WeatherCitiesPresenter.listenToCities()
                .subscribe(Consumer{
                    citiesRecyclerAdapter.addItem(it)
                }, rxError("Failed to listen to cities changes"))
        WeatherCitiesPresenter.start()
    }

    override fun onDestroy() {
        WeatherCitiesPresenter.stop()
        super.onDestroy()
    }

}
