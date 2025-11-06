package com.example.feature_recruiter.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "recruiter_resumes")
data class RecruiterResumeEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val resumeId: String,              // UUID for unique identification
    val fileName: String,
    val candidateName: String,
    val candidateEmail: String,
    val experience: Int,               // Years of experience
    val techStack: String,             // Comma-separated tech stack
    val rawText: String,               // Extracted text from PDF
    val uploadedDate: Long,            // Timestamp
    val recruiterEmail: String         // Link to recruiter
)