package com.weather.countryDialog

import android.content.DialogInterface
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.weather.R
import com.weather.home.WeatherCitiesPresenter
import com.weather.model.City

class CountryDialog {


    fun show(activity: AppCompatActivity): List<City> {

        val countryDialogView = CountryDialogView(activity)
        AlertDialog.Builder(activity)
                .setView(countryDialogView)
                .setPositiveButton(R.string.confirm,
                        DialogInterface.OnClickListener { dialog, id ->
                            // User clicked OK, so save the selectedItems results somewhere
                            // or return them to the component that opened the dialog
                            WeatherCitiesPresenter.addCities(countryDialogView.cityList)
                        })
                .setNegativeButton(R.string.cancel,
                        DialogInterface.OnClickListener { dialog, id ->

                        }).create().show()

//        WeatherCitiesPresenter.addCities(countryDialogView.cityList)
        return countryDialogView.cityList
    }


}