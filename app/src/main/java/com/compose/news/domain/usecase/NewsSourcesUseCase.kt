package com.compose.news.domain.usecase

import androidx.paging.PagingData
import com.compose.news.data.model.source.Source
import com.compose.news.domain.repository.NewsSourcesRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class NewsSourcesUseCase @Inject constructor(private val repository: NewsSourcesRepository) {
    fun getNewsSources(category: String): Flow<PagingData<Source>> {
        return repository.getNewsSources(category).flowOn(Dispatchers.IO)
    }

    fun searchNewsSources(keyword: String): Flow<List<Source>> {
        return repository.searchNewsSources(keyword)
    }
}