package com.example.feature_recruiter.repository

import com.example.feature_recruiter.database.dao.RecruiterResumeDao
import com.example.feature_recruiter.database.entity.RecruiterResumeEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map

class RecruiterResumeRepository(private val dao: RecruiterResumeDao) {

    suspend fun insertResume(resume: RecruiterResumeEntity) {
        try {
            dao.insertResume(resume)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    suspend fun insertMultipleResumes(resumes: List<RecruiterResumeEntity>) {
        try {
            if (resumes.isNotEmpty()) {
                dao.insertMultipleResumes(resumes)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun getAllResumes(email: String): Flow<List<RecruiterResumeEntity>?> {
        return dao.getAllResumesFlow(email)
            .map { it as List<RecruiterResumeEntity>? }
            .catch { e ->
                e.printStackTrace()
            }
    }

    suspend fun getRecentResumes(email: String, limit: Int = 5): List<RecruiterResumeEntity>? {
        return try {
            dao.getRecentResumes(email, limit)
        } catch (e: Exception) {
            e.printStackTrace()
            emptyList()
        }
    }

    suspend fun getResumeById(email: String, id: Int): RecruiterResumeEntity? {
        return try {
            dao.getResumeById(email, id)
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    suspend fun filterByTechStack(email: String, techStack: List<String>): List<RecruiterResumeEntity>? {
        return try {
            if (techStack.isEmpty()) return emptyList()

            val results = mutableSetOf<RecruiterResumeEntity>()
            for (tech in techStack) {
                results.addAll(dao.filterByTechStack(email, tech))
            }
            results.sortedByDescending { it.uploadedDate }
        } catch (e: Exception) {
            e.printStackTrace()
            emptyList()
        }
    }

    suspend fun filterByExperience(email: String, minExp: Int, maxExp: Int): List<RecruiterResumeEntity>? {
        return try {
            dao.filterByExperience(email, minExp, maxExp)
        } catch (e: Exception) {
            e.printStackTrace()
            emptyList()
        }
    }

    suspend fun filterByCombined(
        email: String,
        techStack: List<String>,
        minExp: Int,
        maxExp: Int
    ): List<RecruiterResumeEntity>? {
        return try {
            if (techStack.isEmpty()) {
                return dao.filterByExperience(email, minExp, maxExp)
            }

            val results = mutableSetOf<RecruiterResumeEntity>()
            for (tech in techStack) {
                results.addAll(dao.filterByCombined(email, tech, minExp, maxExp))
            }
            results.sortedByDescending { it.uploadedDate }
        } catch (e: Exception) {
            e.printStackTrace()
            emptyList()
        }
    }

    suspend fun getTotalCount(email: String): Int {
        return try {
            dao.getTotalCount(email)
        } catch (e: Exception) {
            e.printStackTrace()
            0
        }
    }

    suspend fun getAllTechStacks(email: String): List<String> {
        return try {
            val stacks = dao.getAllTechStacks(email)
            stacks
                .flatMap { it.split(",") }
                .map { it.trim() }
                .distinct()
                .sorted()
        } catch (e: Exception) {
            e.printStackTrace()
            emptyList()
        }
    }

    suspend fun deleteResume(resume: RecruiterResumeEntity) {
        try {
            dao.deleteResume(resume)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    suspend fun deleteResumeById(email: String, id: Int) {
        try {
            dao.deleteResumeById(email, id)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    suspend fun getResumesByDateRange(email: String, startDate: Long, endDate: Long): List<RecruiterResumeEntity>? {
        return try {
            dao.getResumesByDateRange(email, startDate, endDate)
        } catch (e: Exception) {
            e.printStackTrace()
            emptyList()
        }
    }

    suspend fun searchResumes(email: String, query: String): List<RecruiterResumeEntity>? {
        return try {
            dao.searchResumes(email, query)
        } catch (e: Exception) {
            e.printStackTrace()
            emptyList()
        }
    }
}