package com.compose.news.data.network

import com.compose.news.data.model.article.NewsArticles
import com.compose.news.data.model.source.NewsSources
import com.compose.news.util.Constants.ARTICLES_ENDPOINT
import com.compose.news.util.Constants.SOURCES_ENDPOINT
import retrofit2.http.GET
import retrofit2.http.Query

interface NewsAPI {
    @GET(SOURCES_ENDPOINT)
    suspend fun getNewsSources(@Query("category") category: String): NewsSources

    @GET(ARTICLES_ENDPOINT)
    suspend fun getNewsArticles(
        @Query("sources") sources: String,
        @Query("q", encoded = true) queryParam: String,
        @Query("page") page: Int,
        @Query("pageSize") perPage: Int
    ): NewsArticles
}