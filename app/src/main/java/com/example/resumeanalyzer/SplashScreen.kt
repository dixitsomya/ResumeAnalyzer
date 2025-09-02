
package com.example.resumeanalyzer

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

import kotlinx.coroutines.delay

//class SplashScreen : ComponentActivity() {
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        setContent {
//            SplashContent {
//                // Navigate to Login screen after splash
//                startActivity(Intent(this@SplashScreen, LoginActivity::class.java))
//                finish()
//            }
//        }
//    }
//}

//@Composable
//fun SplashContent(onSplashFinished: () -> Unit) {
//    var startAnim by remember { mutableStateOf(false) }
//    val scale by animateFloatAsState(
//        targetValue = if (startAnim) 1.2f else 0.8f,
//        animationSpec = tween(1200), label = ""
//    )
//
//    LaunchedEffect(Unit) {
//        startAnim = true
//        delay(2500)
//        onSplashFinished()
//    }
//
//    Box(
//        modifier = Modifier
//            .fillMaxSize()
//            .background(
//                Brush.linearGradient(
//                    listOf(
//                        MaterialTheme.colorScheme.primary,
//                        MaterialTheme.colorScheme.secondary
//                    )
//                )
//            ),
//        contentAlignment = Alignment.Center
//    ) {
//        Column(horizontalAlignment = Alignment.CenterHorizontally) {
//            Image(
//                painter = painterResource(id = R.drawable.app_logo),
//                contentDescription = "App Logo",
//                modifier = Modifier
//                    .size(120.dp)
//                    .scale(scale)
//            )
//
//            Spacer(modifier = Modifier.height(16.dp))
//
//            AnimatedVisibility(visible = startAnim) {
//                Column(horizontalAlignment = Alignment.CenterHorizontally) {
//                    Text(
//                        text = "Resume Analyzer",
//                        style = MaterialTheme.typography.headlineLarge.copy(
//                            fontWeight = FontWeight.Bold,
//                            fontSize = 28.sp
//                        ),
//                        color = MaterialTheme.colorScheme.onPrimary
//                    )
//                    Spacer(modifier = Modifier.height(8.dp))
//                    Text(
//                        text = "Analyze. Optimize. Succeed.",
//                        style = MaterialTheme.typography.bodyLarge.copy(
//                            fontSize = 18.sp,
//                            fontWeight = FontWeight.Medium
//                        ),
//                        color = MaterialTheme.colorScheme.onPrimary
//                    )
//                }
//            }
//        }
//    }
//}

//@Composable
//fun SplashContent(onSplashFinished: () -> Unit) {
//    var startAnim by remember { mutableStateOf(false) }
//    val scale by animateFloatAsState(
//        targetValue = if (startAnim) 1.3f else 1f, // start normal, grow bigger
//        animationSpec = tween(1200), label = ""
//    )
//    val alpha by animateFloatAsState(
//        targetValue = if (startAnim) 1f else 0f,
//        animationSpec = tween(1800), label = ""
//    )
//
//    LaunchedEffect(Unit) {
//        startAnim = true
//        delay(2800) // thoda zyada rakha taaki logo aur text dono dikh sake
//        onSplashFinished()
//    }
//
//    Box(
//        modifier = Modifier
//            .fillMaxSize()
//            .background(
//                Brush.linearGradient(
//                    listOf(
//                        MaterialTheme.colorScheme.primary,
//                        MaterialTheme.colorScheme.secondary
//                    )
//                )
//            ),
//        contentAlignment = Alignment.Center
//    ) {
//        Column(horizontalAlignment = Alignment.CenterHorizontally) {
//            // Bigger Circular Logo Container with shadow
//            Box(
//                modifier = Modifier
//                    .size(180.dp)
//                    .scale(scale) // animate hoke aur bada hoga
//                    .shadow(12.dp, CircleShape)
//                    .background(
//                        color = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.08f),
//                        shape = CircleShape
//                    )
//                    .border(
//                        width = 2.dp,
//                        color = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.25f),
//                        shape = CircleShape
//                    )
//                    .padding(28.dp),
//                contentAlignment = Alignment.Center
//            ) {
//                Image(
//                    painter = painterResource(id = R.drawable.splash_logo),
//                    contentDescription = "App Logo",
//                    modifier = Modifier.size(100.dp) // 👈 logo bhi bada
//                )
//            }
//
//            Spacer(modifier = Modifier.height(28.dp))
//
//            // Title + Subtitle fade-in after logo
//            AnimatedVisibility(visible = startAnim) {
//                Column(horizontalAlignment = Alignment.CenterHorizontally) {
//                    Text(
//                        text = "Resume Analyzer",
//                        style = MaterialTheme.typography.headlineLarge.copy(
//                            fontWeight = FontWeight.ExtraBold,
//                            fontSize = 30.sp,
//                            letterSpacing = 1.5.sp
//                        ),
//                        color = MaterialTheme.colorScheme.onPrimary,
//                        modifier = Modifier.alpha(alpha)
//                    )
//                    Spacer(modifier = Modifier.height(10.dp))
//                    Text(
//                        text = "Analyze • Optimize • Succeed",
//                        style = MaterialTheme.typography.bodyLarge.copy(
//                            fontSize = 18.sp,
//                            fontWeight = FontWeight.Medium
//                        ),
//                        color = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.9f),
//                        modifier = Modifier.alpha(alpha)
//                    )
//                }
//            }
//        }
//    }
//}


@Composable
fun SplashContent(onSplashFinished: () -> Unit) {
    var startAnim by remember { mutableStateOf(false) }

    // Scale animation: normal se bada
    val scale by animateFloatAsState(
        targetValue = if (startAnim) 1.4f else 1.2f,
        animationSpec = tween(1200), label = ""
    )

    // Fade-in animation
    val alpha by animateFloatAsState(
        targetValue = if (startAnim) 1f else 0f,
        animationSpec = tween(1600), label = ""
    )

    LaunchedEffect(Unit) {
        startAnim = true
        delay(3000)
        onSplashFinished()
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.linearGradient(
                    listOf(
                        MaterialTheme.colorScheme.primary,
                        MaterialTheme.colorScheme.secondary
                    )
                )
            ),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {

            // Logo Box
            Box(
                modifier = Modifier
                    .size(200.dp)
                    .scale(scale)
                    .shadow(16.dp, CircleShape, clip = false)
                    .background(
                        color = Color.Transparent,
                        shape = CircleShape
                    ),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = painterResource(id = R.drawable.transparent_logo),
                    contentDescription = "App Logo",
                    modifier = Modifier.fillMaxSize()
                )
            }

            Spacer(modifier = Modifier.height(30.dp))

            // Title + Subtitle fade-in
            AnimatedVisibility(visible = startAnim) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = "Resume Analyzer",
                        style = MaterialTheme.typography.headlineLarge.copy(
                            fontWeight = FontWeight.ExtraBold,
                            fontSize = 30.sp,
                            letterSpacing = 1.3.sp
                        ),
                        color = MaterialTheme.colorScheme.onPrimary,
                        modifier = Modifier.alpha(alpha)
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Analyze • Optimize • Succeed",
                        style = MaterialTheme.typography.bodyLarge.copy(
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Medium
                        ),
                        color = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.9f),
                        modifier = Modifier.alpha(alpha)
                    )
                }
            }
        }
    }
}
