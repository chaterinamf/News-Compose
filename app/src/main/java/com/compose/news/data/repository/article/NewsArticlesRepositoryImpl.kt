package com.compose.news.data.repository.article

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.compose.news.data.model.article.Article
import com.compose.news.data.network.NewsAPI
import com.compose.news.data.repository.paging.NewsArticlesPagingSource
import com.compose.news.domain.repository.NewsArticlesRepository
import com.compose.news.util.Constants
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class NewsArticlesRepositoryImpl @Inject constructor(private val newsAPI: NewsAPI) :
    NewsArticlesRepository {
    override fun getNewsArticles(source: String, queryParam: String): Flow<PagingData<Article>> {
        return Pager(
            config = PagingConfig(pageSize = Constants.MAX_PAGE_SIZE),
            pagingSourceFactory = { NewsArticlesPagingSource(newsAPI, source, queryParam) }
        ).flow
    }
}