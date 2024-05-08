package com.compose.news.presentation.source

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.SearchBar
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import com.compose.news.R
import com.compose.news.data.model.source.Source
import com.compose.news.util.EmptyView
import com.compose.news.util.ErrorMessage
import com.compose.news.util.LoadingNextPageItem
import com.compose.news.util.PageLoader
import com.compose.news.util.orDash
import com.compose.news.util.resource.route.AppScreen

@Composable
fun NewsSourcesScreen(
    navController: NavController,
    onClick: (String) -> Unit = { sourceId ->
        navController.navigate(AppScreen.Articles.route + "/$sourceId")
    }
) {
    val viewModel = hiltViewModel<NewsSourcesViewModel>()
    Column {
        val categories = stringArrayResource(id = R.array.news_categories).toList()
        var selectedCategory by rememberSaveable { mutableStateOf<String?>(categories.first()) }
        viewModel.getNewsSources(selectedCategory.orEmpty())

        SearchBar(viewModel = viewModel, onClick)

        LazyRow {
            items(categories) { category ->
                Row(verticalAlignment = Alignment.CenterVertically) {
                    RadioButton(
                        selected = (category == selectedCategory),
                        onClick = {
                            selectedCategory = category
                            viewModel.getNewsSources(selectedCategory.orEmpty())
                        }
                    )
                    Text(
                        text = category,
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }
        }
        SourceByCategoryLazyColumn(
            newsSourcesPagingItems = viewModel.sourceState.collectAsLazyPagingItems(),
            onClick = onClick,
            selectedCategory.orDash()
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SearchBar(
    viewModel: NewsSourcesViewModel,
    onClick: (String) -> Unit
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
            viewModel.searchNewsSources(it)
            viewModel.saveKeyword(it)
            isActive = false
        },
        active = isActive,
        onActiveChange = { isActive = it },
        placeholder = { Text(text = stringResource(id = R.string.news_sources)) },
        trailingIcon = {
            IconButton(
                onClick = {
                    if (isActive) {
                        text = ""
                        isActive = false
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
                        viewModel.searchNewsSources(key)
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
    SearchLazyColumn(
        searchNewsSources = viewModel.searchSourcesState.collectAsStateWithLifecycle().value,
        onClick = onClick,
        isSearch = text.isNotBlank() && text.isNotEmpty()
    )
}

@Composable
private fun SourceByCategoryLazyColumn(
    newsSourcesPagingItems: LazyPagingItems<Source>,
    onClick: (String) -> Unit,
    category: String
) {
    LazyColumn(modifier = Modifier.padding(horizontal = 16.dp)) {
        items(newsSourcesPagingItems.itemCount) { index ->
            ItemSource(
                source = newsSourcesPagingItems[index],
                onClick = onClick
            )
        }
        newsSourcesPagingItems.apply {
            when {
                loadState.refresh is LoadState.Loading -> {
                    item {
                        PageLoader(
                            modifier = Modifier.fillParentMaxSize(),
                            loadText = stringResource(
                                id = R.string.loading_source_category,
                                category
                            )
                        )
                    }
                }

                this.itemCount == 0 -> {
                    item {
                        EmptyView(
                            modifier = Modifier.fillParentMaxSize(),
                            message = stringResource(id = R.string.sources_empty_info)
                        )
                    }
                }

                loadState.refresh is LoadState.Error -> {
                    val error = newsSourcesPagingItems.loadState.refresh as LoadState.Error
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

@Composable
private fun SearchLazyColumn(
    searchNewsSources: List<Source>,
    onClick: (String) -> Unit,
    isSearch: Boolean
) {
    AnimatedVisibility(
        visible = isSearch,
        modifier = Modifier
            .padding(start = 8.dp, end = 8.dp, top = 8.dp)
            .fillMaxHeight()
    ) {
        LazyColumn {
            items(searchNewsSources.size) { index ->
                ItemSource(
                    source = searchNewsSources[index],
                    onClick = onClick
                )
            }
            if (searchNewsSources.isEmpty())
                item {
                    EmptyView(
                        modifier = Modifier.fillParentMaxSize(),
                        message = stringResource(id = R.string.sources_empty_info)
                    )
                }
        }
    }
}