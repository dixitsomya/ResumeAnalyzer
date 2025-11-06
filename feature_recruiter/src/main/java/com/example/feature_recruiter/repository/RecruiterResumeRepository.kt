package com.example.feature_recruiter.repository

import com.example.feature_recruiter.database.dao.RecruiterResumeDao
import com.example.feature_recruiter.database.entity.RecruiterResumeEntity
import kotlinx.coroutines.flow.Flow

class RecruiterResumeRepository(private val dao: RecruiterResumeDao) {

    suspend fun insertResume(resume: RecruiterResumeEntity) {
        dao.insertResume(resume)
    }

    suspend fun insertMultipleResumes(resumes: List<RecruiterResumeEntity>) {
        dao.insertMultipleResumes(resumes)
    }

    fun getAllResumes(email: String): Flow<List<RecruiterResumeEntity>> {
        return dao.getAllResumesFlow(email)
    }

    suspend fun getRecentResumes(email: String, limit: Int = 5): List<RecruiterResumeEntity> {
        return dao.getRecentResumes(email, limit)
    }

    suspend fun getResumeById(email: String, id: Int): RecruiterResumeEntity? {
        return dao.getResumeById(email, id)
    }

    suspend fun filterByTechStack(email: String, techStack: List<String>): List<RecruiterResumeEntity> {
        if (techStack.isEmpty()) return dao.getRecentResumes(email)

        val results = mutableSetOf<RecruiterResumeEntity>()
        for (tech in techStack) {
            results.addAll(dao.filterByTechStack(email, tech))
        }
        return results.sortedByDescending { it.uploadedDate }
    }

    suspend fun filterByExperience(email: String, minExp: Int, maxExp: Int): List<RecruiterResumeEntity> {
        return dao.filterByExperience(email, minExp, maxExp)
    }

    suspend fun filterByCombined(
        email: String,
        techStack: List<String>,
        minExp: Int,
        maxExp: Int
    ): List<RecruiterResumeEntity> {
        if (techStack.isEmpty()) {
            return dao.filterByExperience(email, minExp, maxExp)
        }

        val results = mutableSetOf<RecruiterResumeEntity>()
        for (tech in techStack) {
            results.addAll(dao.filterByCombined(email, tech, minExp, maxExp))
        }
        return results.sortedByDescending { it.uploadedDate }
    }

    suspend fun getTotalCount(email: String): Int {
        return dao.getTotalCount(email)
    }

    suspend fun getAllTechStacks(email: String): List<String> {
        return dao.getAllTechStacks(email)
            .flatMap { it.split(",") }
            .map { it.trim() }
            .distinct()
            .sorted()
    }

    suspend fun deleteResume(resume: RecruiterResumeEntity) {
        dao.deleteResume(resume)
    }

    suspend fun deleteResumeById(email: String, id: Int) {
        dao.deleteResumeById(email, id)
    }

    suspend fun getResumesByDateRange(email: String, startDate: Long, endDate: Long): List<RecruiterResumeEntity> {
        return dao.getResumesByDateRange(email, startDate, endDate)
    }

    suspend fun searchResumes(email: String, query: String): List<RecruiterResumeEntity> {
        return dao.searchResumes(email, query)
    }
}