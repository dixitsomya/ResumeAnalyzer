package com.example.feature_recruiter.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.feature_recruiter.database.entity.RecruiterResumeEntity
import com.example.feature_recruiter.repository.RecruiterResumeRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class RecruiterResumeViewModel(
    private val repository: RecruiterResumeRepository,
    private val recruiterEmail: String
) : ViewModel() {

    private val _allResumes = MutableStateFlow<List<RecruiterResumeEntity>>(emptyList())
    val allResumes = _allResumes.asStateFlow()

    private val _filteredResumes = MutableStateFlow<List<RecruiterResumeEntity>>(emptyList())
    val filteredResumes = _filteredResumes.asStateFlow()

    private val _totalCount = MutableStateFlow(0)
    val totalCount = _totalCount.asStateFlow()

    private val _allTechStacks = MutableStateFlow<List<String>>(emptyList())
    val allTechStacks = _allTechStacks.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading = _isLoading.asStateFlow()

    init {
        loadAllResumes()
        loadTechStacks()
        loadTotalCount()
    }

    private fun loadAllResumes() {
        viewModelScope.launch {
            repository.getAllResumes(recruiterEmail).collect { resumes ->
                _allResumes.value = resumes
            }
        }
    }

    private fun loadTechStacks() {
        viewModelScope.launch {
            _allTechStacks.value = repository.getAllTechStacks(recruiterEmail)
        }
    }

    private fun loadTotalCount() {
        viewModelScope.launch {
            _totalCount.value = repository.getTotalCount(recruiterEmail)
        }
    }

    fun insertResume(resume: RecruiterResumeEntity) {
        viewModelScope.launch {
            _isLoading.value = true
            repository.insertResume(resume)
            _isLoading.value = false
            loadAllResumes()
            loadTechStacks()
            loadTotalCount()
        }
    }

    fun insertMultipleResumes(resumes: List<RecruiterResumeEntity>) {
        viewModelScope.launch {
            _isLoading.value = true
            repository.insertMultipleResumes(resumes)
            _isLoading.value = false
            loadAllResumes()
            loadTechStacks()
            loadTotalCount()
        }
    }

    fun filterByTechAndExperience(
        selectedTechs: List<String>,
        minExp: Int,
        maxExp: Int
    ) {
        viewModelScope.launch {
            _isLoading.value = true
            val filtered = if (selectedTechs.isEmpty()) {
                repository.filterByExperience(recruiterEmail, minExp, maxExp)
            } else {
                repository.filterByCombined(recruiterEmail, selectedTechs, minExp, maxExp)
            }
            _filteredResumes.value = filtered
            _isLoading.value = false
        }
    }

    fun searchResumes(query: String) {
        viewModelScope.launch {
            _isLoading.value = true
            _filteredResumes.value = repository.searchResumes(recruiterEmail, query)
            _isLoading.value = false
        }
    }

    fun deleteResume(resume: RecruiterResumeEntity) {
        viewModelScope.launch {
            repository.deleteResume(resume)
            loadAllResumes()
            loadTechStacks()
            loadTotalCount()
        }
    }

    fun getRecentResumes(limit: Int = 5) {
        viewModelScope.launch {
            _filteredResumes.value = repository.getRecentResumes(recruiterEmail, limit)
        }
    }
}