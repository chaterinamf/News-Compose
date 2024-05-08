package com.compose.news.domain.repository

import androidx.paging.PagingData
import com.compose.news.data.model.article.Article
import kotlinx.coroutines.flow.Flow

interface NewsArticlesRepository {
    fun getNewsArticles(source: String, queryParam: String): Flow<PagingData<Article>>
}