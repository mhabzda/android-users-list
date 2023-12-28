package com.mhabzda.userlist.model.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.mhabzda.userlist.model.database.dtos.UserLocalDto
import io.reactivex.Single

@Dao
interface UserDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(vararg users: UserLocalDto)

    @Query("SELECT * FROM users")
    fun getUsers(): Single<List<UserLocalDto>>
}
