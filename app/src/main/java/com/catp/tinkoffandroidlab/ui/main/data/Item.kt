package com.catp.tinkoffandroidlab.ui.main.data
import kotlinx.parcelize.Parcelize
import android.os.Parcelable

@Parcelize
data class Item(val id: Long, val description: String, val gifURL: String): Parcelable