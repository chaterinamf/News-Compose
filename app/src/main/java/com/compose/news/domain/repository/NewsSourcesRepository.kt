package com.compose.news.domain.repository

import androidx.paging.PagingData
import com.compose.news.data.model.source.Source
import kotlinx.coroutines.flow.Flow

interface NewsSourcesRepository {
    fun getNewsSources(category: String): Flow<PagingData<Source>>

    fun searchNewsSources(keyword: String): Flow<List<Source>>
}