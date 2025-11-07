//package com.example.feature_recruiter.database.entity
//
//import androidx.room.Entity
//import androidx.room.PrimaryKey
//
//@Entity(tableName = "recruiter_profile")
//data class RecruiterProfileEntity(
//    @PrimaryKey val email: String,     // Primary key is email
//    val companyName: String = "",
//    val recruiterName: String = "",
//    val role: String = "Recruiter",
//    val setupCompleted: Boolean = false,
//    val createdDate: Long = System.currentTimeMillis()
//)

package com.example.feature_recruiter.database.entity

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
//
//@Entity(tableName = "recruiter_profile")
//data class RecruiterProfileEntity(
//    @PrimaryKey val email: String,     // Primary key is email
//    val companyName: String = "",      // Empty if not set
//    val recruiterName: String = "",    // Empty if not set
//    val role: String = "Recruiter",
//    val setupCompleted: Boolean = false, // ✅ KEY: Track if onboarded
//    val createdDate: Long = System.currentTimeMillis()
//)

// entity/RecruiterProfileEntity.kt
@Entity(
    tableName = "recruiter_profile",
    indices = [Index(value = ["email"], unique = true)]
)
data class RecruiterProfileEntity(
    @PrimaryKey val email: String,
    val recruiterName: String,
    val companyName: String,
    val setupCompleted: Boolean = false,
    val createdAt: Long = System.currentTimeMillis()
)

