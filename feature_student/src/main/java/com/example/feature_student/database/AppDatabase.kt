package com.example.feature_student.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.feature_student.database.dao.ResumeDao
import com.example.feature_student.database.dao.AnalysisResultDao
import com.example.feature_student.database.dao.SuggestionDao
import com.example.feature_student.database.entity.ResumeEntity
import com.example.feature_student.database.entity.AnalysisResultEntity
import com.example.feature_student.database.entity.SuggestionEntity

@Database(
    entities = [
        ResumeEntity::class,
        AnalysisResultEntity::class,
        SuggestionEntity::class
    ],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun resumeDao(): ResumeDao
    abstract fun analysisResultDao(): AnalysisResultDao
    abstract fun suggestionDao(): SuggestionDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "resume_analyzer_db"
                )
                    .fallbackToDestructiveMigration() // ⚠️ For development only
                    .build()

                INSTANCE = instance
                instance
            }
        }
    }
}