package com.example.feature_recruiter.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.feature_recruiter.database.entity.RecruiterProfileEntity
import com.example.feature_recruiter.repository.RecruiterProfileRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class RecruiterProfileViewModel(
    private val repository: RecruiterProfileRepository,
    private val recruiterEmail: String
) : ViewModel() {

    private val _profile = MutableStateFlow<RecruiterProfileEntity?>(null)
    val profile = _profile.asStateFlow()

    private val _setupCompleted = MutableStateFlow(false)
    val setupCompleted = _setupCompleted.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading = _isLoading.asStateFlow()

    init {
        loadProfile()
    }

    private fun loadProfile() {
        viewModelScope.launch {
            repository.getProfileFlow(recruiterEmail).collect { profile ->
                _profile.value = profile
                _setupCompleted.value = profile?.setupCompleted ?: false
            }
        }
    }

    fun createInitialProfile(companyName: String, recruiterName: String) {
        viewModelScope.launch {
            _isLoading.value = true
            val profile = RecruiterProfileEntity(
                email = recruiterEmail,
                companyName = companyName,
                recruiterName = recruiterName,
                setupCompleted = true
            )
            repository.insertProfile(profile)
            _isLoading.value = false
        }
    }

    fun updateCompanyInfo(companyName: String, recruiterName: String) {
        viewModelScope.launch {
            _isLoading.value = true
            repository.updateCompanyInfo(recruiterEmail, companyName, recruiterName)
            _isLoading.value = false
        }
    }
}