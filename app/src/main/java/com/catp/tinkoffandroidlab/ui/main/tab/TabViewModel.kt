package com.catp.tinkoffandroidlab.ui.main.tab

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.catp.tinkoffandroidlab.ui.main.data.Item
import com.catp.tinkoffandroidlab.ui.main.data.Repository
import com.catp.tinkoffandroidlab.ui.main.data.TabType
import com.catp.tinkoffandroidlab.ui.main.main.ButtonEnableEventHandler
import com.catp.tinkoffandroidlab.ui.main.main.ButtonEventHandler
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class TabViewModel(private val tabType: TabType) : ViewModel(), ButtonEventHandler {
    private val _items = MutableStateFlow<List<Item>>(emptyList())
    val items = _items.asStateFlow()

    private val _selectionIndex = MutableStateFlow(0)
    val selectionIndex = _selectionIndex.asStateFlow()

    private val _loadingStatus = MutableStateFlow(true)
    val loadingStatus = _loadingStatus.asStateFlow()

    private val repository: Repository = Repository()

    private var currentPageIndex = 0

    private var buttonEnableEventHandler: ButtonEnableEventHandler? = null

    val errorChannel = Channel<Boolean>()

    init {
        fetchPage()
    }

    private fun fetchPage(page: Int = 0) {
        viewModelScope.launch {
            try {
                _loadingStatus.value = true
                repository.fetch(tabType, page).takeIf { !it.isNullOrEmpty() }?.let {
                    _items.emit(it)
                }
            } catch (exception: Throwable) {
                errorChannel.send(true)
            } finally {
                updateNavigationButtons()
                _loadingStatus.value = false
            }
        }
    }

    private fun goNext() {
        if (_loadingStatus.value.not() && _selectionIndex.value >= _items.value.size - PREFETCH_INDEX) {
            fetchPage(++currentPageIndex)
        }
        if (_selectionIndex.value < _items.value.size)
            _selectionIndex.value++

        updateNavigationButtons()
    }

    private fun updateNavigationButtons() {
        buttonEnableEventHandler?.updateBackButtonState(_selectionIndex.value > 0)
        buttonEnableEventHandler?.updateNextButtonState(_selectionIndex.value < _items.value.size)
    }

    private fun goBack() {
        if (_selectionIndex.value > 0)
            _selectionIndex.value--

        updateNavigationButtons()
    }

    fun onRetryClicked() {
        viewModelScope.launch {
            errorChannel.send(false)
            fetchPage(currentPageIndex)
        }
    }

    override fun onBackPressed() {
        goBack()
    }

    override fun onNextPressed() {
        goNext()
    }

    fun setButtonEnableHandler(eventHandler: ButtonEnableEventHandler?) {
        buttonEnableEventHandler = eventHandler
        updateNavigationButtons()
    }

    companion object {
        const val PREFETCH_INDEX = 3
    }

}