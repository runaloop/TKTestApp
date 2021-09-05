package com.catp.tinkoffandroidlab.ui.main.main

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class MainViewModel : ViewModel(), ButtonEnableEventHandler {

    private val handlers: HashSet<ButtonEventHandler> = HashSet()

    private val _backButtonState = MutableStateFlow(false)
    private val _nextButtonState = MutableStateFlow(false)

    val backButtonState = _backButtonState.asStateFlow()
    val nextButtonState = _nextButtonState.asStateFlow()

    override fun updateBackButtonState(enabled: Boolean) {
        _backButtonState.value = enabled
    }

    override fun updateNextButtonState(enabled: Boolean) {
        _nextButtonState.value = enabled
    }

    fun registerNavigationButtonHandler(handler: ButtonEventHandler) {
        handlers += handler
    }

    fun unRegisterNavigationButtonHandler(handler: ButtonEventHandler) {
        handlers -= handler
    }


    fun moveNextItem() {
        handlers.forEach { it.onNextPressed() }
    }

    fun moveBackItem() {
        handlers.forEach { it.onBackPressed() }
    }

    override fun onCleared() {
        handlers.clear()
        super.onCleared()
    }
}