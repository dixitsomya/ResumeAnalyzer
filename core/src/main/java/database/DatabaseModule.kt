package com.example.resumeanalyzer.core.database

import android.content.Context
import androidx.room.Room
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.resumeanalyzer.core.database.dao.UserDao

object DatabaseModule {
    private var INSTANCE: AppDatabase? = null

    // Define migrations for version changes
    private val MIGRATION_1_2 = object : Migration(1, 2) {
        override fun migrate(database: SupportSQLiteDatabase) {
            // Add your migration logic here if there were schema changes
            // Example: database.execSQL("ALTER TABLE users ADD COLUMN new_column TEXT")
        }
    }

    private val MIGRATION_2_3 = object : Migration(2, 3) {
        override fun migrate(database: SupportSQLiteDatabase) {
            // Add your migration logic here if there were schema changes
        }
    }

    fun provideDatabase(context: Context): AppDatabase {
        return INSTANCE ?: synchronized(this) {
            val instance = Room.databaseBuilder(
                context.applicationContext,
                AppDatabase::class.java,
                "user_login_db"
            )
                // Add migrations to preserve data
                .addMigrations(MIGRATION_1_2, MIGRATION_2_3)
                // Only use fallback as last resort (will still delete data)
                .fallbackToDestructiveMigration()
                .build()
            INSTANCE = instance
            instance
        }
    }

    fun provideUserDao(db: AppDatabase): UserDao = db.userDao()

    // Helper function to clear instance (useful for testing)
    fun clearInstance() {
        INSTANCE = null
    }
}