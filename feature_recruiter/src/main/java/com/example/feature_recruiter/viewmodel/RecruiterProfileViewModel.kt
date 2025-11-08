package com.example.feature_recruiter.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.feature_recruiter.database.entity.RecruiterProfileEntity
import com.example.feature_recruiter.repository.RecruiterProfileRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class RecruiterProfileViewModel(
    private val repository: RecruiterProfileRepository,
    private val recruiterEmail: String
) : ViewModel() {

    private val _profile = MutableStateFlow<RecruiterProfileEntity?>(null)
    val profile = _profile.asStateFlow()

    private val _showSetupDialog = MutableStateFlow(false)
    val showSetupDialog = _showSetupDialog.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading = _isLoading.asStateFlow()

    init {
        checkAndLoadProfile()
    }

    private fun checkAndLoadProfile() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val existingProfile = repository.getProfileByEmail(recruiterEmail)
                _profile.value = existingProfile

                // ✅ Show dialog if:
                // 1. No profile exists, OR
                // 2. Profile exists but setupCompleted = false OR companyName is empty
                if (existingProfile == null) {
                    _showSetupDialog.value = true
                } else if (!existingProfile.setupCompleted || existingProfile.companyName.isBlank()) {
                    _showSetupDialog.value = true
                } else {
                    _showSetupDialog.value = false
                }
            } catch (e: Exception) {
                e.printStackTrace()
                _showSetupDialog.value = true
            }
        }
    }

    fun createInitialProfile(companyName: String, recruiterName: String) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                _isLoading.value = true

                val existingProfile = repository.getProfileByEmail(recruiterEmail)

                val newProfile = if (existingProfile != null) {
                    // Update existing
                    existingProfile.copy(
                        companyName = companyName,
                        recruiterName = recruiterName,
                        setupCompleted = true
                    )
                } else {
                    // Create new
                    RecruiterProfileEntity(
                        email = recruiterEmail,
                        companyName = companyName,
                        recruiterName = recruiterName,
                        setupCompleted = true
                    )
                }

                repository.insertProfile(newProfile)
                _profile.value = newProfile
                _showSetupDialog.value = false

                _isLoading.value = false
            } catch (e: Exception) {
                e.printStackTrace()
                _isLoading.value = false
            }
        }
    }

    fun updateCompanyInfo(companyName: String, recruiterName: String) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                _isLoading.value = true

                val currentProfile = _profile.value
                if (currentProfile != null) {
                    val updatedProfile = currentProfile.copy(
                        companyName = companyName,
                        recruiterName = recruiterName,
                        setupCompleted = true
                    )
                    repository.updateProfile(updatedProfile)
                    _profile.value = updatedProfile
                }

                _isLoading.value = false
            } catch (e: Exception) {
                e.printStackTrace()
                _isLoading.value = false
            }
        }
    }

    fun clearProfile() {
        _profile.value = null
        _showSetupDialog.value = false
        _isLoading.value = false
    }

    fun dismissSetupDialog() {
        _showSetupDialog.value = false
    }
}