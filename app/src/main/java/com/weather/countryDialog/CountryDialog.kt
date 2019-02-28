package com.weather.countryDialog

import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.weather.R
import com.weather.home.WeatherCitiesPresenter

class CountryDialog {

    fun show(activity: AppCompatActivity) {
        val countryDialogView = CountryDialogView(activity)
        AlertDialog.Builder(activity)
                .setView(countryDialogView)
                .setPositiveButton(R.string.confirm) { _, _ ->
                    WeatherCitiesPresenter.addCities(countryDialogView.cityList)
                }
                .setNegativeButton(R.string.cancel) { _, _ -> }
                .create().show()
    }


}