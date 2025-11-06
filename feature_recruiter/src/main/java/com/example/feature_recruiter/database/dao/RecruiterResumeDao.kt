package com.example.feature_recruiter.database.dao

import androidx.room.*
import com.example.feature_recruiter.database.entity.RecruiterResumeEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface RecruiterResumeDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertResume(resume: RecruiterResumeEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMultipleResumes(resumes: List<RecruiterResumeEntity>)

    @Query("SELECT * FROM recruiter_resumes WHERE recruiterEmail = :email ORDER BY uploadedDate DESC")
    fun getAllResumesFlow(email: String): Flow<List<RecruiterResumeEntity>>

    @Query("SELECT * FROM recruiter_resumes WHERE recruiterEmail = :email ORDER BY uploadedDate DESC LIMIT :limit")
    suspend fun getRecentResumes(email: String, limit: Int = 5): List<RecruiterResumeEntity>

    @Query("SELECT * FROM recruiter_resumes WHERE recruiterEmail = :email AND id = :id")
    suspend fun getResumeById(email: String, id: Int): RecruiterResumeEntity?

    @Query("""
        SELECT * FROM recruiter_resumes 
        WHERE recruiterEmail = :email 
        AND techStack LIKE '%' || :tech || '%'
        ORDER BY uploadedDate DESC
    """)
    suspend fun filterByTechStack(email: String, tech: String): List<RecruiterResumeEntity>

    @Query("""
        SELECT * FROM recruiter_resumes 
        WHERE recruiterEmail = :email 
        AND experience >= :minExp 
        AND experience <= :maxExp
        ORDER BY uploadedDate DESC
    """)
    suspend fun filterByExperience(email: String, minExp: Int, maxExp: Int): List<RecruiterResumeEntity>

    @Query("""
        SELECT * FROM recruiter_resumes 
        WHERE recruiterEmail = :email 
        AND techStack LIKE '%' || :tech || '%'
        AND experience >= :minExp 
        AND experience <= :maxExp
        ORDER BY uploadedDate DESC
    """)
    suspend fun filterByCombined(
        email: String,
        tech: String,
        minExp: Int,
        maxExp: Int
    ): List<RecruiterResumeEntity>

    @Query("SELECT COUNT(*) FROM recruiter_resumes WHERE recruiterEmail = :email")
    suspend fun getTotalCount(email: String): Int

    @Query("SELECT DISTINCT techStack FROM recruiter_resumes WHERE recruiterEmail = :email")
    suspend fun getAllTechStacks(email: String): List<String>

    @Delete
    suspend fun deleteResume(resume: RecruiterResumeEntity)

    @Query("DELETE FROM recruiter_resumes WHERE recruiterEmail = :email AND id = :id")
    suspend fun deleteResumeById(email: String, id: Int)

    @Query("""
        SELECT * FROM recruiter_resumes 
        WHERE recruiterEmail = :email 
        AND uploadedDate >= :startDate 
        AND uploadedDate <= :endDate
        ORDER BY uploadedDate DESC
    """)
    suspend fun getResumesByDateRange(
        email: String,
        startDate: Long,
        endDate: Long
    ): List<RecruiterResumeEntity>

    @Query("""
        SELECT * FROM recruiter_resumes 
        WHERE recruiterEmail = :email 
        AND (candidateName LIKE '%' || :query || '%' 
        OR candidateEmail LIKE '%' || :query || '%'
        OR techStack LIKE '%' || :query || '%')
        ORDER BY uploadedDate DESC
    """)
    suspend fun searchResumes(email: String, query: String): List<RecruiterResumeEntity>
}
