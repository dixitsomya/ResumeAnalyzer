package com.example.feature_recruiter.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "recruiter_profile")
data class RecruiterProfileEntity(
    @PrimaryKey val email: String,     // Primary key is email
    val companyName: String = "",
    val recruiterName: String = "",
    val role: String = "Recruiter",
    val setupCompleted: Boolean = false,
    val createdDate: Long = System.currentTimeMillis()
)