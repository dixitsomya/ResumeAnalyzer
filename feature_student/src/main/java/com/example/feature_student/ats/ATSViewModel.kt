package com.example.feature_student.ats

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.feature_student.data.LocalResumeDatabase
import com.example.feature_student.model.Resume
import com.example.feature_student.model.ATSAnalysisResult
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ATSViewModel : ViewModel() {

    private val _latestResume = MutableStateFlow<Resume?>(null)
    val latestResume: StateFlow<Resume?> = _latestResume.asStateFlow()

    private val _analysisResult = MutableStateFlow<ATSAnalysisResult?>(null)
    val analysisResult: StateFlow<ATSAnalysisResult?> = _analysisResult.asStateFlow()

    private val _analyzedCount = MutableStateFlow(0)
    val analyzedCount: StateFlow<Int> = _analyzedCount.asStateFlow()

    init {
        loadData()
    }

    fun loadData() {
        viewModelScope.launch {
            try {
                val selectedResumeId = LocalResumeDatabase.selectedResumeId.value

                if (selectedResumeId != null) {
                    val selectedResume = LocalResumeDatabase.getResumes()
                        .find { it.id == selectedResumeId }

                    if (selectedResume != null) {
                        _latestResume.value = selectedResume
                        // FIX: Now a suspend function
                        _analysisResult.value = LocalResumeDatabase.getAnalysisResult(selectedResumeId)
                    } else {
                        loadLatestResume()
                    }
                } else {
                    loadLatestResume()
                }

                _analyzedCount.value = LocalResumeDatabase.getTotalAnalyzed()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    private suspend fun loadLatestResume() {
        _latestResume.value = LocalResumeDatabase.getLatestResume()
        _analysisResult.value = LocalResumeDatabase.getLatestAnalysisResult()
    }

    fun refresh() {
        loadData()
    }
}