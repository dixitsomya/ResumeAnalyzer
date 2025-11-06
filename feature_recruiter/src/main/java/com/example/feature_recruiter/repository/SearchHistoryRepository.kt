package com.example.feature_recruiter.repository

import com.example.feature_recruiter.database.dao.SearchHistoryDao
import com.example.feature_recruiter.database.entity.SearchHistoryEntity
import kotlinx.coroutines.flow.Flow

class SearchHistoryRepository(private val dao: SearchHistoryDao) {

    suspend fun insertSearchHistory(history: SearchHistoryEntity) {
        dao.insertSearchHistory(history)
    }

    fun getSearchHistory(email: String): Flow<List<SearchHistoryEntity>> {
        return dao.getSearchHistory(email)
    }

    suspend fun getRecentSearches(email: String, limit: Int = 10): List<SearchHistoryEntity> {
        return dao.getRecentSearches(email, limit)
    }

    suspend fun getSearchesByDateRange(
        email: String,
        startDate: Long,
        endDate: Long
    ): List<SearchHistoryEntity> {
        return dao.getSearchesByDateRange(email, startDate, endDate)
    }

    suspend fun deleteSearchHistory(history: SearchHistoryEntity) {
        dao.deleteSearchHistory(history)
    }

    suspend fun clearAllHistory(email: String) {
        dao.clearAllHistory(email)
    }
}