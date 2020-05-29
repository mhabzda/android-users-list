package com.users.list.ui

import com.users.list.model.domain.UserEntity
import com.users.list.testutils.BaseInputProvider
import com.users.list.ui.filter.ListItemsFilter
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ArgumentsSource

class ListItemsFilterTests {
  @ParameterizedTest
  @ArgumentsSource(InputProvider::class)
  fun `it should filter items`(input: Input) {
    val filter = ListItemsFilter()
    val result = filter.filterItems(input.query, input.users)

    assertEquals(input.expectedFilteredUsers, result)
  }

  class InputProvider : BaseInputProvider() {
    private val testUserEntity = UserEntity("john", "url", listOf("repo1", "jsonSerialize"))
    private val secondTestUserEntity = UserEntity("micheal", "url", listOf("repo2"))

    override val listInput: List<Any>
      get() = listOf(
        Input("j", listOf(testUserEntity, secondTestUserEntity), listOf(testUserEntity)),
        Input("micheal", listOf(testUserEntity, secondTestUserEntity), listOf(secondTestUserEntity)),
        Input("repo1", listOf(testUserEntity, secondTestUserEntity), listOf(testUserEntity)),
        Input("scottie", listOf(testUserEntity, secondTestUserEntity), emptyList())
      )
  }

  data class Input(val query: String, val users: List<UserEntity>, val expectedFilteredUsers: List<UserEntity>)
}