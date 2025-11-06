//package com.example.feature_recruiter.util
//
//import android.content.Context
//import androidx.room.Room
//import com.example.feature_recruiter.database.RecruiterDatabase
//
//object DatabaseHelper {
//
//    private var instance: RecruiterDatabase? = null
//
//    fun getDatabase(context: Context): RecruiterDatabase {
//        return instance ?: synchronized(this) {
//            val db = Room.databaseBuilder(
//                context.applicationContext,
//                RecruiterDatabase::class.java,
//                "recruiter_database"
//            )
//                .fallbackToDestructiveMigration()
//                .build()
//            instance = db
//            db
//        }
//    }
//}


package com.example.feature_recruiter.util

import android.content.Context
import androidx.room.Room
import com.example.feature_recruiter.database.RecruiterDatabase

object DatabaseHelper {

    @Volatile
    private var instance: RecruiterDatabase? = null

    fun getDatabase(context: Context): RecruiterDatabase {
        return instance ?: synchronized(this) {
            val db = Room.databaseBuilder(
                context.applicationContext,
                RecruiterDatabase::class.java,
                "recruiter_database"
            )
                .fallbackToDestructiveMigration()
                .enableMultiInstanceInvalidation()  // ✅ Prevent binder issues
                .build()
            instance = db
            db
        }
    }
}