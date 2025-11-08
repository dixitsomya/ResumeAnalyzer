//
//package com.example.feature_recruiter.viewmodel
//
//import androidx.lifecycle.ViewModel
//import androidx.lifecycle.viewModelScope
//import com.example.feature_recruiter.database.entity.RecruiterResumeEntity
//import com.example.feature_recruiter.database.entity.SearchHistoryEntity
//import com.example.feature_recruiter.repository.RecruiterResumeRepository
//import com.example.feature_recruiter.repository.SearchHistoryRepository
//import kotlinx.coroutines.Dispatchers
//import kotlinx.coroutines.flow.MutableStateFlow
//import kotlinx.coroutines.flow.asStateFlow
//import kotlinx.coroutines.launch
//
//class RecruiterResumeViewModel(
//    private val repository: RecruiterResumeRepository,
//    private val recruiterEmail: String,
//    private val searchHistoryRepository: SearchHistoryRepository? = null
//) : ViewModel() {
//
//    private val _allResumes = MutableStateFlow<List<RecruiterResumeEntity>>(emptyList())
//    val allResumes = _allResumes.asStateFlow()
//
//    private val _filteredResumes = MutableStateFlow<List<RecruiterResumeEntity>>(emptyList())
//    val filteredResumes = _filteredResumes.asStateFlow()
//
//    private val _totalCount = MutableStateFlow(0)
//    val totalCount = _totalCount.asStateFlow()
//
//    private val _allTechStacks = MutableStateFlow<List<String>>(emptyList())
//    val allTechStacks = _allTechStacks.asStateFlow()
//
//    private val _isLoading = MutableStateFlow(false)
//    val isLoading = _isLoading.asStateFlow()
//
//    private val _searchHistory = MutableStateFlow<List<SearchHistoryEntity>>(emptyList())
//    val searchHistory = _searchHistory.asStateFlow()
//
//    init {
//        // ✅ Load initial data safely
////        loadTotalCount()
////        loadAllTechStacks()
////        loadAllResumes()
////        loadSearchHistory()
//        viewModelScope.launch(Dispatchers.IO) {
//            launch { loadTotalCount() }
//            launch { loadAllTechStacks() }
//            launch { loadAllResumes() }
//            launch { loadSearchHistory() }
//        }
//    }
//
//    private fun loadTotalCount() {
//        viewModelScope.launch(Dispatchers.IO) {
//            try {
//                val count = repository.getTotalCount(recruiterEmail)
//                _totalCount.value = count
//            } catch (e: Exception) {
//                e.printStackTrace()
//                _totalCount.value = 0
//            }
//        }
//    }
//
//    private fun loadAllTechStacks() {
//        viewModelScope.launch(Dispatchers.IO) {
//            try {
//                val techs = repository.getAllTechStacks(recruiterEmail)
//                _allTechStacks.value = techs
//            } catch (e: Exception) {
//                e.printStackTrace()
//                _allTechStacks.value = emptyList()
//            }
//        }
//    }
//
//    fun loadAllResumes() {
////        viewModelScope.launch(Dispatchers.IO) {
////            try {
////                repository.getAllResumes(recruiterEmail).collect { resumes ->
////                    _allResumes.value = resumes ?: emptyList()
////                }
////            } catch (e: Exception) {
////                e.printStackTrace()
////                _allResumes.value = emptyList()
////            }
////        }
//        viewModelScope.launch(Dispatchers.IO) {
//            _isLoading.value = true
//            try {
//                repository.getAllResumes(recruiterEmail).collect { resumes ->
//                    _allResumes.value = resumes ?: emptyList()
//                }
//            } catch (e: Exception) {
//                e.printStackTrace()
//            } finally {
//                _isLoading.value = false
//            }
//        }
//    }
//
//    fun loadSearchHistory() {
//        if (searchHistoryRepository == null) return
//
//        viewModelScope.launch(Dispatchers.IO) {
//            try {
//                searchHistoryRepository.getSearchHistory(recruiterEmail).collect { history ->
//                    _searchHistory.value = history ?: emptyList()
//                }
//            } catch (e: Exception) {
//                e.printStackTrace()
//                _searchHistory.value = emptyList()
//            }
//        }
//    }
//
//    fun insertResume(resume: RecruiterResumeEntity) {
//        viewModelScope.launch(Dispatchers.IO) {
//            try {
//                _isLoading.value = true
//                repository.insertResume(resume)
//                loadTotalCount()
//                loadAllTechStacks()
//                loadAllResumes()
//            } catch (e: Exception) {
//                e.printStackTrace()
//            } finally {
//                _isLoading.value = false
//            }
//        }
//    }
//
//    fun insertMultipleResumes(resumes: List<RecruiterResumeEntity>) {
//        viewModelScope.launch(Dispatchers.IO) {
//            try {
//                _isLoading.value = true
//                repository.insertMultipleResumes(resumes)
//                loadTotalCount()
//                loadAllTechStacks()
//                loadAllResumes()
//            } catch (e: Exception) {
//                e.printStackTrace()
//            } finally {
//                _isLoading.value = false
//            }
//        }
//    }
//
//    fun filterByTechAndExperience(
//        selectedTechs: List<String>,
//        minExp: Int,
//        maxExp: Int
//    ) {
//        viewModelScope.launch(Dispatchers.IO) {
//            try {
//                _isLoading.value = true
//                val filtered = if (selectedTechs.isEmpty()) {
//                    repository.filterByExperience(recruiterEmail, minExp, maxExp)
//                } else {
//                    repository.filterByCombined(recruiterEmail, selectedTechs, minExp, maxExp)
//                }
//                _filteredResumes.value = filtered ?: emptyList()
//
//                // ✅ Save history
//                saveSearchHistory(selectedTechs, minExp, maxExp, filtered?.size ?: 0)
//            } catch (e: Exception) {
//                e.printStackTrace()
//                _filteredResumes.value = emptyList()
//            } finally {
//                _isLoading.value = false
//            }
//        }
//    }
//
//    private fun saveSearchHistory(
//        techs: List<String>,
//        minExp: Int,
//        maxExp: Int,
//        resultCount: Int
//    ) {
//        if (searchHistoryRepository == null) return
//
//        viewModelScope.launch(Dispatchers.IO) {
//            try {
//                val history = SearchHistoryEntity(
//                    recruiterEmail = recruiterEmail,
//                    techStack = techs.joinToString(", "),
//                    minExperience = minExp,
//                    maxExperience = maxExp,
//                    resultCount = resultCount,
//                    searchedAt = System.currentTimeMillis()
//                )
//                searchHistoryRepository.insertSearchHistory(history)
//                loadSearchHistory()
//            } catch (e: Exception) {
//                e.printStackTrace()
//            }
//        }
//    }
//
//    fun searchResumes(query: String) {
//        viewModelScope.launch(Dispatchers.IO) {
//            try {
//                _isLoading.value = true
//                val results = repository.searchResumes(recruiterEmail, query)
//                _filteredResumes.value = results ?: emptyList()
//            } catch (e: Exception) {
//                e.printStackTrace()
//                _filteredResumes.value = emptyList()
//            } finally {
//                _isLoading.value = false
//            }
//        }
//    }
//
//    fun deleteResume(resume: RecruiterResumeEntity) {
//        viewModelScope.launch(Dispatchers.IO) {
//            try {
//                repository.deleteResume(resume)
//                loadAllResumes()
//                loadTotalCount()
//                loadAllTechStacks()
//            } catch (e: Exception) {
//                e.printStackTrace()
//            }
//        }
//    }
//
//    fun getRecentResumes(limit: Int = 5) {
//        viewModelScope.launch(Dispatchers.IO) {
//            try {
//                _isLoading.value = true
//                val recent = repository.getRecentResumes(recruiterEmail, limit)
//                _filteredResumes.value = recent ?: emptyList()
//            } catch (e: Exception) {
//                e.printStackTrace()
//               // _filteredResumes.value = emptyList()
//            } finally {
//                _isLoading.value = false
//            }
//        }
//    }
//
//    // ✅ Extension function for saving filter search history
//    fun filterSearchHistory(
//        techs: List<String>,
//        minExp: Int,
//        maxExp: Int,
//        resultCount: Int
//    ) {
//        if (searchHistoryRepository == null) return
//
//        viewModelScope.launch(Dispatchers.IO) {
//            try {
//                val history = SearchHistoryEntity(
//                    recruiterEmail = recruiterEmail,
//                    techStack = techs.joinToString(", "),
//                    minExperience = minExp,
//                    maxExperience = maxExp,
//                    resultCount = resultCount,
//                    searchedAt = System.currentTimeMillis()
//                )
//                searchHistoryRepository.insertSearchHistory(history)
//                loadSearchHistory()
//            } catch (e: Exception) {
//                e.printStackTrace()
//            }
//        }
//    }
//
//    fun clearAllHistory() {
//        viewModelScope.launch(Dispatchers.IO) {
//            try {
//                // ✅ Clear resume upload history
//                repository.clearAllResumes(recruiterEmail)
//
//                // ✅ Clear search history (if repository exists)
//                searchHistoryRepository?.clearAllHistory(recruiterEmail)
//
//                // ✅ Refresh UI lists
//                loadAllResumes()
//                loadSearchHistory()
//                loadTotalCount()
//                loadAllTechStacks()
//            } catch (e: Exception) {
//                e.printStackTrace()
//            }
//        }
//    }
//
//}

package com.example.feature_recruiter.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.feature_recruiter.database.entity.RecruiterResumeEntity
import com.example.feature_recruiter.database.entity.SearchHistoryEntity
import com.example.feature_recruiter.repository.RecruiterResumeRepository
import com.example.feature_recruiter.repository.SearchHistoryRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class RecruiterResumeViewModel(
    private val resumeRepository: RecruiterResumeRepository,
    private val userEmail: String,
    private val searchHistoryRepository: SearchHistoryRepository
) : ViewModel() {

    private val _filteredResumes = MutableStateFlow<List<RecruiterResumeEntity>>(emptyList())
    val filteredResumes: StateFlow<List<RecruiterResumeEntity>> = _filteredResumes.asStateFlow()

    private val _totalCount = MutableStateFlow(0)
    val totalCount: StateFlow<Int> = _totalCount.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _searchHistory = MutableStateFlow<List<SearchHistoryEntity>>(emptyList())
    val searchHistory: StateFlow<List<SearchHistoryEntity>> = _searchHistory.asStateFlow()

    private var isInitialized = false

    // ✅ Initialize once per email
    fun initialize(email: String) {
        if (isInitialized) {
            return // Already initialized
        }

        isInitialized = true

        viewModelScope.launch {
            _isLoading.value = true
            try {
                // Load all three things once
                loadAllResumes(email)
                loadSearchHistory(email)
                getTotalCount(email)
            } finally {
                _isLoading.value = false
            }
        }
    }

    // ✅ Load all resumes
    private fun loadAllResumes(email: String) {
        viewModelScope.launch {
            resumeRepository.getAllResumes(email).collect { resumes ->
                _filteredResumes.value = resumes ?: emptyList()
            }
        }
    }

    // ✅ Load search history once
    private fun loadSearchHistory(email: String) {
        viewModelScope.launch {
            searchHistoryRepository.getSearchHistory(email).collect { history ->
                _searchHistory.value = history
            }
        }
    }

    // ✅ Get total count
    private fun getTotalCount(email: String) {
        viewModelScope.launch {
            try {
                val count = resumeRepository.getTotalCount(email)
                _totalCount.value = count
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    // ✅ Filter by tech stack
    fun filterByTechStack(techList: List<String>) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val filtered = resumeRepository.filterByTechStack(userEmail, techList)
                _filteredResumes.value = filtered ?: emptyList()

                // ✅ Save search history with proper parameters
                saveSearchHistory(
                    techStack = techList.joinToString(", "),
                    minExp = 0,
                    maxExp = 100,
                    resultCount = filtered?.size ?: 0
                )
            } finally {
                _isLoading.value = false
            }
        }
    }

    // ✅ Filter by experience
    fun filterByExperience(minExp: Int, maxExp: Int) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val filtered = resumeRepository.filterByExperience(userEmail, minExp, maxExp)
                _filteredResumes.value = filtered ?: emptyList()

                // ✅ Save search history with proper parameters
                saveSearchHistory(
                    techStack = "",
                    minExp = minExp,
                    maxExp = maxExp,
                    resultCount = filtered?.size ?: 0
                )
            } finally {
                _isLoading.value = false
            }
        }
    }

    // ✅ Combined filter
    fun filterByCombined(techList: List<String>, minExp: Int, maxExp: Int) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val filtered = resumeRepository.filterByCombined(userEmail, techList, minExp, maxExp)
                _filteredResumes.value = filtered ?: emptyList()

                // ✅ Save search history with proper parameters
                saveSearchHistory(
                    techStack = techList.joinToString(", "),
                    minExp = minExp,
                    maxExp = maxExp,
                    resultCount = filtered?.size ?: 0
                )
            } finally {
                _isLoading.value = false
            }
        }
    }

    // ✅ Search resumes
    fun searchResumes(query: String) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val results = resumeRepository.searchResumes(userEmail, query)
                _filteredResumes.value = results ?: emptyList()

                // ✅ Save search history with proper parameters
                saveSearchHistory(
                    techStack = query,
                    minExp = 0,
                    maxExp = 100,
                    resultCount = results?.size ?: 0
                )
            } finally {
                _isLoading.value = false
            }
        }
    }

    // ✅ Save search history with all required parameters
    private fun saveSearchHistory(
        techStack: String,
        minExp: Int,
        maxExp: Int,
        resultCount: Int
    ) {
        viewModelScope.launch {
            try {
                val history = SearchHistoryEntity(
                    recruiterEmail = userEmail,
                    techStack = techStack,
                    minExperience = minExp,
                    maxExperience = maxExp,
                    resultCount = resultCount,
                    searchedAt = System.currentTimeMillis()
                )
                searchHistoryRepository.insertSearchHistory(history)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    // ✅ Reset filters - go back to all resumes
    fun resetFilters() {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                loadAllResumes(userEmail)
            } finally {
                _isLoading.value = false
            }
        }
    }

    // ✅ Insert single resume
    fun insertResume(resume: RecruiterResumeEntity) {
        viewModelScope.launch {
            try {
                resumeRepository.insertResume(resume)
                // Refresh data after insert
                loadAllResumes(userEmail)
                getTotalCount(userEmail)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    // ✅ Insert multiple resumes
    fun insertMultipleResumes(resumes: List<RecruiterResumeEntity>) {
        viewModelScope.launch {
            try {
                resumeRepository.insertMultipleResumes(resumes)
                // Refresh data after insert
                loadAllResumes(userEmail)
                getTotalCount(userEmail)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    // ✅ Get all tech stacks for filter UI
    fun getAllTechStacks() {
        viewModelScope.launch {
            try {
                resumeRepository.getAllTechStacks(userEmail)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    // ✅ Delete resume
    fun deleteResume(resume: RecruiterResumeEntity) {
        viewModelScope.launch {
            try {
                resumeRepository.deleteResume(resume)
                // Refresh data after delete
                loadAllResumes(userEmail)
                getTotalCount(userEmail)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    // ✅ Load all resumes for display
    fun loadAllResumes() {
        viewModelScope.launch {
            loadAllResumes(userEmail)
        }
    }

    // ✅ Filter search history - save filter criteria
    fun filterSearchHistory(
        techs: List<String>,
        minExp: Int,
        maxExp: Int,
        resultCount: Int
    ) {
        viewModelScope.launch {
            try {
                val history = SearchHistoryEntity(
                    recruiterEmail = userEmail,
                    techStack = techs.joinToString(", "),
                    minExperience = minExp,
                    maxExperience = maxExp,
                    resultCount = resultCount,
                    searchedAt = System.currentTimeMillis()
                )
                searchHistoryRepository.insertSearchHistory(history)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    // ✅ Clear all history (resumes and searches)
    fun clearAllHistory() {
        viewModelScope.launch {
            try {
                resumeRepository.clearAllResumes(userEmail)
                searchHistoryRepository.clearAllHistory(userEmail)

                // Refresh data
                _filteredResumes.value = emptyList()
                _totalCount.value = 0
                _searchHistory.value = emptyList()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    // ✅ Clear all data for logout
    fun clearData() {
        _filteredResumes.value = emptyList()
        _totalCount.value = 0
        _searchHistory.value = emptyList()
        isInitialized = false
    }
}