
package com.example.resumeanalyzer

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.feature_login.LoginScreen
import com.example.feature_login.RegisterScreen
import com.example.feature_student.StudentSplashScreen
import com.example.feature_student.StudentMainScreen
import com.example.feature_recruiter.RecruiterScreen
import com.example.feature_student.SettingsScreen
import com.example.resumeanalyzer.core.navigation.datastore.UserCache
import com.example.resumeanalyzer.core.navigation.datastore.UserPreference


@Composable
fun AppNavGraph(
    navController: NavHostController,
    startDestination: String,
    onThemeChange: (String) -> Unit // <- pass this from MainActivity
) {

    NavHost(navController = navController, startDestination = startDestination) {

        // Splash
        composable("splash") {
            val context = LocalContext.current
            val user by UserPreference.getUser(context).collectAsState(initial = UserCache())

            SplashContent(onSplashFinished = {
                when {
                    user.email != null && user.role == "Student" -> {
//                        navController.navigate("studentMain/${user.email}") {
                          navController.navigate("studentMain") {
                            popUpTo("splash") { inclusive = true }
                        }
                    }
                    user.email != null && user.role == "Recruiter" -> {
                        navController.navigate("recruiter") {
                            popUpTo("splash") { inclusive = true }
                        }
                    }
                    else -> {
                        navController.navigate("login") {
                            popUpTo("splash") { inclusive = true }
                        }
                    }
                }
            })
        }

        // Login
        composable("login") {
            LoginScreen(
                onStudentLogin = { email ->
                    navController.navigate("home/$email") {
                        popUpTo("login") { inclusive = true }
                    }
                },
                onRecruiterLogin = {
                    navController.navigate("recruiter") {
                        popUpTo("login") { inclusive = true }
                    }
                },
                onRegisterClick = {
                    navController.navigate("register")
                }
            )
        }

        // Register
        composable("register") {
            RegisterScreen(
                onRegisterSuccess = {
                    navController.navigate("login") {
                        popUpTo("register") { inclusive = true }
                    }
                },
                onLoginClick = {
                    navController.navigate("login") {
                        popUpTo("register") { inclusive = true }
                    }
                }
            )
        }

        // Student Splash
        composable(
            route = "home/{email}",
            arguments = listOf(navArgument("email") { defaultValue = "" })
        ) { backStackEntry ->
            val email = backStackEntry.arguments?.getString("email") ?: ""
            StudentSplashScreen(
                onSplashFinished = {
                    //navController.navigate("studentMain/$email") {
                    navController.navigate("studentMain") {
                        popUpTo("home/{email}") { inclusive = true }
                    }
                }
            )
        }

        // Student Main
        composable(
            //route = "studentMain/{email}",
            route = "studentMain",
            //arguments = listOf(navArgument("email") { defaultValue = "" })
        ) { //backStackEntry ->
            //val email = backStackEntry.arguments?.getString("email") ?: ""
            StudentMainScreen(
                //userEmail = email,
                navController = navController,
                onNavigate = { route -> navController.navigate(route) }
            )
        }

        // Recruiter
        composable("recruiter") {
            RecruiterScreen()
        }

        // Settings Screen
        composable("settings") {
            val context = LocalContext.current
            val user by UserPreference.getUser(context).collectAsState(initial = UserCache())

            SettingsScreen(
                userEmail = user.email ?: "",
                onBack = { navController.popBackStack() },
                onThemeChange = { theme ->
                    onThemeChange(theme) // <-- global apply
                },
                navController = navController
            )
        }
    }
}
