package com.users.list.ui

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.users.R
import com.users.list.model.api.RemoteUserRepository
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.rxkotlin.subscribeBy
import kotlinx.android.synthetic.main.activity_main.users_list as usersRecyclerView

class ListActivity : AppCompatActivity() {
  private lateinit var usersAdapter: UsersAdapter

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_main)

    usersAdapter = UsersAdapter()
    usersRecyclerView.adapter = usersAdapter
    usersRecyclerView.layoutManager = LinearLayoutManager(this)
  }

  override fun onResume() {
    super.onResume()

    val repo = RemoteUserRepository()

    repo.retrieveUsers()
      .observeOn(AndroidSchedulers.mainThread())
      .subscribeBy(
        onSuccess = {
          usersAdapter.users = it
        },
        onError = {
          Log.e("ListActivity", "Error", it)
        }
      )
  }
}
