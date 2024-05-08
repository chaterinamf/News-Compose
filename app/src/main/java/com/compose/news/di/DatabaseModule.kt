package com.compose.news.di

import android.content.Context
import androidx.room.Room
import com.compose.news.data.local.NewsDatabase
import com.compose.news.util.Constants
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object DatabaseModule {
    @Singleton
    @Provides
    fun provideDatabase(@ApplicationContext context: Context): NewsDatabase =
        Room.databaseBuilder(
            context.applicationContext,
            NewsDatabase::class.java,
            Constants.DATABASE_NAME,
        ).fallbackToDestructiveMigration().build()

    @Singleton
    @Provides
    fun provideNewsSourcesDao(database: NewsDatabase) = database.NewsSourcesDao()
}