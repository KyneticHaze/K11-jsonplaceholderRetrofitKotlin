package com.example.retrofitanykotlin.adapter

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.retrofitanykotlin.databinding.UsersRecyclerItemBinding
import com.example.retrofitanykotlin.model.UsersModel

class UsersAdapter(private var usersList : List<UsersModel>, private var listener : Listener) : RecyclerView.Adapter<UsersAdapter.UsersViewHolder>() {
    private val colors = arrayOf("#e3d9bc", "#c4f5d7", "#c4d9f5", "#e7cbf5", "#9db096")

    interface Listener {
        fun onItemClick(userModel: UsersModel)
    }

    inner class UsersViewHolder(private var binding : UsersRecyclerItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(userModel : UsersModel, color: Array<String>, position: Int, listener : Listener) {

            itemView.setOnClickListener {
                listener.onItemClick(userModel)
            }

            itemView.setBackgroundColor(Color.parseColor(color[position % 5]))

            binding.name.text = userModel.name
            binding.userName.text = userModel.userName
            binding.email.text = userModel.email
            binding.website.text = userModel.website
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UsersViewHolder {
        val binding = UsersRecyclerItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return UsersViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return usersList.size
    }

    override fun onBindViewHolder(holder: UsersViewHolder, position: Int) {
        holder.bind(usersList[position], colors, position, listener)
    }
}