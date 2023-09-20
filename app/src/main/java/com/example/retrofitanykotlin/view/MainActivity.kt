package com.example.retrofitanykotlin.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.retrofitanykotlin.adapter.UsersAdapter
import com.example.retrofitanykotlin.databinding.ActivityMainBinding
import com.example.retrofitanykotlin.model.UsersModel
import com.example.retrofitanykotlin.service.UsersApiService
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.schedulers.Schedulers
import retrofit2.Retrofit
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory


class MainActivity : AppCompatActivity(), UsersAdapter.Listener {
    private lateinit var binding: ActivityMainBinding
    private val baseUrl = "https://jsonplaceholder.typicode.com/"
    private var usersModels : List<UsersModel>? = null
    private var cd : CompositeDisposable? = null
    private lateinit var usersAdapter : UsersAdapter
    private val phoneNumberStr = "Phone Number:"
    private val alertButtonMessage = "Cancel?"
    private val alertMessageResult = "Canceled"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        cd = CompositeDisposable()
        binding.usersRecycler.layoutManager = LinearLayoutManager(this)

        pullData()
    }

    private fun pullData() {
        val retrofit = Retrofit.Builder()
            .baseUrl(baseUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
            .build().create(UsersApiService::class.java)

        val getData = retrofit.getUsersData()

        cd?.addAll(getData
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(this::handleResponse, this::handleError)
        )
    }

    private fun handleResponse(userListParams : List<UsersModel>) {
        usersModels = userListParams

        usersModels?.let {
            usersAdapter = UsersAdapter(it, this@MainActivity)
        }

        binding.usersRecycler.adapter = usersAdapter
    }

    private fun handleError(exception :Throwable) {
        println(exception.localizedMessage)
    }

    override fun onItemClick(userModel: UsersModel) {
        val alert = AlertDialog.Builder(this@MainActivity)

        alert.setTitle(userModel.name)
        alert.setMessage("$phoneNumberStr ${userModel.phone}")
        alert.setNegativeButton(alertButtonMessage) {
                p0, p1 ->
            Toast
                .makeText(this@MainActivity, alertMessageResult, Toast.LENGTH_SHORT)
                .show()
        }
        alert.show()
    }

    override fun onDestroy() {
        super.onDestroy()
        cd?.clear()
    }
}