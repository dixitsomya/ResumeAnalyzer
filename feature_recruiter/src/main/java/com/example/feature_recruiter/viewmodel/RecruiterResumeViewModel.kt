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
//        loadTotalCount()
//        loadAllTechStacks()
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
//        viewModelScope.launch(Dispatchers.IO) {
//            try {
//                repository.getAllResumes(recruiterEmail).collect { resumes ->
//                    _allResumes.value = resumes ?: emptyList()
//                }
//            } catch (e: Exception) {
//                e.printStackTrace()
//                _allResumes.value = emptyList()
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
//                _filteredResumes.value = emptyList()
//            } finally {
//                _isLoading.value = false
//            }
//        }
//    }
//}


package com.example.feature_recruiter.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.feature_recruiter.database.entity.RecruiterResumeEntity
import com.example.feature_recruiter.database.entity.SearchHistoryEntity
import com.example.feature_recruiter.repository.RecruiterResumeRepository
import com.example.feature_recruiter.repository.SearchHistoryRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class RecruiterResumeViewModel(
    private val repository: RecruiterResumeRepository,
    private val recruiterEmail: String,
    private val searchHistoryRepository: SearchHistoryRepository? = null
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

    private val _searchHistory = MutableStateFlow<List<SearchHistoryEntity>>(emptyList())
    val searchHistory = _searchHistory.asStateFlow()

    init {
        // ✅ Load initial data safely
        loadTotalCount()
        loadAllTechStacks()
    }

    private fun loadTotalCount() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val count = repository.getTotalCount(recruiterEmail)
                _totalCount.value = count
            } catch (e: Exception) {
                e.printStackTrace()
                _totalCount.value = 0
            }
        }
    }

    private fun loadAllTechStacks() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val techs = repository.getAllTechStacks(recruiterEmail)
                _allTechStacks.value = techs
            } catch (e: Exception) {
                e.printStackTrace()
                _allTechStacks.value = emptyList()
            }
        }
    }

    fun loadAllResumes() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                repository.getAllResumes(recruiterEmail).collect { resumes ->
                    _allResumes.value = resumes ?: emptyList()
                }
            } catch (e: Exception) {
                e.printStackTrace()
                _allResumes.value = emptyList()
            }
        }
    }

    fun loadSearchHistory() {
        if (searchHistoryRepository == null) return

        viewModelScope.launch(Dispatchers.IO) {
            try {
                searchHistoryRepository.getSearchHistory(recruiterEmail).collect { history ->
                    _searchHistory.value = history ?: emptyList()
                }
            } catch (e: Exception) {
                e.printStackTrace()
                _searchHistory.value = emptyList()
            }
        }
    }

    fun insertResume(resume: RecruiterResumeEntity) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                _isLoading.value = true
                repository.insertResume(resume)
                loadTotalCount()
                loadAllTechStacks()
                loadAllResumes()
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun insertMultipleResumes(resumes: List<RecruiterResumeEntity>) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                _isLoading.value = true
                repository.insertMultipleResumes(resumes)
                loadTotalCount()
                loadAllTechStacks()
                loadAllResumes()
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun filterByTechAndExperience(
        selectedTechs: List<String>,
        minExp: Int,
        maxExp: Int
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                _isLoading.value = true
                val filtered = if (selectedTechs.isEmpty()) {
                    repository.filterByExperience(recruiterEmail, minExp, maxExp)
                } else {
                    repository.filterByCombined(recruiterEmail, selectedTechs, minExp, maxExp)
                }
                _filteredResumes.value = filtered ?: emptyList()

                // ✅ Save history
                saveSearchHistory(selectedTechs, minExp, maxExp, filtered?.size ?: 0)
            } catch (e: Exception) {
                e.printStackTrace()
                _filteredResumes.value = emptyList()
            } finally {
                _isLoading.value = false
            }
        }
    }

    private fun saveSearchHistory(
        techs: List<String>,
        minExp: Int,
        maxExp: Int,
        resultCount: Int
    ) {
        if (searchHistoryRepository == null) return

        viewModelScope.launch(Dispatchers.IO) {
            try {
                val history = SearchHistoryEntity(
                    recruiterEmail = recruiterEmail,
                    techStack = techs.joinToString(", "),
                    minExperience = minExp,
                    maxExperience = maxExp,
                    resultCount = resultCount,
                    searchedAt = System.currentTimeMillis()
                )
                searchHistoryRepository.insertSearchHistory(history)
                loadSearchHistory()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun searchResumes(query: String) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                _isLoading.value = true
                val results = repository.searchResumes(recruiterEmail, query)
                _filteredResumes.value = results ?: emptyList()
            } catch (e: Exception) {
                e.printStackTrace()
                _filteredResumes.value = emptyList()
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun deleteResume(resume: RecruiterResumeEntity) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                repository.deleteResume(resume)
                loadAllResumes()
                loadTotalCount()
                loadAllTechStacks()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun getRecentResumes(limit: Int = 5) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                _isLoading.value = true
                val recent = repository.getRecentResumes(recruiterEmail, limit)
                _filteredResumes.value = recent ?: emptyList()
            } catch (e: Exception) {
                e.printStackTrace()
                _filteredResumes.value = emptyList()
            } finally {
                _isLoading.value = false
            }
        }
    }

    // ✅ Extension function for saving filter search history
    fun filterSearchHistory(
        techs: List<String>,
        minExp: Int,
        maxExp: Int,
        resultCount: Int
    ) {
        if (searchHistoryRepository == null) return

        viewModelScope.launch(Dispatchers.IO) {
            try {
                val history = SearchHistoryEntity(
                    recruiterEmail = recruiterEmail,
                    techStack = techs.joinToString(", "),
                    minExperience = minExp,
                    maxExperience = maxExp,
                    resultCount = resultCount,
                    searchedAt = System.currentTimeMillis()
                )
                searchHistoryRepository.insertSearchHistory(history)
                loadSearchHistory()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}