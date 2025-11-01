
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
            _resumes.value = LocalResumeDatabase.getAnalyzedResumes()
            _averageScore.value = LocalResumeDatabase.getAverageScore()
        }
    }

    fun refresh() {
        loadHistory()
    }
}