package com.weather.home

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import android.widget.TextView
import com.weather.R
import com.weather.model.OpenWeather


class CitiesRecyclerAdapter : RecyclerView.Adapter<MyViewHolder>() {

    private val dataList: ArrayList<OpenWeather> = ArrayList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val parentView = LayoutInflater.from(parent.context).inflate(R.layout.city_view, parent, false)
        return MyViewHolder(parentView)
    }

    // Replace the contents of a view (invoked by the layout manager)
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {

        val context = holder.view.context
        holder.view.findViewById<TextView>(R.id.cityView_txt_name).text = context.getString(R.string.city, dataList[position].name)
        holder.view.findViewById<TextView>(R.id.cityView_txt_description).text = context.getString(R.string.description ) + dataList[position].weather[0].description
        holder.view.findViewById<TextView>(R.id.cityView_txt_humidity).text = context.getString(R.string.humidity, dataList[position].main.humidity.toString())
        holder.view.findViewById<TextView>(R.id.cityView_txt_temp).text = context.getString(R.string.temperature, dataList[position].main.temp.toString())

        holder.view.setOnClickListener({view ->

        })
    }

    override fun getItemCount() = dataList.size

    fun addItem(openWeather: OpenWeather) {
        dataList.add(openWeather)
        notifyItemInserted(dataList.size - 1);
        notifyDataSetChanged()
    }

}

class MyViewHolder(val view: View) : RecyclerView.ViewHolder(view)