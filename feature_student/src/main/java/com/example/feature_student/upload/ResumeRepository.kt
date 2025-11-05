//
//package com.example.feature_student.upload
//
//import android.content.Context
//import android.net.Uri
//import android.util.Log
//import com.example.feature_student.model.Resume
//import com.example.feature_student.model.ResumeStatus
//import com.example.feature_student.model.ATSAnalysisResult
//import com.example.feature_student.data.LocalResumeDatabase
//import com.example.feature_student.ats.ResumeParser
//import com.example.feature_student.ats.ATSCalculator
//import kotlinx.coroutines.Dispatchers
//import kotlinx.coroutines.delay
//import kotlinx.coroutines.withContext
//import java.io.File
//import java.io.FileOutputStream
//import java.util.UUID
//
//class ResumeRepository(private val context: Context) {
//
//    private val parser = ResumeParser()
//    private val huggingFaceAI = ATSCalculator(context)
//    private val TAG = "ResumeRepo"
//
//    suspend fun uploadResume(
//        ctx: Context,
//        uri: Uri,
//        fileName: String
//    ): Result<Resume> = withContext(Dispatchers.IO) {
//        try {
//            Log.d(TAG, "📤 Uploading resume: $fileName")
//
//            val resumeDir = File(ctx.filesDir, "resumes")
//            if (!resumeDir.exists()) {
//                resumeDir.mkdirs()
//            }
//
//            val fileExtension = fileName.substringAfterLast(".", "pdf")
//            val uniqueFileName = "${UUID.randomUUID()}.$fileExtension"
//            val destinationFile = File(resumeDir, uniqueFileName)
//
//            ctx.contentResolver.openInputStream(uri)?.use { input ->
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
//            LocalResumeDatabase.addResume(resume)
//            Log.d(TAG, "✅ Resume uploaded successfully")
//            Result.success(resume)
//        } catch (e: Exception) {
//            Log.e(TAG, "❌ Upload failed: ${e.message}")
//            Result.failure(e)
//        }
//    }
//
//    suspend fun analyzeResume(resume: Resume): Result<ATSAnalysisResult> = withContext(Dispatchers.IO) {
//        try {
//            Log.d(TAG, "🔄 Analyzing resume: ${resume.fileName}")
//
//            val analyzingResume = resume.copy(status = ResumeStatus.ANALYZING)
//            LocalResumeDatabase.updateResume(analyzingResume)
//
//            // Parse resume
//            val extractedText = parser.parseResume(resume.filePath)
//            delay(1500)
//
//            // Get AI analysis (with fallback)
//            val analysisResult = huggingFaceAI.analyzeResume(resume.id, extractedText)
//
//            // Update resume with score
//            val analyzedResume = resume.copy(
//                atsScore = analysisResult.overallScore,
//                status = ResumeStatus.ANALYZED
//            )
//
//            LocalResumeDatabase.updateResume(analyzedResume)
//            LocalResumeDatabase.saveAnalysisResult(analysisResult)
//
//            Log.d(TAG, "✅ Analysis complete - Score: ${analysisResult.overallScore}")
//            Result.success(analysisResult)
//        } catch (e: Exception) {
//            Log.e(TAG, "❌ Analysis failed: ${e.message}")
//            val errorResume = resume.copy(status = ResumeStatus.ERROR)
//            LocalResumeDatabase.updateResume(errorResume)
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
import android.util.Log
import com.example.feature_student.model.Resume
import com.example.feature_student.model.ResumeStatus
import com.example.feature_student.model.ATSAnalysisResult
import com.example.feature_student.data.LocalResumeDatabase
import com.example.feature_student.ats.ResumeParser
import com.example.feature_student.ats.ATSCalculator
import com.example.feature_student.database.AppDatabase
import com.example.feature_student.database.entity.ResumeEntity
import com.example.feature_student.database.entity.AnalysisResultEntity
import com.example.feature_student.database.entity.SuggestionEntity
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream
import java.util.UUID

class ResumeRepository(private val context: Context) {

    private val parser = ResumeParser()
    private val atsCalculator = ATSCalculator(context)
    private val db = AppDatabase.getInstance(context)
    private val resumeDao = db.resumeDao()
    private val analysisResultDao = db.analysisResultDao()
    private val suggestionDao = db.suggestionDao()
    private val gson = Gson()
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

            val resumeId = UUID.randomUUID().toString()
            val resumeEntity = ResumeEntity(
                id = resumeId,
                fileName = fileName,
                filePath = destinationFile.absolutePath,
                fileSize = destinationFile.length(),
                uploadedDate = System.currentTimeMillis(),
                status = ResumeStatus.UPLOADED.name
            )

            // Save to Room DB
            resumeDao.insertResume(resumeEntity)

            // Also update local cache
            val resume = Resume(
                id = resumeId,
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

            // Update status in DB
            val analyzingEntity = ResumeEntity(
                id = resume.id,
                fileName = resume.fileName,
                filePath = resume.filePath,
                fileSize = resume.fileSize,
                uploadedDate = resume.uploadedDate,
                status = ResumeStatus.ANALYZING.name
            )
            resumeDao.updateResume(analyzingEntity)

            // Parse resume
            val extractedText = parser.parseResume(resume.filePath)
            delay(1500)

            // Get AI analysis
            val analysisResult = atsCalculator.analyzeResume(resume.id, extractedText)

            // Save analysis result to Room DB
            val analysisEntity = AnalysisResultEntity(
                resumeId = resume.id,
                overallScore = analysisResult.overallScore,
                extractedText = analysisResult.extractedText,
                formatScore = analysisResult.breakdown.formatScore,
                keywordScore = analysisResult.breakdown.keywordScore,
                contentScore = analysisResult.breakdown.contentScore,
                structureScore = analysisResult.breakdown.structureScore,
                contactScore = analysisResult.breakdown.contactScore,
                foundKeywords = gson.toJson(analysisResult.keywords.foundKeywords),
                missingKeywords = gson.toJson(analysisResult.keywords.missingKeywords),
                keywordDensity = analysisResult.keywords.keywordDensity,
                hasContactInfo = analysisResult.sections.hasContactInfo,
                hasObjective = analysisResult.sections.hasObjective,
                hasExperience = analysisResult.sections.hasExperience,
                hasEducation = analysisResult.sections.hasEducation,
                hasSkills = analysisResult.sections.hasSkills,
                hasCertifications = analysisResult.sections.hasCertifications,
                strengths = gson.toJson(analysisResult.strengths),
                weaknesses = gson.toJson(analysisResult.weaknesses),
                detailedReport = analysisResult.detailedReport,
                analyzedBy = analysisResult.analyzedBy,
                analyzedAt = analysisResult.analyzedAt,
                suggestionsJson = gson.toJson(analysisResult.suggestions)
            )
            analysisResultDao.insertAnalysisResult(analysisEntity)

            // Save suggestions to DB (optional - for detailed queries)
            val suggestionEntities = analysisResult.suggestions.map { suggestion ->
                SuggestionEntity(
                    id = suggestion.id,
                    resumeId = resume.id,
                    category = suggestion.category.name,
                    title = suggestion.title,
                    description = suggestion.description,
                    priority = suggestion.priority.name,
                    isFixed = suggestion.isFixed
                )
            }
            suggestionDao.insertSuggestions(suggestionEntities)

            // Update resume with analyzed status
            val analyzedEntity = ResumeEntity(
                id = resume.id,
                fileName = resume.fileName,
                filePath = resume.filePath,
                fileSize = resume.fileSize,
                uploadedDate = resume.uploadedDate,
                atsScore = analysisResult.overallScore,
                status = ResumeStatus.ANALYZED.name
            )
            resumeDao.updateResume(analyzedEntity)

            // Update local cache
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

            val errorEntity = ResumeEntity(
                id = resume.id,
                fileName = resume.fileName,
                filePath = resume.filePath,
                fileSize = resume.fileSize,
                uploadedDate = resume.uploadedDate,
                status = ResumeStatus.ERROR.name
            )
            resumeDao.updateResume(errorEntity)

            val errorResume = resume.copy(status = ResumeStatus.ERROR)
            LocalResumeDatabase.updateResume(errorResume)

            Result.failure(e)
        }
    }

    // Load all resumes from DB
    suspend fun getAllResumes(): List<Resume> = withContext(Dispatchers.IO) {
        try {
            val entities = resumeDao.getAllResumes().collect { list ->
                list
            }
            emptyList() // Flow use case
        } catch (e: Exception) {
            Log.e(TAG, "Error loading resumes: ${e.message}")
            emptyList()
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