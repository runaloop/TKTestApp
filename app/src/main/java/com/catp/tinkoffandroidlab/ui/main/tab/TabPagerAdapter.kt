package com.catp.tinkoffandroidlab.ui.main.tab

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.catp.tinkoffandroidlab.ui.main.data.Item
import com.catp.tinkoffandroidlab.ui.main.data.TabType
import com.catp.tinkoffandroidlab.ui.main.gifing.ContentFragment

class TabPagerAdapter(parentFragment: Fragment) : FragmentStateAdapter(parentFragment) {

    override fun createFragment(position: Int): Fragment = TabFragment.newInstance(position)

    override fun getItemCount(): Int = 3
}