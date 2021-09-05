package com.catp.tinkoffandroidlab.ui.main.data

sealed class ButtonEvent{
    object OnNextButtonPressed: ButtonEvent()
    object OnBackButtonPressed: ButtonEvent()
}