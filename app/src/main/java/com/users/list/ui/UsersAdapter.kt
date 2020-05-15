package com.users.list.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.users.R
import com.users.list.model.api.dtos.UserRemoteDto

class UsersAdapter : RecyclerView.Adapter<UsersAdapter.ViewHolder>() {

  var users = emptyList<UserRemoteDto>()
    set(value) {
      notifyDataSetChanged()
      field = value
    }

  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
    val context = parent.context
    val inflater = LayoutInflater.from(context)
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
    private val nameTextView = itemView.findViewById<TextView>(R.id.user_name)
    private val imageUrlTextView = itemView.findViewById<TextView>(R.id.user_image)

    fun bind(item: UserRemoteDto) {
      nameTextView.text = item.login
      imageUrlTextView.text = item.avatarUrl
    }
  }
}