package com.compose.news.data.repository.source

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.compose.news.data.local.NewsSourcesDao
import com.compose.news.data.model.source.Source
import com.compose.news.data.network.NewsAPI
import com.compose.news.data.repository.paging.NewsSourcesPagingSource
import com.compose.news.domain.repository.NewsSourcesRepository
import com.compose.news.util.Constants
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class NewsSourceRepositoryImpl @Inject constructor(
    private val newsAPI: NewsAPI,
    private val newsSourcesDao: NewsSourcesDao
) : NewsSourcesRepository {
    override fun getNewsSources(category: String): Flow<PagingData<Source>> {
        return Pager(
            config = PagingConfig(pageSize = Constants.MAX_PAGE_SIZE),
            pagingSourceFactory = { NewsSourcesPagingSource(newsAPI, newsSourcesDao, category) }
        ).flow
    }

    override fun searchNewsSources(keyword: String): Flow<List<Source>> {
        return newsSourcesDao.searchNewsSources("%${keyword}%")
    }
}