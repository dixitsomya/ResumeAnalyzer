

// ============ SuggestionsViewModel.kt ============
//package com.example.feature_student.suggestions
//
//import androidx.lifecycle.ViewModel
//import com.example.feature_student.data.LocalResumeDatabase
//import com.example.feature_student.model.Suggestion
//import com.example.feature_student.model.SuggestionGenerator
//import kotlinx.coroutines.flow.MutableStateFlow
//import kotlinx.coroutines.flow.StateFlow
//import kotlinx.coroutines.flow.asStateFlow
//
//class SuggestionsViewModel : ViewModel() {
//
//    private val _suggestions = MutableStateFlow<List<Suggestion>>(emptyList())
//    val suggestions: StateFlow<List<Suggestion>> = _suggestions.asStateFlow()
//
//    private val _hasResume = MutableStateFlow(false)
//    val hasResume: StateFlow<Boolean> = _hasResume.asStateFlow()
//
//    init {
//        loadSuggestions()
//    }
//
//    private fun loadSuggestions() {
//        val latestResume = LocalResumeDatabase.getLatestResume()
//
//        if (latestResume != null && latestResume.atsScore != null) {
//            _hasResume.value = true
//            _suggestions.value = SuggestionGenerator.generateSuggestions(latestResume.atsScore!!)
//        } else {
//            _hasResume.value = false
//            _suggestions.value = emptyList()
//        }
//    }
//
//    fun toggleSuggestionFixed(suggestionId: String) {
//        val updatedList = _suggestions.value.map { suggestion ->
//            if (suggestion.id == suggestionId) {
//                suggestion.copy(isFixed = !suggestion.isFixed)
//            } else {
//                suggestion
//            }
//        }
//        _suggestions.value = updatedList
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
        val updatedList = _suggestions.value.map { suggestion ->
            if (suggestion.id == suggestionId) {
                suggestion.copy(isFixed = !suggestion.isFixed)
            } else {
                suggestion
            }
        }
        _suggestions.value = updatedList
    }

    fun refresh() {
        loadSuggestions()
    }
}