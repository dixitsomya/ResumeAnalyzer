//package com.example.feature_student.ats
//
//import androidx.lifecycle.ViewModel
//import androidx.lifecycle.viewModelScope
//import com.example.feature_student.data.LocalResumeDatabase
//import com.example.feature_student.model.Resume
//import kotlinx.coroutines.flow.MutableStateFlow
//import kotlinx.coroutines.flow.StateFlow
//import kotlinx.coroutines.flow.asStateFlow
//import kotlinx.coroutines.launch
//
//class ATSViewModel : ViewModel() {
//
//    private val _latestResume = MutableStateFlow<Resume?>(null)
//    val latestResume: StateFlow<Resume?> = _latestResume.asStateFlow()
//
//    private val _analyzedCount = MutableStateFlow(0)
//    val analyzedCount: StateFlow<Int> = _analyzedCount.asStateFlow()
//
//    init {
//        loadData()
//    }
//
//    fun loadData() {
//        viewModelScope.launch {
//            _latestResume.value = LocalResumeDatabase.getLatestResume()
//            _analyzedCount.value = LocalResumeDatabase.getTotalAnalyzed()
//        }
//    }
//
//    fun refresh() {
//        loadData()
//    }
//}


// ============ ATSViewModel.kt ============
package com.example.feature_student.ats

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.feature_student.data.LocalResumeDatabase
import com.example.feature_student.model.Resume
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ATSViewModel : ViewModel() {

    private val _latestResume = MutableStateFlow<Resume?>(null)
    val latestResume: StateFlow<Resume?> = _latestResume.asStateFlow()

    private val _analyzedCount = MutableStateFlow(0)
    val analyzedCount: StateFlow<Int> = _analyzedCount.asStateFlow()

    init {
        loadData()
    }

    fun loadData() {
        viewModelScope.launch {
            _latestResume.value = LocalResumeDatabase.getLatestResume()
            _analyzedCount.value = LocalResumeDatabase.getTotalAnalyzed()
        }
    }

    fun refresh() {
        loadData()
    }
}