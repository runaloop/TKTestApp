package com.catp.tinkoffandroidlab.ui.main.gifing

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.catp.tinkoffandroidlab.ui.main.data.Item

class ContentPagerAdapter(parentFragment: Fragment) : FragmentStateAdapter(parentFragment) {

    private val items: MutableList<Item> = mutableListOf()

    fun addItems(newItems: List<Item>){
        val newData = items.toList().plus(newItems).distinctBy { it.id }
        items.clear()
        items.addAll(newData)
        notifyDataSetChanged()
    }

    override fun createFragment(position: Int): Fragment = ContentFragment.newInstance(items[position])

    override fun getItemCount(): Int = items.size
}