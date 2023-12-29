package com.mhabzda.userlist.data.database.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "userRepositories",
    foreignKeys = [ForeignKey(
        entity = UserLocalDto::class,
        parentColumns = ["login"],
        childColumns = ["userLogin"],
        onDelete = ForeignKey.CASCADE
    )],
    indices = [Index("userLogin")]
)
internal data class UserRepositoryLocalDto(
    @ColumnInfo(name = "userLogin")
    val userLogin: String,

    @ColumnInfo(name = "name")
    val name: String,
) {
    @PrimaryKey(autoGenerate = true)
    var id: Long = 0L
}
