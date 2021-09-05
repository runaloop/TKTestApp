package com.catp.tinkoffandroidlab.ui.main.data

import android.os.Parcelable
import kotlinx.parcelize.Parcelize


sealed class TabType(val index: Int, val api: String) : Parcelable {
    @Parcelize
    object Last : TabType(0, "latest")

    @Parcelize
    object Top : TabType(1, "top")

    @Parcelize
    object Hot : TabType(2, "hot")

    companion object {
        fun byIndex(index: Int): TabType {
            return listOf(Last, Top, Hot).first { it.index == index }
        }
    }
}