package com.compose.news.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.compose.news.data.model.source.Source
import kotlinx.coroutines.flow.Flow

@Dao
interface NewsSourcesDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(sources: List<Source>)

    @Query("DELETE FROM sources")
    suspend fun delete()

    @Transaction
    suspend fun saveNewsSources(sources: List<Source>) {
        delete()
        insert(sources)
    }

    @Query("SELECT * FROM sources WHERE name LIKE :keyword")
    fun searchNewsSources(keyword: String): Flow<List<Source>>
}