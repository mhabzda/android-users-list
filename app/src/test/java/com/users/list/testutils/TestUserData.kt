package com.users.list.testutils

import com.users.list.model.domain.UserEntity

object TestUserData {
    val firstTestUserEntity = UserEntity("john", "url", listOf("repo1", "jsonSerialize"))
    val secondTestUserEntity = UserEntity("micheal", "url", listOf("repo2"))
}
