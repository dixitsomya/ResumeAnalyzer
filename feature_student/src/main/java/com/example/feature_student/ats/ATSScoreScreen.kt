//package com.example.feature_student.ats
//
//import androidx.compose.animation.core.*
//import androidx.compose.foundation.Canvas
//import androidx.compose.foundation.background
//import androidx.compose.foundation.layout.*
//import androidx.compose.foundation.lazy.LazyColumn
//import androidx.compose.foundation.lazy.items
//import androidx.compose.foundation.shape.CircleShape
//import androidx.compose.foundation.shape.RoundedCornerShape
//import androidx.compose.material.icons.Icons
//import androidx.compose.material.icons.filled.*
//import androidx.compose.material3.*
//import androidx.compose.runtime.*
//import androidx.compose.ui.Alignment
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.draw.clip
//import androidx.compose.ui.graphics.Brush
//import androidx.compose.ui.graphics.Color
//import androidx.compose.ui.graphics.StrokeCap
//import androidx.compose.ui.graphics.drawscope.Stroke
//import androidx.compose.ui.text.font.FontWeight
//import androidx.compose.ui.unit.dp
//import androidx.compose.ui.unit.sp
//import androidx.lifecycle.viewmodel.compose.viewModel
//import com.example.feature_student.model.Resume
//import java.text.SimpleDateFormat
//import java.util.*
//
//@Composable
//fun ATSScoreScreen(
//    modifier: Modifier = Modifier,
//    isDark: Boolean,
//    viewModel: ATSViewModel = viewModel()
//) {
//    val latestResume by viewModel.latestResume.collectAsState()
//    val analyzedCount by viewModel.analyzedCount.collectAsState()
//
//    LaunchedEffect(Unit) {
//        viewModel.refresh()
//    }
//
//    val backgroundBrush = if (isDark) {
//        Brush.verticalGradient(
//            listOf(Color(0xFF1a1a2e), Color(0xFF16213e))
//        )
//    } else {
//        Brush.verticalGradient(
//            listOf(Color(0xFFf0f4f8), Color(0xFFe4e9f2))
//        )
//    }
//
//    Box(
//        modifier = modifier
//            .fillMaxSize()
//            .background(backgroundBrush)
//    ) {
//        if (latestResume == null) {
//            EmptyATSState(isDark = isDark)
//        } else {
//            ATSScoreContent(
//                resume = latestResume!!,
//                analyzedCount = analyzedCount,
//                isDark = isDark
//            )
//        }
//    }
//}
//
//@Composable
//fun EmptyATSState(isDark: Boolean) {
//    Column(
//        modifier = Modifier
//            .fillMaxSize()
//            .padding(24.dp),
//        horizontalAlignment = Alignment.CenterHorizontally,
//        verticalArrangement = Arrangement.Center
//    ) {
//        Icon(
//            Icons.Default.Assessment,
//            contentDescription = null,
//            modifier = Modifier.size(100.dp),
//            tint = if (isDark) Color(0xFF6C63FF).copy(0.5f) else Color(0xFF6C63FF)
//        )
//
//        Spacer(Modifier.height(24.dp))
//
//        Text(
//            "No ATS Score Yet",
//            fontSize = 24.sp,
//            fontWeight = FontWeight.Bold,
//            color = if (isDark) Color.White else Color(0xFF1a1a2e)
//        )
//
//        Spacer(Modifier.height(12.dp))
//
//        Text(
//            "Upload and analyze your resume first\nto see your ATS compatibility score",
//            fontSize = 16.sp,
//            color = if (isDark) Color.White.copy(0.6f) else Color.Gray,
//            textAlign = androidx.compose.ui.text.style.TextAlign.Center
//        )
//    }
//}
//
//@Composable
//fun ATSScoreContent(
//    resume: Resume,
//    analyzedCount: Int,
//    isDark: Boolean
//) {
//    LazyColumn(
//        modifier = Modifier
//            .fillMaxSize()
//            .padding(20.dp),
//        verticalArrangement = Arrangement.spacedBy(20.dp),
//        contentPadding = PaddingValues(bottom = 20.dp)
//    ) {
//        item {
//            Card(
//                modifier = Modifier.fillMaxWidth(),
//                shape = RoundedCornerShape(24.dp),
//                colors = CardDefaults.cardColors(
//                    containerColor = if (isDark) Color(0xFF2a2a3e) else Color.White
//                ),
//                elevation = CardDefaults.cardElevation(12.dp)
//            ) {
//                Column(
//                    modifier = Modifier
//                        .fillMaxWidth()
//                        .padding(32.dp),
//                    horizontalAlignment = Alignment.CenterHorizontally
//                ) {
//                    Text(
//                        "Your ATS Score",
//                        fontSize = 22.sp,
//                        fontWeight = FontWeight.Bold,
//                        color = if (isDark) Color.White else Color(0xFF1a1a2e)
//                    )
//
//                    Spacer(Modifier.height(24.dp))
//
//                    AnimatedCircularProgress(
//                        score = resume.atsScore ?: 0,
//                        isDark = isDark
//                    )
//
//                    Spacer(Modifier.height(24.dp))
//
//                    val (message, emoji) = getScoreMessage(resume.atsScore ?: 0)
//                    Text(
//                        "$message $emoji",
//                        fontSize = 18.sp,
//                        fontWeight = FontWeight.Medium,
//                        color = if (isDark) Color.White.copy(0.8f) else Color(0xFF1a1a2e)
//                    )
//                }
//            }
//        }
//
//        item {
//            Card(
//                modifier = Modifier.fillMaxWidth(),
//                shape = RoundedCornerShape(20.dp),
//                colors = CardDefaults.cardColors(
//                    containerColor = if (isDark) Color(0xFF2a2a3e) else Color.White
//                ),
//                elevation = CardDefaults.cardElevation(8.dp)
//            ) {
//                Column(
//                    modifier = Modifier
//                        .fillMaxWidth()
//                        .padding(20.dp)
//                ) {
//                    Text(
//                        "Score Breakdown",
//                        fontSize = 20.sp,
//                        fontWeight = FontWeight.Bold,
//                        color = if (isDark) Color.White else Color.Black
//                    )
//
//                    Spacer(Modifier.height(16.dp))
//
//                    ScoreBreakdownItem(
//                        "Format Compatibility",
//                        getRandomScore(resume.atsScore ?: 0, 5),
//                        isDark
//                    )
//                    ScoreBreakdownItem(
//                        "Keyword Optimization",
//                        getRandomScore(resume.atsScore ?: 0, 10),
//                        isDark
//                    )
//                    ScoreBreakdownItem(
//                        "Content Quality",
//                        getRandomScore(resume.atsScore ?: 0, 8),
//                        isDark
//                    )
//                    ScoreBreakdownItem(
//                        "Structure & Organization",
//                        getRandomScore(resume.atsScore ?: 0, 7),
//                        isDark
//                    )
//                }
//            }
//        }
//
//        item {
//            Row(
//                modifier = Modifier.fillMaxWidth(),
//                horizontalArrangement = Arrangement.spacedBy(12.dp)
//            ) {
//                ATSStatCard(
//                    title = "Total Analyzed",
//                    value = analyzedCount.toString(),
//                    icon = Icons.Default.Description,
//                    color = Color(0xFF6C63FF),
//                    isDark = isDark,
//                    modifier = Modifier.weight(1f)
//                )
//
//                ATSStatCard(
//                    title = "Latest Score",
//                    value = "${resume.atsScore ?: 0}",
//                    icon = Icons.Default.TrendingUp,
//                    color = Color(0xFFE91E63),
//                    isDark = isDark,
//                    modifier = Modifier.weight(1f)
//                )
//            }
//        }
//
//        item {
//            Card(
//                modifier = Modifier.fillMaxWidth(),
//                shape = RoundedCornerShape(20.dp),
//                colors = CardDefaults.cardColors(
//                    containerColor = if (isDark) Color(0xFF2a2a3e) else Color.White
//                ),
//                elevation = CardDefaults.cardElevation(8.dp)
//            ) {
//                Column(
//                    modifier = Modifier
//                        .fillMaxWidth()
//                        .padding(20.dp)
//                ) {
//                    Row(
//                        verticalAlignment = Alignment.CenterVertically
//                    ) {
//                        Icon(
//                            Icons.Default.InsertDriveFile,
//                            contentDescription = null,
//                            tint = Color(0xFF6C63FF),
//                            modifier = Modifier.size(24.dp)
//                        )
//                        Spacer(Modifier.width(8.dp))
//                        Text(
//                            "Analyzed Resume",
//                            fontSize = 18.sp,
//                            fontWeight = FontWeight.Bold,
//                            color = if (isDark) Color.White else Color.Black
//                        )
//                    }
//
//                    Spacer(Modifier.height(16.dp))
//
//                    Text(
//                        resume.fileName,
//                        fontSize = 16.sp,
//                        fontWeight = FontWeight.Medium,
//                        color = if (isDark) Color.White.copy(0.9f) else Color.Black,
//                        maxLines = 2
//                    )
//
//                    Spacer(Modifier.height(8.dp))
//
//                    Text(
//                        "Analyzed on ${formatDate(resume.uploadedDate)}",
//                        fontSize = 14.sp,
//                        color = if (isDark) Color.White.copy(0.6f) else Color.Gray
//                    )
//                }
//            }
//        }
//    }
//}
//
//@Composable
//fun AnimatedCircularProgress(
//    score: Int,
//    isDark: Boolean
//) {
//    var animatedProgress by remember { mutableStateOf(0f) }
//
//    LaunchedEffect(score) {
//        animatedProgress = score / 100f
//    }
//
//    val animatedValue by animateFloatAsState(
//        targetValue = animatedProgress,
//        animationSpec = tween(durationMillis = 1500, easing = FastOutSlowInEasing),
//        label = "score_animation"
//    )
//
//    Box(
//        modifier = Modifier.size(200.dp),
//        contentAlignment = Alignment.Center
//    ) {
//        Canvas(modifier = Modifier.size(200.dp)) {
//            drawCircle(
//                color = if (isDark) Color.White.copy(0.1f) else Color.LightGray.copy(0.3f),
//                style = Stroke(width = 20.dp.toPx(), cap = StrokeCap.Round)
//            )
//
//            drawArc(
//                brush = Brush.sweepGradient(
//                    listOf(
//                        Color(0xFF6C63FF),
//                        Color(0xFF764ba2),
//                        Color(0xFF6C63FF)
//                    )
//                ),
//                startAngle = -90f,
//                sweepAngle = 360f * animatedValue,
//                useCenter = false,
//                style = Stroke(width = 20.dp.toPx(), cap = StrokeCap.Round)
//            )
//        }
//
//        Column(horizontalAlignment = Alignment.CenterHorizontally) {
//            Text(
//                "${(animatedValue * 100).toInt()}",
//                fontSize = 56.sp,
//                fontWeight = FontWeight.Bold,
//                color = Color(0xFF6C63FF)
//            )
//            Text(
//                "out of 100",
//                fontSize = 14.sp,
//                color = if (isDark) Color.White.copy(0.6f) else Color.Gray
//            )
//        }
//    }
//}
//
//@Composable
//fun ScoreBreakdownItem(
//    title: String,
//    score: Int,
//    isDark: Boolean
//) {
//    Column(modifier = Modifier.padding(vertical = 8.dp)) {
//        Row(
//            modifier = Modifier.fillMaxWidth(),
//            horizontalArrangement = Arrangement.SpaceBetween,
//            verticalAlignment = Alignment.CenterVertically
//        ) {
//            Text(
//                title,
//                fontSize = 14.sp,
//                color = if (isDark) Color.White.copy(0.8f) else Color(0xFF1a1a2e)
//            )
//            Text(
//                "$score%",
//                fontSize = 14.sp,
//                fontWeight = FontWeight.Bold,
//                color = if (isDark) Color.White else Color.Black
//            )
//        }
//
//        Spacer(Modifier.height(8.dp))
//
//        LinearProgressIndicator(
//            progress = score / 100f,
//            modifier = Modifier
//                .fillMaxWidth()
//                .height(8.dp)
//                .clip(RoundedCornerShape(4.dp)),
//            color = when {
//                score >= 80 -> Color(0xFF4CAF50)
//                score >= 60 -> Color(0xFFFF9800)
//                else -> Color(0xFFF44336)
//            },
//            trackColor = if (isDark) Color.White.copy(0.1f) else Color.LightGray.copy(0.3f)
//        )
//    }
//}
//
//@Composable
//fun ATSStatCard(
//    title: String,
//    value: String,
//    icon: androidx.compose.ui.graphics.vector.ImageVector,
//    color: Color,
//    isDark: Boolean,
//    modifier: Modifier = Modifier
//) {
//    Card(
//        modifier = modifier.height(110.dp),
//        shape = RoundedCornerShape(16.dp),
//        colors = CardDefaults.cardColors(
//            containerColor = if (isDark) Color(0xFF2a2a3e) else Color.White
//        ),
//        elevation = CardDefaults.cardElevation(8.dp)
//    ) {
//        Column(
//            modifier = Modifier
//                .fillMaxSize()
//                .padding(16.dp),
//            verticalArrangement = Arrangement.SpaceBetween
//        ) {
//            Icon(
//                icon,
//                contentDescription = null,
//                tint = color,
//                modifier = Modifier.size(32.dp)
//            )
//
//            Column {
//                Text(
//                    value,
//                    fontSize = 28.sp,
//                    fontWeight = FontWeight.Bold,
//                    color = if (isDark) Color.White else Color.Black
//                )
//                Text(
//                    title,
//                    fontSize = 12.sp,
//                    color = if (isDark) Color.White.copy(0.7f) else Color.Gray
//                )
//            }
//        }
//    }
//}
//
//fun getScoreMessage(score: Int): Pair<String, String> {
//    return when {
//        score >= 90 -> Pair("Excellent! Your resume is ATS-ready", "🎉")
//        score >= 80 -> Pair("Great! Minor improvements needed", "✨")
//        score >= 70 -> Pair("Good, but can be improved", "👍")
//        score >= 60 -> Pair("Fair, needs optimization", "💪")
//        else -> Pair("Needs significant improvement", "📝")
//    }
//}
//
//fun getRandomScore(baseScore: Int, variance: Int): Int {
//    return (baseScore - variance..baseScore + variance).random().coerceIn(0, 100)
//}
//
//fun formatDate(timestamp: Long): String {
//    val sdf = SimpleDateFormat("MMM dd, yyyy", Locale.getDefault())
//    return sdf.format(Date(timestamp))
//}
//
//



package com.example.feature_student.ats

import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
fun ATSScoreScreen(
    modifier: Modifier = Modifier,
    isDark: Boolean,
    viewModel: ATSViewModel = viewModel()
) {
    val latestResume by viewModel.latestResume.collectAsState()
    val analysisResult by viewModel.analysisResult.collectAsState()
    val analyzedCount by viewModel.analyzedCount.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.refresh()
    }

    val backgroundBrush = if (isDark) {
        Brush.verticalGradient(
            listOf(Color(0xFF1a1a2e), Color(0xFF16213e))
        )
    } else {
        Brush.verticalGradient(
            listOf(Color(0xFFf0f4f8), Color(0xFFe4e9f2))
        )
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(backgroundBrush)
    ) {
        if (latestResume == null || analysisResult == null) {
            EmptyATSState(isDark = isDark)
        } else {
            ATSScoreContent(
                resume = latestResume!!,
                analysis = analysisResult!!,
                analyzedCount = analyzedCount,
                isDark = isDark
            )
        }
    }
}

@Composable
fun EmptyATSState(isDark: Boolean) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            Icons.Default.Assessment,
            contentDescription = null,
            modifier = Modifier.size(100.dp),
            tint = if (isDark) Color(0xFF6C63FF).copy(0.5f) else Color(0xFF6C63FF)
        )

        Spacer(Modifier.height(24.dp))

        Text(
            "No ATS Score Yet",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = if (isDark) Color.White else Color(0xFF1a1a2e)
        )

        Spacer(Modifier.height(12.dp))

        Text(
            "Upload and analyze your resume first\nto see your ATS compatibility score",
            fontSize = 16.sp,
            color = if (isDark) Color.White.copy(0.6f) else Color.Gray,
            textAlign = androidx.compose.ui.text.style.TextAlign.Center
        )
    }
}

@Composable
fun ATSScoreContent(
    resume: com.example.feature_student.model.Resume,
    analysis: com.example.feature_student.model.ATSAnalysisResult,
    analyzedCount: Int,
    isDark: Boolean
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp),
        verticalArrangement = Arrangement.spacedBy(20.dp),
        contentPadding = PaddingValues(bottom = 20.dp)
    ) {
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(
                    containerColor = if (isDark) Color(0xFF2a2a3e) else Color.White
                ),
                elevation = CardDefaults.cardElevation(12.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(32.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        "Your ATS Score",
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold,
                        color = if (isDark) Color.White else Color(0xFF1a1a2e)
                    )

                    Spacer(Modifier.height(24.dp))

                    AnimatedCircularProgress(
                        score = analysis.overallScore,
                        isDark = isDark
                    )

                    Spacer(Modifier.height(24.dp))

                    val (message, emoji) = getScoreMessage(analysis.overallScore)
                    Text(
                        "$message $emoji",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Medium,
                        color = if (isDark) Color.White.copy(0.8f) else Color(0xFF1a1a2e),
                        textAlign = androidx.compose.ui.text.style.TextAlign.Center
                    )
                }
            }
        }

        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(
                    containerColor = if (isDark) Color(0xFF2a2a3e) else Color.White
                ),
                elevation = CardDefaults.cardElevation(8.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp)
                ) {
                    Text(
                        "Score Breakdown",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = if (isDark) Color.White else Color.Black
                    )

                    Spacer(Modifier.height(16.dp))

                    ScoreBreakdownItem(
                        "Format Compatibility",
                        analysis.breakdown.formatScore,
                        isDark
                    )
                    ScoreBreakdownItem(
                        "Keyword Optimization",
                        analysis.breakdown.keywordScore,
                        isDark
                    )
                    ScoreBreakdownItem(
                        "Content Quality",
                        analysis.breakdown.contentScore,
                        isDark
                    )
                    ScoreBreakdownItem(
                        "Structure & Organization",
                        analysis.breakdown.structureScore,
                        isDark
                    )
                    ScoreBreakdownItem(
                        "Contact Information",
                        analysis.breakdown.contactScore,
                        isDark
                    )
                }
            }
        }

        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(
                    containerColor = if (isDark) Color(0xFF2a2a3e) else Color.White
                ),
                elevation = CardDefaults.cardElevation(8.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            Icons.Default.Key,
                            contentDescription = null,
                            tint = Color(0xFF6C63FF),
                            modifier = Modifier.size(24.dp)
                        )
                        Spacer(Modifier.width(8.dp))
                        Text(
                            "Keyword Analysis",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = if (isDark) Color.White else Color.Black
                        )
                    }

                    Spacer(Modifier.height(16.dp))

                    Text(
                        "Found Keywords (${analysis.keywords.foundKeywords.size})",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF4CAF50)
                    )

                    Spacer(Modifier.height(8.dp))

                    Text(
                        analysis.keywords.foundKeywords.take(15).joinToString(", "),
                        fontSize = 13.sp,
                        color = if (isDark) Color.White.copy(0.7f) else Color.Gray,
                        lineHeight = 18.sp
                    )

                    if (analysis.keywords.missingKeywords.isNotEmpty()) {
                        Spacer(Modifier.height(16.dp))

                        Text(
                            "Suggested Keywords",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFFFF9800)
                        )

                        Spacer(Modifier.height(8.dp))

                        Text(
                            analysis.keywords.missingKeywords.take(10).joinToString(", "),
                            fontSize = 13.sp,
                            color = if (isDark) Color.White.copy(0.7f) else Color.Gray,
                            lineHeight = 18.sp
                        )
                    }
                }
            }
        }

        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                ATSStatCard(
                    title = "Total Analyzed",
                    value = analyzedCount.toString(),
                    icon = Icons.Default.Description,
                    color = Color(0xFF6C63FF),
                    isDark = isDark,
                    modifier = Modifier.weight(1f)
                )

                ATSStatCard(
                    title = "Current Score",
                    value = "${analysis.overallScore}",
                    icon = Icons.Default.TrendingUp,
                    color = Color(0xFFE91E63),
                    isDark = isDark,
                    modifier = Modifier.weight(1f)
                )
            }
        }

        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(
                    containerColor = if (isDark) Color(0xFF2a2a3e) else Color.White
                ),
                elevation = CardDefaults.cardElevation(8.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            Icons.Default.InsertDriveFile,
                            contentDescription = null,
                            tint = Color(0xFF6C63FF),
                            modifier = Modifier.size(24.dp)
                        )
                        Spacer(Modifier.width(8.dp))
                        Text(
                            "Analyzed Resume",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = if (isDark) Color.White else Color.Black
                        )
                    }

                    Spacer(Modifier.height(16.dp))

                    Text(
                        resume.fileName,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Medium,
                        color = if (isDark) Color.White.copy(0.9f) else Color.Black,
                        maxLines = 2
                    )

                    Spacer(Modifier.height(8.dp))

                    Text(
                        "Analyzed on ${formatDate(resume.uploadedDate)}",
                        fontSize = 14.sp,
                        color = if (isDark) Color.White.copy(0.6f) else Color.Gray
                    )
                }
            }
        }
    }
}

@Composable
fun AnimatedCircularProgress(
    score: Int,
    isDark: Boolean
) {
    var animatedProgress by remember { mutableStateOf(0f) }

    LaunchedEffect(score) {
        animatedProgress = score / 100f
    }

    val animatedValue by animateFloatAsState(
        targetValue = animatedProgress,
        animationSpec = tween(durationMillis = 1500, easing = FastOutSlowInEasing),
        label = "score_animation"
    )

    Box(
        modifier = Modifier.size(200.dp),
        contentAlignment = Alignment.Center
    ) {
        Canvas(modifier = Modifier.size(200.dp)) {
            drawCircle(
                color = if (isDark) Color.White.copy(0.1f) else Color.LightGray.copy(0.3f),
                style = Stroke(width = 20.dp.toPx(), cap = StrokeCap.Round)
            )

            drawArc(
                brush = Brush.sweepGradient(
                    listOf(
                        Color(0xFF6C63FF),
                        Color(0xFF764ba2),
                        Color(0xFF6C63FF)
                    )
                ),
                startAngle = -90f,
                sweepAngle = 360f * animatedValue,
                useCenter = false,
                style = Stroke(width = 20.dp.toPx(), cap = StrokeCap.Round)
            )
        }

        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                "${(animatedValue * 100).toInt()}",
                fontSize = 56.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF6C63FF)
            )
            Text(
                "out of 100",
                fontSize = 14.sp,
                color = if (isDark) Color.White.copy(0.6f) else Color.Gray
            )
        }
    }
}

@Composable
fun ScoreBreakdownItem(
    title: String,
    score: Int,
    isDark: Boolean
) {
    Column(modifier = Modifier.padding(vertical = 8.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                title,
                fontSize = 14.sp,
                color = if (isDark) Color.White.copy(0.8f) else Color(0xFF1a1a2e)
            )
            Text(
                "$score%",
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                color = if (isDark) Color.White else Color.Black
            )
        }

        Spacer(Modifier.height(8.dp))

        LinearProgressIndicator(
            progress = score / 100f,
            modifier = Modifier
                .fillMaxWidth()
                .height(8.dp)
                .clip(RoundedCornerShape(4.dp)),
            color = when {
                score >= 80 -> Color(0xFF4CAF50)
                score >= 60 -> Color(0xFFFF9800)
                else -> Color(0xFFF44336)
            },
            trackColor = if (isDark) Color.White.copy(0.1f) else Color.LightGray.copy(0.3f)
        )
    }
}

@Composable
fun ATSStatCard(
    title: String,
    value: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    color: Color,
    isDark: Boolean,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.height(110.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (isDark) Color(0xFF2a2a3e) else Color.White
        ),
        elevation = CardDefaults.cardElevation(8.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Icon(
                icon,
                contentDescription = null,
                tint = color,
                modifier = Modifier.size(32.dp)
            )

            Column {
                Text(
                    value,
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Bold,
                    color = if (isDark) Color.White else Color.Black
                )
                Text(
                    title,
                    fontSize = 12.sp,
                    color = if (isDark) Color.White.copy(0.7f) else Color.Gray
                )
            }
        }
    }
}

fun getScoreMessage(score: Int): Pair<String, String> {
    return when {
        score >= 90 -> Pair("Excellent! Your resume is ATS-ready", "🎉")
        score >= 80 -> Pair("Great! Minor improvements needed", "✨")
        score >= 70 -> Pair("Good, but can be improved", "👍")
        score >= 60 -> Pair("Fair, needs optimization", "💪")
        else -> Pair("Needs significant improvement", "📝")
    }
}

fun formatDate(timestamp: Long): String {
    val sdf = java.text.SimpleDateFormat("MMM dd, yyyy", java.util.Locale.getDefault())
    return sdf.format(java.util.Date(timestamp))
}