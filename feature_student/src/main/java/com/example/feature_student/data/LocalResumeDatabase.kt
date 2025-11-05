
package com.example.feature_student.data

import android.content.Context
import android.util.Log
import com.example.feature_student.model.Resume
import com.example.feature_student.model.ResumeStatus
import com.example.feature_student.model.ATSAnalysisResult
import com.example.feature_student.database.AppDatabase
import com.example.feature_student.database.entity.ResumeEntity
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.withContext

object LocalResumeDatabase {
    private const val MAX_RESUMES = 10
    private const val TAG = "LocalResumeDB"

    private lateinit var context: Context
    private lateinit var db: AppDatabase
    private val gson = Gson()

    // In-memory cache
    private val _resumes = MutableStateFlow<List<Resume>>(emptyList())
    val resumes: StateFlow<List<Resume>> = _resumes.asStateFlow()

    private val _analysisResults = MutableStateFlow<Map<String, ATSAnalysisResult>>(emptyMap())
    val analysisResults: StateFlow<Map<String, ATSAnalysisResult>> = _analysisResults.asStateFlow()

    private val _selectedResumeId = MutableStateFlow<String?>(null)
    val selectedResumeId: StateFlow<String?> = _selectedResumeId.asStateFlow()

    fun init(appContext: Context) {
        context = appContext
        db = AppDatabase.getInstance(appContext)
        Log.d(TAG, "Database initialized")
    }

    // ✅ Load from database on IO thread, not main thread
    private suspend fun loadFromDatabaseAsync() {
        withContext(Dispatchers.IO) {
            try {
                Log.d(TAG, "Loading resumes from database...")
                val resumeList = db.resumeDao().getAllResumesSync() // Use non-Flow version

                val resumes = resumeList.map { entity ->
                    Resume(
                        id = entity.id,
                        fileName = entity.fileName,
                        filePath = entity.filePath,
                        fileSize = entity.fileSize,
                        uploadedDate = entity.uploadedDate,
                        atsScore = entity.atsScore,
                        status = ResumeStatus.valueOf(entity.status)
                    )
                }
                _resumes.value = resumes
                Log.d(TAG, "Loaded ${resumes.size} resumes")

                // ✅ Also load all analysis results from database
                loadAnalysisResultsFromDB()
            } catch (e: Exception) {
                Log.e(TAG, "Error loading resumes: ${e.message}", e)
            }
        }
    }

    // ✅ NEW: Load all analysis results from database
    private suspend fun loadAnalysisResultsFromDB() {
        withContext(Dispatchers.IO) {
            try {
                val resumeIds = _resumes.value.map { it.id }
                val resultsMap = mutableMapOf<String, ATSAnalysisResult>()

                for (resumeId in resumeIds) {
                    val entity = db.analysisResultDao().getAnalysisResultSync(resumeId)
                    if (entity != null) {
                        val result = convertEntityToResult(entity)
                        resultsMap[resumeId] = result
                    }
                }

                _analysisResults.value = resultsMap
                Log.d(TAG, "Loaded ${resultsMap.size} analysis results")
            } catch (e: Exception) {
                Log.e(TAG, "Error loading analysis results: ${e.message}", e)
            }
        }
    }

    // ✅ NEW: Convert database entity to model
    private fun convertEntityToResult(entity: com.example.feature_student.database.entity.AnalysisResultEntity): ATSAnalysisResult {
        return ATSAnalysisResult(
            resumeId = entity.resumeId,
            overallScore = entity.overallScore,
            extractedText = entity.extractedText,
            breakdown = com.example.feature_student.model.ScoreBreakdown(
                formatScore = entity.formatScore,
                keywordScore = entity.keywordScore,
                contentScore = entity.contentScore,
                structureScore = entity.structureScore,
                contactScore = entity.contactScore
            ),
            keywords = com.example.feature_student.model.KeywordAnalysis(
                foundKeywords = gson.fromJson(entity.foundKeywords, Array<String>::class.java).toList(),
                missingKeywords = gson.fromJson(entity.missingKeywords, Array<String>::class.java).toList(),
                keywordDensity = entity.keywordDensity
            ),
            sections = com.example.feature_student.model.SectionAnalysis(
                hasContactInfo = entity.hasContactInfo,
                hasObjective = entity.hasObjective,
                hasExperience = entity.hasExperience,
                hasEducation = entity.hasEducation,
                hasSkills = entity.hasSkills,
                hasCertifications = entity.hasCertifications
            ),
            strengths = gson.fromJson(entity.strengths, Array<String>::class.java).toList(),
            weaknesses = gson.fromJson(entity.weaknesses, Array<String>::class.java).toList(),
            detailedReport = entity.detailedReport,
            analyzedBy = entity.analyzedBy,
            analyzedAt = entity.analyzedAt,
            suggestions = gson.fromJson(entity.suggestionsJson, Array<com.example.feature_student.model.Suggestion>::class.java).toList()
        )
    }

    // ✅ FIX: Add resume on background thread
    suspend fun addResume(resume: Resume) {
        withContext(Dispatchers.IO) {
            try {
                val currentList = _resumes.value.toMutableList()
                currentList.add(0, resume)

                // ✅ FIXED: Only delete if exceeds limit AND is ANALYZED
                if (currentList.size > MAX_RESUMES) {
                    // Find oldest ANALYZED resume to delete
                    val oldestAnalyzed = currentList
                        .filter { it.status == ResumeStatus.ANALYZED }
                        .minByOrNull { it.uploadedDate }

                    if (oldestAnalyzed != null) {
                        currentList.remove(oldestAnalyzed)
                        deleteResumeFromDBAsync(oldestAnalyzed.id)
                        Log.d(TAG, "Deleted oldest analyzed resume: ${oldestAnalyzed.id}")
                    }
                }

                _resumes.value = currentList

                val entity = ResumeEntity(
                    id = resume.id,
                    fileName = resume.fileName,
                    filePath = resume.filePath,
                    fileSize = resume.fileSize,
                    uploadedDate = resume.uploadedDate,
                    atsScore = resume.atsScore,
                    status = resume.status.name
                )
                db.resumeDao().insertResume(entity)
                Log.d(TAG, "Resume added: ${resume.fileName}")
            } catch (e: Exception) {
                Log.e(TAG, "Error adding resume: ${e.message}", e)
            }
        }
    }

    // ✅ FIX: Update resume on background thread
    suspend fun updateResume(resume: Resume) {
        withContext(Dispatchers.IO) {
            try {
                val currentList = _resumes.value.toMutableList()
                val index = currentList.indexOfFirst { it.id == resume.id }
                if (index != -1) {
                    currentList[index] = resume
                    _resumes.value = currentList
                }

                val entity = ResumeEntity(
                    id = resume.id,
                    fileName = resume.fileName,
                    filePath = resume.filePath,
                    fileSize = resume.fileSize,
                    uploadedDate = resume.uploadedDate,
                    atsScore = resume.atsScore,
                    status = resume.status.name
                )
                db.resumeDao().updateResume(entity)
                Log.d(TAG, "Resume updated: ${resume.fileName}")
            } catch (e: Exception) {
                Log.e(TAG, "Error updating resume: ${e.message}", e)
            }
        }
    }

    private suspend fun deleteResumeFromDBAsync(resumeId: String) {
        withContext(Dispatchers.IO) {
            try {
                val resume = db.resumeDao().getResumeById(resumeId)
                if (resume != null) {
                    db.resumeDao().deleteResume(resume)
                    db.analysisResultDao().deleteAnalysisResult(resumeId)
                    db.suggestionDao().deleteSuggestionsByResumeId(resumeId)

                    // Remove from memory cache
                    val updatedMap = _analysisResults.value.toMutableMap()
                    updatedMap.remove(resumeId)
                    _analysisResults.value = updatedMap

                    Log.d(TAG, "Deleted old resume: $resumeId")
                }
            } catch (e: Exception) {
                Log.e(TAG, "Error deleting resume: ${e.message}", e)
            }
        }
    }

    // ✅ FIX: Save analysis on background thread
    suspend fun saveAnalysisResult(result: ATSAnalysisResult) {
        withContext(Dispatchers.IO) {
            try {
                val currentMap = _analysisResults.value.toMutableMap()
                currentMap[result.resumeId] = result
                _analysisResults.value = currentMap

                val entity = com.example.feature_student.database.entity.AnalysisResultEntity(
                    resumeId = result.resumeId,
                    overallScore = result.overallScore,
                    extractedText = result.extractedText,
                    formatScore = result.breakdown.formatScore,
                    keywordScore = result.breakdown.keywordScore,
                    contentScore = result.breakdown.contentScore,
                    structureScore = result.breakdown.structureScore,
                    contactScore = result.breakdown.contactScore,
                    foundKeywords = gson.toJson(result.keywords.foundKeywords),
                    missingKeywords = gson.toJson(result.keywords.missingKeywords),
                    keywordDensity = result.keywords.keywordDensity,
                    hasContactInfo = result.sections.hasContactInfo,
                    hasObjective = result.sections.hasObjective,
                    hasExperience = result.sections.hasExperience,
                    hasEducation = result.sections.hasEducation,
                    hasSkills = result.sections.hasSkills,
                    hasCertifications = result.sections.hasCertifications,
                    strengths = gson.toJson(result.strengths),
                    weaknesses = gson.toJson(result.weaknesses),
                    detailedReport = result.detailedReport,
                    analyzedBy = result.analyzedBy,
                    analyzedAt = result.analyzedAt,
                    suggestionsJson = gson.toJson(result.suggestions)
                )
                db.analysisResultDao().insertAnalysisResult(entity)
                Log.d(TAG, "Analysis saved for resume: ${result.resumeId}")
            } catch (e: Exception) {
                Log.e(TAG, "Error saving analysis: ${e.message}", e)
            }
        }
    }

    // ✅ FIX: Get analysis with database fallback
    suspend fun getAnalysisResult(resumeId: String): ATSAnalysisResult? {
        return withContext(Dispatchers.IO) {
            try {
                // First check memory cache
                val cached = _analysisResults.value[resumeId]
                if (cached != null) {
                    Log.d(TAG, "Analysis found in memory cache: $resumeId")
                    return@withContext cached
                }

                // If not in cache, load from database
                Log.d(TAG, "Loading analysis from database: $resumeId")
                val entity = db.analysisResultDao().getAnalysisResultSync(resumeId)
                if (entity != null) {
                    val result = convertEntityToResult(entity)
                    // Cache it
                    val updatedMap = _analysisResults.value.toMutableMap()
                    updatedMap[resumeId] = result
                    _analysisResults.value = updatedMap
                    return@withContext result
                }
                null
            } catch (e: Exception) {
                Log.e(TAG, "Error getting analysis: ${e.message}", e)
                null
            }
        }
    }

    suspend fun updateSuggestionFixedState(resumeId: String, suggestionId: String, isFixed: Boolean) {
        withContext(Dispatchers.IO) {
            try {
                val currentMap = _analysisResults.value.toMutableMap()
                val result = currentMap[resumeId] ?: return@withContext

                val updatedSuggestions = result.suggestions.map { suggestion ->
                    if (suggestion.id == suggestionId) {
                        suggestion.copy(isFixed = isFixed)
                    } else {
                        suggestion
                    }
                }

                val updatedResult = result.copy(suggestions = updatedSuggestions)
                currentMap[resumeId] = updatedResult
                _analysisResults.value = currentMap

                db.suggestionDao().updateSuggestion(
                    com.example.feature_student.database.entity.SuggestionEntity(
                        id = suggestionId,
                        resumeId = resumeId,
                        category = "",
                        title = "",
                        description = "",
                        priority = "",
                        isFixed = isFixed
                    )
                )
                Log.d(TAG, "Suggestion updated: $suggestionId")
            } catch (e: Exception) {
                Log.e(TAG, "Error updating suggestion: ${e.message}", e)
            }
        }
    }

    fun setSelectedResume(resumeId: String?) {
        _selectedResumeId.value = resumeId
        Log.d(TAG, "Selected resume: $resumeId")
    }

    fun getSelectedResume(): Resume? {
        val id = _selectedResumeId.value ?: return null
        return _resumes.value.find { it.id == id }
    }

    fun getResumes(): List<Resume> = _resumes.value

    suspend fun getAnalyzedResumes(): List<Resume> {
        loadFromDatabaseAsync() // Ensure data is loaded
        return _resumes.value.filter { it.status == ResumeStatus.ANALYZED }
    }

    suspend fun getAverageScore(): Double {
        return withContext(Dispatchers.IO) {
            try {
                val analyzedResumes = _resumes.value.filter { it.status == ResumeStatus.ANALYZED }
                if (analyzedResumes.isEmpty()) return@withContext 0.0
                val totalScore = analyzedResumes.mapNotNull { it.atsScore }.sum()
                totalScore.toDouble() / analyzedResumes.size
            } catch (e: Exception) {
                Log.e(TAG, "Error calculating average: ${e.message}", e)
                0.0
            }
        }
    }

    fun getTotalAnalyzed(): Int = _resumes.value.filter { it.status == ResumeStatus.ANALYZED }.size

    suspend fun getLatestResume(): Resume? {
        loadFromDatabaseAsync()
        return _resumes.value.filter { it.status == ResumeStatus.ANALYZED }.maxByOrNull { it.uploadedDate }
    }

    suspend fun getLatestAnalysisResult(): ATSAnalysisResult? {
        val latestResume = getLatestResume() ?: return null
        return getAnalysisResult(latestResume.id)
    }

    suspend fun deleteResumeFromDB(resumeId: String) {
        withContext(Dispatchers.IO) {
            try {
                // Remove from memory cache
                val updatedResumeList = _resumes.value.toMutableList()
                updatedResumeList.removeAll { it.id == resumeId }
                _resumes.value = updatedResumeList

                val updatedAnalysisMap = _analysisResults.value.toMutableMap()
                updatedAnalysisMap.remove(resumeId)
                _analysisResults.value = updatedAnalysisMap

                // Delete from database
                val resume = db.resumeDao().getResumeById(resumeId)
                if (resume != null) {
                    db.resumeDao().deleteResume(resume)
                    db.analysisResultDao().deleteAnalysisResult(resumeId)
                    db.suggestionDao().deleteSuggestionsByResumeId(resumeId)
                    Log.d(TAG, "Resume deleted: $resumeId")
                }
            } catch (e: Exception) {
                Log.e(TAG, "Error deleting resume: ${e.message}", e)
            }
        }
    }

    suspend fun clearAll() {
        withContext(Dispatchers.IO) {
            try {
                _resumes.value = emptyList()
                _analysisResults.value = emptyMap()
                _selectedResumeId.value = null

                db.resumeDao().deleteAll()
                db.analysisResultDao().deleteAll()
                db.suggestionDao().deleteAll()
                Log.d(TAG, "All data cleared")
            } catch (e: Exception) {
                Log.e(TAG, "Error clearing data: ${e.message}", e)
            }
        }
    }
}