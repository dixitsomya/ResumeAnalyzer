//package com.example.feature_student
//
//import android.app.Application
//import android.util.Log
//import com.example.feature_student.data.LocalResumeDatabase
//import kotlinx.coroutines.CoroutineScope
//import kotlinx.coroutines.Dispatchers
//import kotlinx.coroutines.launch
//
//class ResumeAnalyzerApp : Application() {
//    override fun onCreate() {
//        super.onCreate()
//
//        Log.d("ResumeAnalyzerApp", "Initializing app...")
//
//        // ✅ FIX: Only initialize database instance, don't load data
//        LocalResumeDatabase.init(this)
//
//        // ✅ Load data on background thread after app startup
//        CoroutineScope(Dispatchers.IO).launch {
//            try {
//                // Optional: Load data in background
//                LocalResumeDatabase.getAnalyzedResumes()
//                Log.d("ResumeAnalyzerApp", "Initial data loaded")
//            } catch (e: Exception) {
//                Log.e("ResumeAnalyzerApp", "Error loading initial data: ${e.message}")
//            }
//        }
//    }
//}

package com.example.feature_student

import android.app.Application
import android.util.Log
import com.example.feature_student.data.LocalResumeDatabase
import com.example.resumeanalyzer.core.navigation.datastore.UserPreference
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ResumeAnalyzerApp : Application() {
    override fun onCreate() {
        super.onCreate()

        Log.d("ResumeAnalyzerApp", "Initializing app...")

        // ✅ FIX 1: Initialize database instance
        LocalResumeDatabase.init(this)

        // ✅ FIX 2: Restore user session from DataStore on app startup
        CoroutineScope(Dispatchers.IO).launch {
            try {
                // Get saved user from DataStore
                UserPreference.getUser(this@ResumeAnalyzerApp).collect { userCache ->
                    if (userCache?.email != null) {
                        // ✅ Restore currentUserEmail if user was previously logged in
                        LocalResumeDatabase.setCurrentUser(userCache.email!!)
                        Log.d("ResumeAnalyzerApp", "User restored: ${userCache.email}")

                        // Load user's resumes
                        LocalResumeDatabase.getAnalyzedResumes()
                        Log.d("ResumeAnalyzerApp", "User data loaded")
                    } else {
                        Log.d("ResumeAnalyzerApp", "No saved user found")
                    }
                }
            } catch (e: Exception) {
                Log.e("ResumeAnalyzerApp", "Error restoring user: ${e.message}")
            }
        }
    }
}