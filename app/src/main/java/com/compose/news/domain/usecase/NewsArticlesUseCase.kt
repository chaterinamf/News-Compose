package com.compose.news.domain.usecase

import androidx.paging.PagingData
import com.compose.news.data.model.article.Article
import com.compose.news.domain.repository.NewsArticlesRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class NewsArticlesUseCase @Inject constructor(private val repository: NewsArticlesRepository) {
    fun getNewsArticles(source: String, queryParam: String): Flow<PagingData<Article>> {
        return repository.getNewsArticles(source, queryParam).flowOn(Dispatchers.IO)
    }
}