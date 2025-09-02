//package com.example.resumeanalyzer
//
//import android.os.Bundle
//import androidx.activity.ComponentActivity
//import androidx.activity.compose.setContent
//import androidx.compose.material3.Surface
//import androidx.compose.runtime.Composable
//import androidx.compose.ui.Modifier
//import androidx.navigation.compose.rememberNavController
//import com.example.resumeanalyzer.ui.theme.ResumeAnalyzerTheme
//
//class MainActivity : ComponentActivity() {
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        setContent {
//            ResumeAnalyzerApp()
//        }
//    }
//}
//
//@Composable
//fun ResumeAnalyzerApp() {
//    ResumeAnalyzerTheme {
//        Surface(modifier = Modifier) {
//            val navController = rememberNavController()
//            AppNavGraph(navController = navController, startDestination = "splash")
//        }
//    }
//}


package com.example.resumeanalyzer

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.OnBackPressedCallback
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import com.example.resumeanalyzer.ui.theme.ResumeAnalyzerTheme

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Handle back press properly
        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                // Move app to background instead of closing
                moveTaskToBack(true)
            }
        })

        setContent {
            ResumeAnalyzerApp()
        }
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        setIntent(intent)
    }

    override fun onResume() {
        super.onResume()
        // Ensure app is properly resumed
    }

    override fun onPause() {
        super.onPause()
        // Handle pause properly without finishing activity
    }
}

@Composable
fun ResumeAnalyzerApp() {
    ResumeAnalyzerTheme {
        Surface(modifier = Modifier) {
            val navController = rememberNavController()
            AppNavGraph(navController = navController, startDestination = "splash")
        }
    }
}
