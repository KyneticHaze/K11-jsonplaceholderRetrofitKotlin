package com.example.retrofitanykotlin.service

import com.example.retrofitanykotlin.model.UsersModel
import retrofit2.Response
import retrofit2.http.GET

interface UsersApiService {
    @GET("users")
    suspend fun getUsersData() : Response<List<UsersModel>>
}