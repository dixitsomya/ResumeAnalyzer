

//package com.example.feature_student
//
//import androidx.compose.foundation.layout.*
//import androidx.compose.material3.*
//import androidx.compose.runtime.*
//import androidx.compose.ui.Alignment
//import androidx.compose.ui.Modifier
//import kotlinx.coroutines.delay
//
//@Composable
//fun StudentSplashScreen(
//    onSplashFinished: () -> Unit
//) {
//    LaunchedEffect(Unit) {
//        delay(2000) // 2 seconds
//        onSplashFinished()
//    }
//
//    Box(
//        modifier = Modifier.fillMaxSize(),
//        contentAlignment = Alignment.Center
//    ) {
//        Text(
//            text = "Welcome, Student 👋",
//            style = MaterialTheme.typography.headlineMedium
//        )
//    }
//}
//



package com.example.feature_student

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.TrendingUp
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay

//@Composable
//fun StudentSplashScreen(
//    onSplashFinished: () -> Unit
//) {
//    var scale by remember { mutableStateOf(0.8f) }
//
//    LaunchedEffect(Unit) {
//        scale = 1f
//        delay(2000) // 2 sec splash
//        onSplashFinished()
//    }
//
//    Box(
//        modifier = Modifier
//            .fillMaxSize()
//            .background(
//                Brush.verticalGradient(
//                    listOf(
//                        Color(0xFF64B5F6), // Light Blue (smooth login feel)
//                        Color(0xFF9575CD), // Soft Purple (bridge color)
//                        Color(0xFFD1C4E9)  // Very Light Purple (inside app feel)
//                    )
//                )
//            ),
//        contentAlignment = Alignment.Center
//    ) {
//        Card(
//            modifier = Modifier
//                .padding(32.dp)
//                .scale(scale),
//            shape = RoundedCornerShape(28.dp),
//            elevation = CardDefaults.cardElevation(10.dp),
//            colors = CardDefaults.cardColors(containerColor = Color.White.copy(alpha = 0.9f))
//        ) {
//            Column(
//                modifier = Modifier.padding(36.dp),
//                horizontalAlignment = Alignment.CenterHorizontally
//            ) {
//                // White circular background with logo inside
//                Box(
//                    modifier = Modifier
//                        .size(100.dp)
//                        .background(Color.White, CircleShape),
//                    contentAlignment = Alignment.Center
//                ) {
//                    Icon(
//                        imageVector = Icons.Default.TrendingUp, // standard icon instead of 🚀
//                        contentDescription = "Career Growth",
//                        tint = Color(0xFF6C63FF), // Purple tint for consistency
//                        modifier = Modifier.size(64.dp)
//                    )
//                }
//
//                Spacer(modifier = Modifier.height(20.dp))
//
//                Text(
//                    text = "Your career journey starts here",
//                    fontSize = 20.sp,
//                    fontWeight = FontWeight.Bold,
//                    color = Color(0xFF333333)
//                )
//            }
//        }
//    }
//}



@Composable
fun StudentSplashScreen(
    onSplashFinished: () -> Unit
) {
    var scale by remember { mutableStateOf(0.8f) }
    var showText by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        scale = 1f
        delay(1000) // 1 sec ke baad text animate hoga
        showText = true
        delay(2000) // total 2 sec splash
        onSplashFinished()
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    listOf(
                        Color(0xFF64B5F6),
                        Color(0xFF9575CD),
                        Color(0xFFD1C4E9)
                    )
                )
            ),
        contentAlignment = Alignment.Center
    ) {
        Card(
            modifier = Modifier
                .padding(32.dp)
                .scale(scale),
            shape = RoundedCornerShape(28.dp),
            elevation = CardDefaults.cardElevation(10.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White.copy(alpha = 0.9f))
        ) {
            Column(
                modifier = Modifier.padding(36.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            )
            {
                // White circular background with logo inside
                Box(
                    modifier = Modifier
                        .size(100.dp)
                        .background(Color.White, CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.TrendingUp,
                        contentDescription = "Career Growth",
                        tint = Color(0xFF6C63FF),
                        modifier = Modifier.size(64.dp)
                    )
                }

                Spacer(modifier = Modifier.height(20.dp))

                // Animate text with fade + slide
                AnimatedVisibility(
                    visible = showText,
                    enter = fadeIn(animationSpec = tween(1200)) + slideInVertically(
                        initialOffsetY = { it / 2 }, // slide from bottom
                        animationSpec = tween(1200)
                    )
                ) {
                    Text(
                        text = "Your career journey starts here",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF333333)
                    )
                }
            }
        }
    }
}
