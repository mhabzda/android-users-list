package com.users.list.ui.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.users.databinding.ItemUserBinding
import com.users.list.model.domain.UserEntity
import com.users.list.utils.loadImage

class UsersAdapter : RecyclerView.Adapter<UsersAdapter.ViewHolder>() {

    var users = emptyList<UserEntity>()
        @SuppressLint("NotifyDataSetChanged")
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemUserBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int =
        users.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(users[position])
    }

    inner class ViewHolder(private val binding: ItemUserBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: UserEntity) = with(binding) {
            userNameText.text = item.name
            userImage.loadImage(item.avatarUrl)
            userRepositoriesText.text = item.repositories.joinToString()
        }
    }
}
