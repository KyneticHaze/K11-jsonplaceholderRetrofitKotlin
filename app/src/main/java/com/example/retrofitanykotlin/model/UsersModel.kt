package com.example.retrofitanykotlin.model

import com.google.gson.annotations.SerializedName

data class UsersModel(
    @SerializedName("name")
    var name : String,
    @SerializedName("username")
    var userName : String,
    @SerializedName("email")
    var email : String,
    @SerializedName("website")
    var website : String,
    @SerializedName("phone")
    var phone : String
)
