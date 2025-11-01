
package com.example.feature_student.upload

import android.content.Context
import android.net.Uri
import android.util.Log
import com.example.feature_student.model.Resume
import com.example.feature_student.model.ResumeStatus
import com.example.feature_student.model.ATSAnalysisResult
import com.example.feature_student.data.LocalResumeDatabase
import com.example.feature_student.ats.ResumeParser
import com.example.feature_student.ats.ATSCalculator
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream
import java.util.UUID

class ResumeRepository(private val context: Context) {

    private val parser = ResumeParser()
    private val huggingFaceAI = ATSCalculator(context)
    private val TAG = "ResumeRepo"

    suspend fun uploadResume(
        ctx: Context,
        uri: Uri,
        fileName: String
    ): Result<Resume> = withContext(Dispatchers.IO) {
        try {
            Log.d(TAG, "📤 Uploading resume: $fileName")

            val resumeDir = File(ctx.filesDir, "resumes")
            if (!resumeDir.exists()) {
                resumeDir.mkdirs()
            }

            val fileExtension = fileName.substringAfterLast(".", "pdf")
            val uniqueFileName = "${UUID.randomUUID()}.$fileExtension"
            val destinationFile = File(resumeDir, uniqueFileName)

            ctx.contentResolver.openInputStream(uri)?.use { input ->
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

            LocalResumeDatabase.addResume(resume)
            Log.d(TAG, "✅ Resume uploaded successfully")
            Result.success(resume)
        } catch (e: Exception) {
            Log.e(TAG, "❌ Upload failed: ${e.message}")
            Result.failure(e)
        }
    }

    suspend fun analyzeResume(resume: Resume): Result<ATSAnalysisResult> = withContext(Dispatchers.IO) {
        try {
            Log.d(TAG, "🔄 Analyzing resume: ${resume.fileName}")

            val analyzingResume = resume.copy(status = ResumeStatus.ANALYZING)
            LocalResumeDatabase.updateResume(analyzingResume)

            // Parse resume
            val extractedText = parser.parseResume(resume.filePath)
            delay(1500)

            // Get AI analysis (with fallback)
            val analysisResult = huggingFaceAI.analyzeResume(resume.id, extractedText)

            // Update resume with score
            val analyzedResume = resume.copy(
                atsScore = analysisResult.overallScore,
                status = ResumeStatus.ANALYZED
            )

            LocalResumeDatabase.updateResume(analyzedResume)
            LocalResumeDatabase.saveAnalysisResult(analysisResult)

            Log.d(TAG, "✅ Analysis complete - Score: ${analysisResult.overallScore}")
            Result.success(analysisResult)
        } catch (e: Exception) {
            Log.e(TAG, "❌ Analysis failed: ${e.message}")
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