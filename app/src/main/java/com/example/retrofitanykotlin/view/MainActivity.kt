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
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class MainActivity : AppCompatActivity(), UsersAdapter.Listener {
    private lateinit var binding: ActivityMainBinding
    private val baseUrl = "https://jsonplaceholder.typicode.com/"
    private var usersModels : List<UsersModel>? = null
    private lateinit var usersAdapter : UsersAdapter
    private val phoneNumberStr = "Phone Number:"
    private val alertButtonMessage = "Cancel?"
    private val alertMessageResult = "Canceled"
    private var job: Job? = null
    private var handler = CoroutineExceptionHandler { coroutineContext, throwable ->
        Toast.makeText(this, throwable.localizedMessage, Toast.LENGTH_LONG).show()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        binding.usersRecycler.layoutManager = LinearLayoutManager(this)

        pullData()
    }

    private fun pullData() {
        val retrofit = Retrofit.Builder()
            .baseUrl(baseUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(UsersApiService::class.java)

        job = CoroutineScope(Dispatchers.IO + handler).launch {
            val response = retrofit.getUsersData()

            withContext(Dispatchers.Main) {
                if (response.isSuccessful) {
                    response.body()?.let {userModeList ->
                        usersModels = ArrayList(userModeList)

                        usersModels?.let {userModel ->
                            usersAdapter = UsersAdapter(userModel,this@MainActivity)
                        }
                        binding.usersRecycler.adapter = usersAdapter
                    }
                }
            }
        }
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
        job?.cancel()
    }
}