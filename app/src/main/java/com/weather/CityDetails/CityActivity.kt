package com.weather.CityDetails

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.weather.R
import com.weather.home.CitiesRecyclerAdapter
import com.weather.model.OpenWeather
import com.weather.utils.rxError
import io.reactivex.functions.Consumer
import kotlinx.android.synthetic.main.activity_city.*
import kotlinx.android.synthetic.main.content_city.*

class CityActivity : AppCompatActivity() {
    lateinit var openWeather: OpenWeather

    companion object {
        const val MODEL = "model"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_city)
        setSupportActionBar(toolbar)

        openWeather = intent.extras?.get(MODEL) as OpenWeather

        adjustRecyclerItems()
    }

    @SuppressLint("CheckResult")
    private fun adjustRecyclerItems() {
        val citiesRecyclerAdapter = CitiesRecyclerAdapter(false)

        activityCity_recycler.layoutManager = LinearLayoutManager(this)
        activityCity_recycler.setHasFixedSize(true)
        val dividerItemDecoration = DividerItemDecoration(this, DividerItemDecoration.VERTICAL)
        dividerItemDecoration.setDrawable(resources.getDrawable( R.drawable.divider))

        activityCity_recycler.addItemDecoration(dividerItemDecoration)
        activityCity_recycler.adapter = citiesRecyclerAdapter

        val presenter = CityDetailsPresenter(openWeather.id)
        presenter.requestDataForCities()
                .subscribe(Consumer {
                    toolbar.title = it.city.name
                    citiesRecyclerAdapter.addItem(it.list)
                }, rxError("failed to request the forecast for the city"))

    }

}
