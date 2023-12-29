package com.mhabzda.userlist.base

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

abstract class BaseViewModel<State, Effect>(
    defaultState: State,
    private val emitSnackbarMessage: suspend (String) -> Unit = {},
) : ViewModel() {
    private val stateFlow = MutableStateFlow(defaultState)
    val state = stateFlow.asStateFlow()

    private val effectsFlow = MutableSharedFlow<Effect>()
    val effects = effectsFlow.asSharedFlow()

    protected fun updateState(block: State.() -> State) {
        stateFlow.value = block(stateFlow.value)
    }

    protected fun emit(effect: Effect) = viewModelScope.launch {
        effectsFlow.emit(effect)
    }

    suspend fun showSnackbar(message: String) {
        emitSnackbarMessage(message)
    }
}
