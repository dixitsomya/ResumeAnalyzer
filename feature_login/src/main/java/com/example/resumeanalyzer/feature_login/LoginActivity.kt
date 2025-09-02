//package com.example.feature_login
//
//import android.content.Intent
//import android.os.Bundle
//import androidx.activity.ComponentActivity
//import androidx.activity.compose.setContent
//import androidx.compose.material3.MaterialTheme
//import com.example.feature_student.HomeActivity
//
//class LoginActivity : ComponentActivity() {
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        setContent {
//            MaterialTheme {
//                LoginScreen(
//                    onLoginSuccess = {
//                        // Agar login successful ho → HomeActivity khol do
//                        startActivity(Intent(this, HomeActivity::class.java))
//                        finish()
//                    },
//                    onRegisterClick = {
//                        // Agar user "Register" karna chahe → RegisterActivity khol do
//                        startActivity(Intent(this, RegisterActivity::class.java))
//                    }
//                )
//            }
//        }
//    }
//}
