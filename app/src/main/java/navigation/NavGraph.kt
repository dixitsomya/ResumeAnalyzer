

//package com.example.resumeanalyzer
//
//import androidx.compose.runtime.Composable
//import androidx.navigation.NavHostController
//import androidx.navigation.compose.NavHost
//import androidx.navigation.compose.composable
//import com.example.feature_login.LoginScreen
//import com.example.feature_login.RegisterScreen
//import com.example.feature_student.StudentSplashScreen
//import com.example.feature_recruiter.RecruiterScreen
//
//@Composable
//fun AppNavGraph(navController: NavHostController, startDestination: String) {
//
//    NavHost(navController = navController, startDestination = startDestination) {
//
//        composable("splash") {
//            SplashContent(onSplashFinished = {
//                navController.navigate("login") {
//                    popUpTo("splash") { inclusive = true }
//                }
//            })
//        }
//
//        composable("login") {
//            LoginScreen(
//                onStudentLogin = {
//                    navController.navigate("home") {
//                        popUpTo("login") { inclusive = true }
//                    }
//                },
//                onRecruiterLogin = {
//                    navController.navigate("recruiter") {
//                        popUpTo("login") { inclusive = true }
//                    }
//                },
//                onRegisterClick = {
//                    navController.navigate("register")
//                }
//            )
//        }
//
//        composable("register") {
//            RegisterScreen(
//                onRegisterSuccess = {
//                    navController.navigate("home") {
//                        popUpTo("login") { inclusive = true }
//                    }
//                },
//                onLoginClick = {
//                    navController.navigate("login") {
//                        popUpTo("register") { inclusive = true }
//                    }
//                }
//            )
//        }
//
//        composable("home") {
//            StudentSplashScreen()
//        }
//
//        composable("recruiter") {
//            RecruiterScreen()
//        }
//    }
//}


package com.example.resumeanalyzer

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.feature_login.LoginScreen
import com.example.feature_login.RegisterScreen
import com.example.feature_student.StudentSplashScreen
import com.example.feature_student.StudentMainScreen
import com.example.feature_recruiter.RecruiterScreen

@Composable
fun AppNavGraph(navController: NavHostController, startDestination: String) {

    NavHost(navController = navController, startDestination = startDestination) {

        // Splash
        composable("splash") {
            SplashContent(onSplashFinished = {
                navController.navigate("login") {
                    popUpTo("splash") { inclusive = true }
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
                    // yahan tum chahe to email bhi pass kar sakti ho agar register screen return kare
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

        // Student Splash (with email)
        composable(
            route = "home/{email}",
            arguments = listOf(navArgument("email") { defaultValue = "" })
        ) { backStackEntry ->
            val email = backStackEntry.arguments?.getString("email") ?: ""
            StudentSplashScreen(
                onSplashFinished = {
                    navController.navigate("studentMain/$email") {
                        popUpTo("home/{email}") { inclusive = true }
                    }
                }
            )
        }

        // Student Main (with email)
        composable(
            route = "studentMain/{email}",
            arguments = listOf(navArgument("email") { defaultValue = "" })
        ) { backStackEntry ->
            val email = backStackEntry.arguments?.getString("email") ?: ""
            StudentMainScreen(
                userEmail = email,
                navController = navController,
                onNavigate = { route -> navController.navigate(route) }
            )
        }

        // Recruiter
        composable("recruiter") {
            RecruiterScreen()
        }
    }
}
