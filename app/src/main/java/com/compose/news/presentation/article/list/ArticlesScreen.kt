package com.compose.news.presentation.article.list

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SearchBar
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import com.compose.news.R
import com.compose.news.data.model.article.Article
import com.compose.news.util.EmptyView
import com.compose.news.util.ErrorMessage
import com.compose.news.util.LoadingNextPageItem
import com.compose.news.util.PageLoader
import com.compose.news.util.resource.route.AppScreen


@Composable
fun ArticlesScreen(
    navController: NavController,
    sourceId: String,
    onClick: (String) -> Unit = { articleUrl ->
        navController.navigate(AppScreen.ArticleDetail.route + "?articleUrl=$articleUrl")
    }
) {
    val viewModel = hiltViewModel<NewsArticlesViewModel>()
    viewModel.getNewsArticles(sourceId)
    Column {
        SearchBar(
            viewModel = viewModel,
            sourceId = sourceId,
            onClick = onClick,
            backButton = { navController.popBackStack() }
        )
        ArticlesLazyColumn(
            articlePagingItems = viewModel.articleState.collectAsLazyPagingItems(),
            onClick = onClick
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SearchBar(
    viewModel: NewsArticlesViewModel,
    sourceId: String,
    onClick: (String) -> Unit,
    backButton: () -> Unit
) {
    var text by rememberSaveable { mutableStateOf("") }
    var isActive by rememberSaveable { mutableStateOf(false) }

    SearchBar(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = if (isActive) 0.dp else 8.dp),
        query = text,
        onQueryChange = {
            text = it
        },
        onSearch = {
            viewModel.getNewsArticles(sourceId, it)
            viewModel.saveKeyword(it)
            isActive = false
        },
        active = isActive,
        onActiveChange = { isActive = it },
        placeholder = { Text(text = stringResource(id = R.string.news_articles)) },
        leadingIcon = {
            IconButton(onClick = backButton) {
                Icon(imageVector = Icons.AutoMirrored.Filled.ArrowBack, contentDescription = null)
            }
        },
        trailingIcon = {
            IconButton(
                onClick = {
                    if (isActive) {
                        text = ""
                        isActive = false
                        viewModel.getNewsArticles(sourceId)
                    }
                }
            ) {
                Icon(
                    imageVector = if (!isActive) Icons.Default.Search else Icons.Default.Clear,
                    contentDescription = null
                )
            }
        }
    ) {
        LazyColumn {
            items(viewModel.keywordHistory) { key ->
                TextButton(
                    onClick = {
                        text = key
                        viewModel.getNewsArticles(sourceId, key)
                        isActive = false
                    }
                ) {
                    Text(text = key, modifier = Modifier.fillMaxWidth())
                }

                Spacer(
                    modifier = Modifier
                        .height(1.dp)
                        .fillMaxWidth()
                        .background(color = MaterialTheme.colorScheme.primary)
                )
            }
        }
    }
    ArticlesLazyColumn(
        articlePagingItems = viewModel.articleState.collectAsLazyPagingItems(),
        onClick = onClick
    )
}

@Composable
private fun ArticlesLazyColumn(
    articlePagingItems: LazyPagingItems<Article>,
    onClick: (String) -> Unit
) {
    LazyColumn(modifier = Modifier.padding(horizontal = 16.dp)) {
        items(articlePagingItems.itemCount) { index ->
            ItemArticle(
                article = articlePagingItems[index],
                onClick = onClick
            )
        }
        articlePagingItems.apply {
            when {
                loadState.refresh is LoadState.Loading -> {
                    item {
                        PageLoader(modifier = Modifier.fillParentMaxSize())
                    }
                }

                this.itemCount == 0 -> {
                    item {
                        EmptyView(
                            modifier = Modifier.fillParentMaxSize(),
                            message = stringResource(id = R.string.articles_empty_info)
                        )
                    }
                }

                loadState.refresh is LoadState.Error -> {
                    val error = articlePagingItems.loadState.refresh as LoadState.Error
                    item {
                        ErrorMessage(
                            modifier = Modifier.fillParentMaxSize(),
                            message = error.error.localizedMessage,
                            onClickRetry = { retry() })
                    }
                }

                loadState.append is LoadState.Loading -> {
                    item { LoadingNextPageItem() }
                }

                loadState.append is LoadState.Error -> {
                    val error = loadState.append as LoadState.Error
                    item {
                        ErrorMessage(
                            modifier = Modifier.fillParentMaxSize(),
                            message = error.error.localizedMessage,
                            onClickRetry = { retry() })
                    }
                }
            }
        }
    }
}