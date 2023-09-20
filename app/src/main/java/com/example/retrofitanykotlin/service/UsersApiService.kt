package com.example.retrofitanykotlin.service

import com.example.retrofitanykotlin.model.UsersModel
import io.reactivex.rxjava3.core.Observable
import retrofit2.http.GET

interface UsersApiService {
    @GET("users")
    fun getUsersData() : Observable<List<UsersModel>>
}