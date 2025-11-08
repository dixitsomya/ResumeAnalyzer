package com.example.feature_student.database.dao

import androidx.room.*
import com.example.feature_student.database.entity.AnalysisResultEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface AnalysisResultDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAnalysisResult(result: AnalysisResultEntity)

    @Update
    suspend fun updateAnalysisResult(result: AnalysisResultEntity)

    @Delete
    suspend fun deleteAnalysisResult(result: AnalysisResultEntity)

    @Query("DELETE FROM analysis_results WHERE resumeId = :resumeId")
    suspend fun deleteAnalysisResult(resumeId: String)  // 🚀 NEW - for 10 file limit

    @Query("SELECT * FROM analysis_results WHERE resumeId = :resumeId")
    suspend fun getAnalysisResult(resumeId: String): AnalysisResultEntity?

    @Query("SELECT * FROM analysis_results ORDER BY analyzedAt DESC LIMIT 1")
    suspend fun getLatestAnalysisResult(): AnalysisResultEntity?

    @Query("SELECT * FROM analysis_results WHERE resumeId = :resumeId")
    suspend fun getAnalysisResultSync(resumeId: String): AnalysisResultEntity?

    @Query("DELETE FROM analysis_results")
    suspend fun deleteAll()

    @Query("SELECT * FROM analysis_results WHERE userEmail = :userEmail AND resumeId = :resumeId")
    suspend fun getAnalysisResultByUser(userEmail: String, resumeId: String): AnalysisResultEntity?

}