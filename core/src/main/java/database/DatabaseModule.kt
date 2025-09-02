package com.example.resumeanalyzer.core.database

import android.content.Context
import androidx.room.Room
import com.example.resumeanalyzer.core.database.dao.UserDao

object DatabaseModule {
    private var INSTANCE: AppDatabase? = null

    fun provideDatabase(context: Context): AppDatabase {
        return INSTANCE ?: synchronized(this) {
            val instance = Room.databaseBuilder(
                context.applicationContext,
                AppDatabase::class.java,
                "resume_analyzer_db"
            )
                .fallbackToDestructiveMigration()
                .build()
            INSTANCE = instance
            instance
        }
    }

    fun provideUserDao(db: AppDatabase): UserDao = db.userDao()
}
