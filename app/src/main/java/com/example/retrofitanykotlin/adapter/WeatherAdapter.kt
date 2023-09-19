package com.example.retrofitanykotlin.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.retrofitanykotlin.R
import com.example.retrofitanykotlin.databinding.FragmentRecyclerBinding
import com.example.retrofitanykotlin.databinding.RecyclerItemBinding
import com.example.retrofitanykotlin.model.WeatherApiModel

class WeatherAdapter(private var weatherList : List<WeatherApiModel>) : RecyclerView.Adapter<WeatherAdapter.WeatherViewHolder>() {

    inner class WeatherViewHolder(val binding: RecyclerItemBinding) : RecyclerView.ViewHolder(binding.root) {

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WeatherViewHolder {
        val binding = RecyclerItemBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return WeatherViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return weatherList.size
    }

    override fun onBindViewHolder(holder: WeatherViewHolder, position: Int) {
        holder.binding.weatherName.text = weatherList[position].date
    }
}