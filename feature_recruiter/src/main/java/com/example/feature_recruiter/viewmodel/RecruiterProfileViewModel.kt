//package com.example.feature_recruiter.viewmodel
//
//import androidx.lifecycle.ViewModel
//import androidx.lifecycle.viewModelScope
//import com.example.feature_recruiter.database.entity.RecruiterProfileEntity
//import com.example.feature_recruiter.repository.RecruiterProfileRepository
//import kotlinx.coroutines.flow.MutableStateFlow
//import kotlinx.coroutines.flow.asStateFlow
//import kotlinx.coroutines.launch
//
//class RecruiterProfileViewModel(
//    private val repository: RecruiterProfileRepository,
//    private val recruiterEmail: String
//) : ViewModel() {
//
//    private val _profile = MutableStateFlow<RecruiterProfileEntity?>(null)
//    val profile = _profile.asStateFlow()
//
//    private val _setupCompleted = MutableStateFlow(false)
//    val setupCompleted = _setupCompleted.asStateFlow()
//
//    private val _isLoading = MutableStateFlow(false)
//    val isLoading = _isLoading.asStateFlow()
//
//    init {
//        loadProfile()
//    }
//
//    private fun loadProfile() {
//        viewModelScope.launch {
//            repository.getProfileFlow(recruiterEmail).collect { profile ->
//                _profile.value = profile
//                _setupCompleted.value = profile?.setupCompleted ?: false
//            }
//        }
//    }
//
//    fun createInitialProfile(companyName: String, recruiterName: String) {
//        viewModelScope.launch {
//            _isLoading.value = true
//            val profile = RecruiterProfileEntity(
//                email = recruiterEmail,
//                companyName = companyName,
//                recruiterName = recruiterName,
//                setupCompleted = true
//            )
//            repository.insertProfile(profile)
//            _isLoading.value = false
//        }
//    }
//
//    fun updateCompanyInfo(companyName: String, recruiterName: String) {
//        viewModelScope.launch {
//            _isLoading.value = true
//            repository.updateCompanyInfo(recruiterEmail, companyName, recruiterName)
//            _isLoading.value = false
//        }
//    }
//}


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
        // ✅ Load profile and check setup status
        checkAndLoadProfile()
    }

    private fun checkAndLoadProfile() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                // ✅ Check if profile exists and is setup completed
                val existingProfile = repository.getProfileByEmail(recruiterEmail)

                _profile.value = existingProfile

                // ✅ Show dialog only if profile doesn't exist or setupCompleted is false
                _showSetupDialog.value = (existingProfile == null || !existingProfile.setupCompleted)
            } catch (e: Exception) {
                e.printStackTrace()
                _showSetupDialog.value = true  // Show dialog on error
            }
        }
    }

    fun createInitialProfile(companyName: String, recruiterName: String) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                _isLoading.value = true

                // ✅ Create new profile with setupCompleted = true
                val newProfile = RecruiterProfileEntity(
                    email = recruiterEmail,
                    companyName = companyName,
                    recruiterName = recruiterName,
                    setupCompleted = true
                )

                repository.insertProfile(newProfile)

                // ✅ Update state
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
                        recruiterName = recruiterName
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

    fun dismissSetupDialog() {
        _showSetupDialog.value = false
    }
}