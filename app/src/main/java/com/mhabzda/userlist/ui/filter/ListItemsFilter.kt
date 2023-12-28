package com.mhabzda.userlist.ui.filter

import com.mhabzda.userlist.domain.model.UserEntity
import javax.inject.Inject

class ListItemsFilter @Inject constructor() {

    fun filterItems(searchQuery: String, users: List<UserEntity>): List<UserEntity> =
        users.filter { it.name.contains(searchQuery) || it.repositories.toString().contains(searchQuery) }
}
