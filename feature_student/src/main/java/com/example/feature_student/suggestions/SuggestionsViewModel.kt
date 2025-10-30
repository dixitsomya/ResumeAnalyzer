//
//
//package com.example.feature_student.suggestions
//
//import androidx.lifecycle.ViewModel
//import androidx.lifecycle.viewModelScope
//import com.example.feature_student.data.LocalResumeDatabase
//import com.example.feature_student.model.Suggestion
//import com.example.feature_student.model.ATSAnalysisResult
//import kotlinx.coroutines.flow.MutableStateFlow
//import kotlinx.coroutines.flow.StateFlow
//import kotlinx.coroutines.flow.asStateFlow
//import kotlinx.coroutines.launch
//
//class SuggestionsViewModel : ViewModel() {
//
//    private val _suggestions = MutableStateFlow<List<Suggestion>>(emptyList())
//    val suggestions: StateFlow<List<Suggestion>> = _suggestions.asStateFlow()
//
//    private val _hasResume = MutableStateFlow(false)
//    val hasResume: StateFlow<Boolean> = _hasResume.asStateFlow()
//
//    private val _analysisResult = MutableStateFlow<ATSAnalysisResult?>(null)
//    val analysisResult: StateFlow<ATSAnalysisResult?> = _analysisResult.asStateFlow()
//
//    init {
//        loadSuggestions()
//    }
//
//    fun loadSuggestions() {
//        viewModelScope.launch {
//            // Check if there's a selected resume first
//            val selectedResumeId = LocalResumeDatabase.selectedResumeId.value
//
//            val result = if (selectedResumeId != null) {
//                LocalResumeDatabase.getAnalysisResult(selectedResumeId)
//            } else {
//                LocalResumeDatabase.getLatestAnalysisResult()
//            }
//
//            if (result != null) {
//                _hasResume.value = true
//                _analysisResult.value = result
//                _suggestions.value = result.suggestions
//            } else {
//                _hasResume.value = false
//                _analysisResult.value = null
//                _suggestions.value = emptyList()
//            }
//        }
//    }
//
//    fun toggleSuggestionFixed(suggestionId: String) {
//        val currentResult = _analysisResult.value ?: return
//        val resumeId = currentResult.resumeId
//
//        // Find the suggestion and toggle its state
//        val currentSuggestion = _suggestions.value.find { it.id == suggestionId }
//        val newFixedState = !(currentSuggestion?.isFixed ?: false)
//
//        // Update in database (persist across navigation)
//        LocalResumeDatabase.updateSuggestionFixedState(resumeId, suggestionId, newFixedState)
//
//        // Update local state
//        val updatedList = _suggestions.value.map { suggestion ->
//            if (suggestion.id == suggestionId) {
//                suggestion.copy(isFixed = newFixedState)
//            } else {
//                suggestion
//            }
//        }
//        _suggestions.value = updatedList
//
//        // Update analysis result
//        _analysisResult.value = currentResult.copy(suggestions = updatedList)
//    }
//
//    fun refresh() {
//        loadSuggestions()
//    }
//}


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
            // Check if there's a selected resume first
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
        }
    }

    fun toggleSuggestionFixed(suggestionId: String) {
        val currentResult = _analysisResult.value ?: return
        val resumeId = currentResult.resumeId

        // Find current state
        val currentSuggestion = _suggestions.value.find { it.id == suggestionId }
        val newFixedState = !(currentSuggestion?.isFixed ?: false)

        // Update in database (persist)
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
    }

    fun refresh() {
        loadSuggestions()
    }
}