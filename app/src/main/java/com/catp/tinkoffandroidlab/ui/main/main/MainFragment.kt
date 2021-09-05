package com.catp.tinkoffandroidlab.ui.main.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import by.kirich1409.viewbindingdelegate.viewBinding
import com.catp.tinkoffandroidlab.R
import com.catp.tinkoffandroidlab.databinding.MainFragmentBinding
import com.catp.tinkoffandroidlab.ui.main.data.TabType
import com.catp.tinkoffandroidlab.ui.main.enableIf
import com.catp.tinkoffandroidlab.ui.main.tab.TabPagerAdapter
import com.google.android.material.tabs.TabLayoutMediator
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

class MainFragment : Fragment() {

    companion object {
        fun newInstance() = MainFragment()
    }

    private lateinit var viewModel: MainViewModel
    private val viewBinding: MainFragmentBinding by viewBinding()
    private var tabPagerAdapter: TabPagerAdapter? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.main_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        viewModel = ViewModelProvider(requireActivity()).get(MainViewModel::class.java)
        initAdapter()
    }

    private fun initAdapter() {
        tabPagerAdapter = TabPagerAdapter(this)
        with(viewBinding) {
            vpContent.adapter = tabPagerAdapter

            TabLayoutMediator(tlSwitcher, vpContent) { tab, position ->
                tab.text = TabType.byIndex(position).api.uppercase()
            }.attach()

            ivGoBack.setOnClickListener {
                viewModel.moveBackItem()
            }
            ivGoNext.setOnClickListener {
                viewModel.moveNextItem()
            }

            viewModel.backButtonState.onEach {
                ivGoBack.enableIf { it }
            }.launchIn(viewLifecycleOwner.lifecycleScope)

            viewModel.nextButtonState.onEach {
                ivGoNext.enableIf { it }
            }.launchIn(viewLifecycleOwner.lifecycleScope)
        }

    }

    override fun onDestroyView() {
        tabPagerAdapter = null
        super.onDestroyView()
    }
}