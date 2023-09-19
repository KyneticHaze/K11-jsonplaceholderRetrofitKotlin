package com.example.retrofitanykotlin.service

import com.example.retrofitanykotlin.model.WeatherApiModel
import io.reactivex.rxjava3.core.Observable
import retrofit2.http.GET

interface WeatherApiService {
    @GET("weather/getWeather?data.lang=tr&data.city=ankara")
    fun getWeatherData() : Observable<List<WeatherApiModel>>
}