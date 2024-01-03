package com.mhabzda.userlist.testutils

import com.mhabzda.userlist.domain.model.UserEntity

object TestUserData {
    val firstTestUserEntity = UserEntity("john", "url", listOf("repo1", "jsonSerialize"))
    val secondTestUserEntity = UserEntity("micheal", "url", listOf("repo2"))
}
