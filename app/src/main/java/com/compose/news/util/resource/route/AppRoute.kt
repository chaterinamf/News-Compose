package com.compose.news.util.resource.route

sealed class AppScreen(val route: String) {
    data object Sources: AppScreen(ConstantAppScreenName.SOURCES_SCREEN)
    data object Articles: AppScreen(ConstantAppScreenName.ARTICLES_SCREEN)
    data object ArticleDetail: AppScreen(ConstantAppScreenName.ARTICLE_DETAIL_SCREEN)
}

object ConstantAppScreenName {
    const val SOURCES_SCREEN = "sources_screen"
    const val ARTICLES_SCREEN = "articles_screen"
    const val ARTICLE_DETAIL_SCREEN = "article_detail_screen"
}