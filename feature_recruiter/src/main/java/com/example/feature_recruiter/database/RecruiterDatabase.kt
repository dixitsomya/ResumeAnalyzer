

package com.example.feature_recruiter.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.feature_recruiter.database.dao.RecruiterResumeDao
import com.example.feature_recruiter.database.dao.RecruiterProfileDao
import com.example.feature_recruiter.database.dao.SearchHistoryDao
import com.example.feature_recruiter.database.entity.RecruiterResumeEntity
import com.example.feature_recruiter.database.entity.RecruiterProfileEntity
import com.example.feature_recruiter.database.entity.SearchHistoryEntity

@Database(
    entities = [
        RecruiterResumeEntity::class,
        RecruiterProfileEntity::class,
        SearchHistoryEntity::class  // ✅ NEW
    ],
    version = 1,
    exportSchema = false
)
abstract class RecruiterDatabase : RoomDatabase() {
    abstract fun recruiterResumeDao(): RecruiterResumeDao
    abstract fun recruiterProfileDao(): RecruiterProfileDao
    abstract fun searchHistoryDao(): SearchHistoryDao  // ✅ NEW
}