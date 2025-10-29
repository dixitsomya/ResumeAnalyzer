package com.example.feature_student.model

data class Resume(
    val id: String = "",
    val fileName: String = "",
    val filePath: String = "",
    val fileSize: Long = 0,
    val uploadedDate: Long = System.currentTimeMillis(),
    val atsScore: Int? = null,
    val status: ResumeStatus = ResumeStatus.UPLOADED
)

enum class ResumeStatus {
    UPLOADING,
    UPLOADED,
    ANALYZING,
    ANALYZED,
    ERROR
}