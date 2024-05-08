package com.compose.news.data.repository.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.compose.news.data.model.article.Article
import com.compose.news.data.network.NewsAPI
import retrofit2.HttpException
import java.io.IOException

class NewsArticlesPagingSource(
    private val newsAPI: NewsAPI,
    private val source: String,
    private val queryParam: String
) : PagingSource<Int, Article>() {
    override fun getRefreshKey(state: PagingState<Int, Article>): Int? {
        return state.anchorPosition
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Article> {
        return try {
            val page = params.key ?: INITIAL_PAGE_INDEX
            val response = newsAPI.getNewsArticles(source, queryParam, page, params.loadSize)

            LoadResult.Page(
                data = response.articles,
                prevKey = if (page == INITIAL_PAGE_INDEX) null else page - INITIAL_PAGE_INDEX,
                nextKey = if (response.articles.isEmpty()) null else page + INITIAL_PAGE_INDEX
            )
        } catch (e: Exception) {
            e.printStackTrace()
            return LoadResult.Error(e)
        } catch (e: IOException) {
            e.printStackTrace()
            return LoadResult.Error(e)
        } catch (e: HttpException) {
            e.printStackTrace()
            return LoadResult.Error(e)
        }
    }

    private companion object {
        const val INITIAL_PAGE_INDEX = 1
    }
}