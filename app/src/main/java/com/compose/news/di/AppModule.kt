package com.compose.news.di

import com.compose.news.data.local.NewsSourcesDao
import com.compose.news.data.network.NewsAPI
import com.compose.news.data.repository.article.NewsArticlesRepositoryImpl
import com.compose.news.data.repository.paging.NewsArticlesPagingSource
import com.compose.news.data.repository.paging.NewsSourcesPagingSource
import com.compose.news.data.repository.source.NewsSourceRepositoryImpl
import com.compose.news.domain.repository.NewsArticlesRepository
import com.compose.news.domain.repository.NewsSourcesRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @Singleton
    @Provides
    fun providesNewsSourcesPagingSource(
        newsAPI: NewsAPI, newsSourcesDao: NewsSourcesDao, category: String
    ): NewsSourcesPagingSource {
        return NewsSourcesPagingSource(newsAPI, newsSourcesDao, category)
    }

    @Singleton
    @Provides
    fun providesNewsArticlesPagingSource(
        newsAPI: NewsAPI,
        source: String,
        queryParam: String
    ): NewsArticlesPagingSource {
        return NewsArticlesPagingSource(newsAPI, source, queryParam)
    }

    @Singleton
    @Provides
    fun providesNewsSourcesRepository(
        newsAPI: NewsAPI, newsSourcesDao: NewsSourcesDao
    ): NewsSourcesRepository {
        return NewsSourceRepositoryImpl(newsAPI, newsSourcesDao)
    }

    @Singleton
    @Provides
    fun providesNewsArticlesRepository(newsAPI: NewsAPI): NewsArticlesRepository {
        return NewsArticlesRepositoryImpl(newsAPI)
    }
}