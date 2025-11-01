

package com.example.feature_student.upload

import android.content.Context
import android.net.Uri
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.feature_student.model.Resume
import com.example.feature_student.model.ATSAnalysisResult
import kotlinx.coroutines.launch

class UploadViewModel : ViewModel() {

    private lateinit var repository: ResumeRepository  // ✅ MAKE non-initialized

    var uploadState by mutableStateOf<UploadState>(UploadState.Idle)
        private set

    var selectedResume by mutableStateOf<Resume?>(null)
        private set

    var analysisResult by mutableStateOf<ATSAnalysisResult?>(null)
        private set

    // ✅ ADD init function
    fun initRepository(context: Context) {
        repository = ResumeRepository(context)
    }

    fun uploadResume(context: Context, uri: Uri, fileName: String) {
        if (!::repository.isInitialized) {
            initRepository(context)
        }

        viewModelScope.launch {
            uploadState = UploadState.Uploading

            repository.uploadResume(context, uri, fileName).fold(
                onSuccess = { resume ->
                    selectedResume = resume
                    uploadState = UploadState.Success(resume)
                },
                onFailure = { error ->
                    uploadState = UploadState.Error(error.message ?: "Upload failed")
                }
            )
        }
    }

    fun analyzeResume(onAnalysisComplete: (ATSAnalysisResult) -> Unit = {}) {
        val resume = selectedResume ?: return

        viewModelScope.launch {
            uploadState = UploadState.Analyzing

            repository.analyzeResume(resume).fold(
                onSuccess = { result ->
                    analysisResult = result
                    selectedResume = selectedResume?.copy(atsScore = result.overallScore)
                    uploadState = UploadState.Analyzed(result)
                    onAnalysisComplete(result)
                },
                onFailure = { error ->
                    uploadState = UploadState.Error(error.message ?: "Analysis failed")
                }
            )
        }
    }

    fun resetState() {
        uploadState = UploadState.Idle
        selectedResume = null
        analysisResult = null
    }

    fun getFileSize(sizeInBytes: Long): String {
        return repository.getFileSize(sizeInBytes)
    }
}

sealed class UploadState {
    object Idle : UploadState()
    object Uploading : UploadState()
    data class Success(val resume: Resume) : UploadState()
    object Analyzing : UploadState()
    data class Analyzed(val result: ATSAnalysisResult) : UploadState()
    data class Error(val message: String) : UploadState()
}