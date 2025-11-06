package com.example.feature_recruiter.repository

import com.example.feature_recruiter.database.dao.RecruiterProfileDao
import com.example.feature_recruiter.database.entity.RecruiterProfileEntity
import kotlinx.coroutines.flow.Flow

class RecruiterProfileRepository(private val dao: RecruiterProfileDao) {

    suspend fun insertProfile(profile: RecruiterProfileEntity) {
        dao.insertProfile(profile)
    }

    suspend fun getProfileByEmail(email: String): RecruiterProfileEntity? {
        return dao.getProfileByEmail(email)
    }

    fun getProfileFlow(email: String): Flow<RecruiterProfileEntity?> {
        return dao.getProfileFlow(email)
    }

    suspend fun updateProfile(profile: RecruiterProfileEntity) {
        dao.updateProfile(profile)
    }

    suspend fun markSetupCompleted(email: String) {
        dao.markSetupCompleted(email)
    }

    suspend fun updateCompanyInfo(
        email: String,
        companyName: String,
        recruiterName: String
    ) {
        dao.updateCompanyInfo(email, companyName, recruiterName)
    }
}