package com.users.list.ui

import com.users.list.model.domain.UserEntity
import com.users.list.testutils.BaseInputProvider
import com.users.list.testutils.TestUserData.firstTestUserEntity
import com.users.list.testutils.TestUserData.secondTestUserEntity
import com.users.list.ui.filter.ListItemsFilter
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ArgumentsSource

class ListItemsFilterTest {
    @ParameterizedTest
    @ArgumentsSource(InputProvider::class)
    fun `it should filter items`(input: Input) {
        val filter = ListItemsFilter()
        val result = filter.filterItems(input.query, input.users)

        assertEquals(input.expectedFilteredUsers, result)
    }

    class InputProvider : BaseInputProvider() {
        override val listInput: List<Any>
            get() = listOf(
                Input("j", listOf(firstTestUserEntity, secondTestUserEntity), listOf(firstTestUserEntity)),
                Input("micheal", listOf(firstTestUserEntity, secondTestUserEntity), listOf(secondTestUserEntity)),
                Input("repo1", listOf(firstTestUserEntity, secondTestUserEntity), listOf(firstTestUserEntity)),
                Input("scottie", listOf(firstTestUserEntity, secondTestUserEntity), emptyList())
            )
    }

    data class Input(val query: String, val users: List<UserEntity>, val expectedFilteredUsers: List<UserEntity>)
}
