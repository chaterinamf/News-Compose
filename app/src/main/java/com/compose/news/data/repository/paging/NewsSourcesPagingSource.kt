package com.compose.news.data.repository.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.compose.news.data.local.NewsSourcesDao
import com.compose.news.data.model.source.Source
import com.compose.news.data.network.NewsAPI
import retrofit2.HttpException
import java.io.IOException

class NewsSourcesPagingSource(
    private val newsAPI: NewsAPI,
    private val newsSourcesDao: NewsSourcesDao,
    private val category: String
) : PagingSource<Int, Source>() {
    override fun getRefreshKey(state: PagingState<Int, Source>): Int? {
        return state.anchorPosition
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Source> {
        return try {
            val response = newsAPI.getNewsSources(category.lowercase())

            newsSourcesDao.saveNewsSources(response.sources)

            /**
             * The News API for Sources Endpoint
             * doesn't have the pageSize and page request parameters
             * Therefore, set the prevKey and the nextKey to null
             * to indicate that there are no previous or next pages, respectively
             */
            LoadResult.Page(
                data = response.sources,
                prevKey = null,
                nextKey = null
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
}