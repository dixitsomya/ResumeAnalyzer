//package com.example.feature_student.upload
//
//import android.content.Context
//import android.net.Uri
//import androidx.core.net.toFile
//import com.example.feature_student.model.Resume
//import com.example.feature_student.model.ResumeStatus
//import kotlinx.coroutines.Dispatchers
//import kotlinx.coroutines.delay
//import kotlinx.coroutines.withContext
//import java.io.File
//import java.io.FileOutputStream
//import java.util.UUID
//
//class ResumeRepository {
//
//    suspend fun uploadResume(
//        context: Context,
//        uri: Uri,
//        fileName: String
//    ): Result<Resume> = withContext(Dispatchers.IO) {
//        try {
//            // Create app directory for resumes
//            val resumeDir = File(context.filesDir, "resumes")
//            if (!resumeDir.exists()) {
//                resumeDir.mkdirs()
//            }
//
//            // Generate unique file name
//            val fileExtension = fileName.substringAfterLast(".", "pdf")
//            val uniqueFileName = "${UUID.randomUUID()}.$fileExtension"
//            val destinationFile = File(resumeDir, uniqueFileName)
//
//            // Copy file to internal storage
//            context.contentResolver.openInputStream(uri)?.use { input ->
//                FileOutputStream(destinationFile).use { output ->
//                    input.copyTo(output)
//                }
//            }
//
//            val resume = Resume(
//                id = UUID.randomUUID().toString(),
//                fileName = fileName,
//                filePath = destinationFile.absolutePath,
//                fileSize = destinationFile.length(),
//                status = ResumeStatus.UPLOADED
//            )
//
//            // Save to local database (add Room DB later)
//            // For now, just return the resume object
//
//            Result.success(resume)
//        } catch (e: Exception) {
//            Result.failure(e)
//        }
//    }
//
//    suspend fun analyzeResume(resume: Resume): Result<Resume> = withContext(Dispatchers.IO) {
//        try {
//            // Simulate API call for ATS analysis
//            delay(2000)
//
//            // Mock ATS score (replace with actual API call)
//            val atsScore = (60..95).random()
//
//            val analyzedResume = resume.copy(
//                atsScore = atsScore,
//                status = ResumeStatus.ANALYZED
//            )
//
//            Result.success(analyzedResume)
//        } catch (e: Exception) {
//            Result.failure(e)
//        }
//    }
//
//    fun getFileSize(sizeInBytes: Long): String {
//        return when {
//            sizeInBytes < 1024 -> "$sizeInBytes B"
//            sizeInBytes < 1024 * 1024 -> "${sizeInBytes / 1024} KB"
//            else -> String.format("%.2f MB", sizeInBytes / (1024.0 * 1024.0))
//        }
//    }
//}


package com.example.feature_student.upload

import android.content.Context
import android.net.Uri
import androidx.core.net.toFile
import com.example.feature_student.model.Resume
import com.example.feature_student.model.ResumeStatus
import com.example.feature_student.data.LocalResumeDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream
import java.util.UUID

class ResumeRepository {

    suspend fun uploadResume(
        context: Context,
        uri: Uri,
        fileName: String
    ): Result<Resume> = withContext(Dispatchers.IO) {
        try {
            val resumeDir = File(context.filesDir, "resumes")
            if (!resumeDir.exists()) {
                resumeDir.mkdirs()
            }

            val fileExtension = fileName.substringAfterLast(".", "pdf")
            val uniqueFileName = "${UUID.randomUUID()}.$fileExtension"
            val destinationFile = File(resumeDir, uniqueFileName)

            context.contentResolver.openInputStream(uri)?.use { input ->
                FileOutputStream(destinationFile).use { output ->
                    input.copyTo(output)
                }
            }

            val resume = Resume(
                id = UUID.randomUUID().toString(),
                fileName = fileName,
                filePath = destinationFile.absolutePath,
                fileSize = destinationFile.length(),
                uploadedDate = System.currentTimeMillis(),
                status = ResumeStatus.UPLOADED
            )

            // ✅ FIXED: Add resume to database immediately after upload
            LocalResumeDatabase.addResume(resume)

            Result.success(resume)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun analyzeResume(resume: Resume): Result<Resume> = withContext(Dispatchers.IO) {
        try {
            delay(2000)

            val atsScore = (60..95).random()

            val analyzedResume = resume.copy(
                atsScore = atsScore,
                status = ResumeStatus.ANALYZED
            )

            // FIXED: Update resume in database with ATS score
            LocalResumeDatabase.updateResume(analyzedResume)

            Result.success(analyzedResume)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    fun getFileSize(sizeInBytes: Long): String {
        return when {
            sizeInBytes < 1024 -> "$sizeInBytes B"
            sizeInBytes < 1024 * 1024 -> "${sizeInBytes / 1024} KB"
            else -> String.format("%.2f MB", sizeInBytes / (1024.0 * 1024.0))
        }
    }
}