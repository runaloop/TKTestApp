package com.catp.tinkoffandroidlab.ui.main.tab

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import by.kirich1409.viewbindingdelegate.viewBinding
import com.catp.tinkoffandroidlab.R
import com.catp.tinkoffandroidlab.databinding.TabFragmentBinding
import com.catp.tinkoffandroidlab.ui.main.data.TabType
import com.catp.tinkoffandroidlab.ui.main.gifing.ContentPagerAdapter
import com.catp.tinkoffandroidlab.ui.main.main.MainViewModel
import com.catp.tinkoffandroidlab.ui.main.visibleIf
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.receiveAsFlow

class TabFragment : Fragment() {
    companion object {
        fun newInstance(tabIndex: Int) = TabFragment().apply {
            arguments = bundleOf(TYPE to TabType.byIndex(tabIndex))
        }

        private const val TYPE = "type"
    }

    private lateinit var viewModel: TabViewModel
    private val viewBinding: TabFragmentBinding by viewBinding()
    private var contentPagerAdapter: ContentPagerAdapter? = null
    private val tabType by lazy { arguments?.get(TYPE) as TabType }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.tab_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        viewModel = ViewModelProvider(this, VMFactory(tabType)).get(TabViewModel::class.java)
        initAdapter()
    }

    private fun initAdapter() {
        contentPagerAdapter = ContentPagerAdapter(this)
        with(viewBinding) {
            vpContent.adapter = contentPagerAdapter

            btnRetry.setOnClickListener { viewModel.onRetryClicked() }

            viewModel.errorChannel
                .receiveAsFlow()
                .onEach { showFailStatus(it) }
                .launchIn(viewLifecycleOwner.lifecycleScope)

            viewModel.items
                .onEach { contentPagerAdapter?.addItems(it) }
                .launchIn(viewLifecycleOwner.lifecycleScope)

            viewModel.loadingStatus
                .onEach { setLoading(it) }
                .launchIn(viewLifecycleOwner.lifecycleScope)

            viewModel.selectionIndex
                .onEach { vpContent.setCurrentItem(it, true) }
                .launchIn(viewLifecycleOwner.lifecycleScope)


        }
    }

    override fun onPause() {
        ViewModelProvider(requireActivity()).get(MainViewModel::class.java).let {
            it.unRegisterNavigationButtonHandler(viewModel)
            viewModel.setButtonEnableHandler(null)
        }
        super.onPause()
    }

    override fun onResume() {
        super.onResume()
        ViewModelProvider(requireActivity()).get(MainViewModel::class.java).let {
            it.registerNavigationButtonHandler(viewModel)
            viewModel.setButtonEnableHandler(it)
        }
    }

    private fun setLoading(loading: Boolean) {
        viewBinding.pbLoading.visibleIf { loading }
    }

    private fun showFailStatus(failStatus: Boolean) {
        viewBinding.gError.visibleIf { failStatus }
    }

    override fun onDestroyView() {
        contentPagerAdapter = null
        super.onDestroyView()
    }

    class VMFactory(private val tabType: TabType) : ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            return TabViewModel(tabType) as T
        }

    }
}