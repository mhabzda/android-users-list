package com.mhabzda.userlist.data.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.mhabzda.userlist.data.database.model.UserLocalDto

@Dao
internal interface UserDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(vararg users: UserLocalDto)

    @Query("SELECT * FROM users")
    suspend fun getUsers(): List<UserLocalDto>
}
