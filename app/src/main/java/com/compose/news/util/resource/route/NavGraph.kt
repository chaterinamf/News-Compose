package com.compose.news.util.resource.route

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.compose.news.presentation.article.detail.ArticleDetailScreen
import com.compose.news.presentation.article.list.ArticlesScreen
import com.compose.news.presentation.source.NewsSourcesScreen

@Composable
@Preview
fun NavGraph() {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = AppScreen.Sources.route) {
        composable(route = AppScreen.Sources.route) {
            NewsSourcesScreen(navController)
        }
        composable(
            route = AppScreen.Articles.route + "/{sourceId}",
            arguments = listOf(navArgument("sourceId") { type = NavType.StringType })
        ) { backStackEntry ->
            ArticlesScreen(
                navController,
                backStackEntry.arguments?.getString("sourceId").orEmpty()
            )
        }
        composable(
            route = AppScreen.ArticleDetail.route + "?articleUrl={articleUrl}",
            arguments = listOf(navArgument("articleUrl") { type = NavType.StringType })
        ) { backStackEntry ->
            ArticleDetailScreen(
                navController,
                backStackEntry.arguments?.getString("articleUrl").orEmpty()
            )
        }
    }
}