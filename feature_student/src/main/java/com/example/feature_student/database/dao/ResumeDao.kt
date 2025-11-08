package com.example.feature_student.database.dao

import androidx.room.*
import com.example.feature_student.database.entity.ResumeEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ResumeDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertResume(resume: ResumeEntity)

    @Update
    suspend fun updateResume(resume: ResumeEntity)

    @Delete
    suspend fun deleteResume(resume: ResumeEntity)

    @Query("SELECT * FROM resumes WHERE id = :id")
    suspend fun getResumeById(id: String): ResumeEntity?

    @Query("SELECT * FROM resumes ORDER BY uploadedDate DESC")
    fun getAllResumes(): Flow<List<ResumeEntity>>

    // ✅ Add this sync version for immediate access
    @Query("SELECT * FROM resumes ORDER BY uploadedDate DESC")
    suspend fun getAllResumesSync(): List<ResumeEntity>

    @Query("SELECT * FROM resumes WHERE status = 'ANALYZED' ORDER BY uploadedDate DESC")
    fun getAnalyzedResumes(): Flow<List<ResumeEntity>>

    @Query("SELECT * FROM resumes WHERE status = 'ANALYZED' ORDER BY uploadedDate DESC LIMIT 1")
    suspend fun getLatestAnalyzedResume(): ResumeEntity?

    @Query("SELECT COUNT(*) FROM resumes WHERE status = 'ANALYZED'")
    fun getAnalyzedCount(): Flow<Int>

    @Query("SELECT AVG(atsScore) FROM resumes WHERE status = 'ANALYZED'")
    fun getAverageScore(): Flow<Double>

    @Query("DELETE FROM resumes")
    suspend fun deleteAll()

    @Query("SELECT * FROM resumes WHERE userEmail = :userEmail ORDER BY uploadedDate DESC")
    suspend fun getResumesByUserEmail(userEmail: String): List<ResumeEntity>

    @Query("SELECT * FROM resumes WHERE userEmail = :userEmail AND status = 'ANALYZED' ORDER BY uploadedDate DESC")
    suspend fun getAnalyzedResumesByUser(userEmail: String): List<ResumeEntity>

    @Query("SELECT AVG(atsScore) FROM resumes WHERE userEmail = :userEmail AND status = 'ANALYZED'")
    suspend fun getAverageScoreByUser(userEmail: String): Double?


}