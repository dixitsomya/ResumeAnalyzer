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

// feature_recruiter/util/DatabaseHelper.kt
object DatabaseHelper {
    @Volatile private var INSTANCE: RecruiterDatabase? = null
    private const val DB_NAME = "recruiter_db_v1" // stable name

    fun getDatabase(context: Context): RecruiterDatabase =
        INSTANCE ?: synchronized(this) {
            INSTANCE ?: Room.databaseBuilder(
                context.applicationContext,
                RecruiterDatabase::class.java,
                DB_NAME
            )
                // .fallbackToDestructiveMigration()  // ❌ REMOVE: this wipes data
                .addMigrations(/* add when you bump version */)
                .build().also { INSTANCE = it }
        }
}
