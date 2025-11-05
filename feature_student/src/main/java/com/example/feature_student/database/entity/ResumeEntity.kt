package com.example.feature_student.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "resumes")
data class ResumeEntity(
    @PrimaryKey val id: String,
    val fileName: String,
    val filePath: String,
    val fileSize: Long,
    val uploadedDate: Long,
    val atsScore: Int? = null,
    val status: String // "UPLOADED", "ANALYZING", "ANALYZED", "ERROR"
)