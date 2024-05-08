package com.compose.news.data.network

import com.compose.news.BuildConfig.NEWS_API_KEY
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject

class APIInterceptor @Inject constructor() : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request().newBuilder()
            .addHeader("Authorization", "Bearer $NEWS_API_KEY")
            .build()
        return chain.proceed(request)
    }
}