package com.compose.news.presentation.source

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.compose.news.data.model.source.Source
import com.compose.news.domain.usecase.NewsSourcesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NewsSourcesViewModel @Inject constructor(private val useCase: NewsSourcesUseCase) :
    ViewModel() {
    private val _sourceState = MutableStateFlow<PagingData<Source>>(PagingData.empty())
    val sourceState: MutableStateFlow<PagingData<Source>> get() = _sourceState

    private val _searchSourcesState = MutableStateFlow<List<Source>>(emptyList())
    val searchSourcesState = _searchSourcesState.asStateFlow()

    private val _keywordHistory = mutableListOf<String>()
    val keywordHistory = _keywordHistory

    fun getNewsSources(category: String) = viewModelScope.launch {
        useCase.getNewsSources(category)
            .distinctUntilChanged()
            .cachedIn(viewModelScope)
            .collect {
                _sourceState.value = it
            }
    }

    fun searchNewsSources(keyword: String) = viewModelScope.launch {
        useCase.searchNewsSources(keyword)
            .distinctUntilChanged()
            .collect {
                _searchSourcesState.value = it
            }
    }

    fun saveKeyword(key: String) = viewModelScope.launch {
        _keywordHistory.add(key)
    }
}