package com.example.feature_student.data

import com.example.feature_student.model.Resume
import com.example.feature_student.model.ResumeStatus
import com.example.feature_student.model.ATSAnalysisResult
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

object LocalResumeDatabase {
    private val _resumes = MutableStateFlow<List<Resume>>(emptyList())
    val resumes: StateFlow<List<Resume>> = _resumes.asStateFlow()

    // Store analysis results separately
    private val _analysisResults = MutableStateFlow<Map<String, ATSAnalysisResult>>(emptyMap())
    val analysisResults: StateFlow<Map<String, ATSAnalysisResult>> = _analysisResults.asStateFlow()

    // Store currently selected resume for navigation
    private val _selectedResumeId = MutableStateFlow<String?>(null)
    val selectedResumeId: StateFlow<String?> = _selectedResumeId.asStateFlow()

    fun addResume(resume: Resume) {
        val currentList = _resumes.value.toMutableList()
        currentList.add(0, resume) // Add at beginning
        _resumes.value = currentList
    }

    fun updateResume(resume: Resume) {
        val currentList = _resumes.value.toMutableList()
        val index = currentList.indexOfFirst { it.id == resume.id }
        if (index != -1) {
            currentList[index] = resume
            _resumes.value = currentList
        }
    }

    fun saveAnalysisResult(result: ATSAnalysisResult) {
        val currentMap = _analysisResults.value.toMutableMap()
        currentMap[result.resumeId] = result
        _analysisResults.value = currentMap
    }

    fun getAnalysisResult(resumeId: String): ATSAnalysisResult? {
        return _analysisResults.value[resumeId]
    }

    // NEW: Update suggestion fixed state
    fun updateSuggestionFixedState(resumeId: String, suggestionId: String, isFixed: Boolean) {
        val currentMap = _analysisResults.value.toMutableMap()
        val result = currentMap[resumeId] ?: return

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
    }

    fun setSelectedResume(resumeId: String?) {
        _selectedResumeId.value = resumeId
    }

    fun getSelectedResume(): Resume? {
        val id = _selectedResumeId.value ?: return null
        return _resumes.value.find { it.id == id }
    }

    fun getResumes(): List<Resume> {
        return _resumes.value
    }

    fun getAnalyzedResumes(): List<Resume> {
        return _resumes.value.filter { it.status == ResumeStatus.ANALYZED }
    }

    fun getAverageScore(): Double {
        val analyzedResumes = getAnalyzedResumes()
        if (analyzedResumes.isEmpty()) return 0.0

        val totalScore = analyzedResumes.mapNotNull { it.atsScore }.sum()
        return totalScore.toDouble() / analyzedResumes.size
    }

    fun getTotalAnalyzed(): Int {
        return getAnalyzedResumes().size
    }

    fun getLatestResume(): Resume? {
        return getAnalyzedResumes().maxByOrNull { it.uploadedDate }
    }

    fun getLatestAnalysisResult(): ATSAnalysisResult? {
        val latestResume = getLatestResume() ?: return null
        return getAnalysisResult(latestResume.id)
    }

    fun clearAll() {
        _resumes.value = emptyList()
        _analysisResults.value = emptyMap()
        _selectedResumeId.value = null
    }
}