package com.users.list.ui.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.users.R
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
        val inflater = LayoutInflater.from(parent.context)
        val contactView = inflater.inflate(R.layout.item_user, parent, false)

        return ViewHolder(contactView)
    }

    override fun getItemCount(): Int {
        return users.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(users[position])
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val userNameTextView = itemView.findViewById<TextView>(R.id.user_name_text)
        private val imageAvatarView = itemView.findViewById<ImageView>(R.id.user_image)
        private val repositoriesTextView = itemView.findViewById<TextView>(R.id.user_repositories_text)

        fun bind(item: UserEntity) {
            userNameTextView.text = item.name
            imageAvatarView.loadImage(item.avatarUrl)
            repositoriesTextView.text = item.repositories.joinToString()
        }
    }
}
