package com.weather.home

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.weather.CityDetails.CityActivity
import com.weather.R
import com.weather.model.OpenWeather

class CitiesRecyclerAdapter(private val allowClick: Boolean) : RecyclerView.Adapter<MyViewHolder>() {

    private val dataList: ArrayList<OpenWeather> = ArrayList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {

        val parentView = if (allowClick) {
            LayoutInflater.from(parent.context).inflate(R.layout.city_view, parent, false)
        } else {
            LayoutInflater.from(parent.context).inflate(R.layout.city_weather_forecast, parent, false)
        }
        return MyViewHolder(parentView)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {

        val context = holder.view.context

        holder.view.findViewById<TextView>(R.id.txt_cityName)?.text = context.getString(R.string.city, dataList[position].name)
        holder.view.findViewById<TextView>(R.id.txt_description).text = String.format(context.getString(R.string.description, dataList[position].weather[0].description))
        holder.view.findViewById<TextView>(R.id.txt_humidity).text = context.getString(R.string.humidity, dataList[position].main.humidity.toString())
        holder.view.findViewById<TextView>(R.id.txt_temp).text = context.getString(R.string.temperature, dataList[position].main.temp.toString())
        holder.view.findViewById<TextView>(R.id.txt_date)?.text = context.getString(R.string.date, dataList[position].dt_txt)
        Glide.with(context).load(dataList[position].getIconUrl()).into(holder.view.findViewById(R.id.img_weather))

        if (allowClick) {
            holder.view.setOnClickListener {
                val intent = Intent(context, CityActivity::class.java)
                intent.putExtra(CityActivity.MODEL, dataList[position])
                context.startActivity(intent)
            }
        }
    }

    override fun getItemCount() = dataList.size

    fun addItem(openWeather: OpenWeather) {
        dataList.add(openWeather)
        notifyItemInserted(dataList.size - 1);
        notifyDataSetChanged()
    }

    fun addItem(openWeather: List<OpenWeather>) {
        dataList.addAll(openWeather)
        notifyDataSetChanged()
    }

}

class MyViewHolder(val view: View) : RecyclerView.ViewHolder(view)