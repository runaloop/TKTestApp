package com.catp.tinkoffandroidlab.ui.main.gifing

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ContentViewModel : ViewModel() {
    private val _imageLoadFailStatus = MutableStateFlow(false)
    val imageLoadFailStatus = _imageLoadFailStatus.asStateFlow()

    val imageLoadChannel = Channel<Unit>()

    fun onLoadFailed() {
        _imageLoadFailStatus.value = true
    }

    fun onRetryImageLoad() {
        _imageLoadFailStatus.value = false
        viewModelScope.launch {

            imageLoadChannel.send(Unit)
        }
    }

}