package com.example.feature_recruiter.database.dao

import androidx.room.*
import com.example.feature_recruiter.database.entity.SearchHistoryEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface SearchHistoryDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSearchHistory(history: SearchHistoryEntity)

    @Query("SELECT * FROM search_history WHERE recruiterEmail = :email ORDER BY searchedAt DESC")
    fun getSearchHistory(email: String): Flow<List<SearchHistoryEntity>>

    @Query("SELECT * FROM search_history WHERE recruiterEmail = :email ORDER BY searchedAt DESC LIMIT :limit")
    suspend fun getRecentSearches(email: String, limit: Int = 10): List<SearchHistoryEntity>

    @Query("SELECT * FROM search_history WHERE recruiterEmail = :email AND searchedAt BETWEEN :startDate AND :endDate ORDER BY searchedAt DESC")
    suspend fun getSearchesByDateRange(
        email: String,
        startDate: Long,
        endDate: Long
    ): List<SearchHistoryEntity>

    @Delete
    suspend fun deleteSearchHistory(history: SearchHistoryEntity)

    @Query("DELETE FROM search_history WHERE recruiterEmail = :email")
    suspend fun clearAllHistory(email: String)
}