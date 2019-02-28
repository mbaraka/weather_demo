package com.weather.CityDetails

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.weather.R
import com.weather.home.CitiesRecyclerAdapter
import com.weather.model.OpenWeather
import com.weather.utils.loge
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

        progress.visibility = View.VISIBLE
        val presenter = CityDetailsPresenter(openWeather.id)
        presenter.requestDataForCities()
                .subscribe({
                    progress.visibility = View.GONE
                    toolbar.title = it.city.name
                    citiesRecyclerAdapter.addItem(it.list)
                }, {
                    progress.visibility = View.GONE
                    Toast.makeText(this, "failed to request the forecast for the city", Toast.LENGTH_LONG).show()
                })

    }

}
