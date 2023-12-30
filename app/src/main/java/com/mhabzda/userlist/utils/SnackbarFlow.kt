package com.mhabzda.userlist.utils

import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SnackbarFlow @Inject constructor() {

    private val mutableFlow = MutableSharedFlow<String>()
    val flow: SharedFlow<String> = mutableFlow.asSharedFlow()

    suspend fun emit(message: String) {
        mutableFlow.emit(message)
    }
}
