package com.compose.news.presentation.article.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.compose.news.data.model.article.Article
import com.compose.news.domain.usecase.NewsArticlesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NewsArticlesViewModel @Inject constructor(private val useCase: NewsArticlesUseCase) :
    ViewModel() {

    private val _articlesState = MutableStateFlow<PagingData<Article>>(value = PagingData.empty())
    val articleState = _articlesState.asStateFlow()

    private val _keywordHistory = mutableListOf<String>()
    val keywordHistory = _keywordHistory

    fun getNewsArticles(source: String, queryParam: String = "") = viewModelScope.launch {
        useCase.getNewsArticles(source, queryParam)
            .distinctUntilChanged()
            .cachedIn(viewModelScope)
            .collect {
                _articlesState.value = it
            }
    }

    fun saveKeyword(key: String) = viewModelScope.launch {
        _keywordHistory.add(key)
    }
}