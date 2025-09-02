package com.example.resumeanalyzer.core.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.resumeanalyzer.core.database.dao.UserDao
import com.example.resumeanalyzer.core.database.entity.UserEntity

@Database(entities = [UserEntity::class], version = 2)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
}
