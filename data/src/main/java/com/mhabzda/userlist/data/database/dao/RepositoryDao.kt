package com.mhabzda.userlist.data.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.mhabzda.userlist.data.database.model.UserRepositoryLocalDto

@Dao
internal interface RepositoryDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(vararg repos: UserRepositoryLocalDto)

    @Query("SELECT * FROM userRepositories WHERE userLogin = :userName")
    suspend fun getRepositories(userName: String): List<UserRepositoryLocalDto>
}
