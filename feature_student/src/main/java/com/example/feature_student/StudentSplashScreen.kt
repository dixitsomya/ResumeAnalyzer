package com.example.feature_student

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.*
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
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
    val isDark = isSystemInDarkTheme()

    var scale by remember { mutableStateOf(0.4f) }
    var showText by remember { mutableStateOf(false) }
    var showSubText by remember { mutableStateOf(false) }

    // Subtle rotation animation
    val infiniteRotation by rememberInfiniteTransition().animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(4000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        )
    )

    LaunchedEffect(Unit) {
        scale = 1f
        delay(600)
        showText = true
        delay(500)
        showSubText = true
        delay(1700) // Total 2.8 seconds
        onSplashFinished()
    }

    // Theme-aware colors
    val backgroundColors = if (isDark) {
        listOf(
            Color(0xFF1A1B3E),
            Color(0xFF2D1B69),
            Color(0xFF0F0C29)
        )
    } else {
        listOf(
            Color(0xFF667eea),
            Color(0xFF764ba2),
            Color(0xFFE8E8F5)
        )
    }

    val cardColor = if (isDark) {
        Color(0xFF1E1E2F).copy(alpha = 0.95f)
    } else {
        Color.White.copy(alpha = 0.95f)
    }

    val primaryColor = if (isDark) Color(0xFF8B7CF6) else Color(0xFF6366F1)
    val textColor = if (isDark) Color(0xFFF1F5F9) else Color(0xFF334155)
    val subtextColor = if (isDark) Color(0xFFCBD5E1) else Color(0xFF64748B)

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = backgroundColors
                )
            ),
        contentAlignment = Alignment.Center
    ) {
        // Subtle floating elements
        repeat(4) { index ->
            Box(
                modifier = Modifier
                    .size((60 + index * 15).dp)
                    .offset(
                        x = ((-120..120).random()).dp,
                        y = ((-150..150).random()).dp
                    )
                    .background(
                        Color.White.copy(alpha = if (isDark) 0.03f else 0.06f),
                        CircleShape
                    )
                    .scale(
                        animateFloatAsState(
                            targetValue = if (scale > 0.6f) 1f else 0f,
                            animationSpec = tween(
                                durationMillis = 1200 + index * 150,
                                easing = EaseOutCubic
                            )
                        ).value
                    )
            )
        }

        Card(
            modifier = Modifier
                .padding(32.dp)
                .scale(
                    animateFloatAsState(
                        targetValue = scale,
                        animationSpec = spring(
                            dampingRatio = Spring.DampingRatioMediumBouncy,
                            stiffness = Spring.StiffnessMedium
                        )
                    ).value
                )
                .shadow(
                    elevation = if (isDark) 12.dp else 20.dp,
                    shape = RoundedCornerShape(28.dp),
                    ambientColor = primaryColor.copy(alpha = 0.1f)
                ),
            shape = RoundedCornerShape(28.dp),
            colors = CardDefaults.cardColors(containerColor = cardColor),
            elevation = CardDefaults.cardElevation(0.dp)
        ) {
            Column(
                modifier = Modifier
                    .padding(36.dp)
                    .fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Elegant icon design
                Box(
                    modifier = Modifier
                        .size(120.dp)
                        .background(
                            Brush.radialGradient(
                                colors = listOf(
                                    primaryColor,
                                    primaryColor.copy(alpha = 0.8f)
                                )
                            ),
                            CircleShape
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Box(
                        modifier = Modifier
                            .size(88.dp)
                            .background(cardColor, CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.TrendingUp,
                            contentDescription = "Resume Analyzer",
                            tint = primaryColor,
                            modifier = Modifier
                                .size(48.dp)
                                .rotate(infiniteRotation * 0.05f) // Very subtle rotation
                        )
                    }
                }

                Spacer(modifier = Modifier.height(28.dp))

                // Main title
                AnimatedVisibility(
                    visible = showText,
                    enter = fadeIn(
                        animationSpec = tween(800, easing = EaseOutCubic)
                    ) + slideInVertically(
                        initialOffsetY = { it / 4 },
                        animationSpec = tween(800, easing = EaseOutCubic)
                    )
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "Resume Analyzer",
                            fontSize = 26.sp,
                            fontWeight = FontWeight.Bold,
                            color = textColor,
                            textAlign = TextAlign.Center,
                            letterSpacing = 0.5.sp
                        )

                        Spacer(modifier = Modifier.height(6.dp))

                        // Elegant underline
                        Box(
                            modifier = Modifier
                                .width(60.dp)
                                .height(2.dp)
                                .background(
                                    primaryColor.copy(alpha = 0.6f),
                                    RoundedCornerShape(1.dp)
                                )
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Subtitle
                AnimatedVisibility(
                    visible = showSubText,
                    enter = fadeIn(
                        animationSpec = tween(600, easing = EaseOutCubic)
                    ) + slideInVertically(
                        initialOffsetY = { it / 5 },
                        animationSpec = tween(600, easing = EaseOutCubic)
                    )
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "Your career journey starts here",
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Medium,
                            color = subtextColor,
                            textAlign = TextAlign.Center,
                            lineHeight = 20.sp
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        // Simple loading dots
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            repeat(3) { index ->
                                Box(
                                    modifier = Modifier
                                        .size(6.dp)
                                        .background(
                                            primaryColor.copy(
                                                alpha = animateFloatAsState(
                                                    targetValue = if ((infiniteRotation / 100f).toInt() % 3 == index) 0.8f else 0.3f,
                                                    animationSpec = tween(400)
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
}