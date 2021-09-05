package com.catp.tinkoffandroidlab.ui.main.main

import com.catp.tinkoffandroidlab.ui.main.data.ButtonEvent
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow

object Injector {
    val buttonEventChannel:Channel<ButtonEvent> = Channel()
    val backButtonAvailability: MutableStateFlow<Boolean> = MutableStateFlow(false)
    val nextButtonAvailability: MutableStateFlow<Boolean> = MutableStateFlow(false)
}