package com.catp.tinkoffandroidlab.ui.main

import android.view.View

fun View.visibleIf(block: () -> Boolean) {
    visibility = if (block()) {
        View.VISIBLE
    } else {
        View.GONE
    }
}

fun View.enableIf(block: () -> Boolean) {
    isEnabled = block()
}