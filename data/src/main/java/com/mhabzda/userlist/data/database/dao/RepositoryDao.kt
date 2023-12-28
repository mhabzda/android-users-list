package com.mhabzda.userlist.data.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.mhabzda.userlist.data.database.dtos.UserRepositoryLocalDto
import io.reactivex.Single

@Dao
interface RepositoryDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(vararg repos: UserRepositoryLocalDto)

    @Query("SELECT * FROM userRepositories WHERE userLogin = :userName")
    fun getRepositories(userName: String): Single<List<UserRepositoryLocalDto>>
}
