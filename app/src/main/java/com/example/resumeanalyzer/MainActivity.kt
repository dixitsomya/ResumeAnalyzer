//
//package com.example.resumeanalyzer
//
//import android.content.Intent
//import android.os.Bundle
//import androidx.activity.ComponentActivity
//import androidx.activity.compose.setContent
//import androidx.activity.OnBackPressedCallback
//import androidx.compose.material3.Surface
//import androidx.compose.runtime.Composable
//import androidx.compose.ui.Modifier
//import androidx.navigation.compose.rememberNavController
//import com.example.resumeanalyzer.ui.theme.ResumeAnalyzerTheme
//
//class MainActivity : ComponentActivity() {
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//
//        // Handle back press properly
//        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
//            override fun handleOnBackPressed() {
//                // Move app to background instead of closing
//                moveTaskToBack(true)
//            }
//        })
//
//        setContent {
//            ResumeAnalyzerApp()
//        }
//    }
//
//    override fun onNewIntent(intent: Intent?) {
//        super.onNewIntent(intent)
//        setIntent(intent)
//    }
//
//    override fun onResume() {
//        super.onResume()
//        // Ensure app is properly resumed
//    }
//
//    override fun onPause() {
//        super.onPause()
//        // Handle pause properly without finishing activity
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


//package com.example.resumeanalyzer
//
//import android.os.Bundle
//import androidx.activity.ComponentActivity
//import androidx.activity.compose.setContent
//import androidx.activity.OnBackPressedCallback
//import androidx.compose.material3.Surface
//import androidx.compose.runtime.*
//import androidx.compose.ui.Modifier
//import androidx.navigation.compose.rememberNavController
//import com.example.resumeanalyzer.core.navigation.datastore.UserPreference
//import com.example.resumeanalyzer.core.navigation.datastore.UserCache
//import com.example.resumeanalyzer.ui.theme.ResumeAnalyzerTheme
//import kotlinx.coroutines.launch
//
//class MainActivity : ComponentActivity() {
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//
//        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
//            override fun handleOnBackPressed() {
//                moveTaskToBack(true)
//            }
//        })
//
//        setContent {
//            val context = this
//            val user by UserPreference.getUser(context).collectAsState(initial = UserCache())
//
//            var appTheme by remember { mutableStateOf(user.theme) }
//            val scope = rememberCoroutineScope()
//
//            ResumeAnalyzerTheme(theme = appTheme) {
//                Surface(modifier = Modifier) {
//                    val navController = rememberNavController()
//                    AppNavGraph(
//                        navController = navController,
//                        startDestination = "splash",
//                        onThemeChange = { theme ->
//                            scope.launch {
//                                UserPreference.saveTheme(context, theme)
//                                appTheme = theme // <-- globally update
//                            }
//                        }
//                    )
//                }
//            }
//        }
//    }
//}

package com.example.resumeanalyzer

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.OnBackPressedCallback
import androidx.activity.compose.setContent
import androidx.compose.material3.Surface
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import com.example.resumeanalyzer.core.navigation.datastore.UserPreference
import com.example.resumeanalyzer.core.navigation.datastore.UserCache
import com.example.resumeanalyzer.ui.theme.ResumeAnalyzerTheme
import kotlinx.coroutines.launch

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
    val context = androidx.compose.ui.platform.LocalContext.current
    val user by UserPreference.getUser(context).collectAsState(initial = UserCache())

    // Theme persist karega jab tak user change na kare
    var appTheme by remember { mutableStateOf(user.theme) }
    val scope = rememberCoroutineScope()

    ResumeAnalyzerTheme(theme = appTheme) {
        Surface(modifier = Modifier) {
            val navController = rememberNavController()
            AppNavGraph(
                navController = navController,
                startDestination = "splash",
                onThemeChange = { theme ->
                    scope.launch {
                        UserPreference.saveTheme(context, theme)
                        appTheme = theme // <-- globally update
                    }
                }
            )
        }
    }
}
