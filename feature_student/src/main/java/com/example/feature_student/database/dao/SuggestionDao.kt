package com.example.feature_student.database.dao

import androidx.room.*
import com.example.feature_student.database.entity.SuggestionEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface SuggestionDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSuggestion(suggestion: SuggestionEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSuggestions(suggestions: List<SuggestionEntity>)

    @Update
    suspend fun updateSuggestion(suggestion: SuggestionEntity)

    @Query("SELECT * FROM suggestions WHERE resumeId = :resumeId ORDER BY priority DESC")
    fun getSuggestionsByResumeId(resumeId: String): Flow<List<SuggestionEntity>>

    @Query("DELETE FROM suggestions WHERE resumeId = :resumeId")
    suspend fun deleteSuggestionsByResumeId(resumeId: String)

    @Query("DELETE FROM suggestions")
    suspend fun deleteAll()

    @Query("SELECT * FROM suggestions WHERE userEmail = :userEmail AND resumeId = :resumeId ORDER BY priority DESC")
    fun getSuggestionsByUser(userEmail: String, resumeId: String): Flow<List<SuggestionEntity>>

}