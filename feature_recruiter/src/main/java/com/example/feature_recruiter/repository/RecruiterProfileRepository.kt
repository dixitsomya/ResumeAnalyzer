
package com.example.feature_recruiter.repository

import com.example.feature_recruiter.database.dao.RecruiterProfileDao
import com.example.feature_recruiter.database.entity.RecruiterProfileEntity
import kotlinx.coroutines.flow.Flow

class RecruiterProfileRepository(private val dao: RecruiterProfileDao) {

    suspend fun insertProfile(profile: RecruiterProfileEntity) {
        try {
            dao.insertProfile(profile)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    suspend fun getProfileByEmail(email: String): RecruiterProfileEntity? {
        return try {
            dao.getProfileByEmail(email)
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    fun getProfileFlow(email: String): Flow<RecruiterProfileEntity?> {
        return dao.getProfileFlow(email)
    }

    suspend fun updateProfile(profile: RecruiterProfileEntity) {
        try {
            dao.updateProfile(profile)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    suspend fun isSetupCompleted(email: String): Boolean {
        return try {
            dao.isSetupCompleted(email) ?: false
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }

    suspend fun markSetupCompleted(email: String) {
        try {
            dao.markSetupCompleted(email)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    suspend fun updateCompanyInfoAndMarkSetup(
        email: String,
        companyName: String,
        recruiterName: String
    ) {
        try {
            dao.updateCompanyInfoAndMarkSetup(email, companyName, recruiterName)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}