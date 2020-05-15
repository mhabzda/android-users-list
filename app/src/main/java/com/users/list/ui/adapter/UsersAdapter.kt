package com.users.list.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.users.R
import com.users.list.ui.displayable.UserDisplayable
import com.users.list.utils.EMPTY
import com.users.list.utils.loadImage
import com.users.list.utils.replace

class UsersAdapter(
  private val loadRepositories: (String) -> Unit
) : RecyclerView.Adapter<UsersAdapter.ViewHolder>() {

  private val searchableList = mutableListOf<UserDisplayable>()
  private var originalUsersList = listOf<UserDisplayable>()

  fun updateUsersList(newUsers: List<UserDisplayable>) {
    searchableList.clear()
    searchableList.addAll(newUsers)
    notifyDataSetChanged()

    originalUsersList = searchableList.toList()
  }

  fun updateUser(userName: String, repositories: List<String>) {
    val user = searchableList.find { it.name == userName }
    user?.let {
      val index = searchableList.indexOf(it)
      searchableList.replace(index, it.copy(repositoriesNames = repositories))
      notifyItemChanged(index)
    }

    originalUsersList = searchableList.toList()
  }

  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
    val context = parent.context
    val inflater = LayoutInflater.from(context)
    val contactView = inflater.inflate(R.layout.item_user, parent, false)

    return ViewHolder(contactView)
  }

  override fun getItemCount(): Int {
    return searchableList.size
  }

  override fun onBindViewHolder(holder: ViewHolder, position: Int) {
    holder.bind(searchableList[position])
  }

  inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    private val userNameTextView = itemView.findViewById<TextView>(R.id.user_name_text)
    private val imageAvatarView = itemView.findViewById<ImageView>(R.id.user_image)
    private val repositoriesTextView = itemView.findViewById<TextView>(R.id.user_repositories_text)
    private val repositoriesProgressBar = itemView.findViewById<ProgressBar>(R.id.user_repositories_progress_bar)

    fun bind(item: UserDisplayable) {
      userNameTextView.text = item.name
      imageAvatarView.loadImage(item.avatarUrl)

      bindRepositories(item)
    }

    private fun bindRepositories(item: UserDisplayable) {
      repositoriesTextView.text = EMPTY
      if (item.repositoriesNames.isEmpty()) {
        repositoriesProgressBar.visibility = View.VISIBLE
        loadRepositories.invoke(item.name)
      } else {
        repositoriesProgressBar.visibility = View.GONE
        repositoriesTextView.text = item.repositoriesNames.toString()
      }
    }
  }

  fun filterItems(searchText: String?) {
    val text = searchText ?: EMPTY
    val filteredList = originalUsersList.filter {
      it.name.contains(text) || it.repositoriesNames.toString().contains(text)
    }

    searchableList.clear()
    searchableList.addAll(filteredList)
    notifyDataSetChanged()
  }
}