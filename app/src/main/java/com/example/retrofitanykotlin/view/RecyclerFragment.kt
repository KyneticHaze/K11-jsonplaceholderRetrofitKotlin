package com.example.retrofitanykotlin.view

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.retrofitanykotlin.adapter.WeatherAdapter
import com.example.retrofitanykotlin.databinding.FragmentRecyclerBinding
import com.example.retrofitanykotlin.model.WeatherApiModel
import com.example.retrofitanykotlin.service.WeatherApiService
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.schedulers.Schedulers
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

class RecyclerFragment : Fragment() {
    private lateinit var binding : FragmentRecyclerBinding
    private val baseUrl = "https://api.collectapi.com/"
    private val apiKey = "4YuRFiJhxK0JSCrG8uoNfw:43G8PQMfsrp5TDKOJpUBlo"
    private var cd : CompositeDisposable? = null
    private lateinit var weatherAdapter : WeatherAdapter
    private var weatherDataList = mutableListOf<WeatherApiModel>()
    private val apiKeyName = "apiKey"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        cd = CompositeDisposable()
        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
        weatherDataList = mutableListOf()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val binding = FragmentRecyclerBinding.inflate(inflater, container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        pullData()
    }

    private fun pullData() {

        val incerceptor = Interceptor { chain ->

            val originalRequest = chain.request()
            val modifiedUrl = originalRequest.url().newBuilder()
                .addQueryParameter(apiKeyName, apiKey)
                .build()
            val modifiedRequest = originalRequest.newBuilder()
                .url(modifiedUrl)
                .build()
            chain.proceed(modifiedRequest)

        }

        val okHttpClient = OkHttpClient.Builder()
            .addInterceptor(incerceptor)
            .build()



        val retrofit = Retrofit.Builder()
            .baseUrl(baseUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
            .client(okHttpClient)
            .build()
            .create(WeatherApiService::class.java)

        val getData = retrofit.getWeatherData()

        activity?.let {
            cd?.add(getData
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::handleResponse)
            )
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun handleResponse(weatherList: List<WeatherApiModel>) {
        weatherDataList.addAll(weatherList)
        weatherAdapter = WeatherAdapter(weatherDataList)
        binding.recyclerView.adapter = weatherAdapter
        weatherAdapter.notifyDataSetChanged()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        cd?.clear()
    }
}