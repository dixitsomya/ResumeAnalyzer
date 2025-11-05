
package com.example.feature_student.suggestions

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.feature_student.data.LocalResumeDatabase
import com.example.feature_student.model.Suggestion
import com.example.feature_student.model.ATSAnalysisResult
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class SuggestionsViewModel : ViewModel() {

    private val _suggestions = MutableStateFlow<List<Suggestion>>(emptyList())
    val suggestions: StateFlow<List<Suggestion>> = _suggestions.asStateFlow()

    private val _hasResume = MutableStateFlow(false)
    val hasResume: StateFlow<Boolean> = _hasResume.asStateFlow()

    private val _analysisResult = MutableStateFlow<ATSAnalysisResult?>(null)
    val analysisResult: StateFlow<ATSAnalysisResult?> = _analysisResult.asStateFlow()

    init {
        loadSuggestions()
    }

    fun loadSuggestions() {
        viewModelScope.launch {
            try {
                val selectedResumeId = LocalResumeDatabase.selectedResumeId.value

                val result = if (selectedResumeId != null) {
                    LocalResumeDatabase.getAnalysisResult(selectedResumeId)
                } else {
                    LocalResumeDatabase.getLatestAnalysisResult()
                }

                if (result != null) {
                    _hasResume.value = true
                    _analysisResult.value = result
                    _suggestions.value = result.suggestions
                } else {
                    _hasResume.value = false
                    _analysisResult.value = null
                    _suggestions.value = emptyList()
                }
            } catch (e: Exception) {
                e.printStackTrace()
                _hasResume.value = false
                _analysisResult.value = null
                _suggestions.value = emptyList()
            }
        }
    }

    // FIXED: Properly toggle and persist suggestion state
    fun toggleSuggestionFixed(suggestionId: String) {
        viewModelScope.launch {
            try {
                val currentResult = _analysisResult.value ?: return@launch
                val resumeId = currentResult.resumeId

                // Find current state
                val currentSuggestion = _suggestions.value.find { it.id == suggestionId }
                val newFixedState = !(currentSuggestion?.isFixed ?: false)

                // Update in database - this will persist the change
                LocalResumeDatabase.updateSuggestionFixedState(resumeId, suggestionId, newFixedState)

                // Update local UI state
                val updatedList = _suggestions.value.map { suggestion ->
                    if (suggestion.id == suggestionId) {
                        suggestion.copy(isFixed = newFixedState)
                    } else {
                        suggestion
                    }
                }
                _suggestions.value = updatedList

                // Update the analysis result in memory
                val updatedResult = currentResult.copy(suggestions = updatedList)
                _analysisResult.value = updatedResult

                // Save updated analysis to database
                LocalResumeDatabase.saveAnalysisResult(updatedResult)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun refresh() {
        loadSuggestions()
    }
}