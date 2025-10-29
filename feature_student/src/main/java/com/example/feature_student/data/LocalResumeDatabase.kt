package com.example.feature_student.data

import com.example.feature_student.model.Resume
import com.example.feature_student.model.ResumeStatus
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

object LocalResumeDatabase {
    private val _resumes = MutableStateFlow<List<Resume>>(emptyList())
    val resumes: StateFlow<List<Resume>> = _resumes.asStateFlow()

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

    fun clearAll() {
        _resumes.value = emptyList()
    }
}