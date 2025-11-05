
package com.example.feature_student.history

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.feature_student.data.LocalResumeDatabase
import com.example.feature_student.model.Resume
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class HistoryViewModel : ViewModel() {

    private val _resumes = MutableStateFlow<List<Resume>>(emptyList())
    val resumes: StateFlow<List<Resume>> = _resumes.asStateFlow()

    private val _averageScore = MutableStateFlow(0.0)
    val averageScore: StateFlow<Double> = _averageScore.asStateFlow()

    init {
        loadHistory()
    }

    private fun loadHistory() {
        viewModelScope.launch {
            try {
                _resumes.value = LocalResumeDatabase.getAnalyzedResumes()
                _averageScore.value = LocalResumeDatabase.getAverageScore()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    // Delete single resume by removing from list and updating DB
    fun deleteResume(resumeId: String) {
        viewModelScope.launch {
            try {
                // Remove from memory cache
                val updatedList = _resumes.value.toMutableList()
                updatedList.removeAll { it.id == resumeId }
                _resumes.value = updatedList

                // Delete from database
                LocalResumeDatabase.deleteResumeFromDB(resumeId)

                // Refresh list
                _resumes.value = LocalResumeDatabase.getAnalyzedResumes()
                _averageScore.value = LocalResumeDatabase.getAverageScore()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    // Delete all resumes using existing clearAll function
    fun deleteAllResumes() {
        viewModelScope.launch {
            try {
                LocalResumeDatabase.clearAll()
                _resumes.value = emptyList()
                _averageScore.value = 0.0
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun refresh() {
        loadHistory()
    }
}