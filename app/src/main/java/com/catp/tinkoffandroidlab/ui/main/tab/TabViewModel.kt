package com.catp.tinkoffandroidlab.ui.main.tab

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.catp.tinkoffandroidlab.ui.main.data.ButtonEvent
import com.catp.tinkoffandroidlab.ui.main.data.Item
import com.catp.tinkoffandroidlab.ui.main.data.Repository
import com.catp.tinkoffandroidlab.ui.main.data.TabType
import com.catp.tinkoffandroidlab.ui.main.main.Injector
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.io.IOException

class TabViewModel(private val tabType: TabType) : ViewModel() {
    private val _items = MutableStateFlow<List<Item>>(emptyList())
    val items = _items.asStateFlow()

    private val _selectionIndex = MutableStateFlow(0)
    val selectionIndex = _selectionIndex.asStateFlow()

    private val _loadingStatus = MutableStateFlow(true)
    val loadingStatus = _loadingStatus.asStateFlow()

    private val repository: Repository = Repository()

    private var currentPageIndex = 0

    val errorChannel = Channel<Boolean>()

    init {
        fetchPage()
    }

    private fun fetchPage(page: Int = 0) {
        println("🔥 ${tabType.api}::fetchNextPage")
        viewModelScope.launch {
            try {
                _loadingStatus.value = true
                repository.fetch(tabType, page).takeIf { !it.isNullOrEmpty() }?.let {
                    println("🔥 ${tabType.api}::fetchNextPage emitting: ${it.size} items")
                    _items.emit(it)
                }
            } catch (exception: Throwable) {
                println("🔥 ${tabType.api}::fetchNextPage catch: ${exception.message}")
                errorChannel.send(true)
            } finally {
                println("🔥 ${tabType.api}::fetchNextPage loadingStatus false")
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
        Injector.nextButtonAvailability.value = _selectionIndex.value < _items.value.size
        Injector.backButtonAvailability.value = _selectionIndex.value > 0
    }

    private fun goBack() {
        if (_selectionIndex.value > 0)
            _selectionIndex.value--

        updateNavigationButtons()
    }

    fun onButtonEvent(event: ButtonEvent) {
        println("🔥 ${tabType.api}::onButtonEvent ${event.javaClass.simpleName}")
        when(event){
            is ButtonEvent.OnBackButtonPressed->goBack()
            is ButtonEvent.OnNextButtonPressed->goNext()
        }
    }

    fun onRetryClicked() {
        viewModelScope.launch {
            errorChannel.send(false)
            fetchPage(currentPageIndex)
        }
    }

    companion object {
        const val PREFETCH_INDEX = 3
    }
}