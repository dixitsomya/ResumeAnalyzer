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


// ============ SuggestionsViewModel.kt ============
package com.example.feature_student.suggestions

import androidx.lifecycle.ViewModel
import com.example.feature_student.data.LocalResumeDatabase
import com.example.feature_student.model.Suggestion
import com.example.feature_student.model.SuggestionGenerator
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class SuggestionsViewModel : ViewModel() {

    private val _suggestions = MutableStateFlow<List<Suggestion>>(emptyList())
    val suggestions: StateFlow<List<Suggestion>> = _suggestions.asStateFlow()

    private val _hasResume = MutableStateFlow(false)
    val hasResume: StateFlow<Boolean> = _hasResume.asStateFlow()

    init {
        loadSuggestions()
    }

    private fun loadSuggestions() {
        val latestResume = LocalResumeDatabase.getLatestResume()

        if (latestResume != null && latestResume.atsScore != null) {
            _hasResume.value = true
            _suggestions.value = SuggestionGenerator.generateSuggestions(latestResume.atsScore!!)
        } else {
            _hasResume.value = false
            _suggestions.value = emptyList()
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