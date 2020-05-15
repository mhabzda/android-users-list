package com.users.list.ui

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.users.R
import com.users.list.model.api.RemoteUserRepository
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.rxkotlin.subscribeBy
import kotlinx.android.synthetic.main.activity_main.example_text as exampleText

class ListActivity : AppCompatActivity() {
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_main)
  }

  override fun onResume() {
    super.onResume()

    val repo = RemoteUserRepository()

    repo.retrieveUsers()
      .observeOn(AndroidSchedulers.mainThread())
      .subscribeBy(
        onSuccess = {
          exampleText.text = it.toString()
        },
        onError = {
          Log.e("ListActivity", "Error", it)
        }
      )
  }
}
