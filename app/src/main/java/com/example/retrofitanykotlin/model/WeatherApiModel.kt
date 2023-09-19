package com.example.retrofitanykotlin.model

import com.google.gson.annotations.SerializedName

data class WeatherApiModel(
    @SerializedName("date")
    var date : String,
    @SerializedName("day")
    var day : String,
    @SerializedName("degree")
    var degree : String,
    @SerializedName("status")
    var status : String,
    @SerializedName("humidity")
    var humidity : String
)
