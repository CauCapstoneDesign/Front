package com.example.adapter


import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.data.WeatherData
import com.example.front.R
import kotlinx.android.synthetic.main.weather_item.view.*

class WeatherAdapter(val i:ArrayList<WeatherData>) : RecyclerView.Adapter<WeatherAdapter.MainViewHolder>() {

    var items = i
    override fun onCreateViewHolder(parent: ViewGroup, p1: Int) = MainViewHolder(parent)


    override fun getItemCount(): Int = items.size


    override fun onBindViewHolder(holer: MainViewHolder, position: Int) {
        items[position].let { item ->
            with(holer) {
                tvTitle.text = item.dt_text
                tvContent.text = item.temp + " " + item.humidity + " " + item.w_d
            }
        }
    }

    inner class MainViewHolder(parent: ViewGroup) : RecyclerView.ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.weather_item, parent, false)) {
        val tvTitle = itemView.tv_main_title
        val tvContent = itemView.tv_main_content
    }
}