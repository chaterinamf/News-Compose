package com.compose.news.data.model.article

data class NewsArticles(
    val status: String,
    val totalResult: Int,
    val articles: List<Article>
)