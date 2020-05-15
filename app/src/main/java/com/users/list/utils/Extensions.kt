package com.users.list.utils

import android.widget.ImageView
import com.bumptech.glide.Glide
import com.users.R

fun <T> MutableList<T>.replace(index: Int, newItem: T) {
  this.removeAt(index)
  this.add(index, newItem)
}

fun ImageView.loadImage(url: String) {
  Glide.with(context)
    .load(url)
    .placeholder(R.drawable.ic_launcher_foreground)
    .into(this)
}

const val EMPTY = ""