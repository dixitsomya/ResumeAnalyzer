//package com.example.feature_login
//
//import android.content.Intent
//import android.os.Bundle
//import androidx.activity.ComponentActivity
//import androidx.activity.compose.setContent
//import androidx.compose.material3.MaterialTheme
//
//class RegisterActivity : ComponentActivity() {
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        setContent {
//            MaterialTheme {
//                RegisterScreen(
//                    onRegisterSuccess = {
//                        // Successfully register hone ke baad → LoginActivity pe bhej do
//                        startActivity(Intent(this, LoginActivity::class.java))
//                        finish()
//                    },
//                    onLoginClick = {
//                        // Agar user bolta hai ki "Already have account → Login"
//                        startActivity(Intent(this, LoginActivity::class.java))
//                        finish()
//                    }
//                )
//            }
//        }
//    }
//}
