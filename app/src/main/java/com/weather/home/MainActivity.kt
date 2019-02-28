package com.weather.home

import android.annotation.SuppressLint
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.weather.R
import com.weather.countryDialog.CountryDialog
import com.weather.utils.rxError
import io.reactivex.disposables.SerialDisposable
import io.reactivex.functions.Consumer
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*

class MainActivity : AppCompatActivity() {
    private lateinit var citiesRecyclerAdapter :CitiesRecyclerAdapter
    private val cityDisposable = SerialDisposable()
    private val errorDisposable = SerialDisposable()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        fab.setOnClickListener { view ->
            CountryDialog().show(this)
        }

        adjustRecyclerItems()
        listenToEvents()
        WeatherCitiesPresenter.loadSaveCities()
    }


    private fun adjustRecyclerItems() {
        citiesRecyclerAdapter = CitiesRecyclerAdapter(true)

        activityMain_recycler.layoutManager = LinearLayoutManager(this)
        activityMain_recycler.setHasFixedSize(true)
        val dividerItemDecoration = DividerItemDecoration(this, DividerItemDecoration.VERTICAL)
        dividerItemDecoration.setDrawable(resources.getDrawable( R.drawable.divider))

        activityMain_recycler.addItemDecoration(dividerItemDecoration)
        activityMain_recycler.adapter = citiesRecyclerAdapter
    }

    @SuppressLint("CheckResult")
    private fun listenToEvents() {
        cityDisposable.set(WeatherCitiesPresenter.listenToCities()
                .subscribe(Consumer{
                    citiesRecyclerAdapter.addItem(it)
                }, rxError("Failed to listen to cities changes")))

        errorDisposable.set(WeatherCitiesPresenter.listenToErrors()
                .subscribe(Consumer{
                    Toast.makeText(this, it, Toast.LENGTH_SHORT).show()
                }, rxError("Failed to listen to weather requests")))
    }

    override fun onResume() {
        listenToEvents()
        super.onResume()
    }

    override fun onDestroy() {
        WeatherCitiesPresenter.stop()
        super.onDestroy()
    }

}
