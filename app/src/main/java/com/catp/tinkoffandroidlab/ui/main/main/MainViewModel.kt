package com.catp.tinkoffandroidlab.ui.main.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.catp.tinkoffandroidlab.ui.main.data.ButtonEvent
import kotlinx.coroutines.launch

class MainViewModel : ViewModel() {
    fun moveNextItem() {
        viewModelScope.launch {
            Injector.buttonEventChannel.send(ButtonEvent.OnNextButtonPressed)
        }
    }

    fun movePrevItem() {
        viewModelScope.launch {
            Injector.buttonEventChannel.send(ButtonEvent.OnBackButtonPressed)
        }
    }
}