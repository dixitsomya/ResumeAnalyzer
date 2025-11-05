package com.example.feature_student

import android.app.Application
import android.util.Log
import com.example.feature_student.data.LocalResumeDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ResumeAnalyzerApp : Application() {
    override fun onCreate() {
        super.onCreate()

        Log.d("ResumeAnalyzerApp", "Initializing app...")

        // ✅ FIX: Only initialize database instance, don't load data
        LocalResumeDatabase.init(this)

        // ✅ Load data on background thread after app startup
        CoroutineScope(Dispatchers.IO).launch {
            try {
                // Optional: Load data in background
                LocalResumeDatabase.getAnalyzedResumes()
                Log.d("ResumeAnalyzerApp", "Initial data loaded")
            } catch (e: Exception) {
                Log.e("ResumeAnalyzerApp", "Error loading initial data: ${e.message}")
            }
        }
    }
}