package com.users.list.model.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.users.list.model.database.dtos.UserLocalDto
import io.reactivex.Maybe

@Dao
interface UserDao {
  @Insert(onConflict = OnConflictStrategy.REPLACE)
  fun insert(vararg users: UserLocalDto)

  @Query("SELECT * FROM users")
  fun getUsers(): Maybe<List<UserLocalDto>>

  @Query("SELECT * FROM users WHERE login = :userLogin")
  fun getUser(userLogin: String): UserLocalDto
}