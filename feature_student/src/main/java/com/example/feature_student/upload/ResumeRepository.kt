
//package com.example.feature_student.upload
//
//import android.content.Context
//import android.net.Uri
//import androidx.core.net.toFile
//import com.example.feature_student.model.Resume
//import com.example.feature_student.model.ResumeStatus
//import com.example.feature_student.data.LocalResumeDatabase
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
//            val resumeDir = File(context.filesDir, "resumes")
//            if (!resumeDir.exists()) {
//                resumeDir.mkdirs()
//            }
//
//            val fileExtension = fileName.substringAfterLast(".", "pdf")
//            val uniqueFileName = "${UUID.randomUUID()}.$fileExtension"
//            val destinationFile = File(resumeDir, uniqueFileName)
//
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
//                uploadedDate = System.currentTimeMillis(),
//                status = ResumeStatus.UPLOADED
//            )
//
//            // ✅ FIXED: Add resume to database immediately after upload
//            LocalResumeDatabase.addResume(resume)
//
//            Result.success(resume)
//        } catch (e: Exception) {
//            Result.failure(e)
//        }
//    }
//
//    suspend fun analyzeResume(resume: Resume): Result<Resume> = withContext(Dispatchers.IO) {
//        try {
//            delay(2000)
//
//            val atsScore = (60..95).random()
//
//            val analyzedResume = resume.copy(
//                atsScore = atsScore,
//                status = ResumeStatus.ANALYZED
//            )
//
//            // FIXED: Update resume in database with ATS score
//            LocalResumeDatabase.updateResume(analyzedResume)
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
import com.example.feature_student.model.Resume
import com.example.feature_student.model.ResumeStatus
import com.example.feature_student.model.ATSAnalysisResult
import com.example.feature_student.data.LocalResumeDatabase
import com.example.feature_student.ats.ResumeParser
import com.example.feature_student.ats.ATSScorer
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream
import java.util.UUID

class ResumeRepository {

    private val parser = ResumeParser()
    private val scorer = ATSScorer()

    suspend fun uploadResume(
        context: Context,
        uri: Uri,
        fileName: String
    ): Result<Resume> = withContext(Dispatchers.IO) {
        try {
            // Create app directory for resumes
            val resumeDir = File(context.filesDir, "resumes")
            if (!resumeDir.exists()) {
                resumeDir.mkdirs()
            }

            // Generate unique file name
            val fileExtension = fileName.substringAfterLast(".", "pdf")
            val uniqueFileName = "${UUID.randomUUID()}.$fileExtension"
            val destinationFile = File(resumeDir, uniqueFileName)

            // Copy file to internal storage
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

            // Save to local database
            LocalResumeDatabase.addResume(resume)

            Result.success(resume)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun analyzeResume(resume: Resume): Result<ATSAnalysisResult> = withContext(Dispatchers.IO) {
        try {
            // Update status to analyzing
            val analyzingResume = resume.copy(status = ResumeStatus.ANALYZING)
            LocalResumeDatabase.updateResume(analyzingResume)

            // Simulate processing time (for UX)
            delay(1500)

            // Step 1: Parse resume text from PDF/DOC
            val extractedText = parser.parseResume(resume.filePath)

            // Step 2: Analyze with ATS Scorer (FREE - No API)
            val analysisResult = scorer.analyzeResume(resume.id, extractedText)

            // Step 3: Update resume with ATS score
            val analyzedResume = resume.copy(
                atsScore = analysisResult.overallScore,
                status = ResumeStatus.ANALYZED
            )

            // Step 4: Save to database
            LocalResumeDatabase.updateResume(analyzedResume)
            LocalResumeDatabase.saveAnalysisResult(analysisResult)

            Result.success(analysisResult)
        } catch (e: Exception) {
            // Update status to error
            val errorResume = resume.copy(status = ResumeStatus.ERROR)
            LocalResumeDatabase.updateResume(errorResume)

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