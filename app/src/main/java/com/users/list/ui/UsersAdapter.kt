package com.users.list.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.users.R
import com.users.list.model.domain.UserEntity

class UsersAdapter(
  private val loadRepositories: (String) -> Unit
) : RecyclerView.Adapter<UsersAdapter.ViewHolder>() {

  private val users = mutableListOf<UserEntity>()

  fun updateUsersList(newUsers: List<UserEntity>) {
    users.clear()
    users.addAll(newUsers)
    notifyDataSetChanged()
  }

  fun updateUser(userName: String, repositories: List<String>) {
    val user = users.find { it.name == userName }
    user?.let {
      val index = users.indexOf(it)
      users.removeAt(index)
      users.add(index, it.copy(repositoriesNames = repositories))
      notifyItemChanged(index)
    }
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
    private val repositoriesTextView = itemView.findViewById<TextView>(R.id.user_repositories)

    fun bind(item: UserEntity) {
      nameTextView.text = item.name
      imageUrlTextView.text = item.avatarUrl

      if (item.repositoriesNames.isEmpty()) {
        loadRepositories.invoke(item.name)
      } else {
        repositoriesTextView.text = item.repositoriesNames.toString()
      }
    }
  }
}