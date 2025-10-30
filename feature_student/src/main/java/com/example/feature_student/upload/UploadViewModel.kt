//
//package com.example.feature_student.upload
//
//import android.content.Context
//import android.net.Uri
//import androidx.compose.runtime.getValue
//import androidx.compose.runtime.mutableStateOf
//import androidx.compose.runtime.setValue
//import androidx.lifecycle.ViewModel
//import androidx.lifecycle.viewModelScope
//import com.example.feature_student.model.Resume
//import com.example.feature_student.model.ResumeStatus
//import kotlinx.coroutines.launch
//
//class UploadViewModel : ViewModel() {
//
//    private val repository = ResumeRepository()
//
//    var uploadState by mutableStateOf<UploadState>(UploadState.Idle)
//        private set
//
//    var selectedResume by mutableStateOf<Resume?>(null)
//        private set
//
//    fun uploadResume(context: Context, uri: Uri, fileName: String) {
//        viewModelScope.launch {
//            uploadState = UploadState.Uploading
//
//            repository.uploadResume(context, uri, fileName).fold(
//                onSuccess = { resume ->
//                    selectedResume = resume
//                    uploadState = UploadState.Success(resume)
//                },
//                onFailure = { error ->
//                    uploadState = UploadState.Error(error.message ?: "Upload failed")
//                }
//            )
//        }
//    }
//
//    fun analyzeResume(onAnalysisComplete: (Resume) -> Unit = {}) {
//        val resume = selectedResume ?: return
//
//        viewModelScope.launch {
//            uploadState = UploadState.Analyzing
//
//            repository.analyzeResume(resume).fold(
//                onSuccess = { analyzedResume ->
//                    selectedResume = analyzedResume
//                    uploadState = UploadState.Analyzed(analyzedResume)
//
//                    // ✅ FIXED: Pass analyzed resume to callback for navigation
//                    onAnalysisComplete(analyzedResume)
//                },
//                onFailure = { error ->
//                    uploadState = UploadState.Error(error.message ?: "Analysis failed")
//                }
//            )
//        }
//    }
//
//    fun resetState() {
//        uploadState = UploadState.Idle
//        selectedResume = null
//    }
//
//    fun getFileSize(sizeInBytes: Long): String {
//        return repository.getFileSize(sizeInBytes)
//    }
//}
//
//sealed class UploadState {
//    object Idle : UploadState()
//    object Uploading : UploadState()
//    data class Success(val resume: Resume) : UploadState()
//    object Analyzing : UploadState()
//    data class Analyzed(val resume: Resume) : UploadState()
//    data class Error(val message: String) : UploadState()
//}
//
//

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

    private val repository = ResumeRepository()

    var uploadState by mutableStateOf<UploadState>(UploadState.Idle)
        private set

    var selectedResume by mutableStateOf<Resume?>(null)
        private set

    var analysisResult by mutableStateOf<ATSAnalysisResult?>(null)
        private set

    fun uploadResume(context: Context, uri: Uri, fileName: String) {
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

                    // Callback for navigation
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
