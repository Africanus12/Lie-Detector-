package com.liedetector.app.data.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface AnalysisDao {
    @Insert
    suspend fun insert(analysis: AnalysisEntity): Long

    @Query("SELECT * FROM analyses ORDER BY timestamp DESC")
    fun getAllAnalyses(): Flow<List<AnalysisEntity>>

    @Query("SELECT * FROM analyses ORDER BY timestamp DESC LIMIT :limit")
    fun getRecentAnalyses(limit: Int = 20): Flow<List<AnalysisEntity>>

    @Query("SELECT * FROM analyses WHERE id = :id")
    suspend fun getById(id: Long): AnalysisEntity?

    @Query("DELETE FROM analyses WHERE id = :id")
    suspend fun deleteById(id: Long)

    @Query("SELECT COUNT(*) FROM analyses WHERE timestamp > :since")
    suspend fun getAnalysisCountSince(since: Long): Int
}
