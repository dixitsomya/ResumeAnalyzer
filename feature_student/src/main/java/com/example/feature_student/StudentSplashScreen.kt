//
//package com.example.feature_student
//
//import androidx.compose.animation.AnimatedVisibility
//import androidx.compose.animation.core.tween
//import androidx.compose.animation.fadeIn
//import androidx.compose.animation.slideInVertically
//import androidx.compose.foundation.background
//import androidx.compose.foundation.layout.*
//import androidx.compose.foundation.shape.CircleShape
//import androidx.compose.foundation.shape.RoundedCornerShape
//import androidx.compose.material.icons.Icons
//import androidx.compose.material.icons.filled.TrendingUp
//import androidx.compose.material3.*
//import androidx.compose.runtime.*
//import androidx.compose.ui.Alignment
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.draw.scale
//import androidx.compose.ui.graphics.Brush
//import androidx.compose.ui.graphics.Color
//import androidx.compose.ui.text.font.FontWeight
//import androidx.compose.ui.unit.dp
//import androidx.compose.ui.unit.sp
//import kotlinx.coroutines.delay
//
//@Composable
//fun StudentSplashScreen(
//    onSplashFinished: () -> Unit
//) {
//    var scale by remember { mutableStateOf(0.8f) }
//    var showText by remember { mutableStateOf(false) }
//
//    LaunchedEffect(Unit) {
//        scale = 1f
//        delay(1000) // 1 sec ke baad text animate hoga
//        showText = true
//        delay(2000) // total 2 sec splash
//        onSplashFinished()
//    }
//
//    Box(
//        modifier = Modifier
//            .fillMaxSize()
//            .background(
//                Brush.verticalGradient(
//                    listOf(
//                        Color(0xFF64B5F6),
//                        Color(0xFF9575CD),
//                        Color(0xFFD1C4E9)
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
//            )
//            {
//                // White circular background with logo inside
//                Box(
//                    modifier = Modifier
//                        .size(100.dp)
//                        .background(Color.White, CircleShape),
//                    contentAlignment = Alignment.Center
//                ) {
//                    Icon(
//                        imageVector = Icons.Default.TrendingUp,
//                        contentDescription = "Career Growth",
//                        tint = Color(0xFF6C63FF),
//                        modifier = Modifier.size(64.dp)
//                    )
//                }
//
//                Spacer(modifier = Modifier.height(20.dp))
//
//                // Animate text with fade + slide
//                AnimatedVisibility(
//                    visible = showText,
//                    enter = fadeIn(animationSpec = tween(1200)) + slideInVertically(
//                        initialOffsetY = { it / 2 }, // slide from bottom
//                        animationSpec = tween(1200)
//                    )
//                ) {
//                    Text(
//                        text = "Your career journey starts here",
//                        fontSize = 20.sp,
//                        fontWeight = FontWeight.Bold,
//                        color = Color(0xFF333333)
//                    )
//                }
//            }
//        }
//    }
//}


package com.example.feature_student

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.*
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
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay

@Composable
fun StudentSplashScreen(
    onSplashFinished: () -> Unit
) {
    var scale by remember { mutableStateOf(0.3f) }
    var showText by remember { mutableStateOf(false) }
    var showSubText by remember { mutableStateOf(false) }
    var iconRotation by remember { mutableStateOf(0f) }

    // Continuous rotation animation for icon
    val infiniteRotation by rememberInfiniteTransition().animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(3000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        )
    )

    LaunchedEffect(Unit) {
        // Smooth scale animation
        scale = 1f
        delay(800)
        showText = true
        delay(600)
        showSubText = true
        delay(1800) // Total 3.2 seconds
        onSplashFinished()
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.radialGradient(
                    colors = listOf(
                        Color(0xFF667eea),
                        Color(0xFF764ba2),
                        Color(0xFF2C1810)
                    ),
                    radius = 1000f
                )
            ),
        contentAlignment = Alignment.Center
    ) {
        // Background floating elements for depth
        repeat(6) { index ->
            Box(
                modifier = Modifier
                    .size((40 + index * 20).dp)
                    .offset(
                        x = ((-150..150).random()).dp,
                        y = ((-200..200).random()).dp
                    )
                    .background(
                        Color.White.copy(alpha = 0.05f),
                        CircleShape
                    )
                    .scale(
                        animateFloatAsState(
                            targetValue = if (scale > 0.5f) 1f else 0f,
                            animationSpec = tween(
                                durationMillis = 1500 + index * 200,
                                easing = FastOutSlowInEasing
                            )
                        ).value
                    )
            )
        }

        Card(
            modifier = Modifier
                .padding(24.dp)
                .scale(
                    animateFloatAsState(
                        targetValue = scale,
                        animationSpec = spring(
                            dampingRatio = Spring.DampingRatioMediumBouncy,
                            stiffness = Spring.StiffnessLow
                        )
                    ).value
                )
                .shadow(24.dp, RoundedCornerShape(32.dp)),
            shape = RoundedCornerShape(32.dp),
            colors = CardDefaults.cardColors(
                containerColor = Color.White.copy(alpha = 0.95f)
            ),
            elevation = CardDefaults.cardElevation(0.dp)
        ) {
            Column(
                modifier = Modifier
                    .padding(40.dp)
                    .fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Enhanced icon container with gradient background
                Box(
                    modifier = Modifier
                        .size(140.dp)
                        .background(
                            Brush.radialGradient(
                                colors = listOf(
                                    Color(0xFF667eea),
                                    Color(0xFF764ba2)
                                )
                            ),
                            CircleShape
                        )
                        .shadow(16.dp, CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    // Inner white circle
                    Box(
                        modifier = Modifier
                            .size(100.dp)
                            .background(Color.White, CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.TrendingUp,
                            contentDescription = "Career Growth",
                            tint = Color(0xFF667eea),
                            modifier = Modifier
                                .size(56.dp)
                                .rotate(infiniteRotation * 0.1f) // Subtle rotation
                        )
                    }
                }

                Spacer(modifier = Modifier.height(32.dp))

                // Main title with enhanced animation
                AnimatedVisibility(
                    visible = showText,
                    enter = fadeIn(
                        animationSpec = tween(1000, easing = FastOutSlowInEasing)
                    ) + slideInVertically(
                        initialOffsetY = { it / 3 },
                        animationSpec = tween(1000, easing = FastOutSlowInEasing)
                    )
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "Resume Analyzer",
                            fontSize = 28.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF2D3748),
                            textAlign = TextAlign.Center
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        // Subtitle line
                        Box(
                            modifier = Modifier
                                .width(80.dp)
                                .height(3.dp)
                                .background(
                                    Brush.horizontalGradient(
                                        listOf(
                                            Color.Transparent,
                                            Color(0xFF667eea),
                                            Color.Transparent
                                        )
                                    ),
                                    RoundedCornerShape(2.dp)
                                )
                        )
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))

                // Subtitle text
                AnimatedVisibility(
                    visible = showSubText,
                    enter = fadeIn(
                        animationSpec = tween(800, easing = FastOutSlowInEasing)
                    ) + slideInVertically(
                        initialOffsetY = { it / 4 },
                        animationSpec = tween(800, easing = FastOutSlowInEasing)
                    )
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "Your career journey starts here",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Medium,
                            color = Color(0xFF4A5568),
                            textAlign = TextAlign.Center,
                            lineHeight = 22.sp
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        Text(
                            text = "Analyze • Improve • Succeed",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Normal,
                            color = Color(0xFF667eea),
                            textAlign = TextAlign.Center,
                            letterSpacing = 1.sp
                        )
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))

                // Loading indicator
                AnimatedVisibility(
                    visible = showSubText,
                    enter = fadeIn(animationSpec = tween(600))
                ) {
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        repeat(3) { index ->
                            Box(
                                modifier = Modifier
                                    .size(8.dp)
                                    .background(
                                        Color(0xFF667eea).copy(
                                            alpha = animateFloatAsState(
                                                targetValue = if ((infiniteRotation / 120f).toInt() % 3 == index) 1f else 0.3f,
                                                animationSpec = tween(300)
                                            ).value
                                        ),
                                        CircleShape
                                    )
                            )
                        }
                    }
                }
            }
        }
    }
}