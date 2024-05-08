package com.compose.news.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.compose.news.data.model.source.Source


@Database(
    entities = [Source::class],
    version = 1,
    exportSchema = false
)
abstract class NewsDatabase: RoomDatabase() {
    abstract fun NewsSourcesDao(): NewsSourcesDao
}