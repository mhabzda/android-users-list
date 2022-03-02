package com.users.list.ui.filter

import com.users.list.model.domain.UserEntity
import javax.inject.Inject

class ListItemsFilter @Inject constructor() {
    fun filterItems(searchQuery: String, users: List<UserEntity>): List<UserEntity> {
        return users.filter { it.name.contains(searchQuery) || it.repositories.toString().contains(searchQuery) }
    }
}
