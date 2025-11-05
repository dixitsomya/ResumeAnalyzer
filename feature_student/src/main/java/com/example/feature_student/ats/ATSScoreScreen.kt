package com.example.feature_student.ats

import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import kotlinx.coroutines.launch

@Composable
fun ATSScoreScreen(
    modifier: Modifier = Modifier,
    isDark: Boolean,
    viewModel: ATSViewModel = viewModel(),
    onNavigateToSuggestions: () -> Unit = {}
) {
    val context = LocalContext.current
    val latestResume by viewModel.latestResume.collectAsState()
    val analysisResult by viewModel.analysisResult.collectAsState()
    val scope = rememberCoroutineScope()
    var showDownloadDialog by remember { mutableStateOf(false) }
    var isDownloading by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        viewModel.refresh()
    }

    val backgroundBrush = if (isDark) {
        Brush.verticalGradient(listOf(Color(0xFF1a1a2e), Color(0xFF16213e)))
    } else {
        Brush.verticalGradient(listOf(Color(0xFFf0f4f8), Color(0xFFe4e9f2)))
    }

    Box(modifier = modifier.fillMaxSize().background(backgroundBrush)) {
        if (latestResume == null || analysisResult == null) {
            EmptyATSState(isDark = isDark)
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize().padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                contentPadding = PaddingValues(bottom = 20.dp)
            ) {
                // Main Score Card
                item {
                    ScoreCard(analysisResult!!, isDark)
                }

                // Score Breakdown
                item {
                    ScoreBreakdownCard(analysisResult!!, isDark)
                }

                // Sections Status
                item {
                    SectionsCard(analysisResult!!, isDark)
                }

                // Strengths
                item {
                    StrengthsCard(analysisResult!!, isDark)
                }

                // Weaknesses
                item {
                    WeaknessesCard(analysisResult!!, isDark)
                }

                // Keywords Preview
                item {
                    KeywordsPreviewCard(analysisResult!!, isDark)
                }

                // Report Preview (Half)
                item {
                    ReportPreviewCard(analysisResult!!, isDark)
                }

                // Download Banner
                item {
                    DownloadBannerCard(isDark) {
                        showDownloadDialog = true
                    }
                }

                // Action Buttons
                item {
                    ActionButtonsCard(
                        onNavigateToSuggestions = onNavigateToSuggestions,
                        onDownloadReport = { showDownloadDialog = true }
                    )
                }
            }
        }
    }

    if (showDownloadDialog && analysisResult != null && latestResume != null) {
        DownloadDialogBox(
            isDark = isDark,
            isDownloading = isDownloading,
            onConfirm = {
                isDownloading = true
                scope.launch {
                    try {
                        val reportGenerator = ReportGenerator(context)
                        val file = reportGenerator.generatePDFReport(latestResume!!, analysisResult!!)
                        if (file != null) {
                            reportGenerator.openPDFReport(file)
                        }
                    } catch (e: Exception) {
                        e.printStackTrace()
                    } finally {
                        isDownloading = false
                        showDownloadDialog = false
                    }
                }
            },
            onDismiss = { showDownloadDialog = false }
        )
    }
}

@Composable
fun EmptyATSState(isDark: Boolean) {
    Column(
        modifier = Modifier.fillMaxSize().padding(24.dp),
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
            textAlign = TextAlign.Center
        )
    }
}

@Composable
fun ScoreCard(analysis: com.example.feature_student.model.ATSAnalysisResult, isDark: Boolean) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (isDark) Color(0xFF2a2a3e) else Color.White
        ),
        elevation = CardDefaults.cardElevation(12.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxWidth().padding(28.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                "Your ATS Score",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = if (isDark) Color.White else Color(0xFF1a1a2e)
            )

            Spacer(Modifier.height(20.dp))

            AnimatedCircularProgress(analysis.overallScore, isDark)

            Spacer(Modifier.height(20.dp))

            val (message, emoji) = getScoreMessage(analysis.overallScore)
            Text(
                "$emoji $message",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = if (isDark) Color.White else Color(0xFF1a1a2e),
                textAlign = TextAlign.Center
            )

            Spacer(Modifier.height(12.dp))

            Text(
                "Analyzed by ${analysis.analyzedBy}",
                fontSize = 11.sp,
                color = if (isDark) Color.White.copy(0.6f) else Color.Gray
            )
        }
    }
}

@Composable
fun ScoreBreakdownCard(analysis: com.example.feature_student.model.ATSAnalysisResult, isDark: Boolean) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (isDark) Color(0xFF2a2a3e) else Color.White
        ),
        elevation = CardDefaults.cardElevation(8.dp)
    ) {
        Column(modifier = Modifier.fillMaxWidth().padding(16.dp)) {
            Text(
                "📊 Score Breakdown",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = if (isDark) Color.White else Color.Black
            )

            Spacer(Modifier.height(12.dp))

            ScoreBreakdownItem("Format", analysis.breakdown.formatScore, isDark)
            ScoreBreakdownItem("Keywords", analysis.breakdown.keywordScore, isDark)
            ScoreBreakdownItem("Content", analysis.breakdown.contentScore, isDark)
            ScoreBreakdownItem("Structure", analysis.breakdown.structureScore, isDark)
            ScoreBreakdownItem("Contact", analysis.breakdown.contactScore, isDark)
        }
    }
}

@Composable
fun SectionsCard(analysis: com.example.feature_student.model.ATSAnalysisResult, isDark: Boolean) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (isDark) Color(0xFF2a2a3e) else Color.White
        ),
        elevation = CardDefaults.cardElevation(8.dp)
    ) {
        Column(modifier = Modifier.fillMaxWidth().padding(16.dp)) {
            Text(
                "✅ Resume Sections",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = if (isDark) Color.White else Color.Black
            )

            Spacer(Modifier.height(8.dp))

            val sections = analysis.sections
            SectionCheckItem("Contact Info", sections.hasContactInfo, isDark)
            SectionCheckItem("Summary", sections.hasObjective, isDark)
            SectionCheckItem("Experience", sections.hasExperience, isDark)
            SectionCheckItem("Education", sections.hasEducation, isDark)
            SectionCheckItem("Skills", sections.hasSkills, isDark)
            SectionCheckItem("Certifications", sections.hasCertifications, isDark)
        }
    }
}

@Composable
fun StrengthsCard(analysis: com.example.feature_student.model.ATSAnalysisResult, isDark: Boolean) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (isDark) Color(0xFF2a2a3e) else Color.White
        ),
        elevation = CardDefaults.cardElevation(8.dp)
    ) {
        Column(modifier = Modifier.fillMaxWidth().padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.CheckCircle, null, tint = Color(0xFF4CAF50), modifier = Modifier.size(20.dp))
                Spacer(Modifier.width(8.dp))
                Text("Strengths", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = Color(0xFF4CAF50))
            }

            Spacer(Modifier.height(8.dp))

            analysis.strengths.take(3).forEach { strength ->
                Text("✓ $strength", fontSize = 12.sp, color = if (isDark) Color.White.copy(0.7f) else Color.Gray, lineHeight = 16.sp)
                Spacer(Modifier.height(4.dp))
            }
        }
    }
}

@Composable
fun WeaknessesCard(analysis: com.example.feature_student.model.ATSAnalysisResult, isDark: Boolean) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (isDark) Color(0xFF2a2a3e) else Color.White
        ),
        elevation = CardDefaults.cardElevation(8.dp)
    ) {
        Column(modifier = Modifier.fillMaxWidth().padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.WarningAmber, null, tint = Color(0xFFF44336), modifier = Modifier.size(20.dp))
                Spacer(Modifier.width(8.dp))
                Text("Areas to Improve", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = Color(0xFFF44336))
            }

            Spacer(Modifier.height(8.dp))

            analysis.weaknesses.take(3).forEach { weakness ->
                Text("→ $weakness", fontSize = 12.sp, color = if (isDark) Color.White.copy(0.7f) else Color.Gray, lineHeight = 16.sp)
                Spacer(Modifier.height(4.dp))
            }
        }
    }
}

@Composable
fun KeywordsPreviewCard(analysis: com.example.feature_student.model.ATSAnalysisResult, isDark: Boolean) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (isDark) Color(0xFF2a2a3e) else Color.White
        ),
        elevation = CardDefaults.cardElevation(8.dp)
    ) {
        Column(modifier = Modifier.fillMaxWidth().padding(16.dp)) {
            Text("🔑 Keywords Found", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = Color(0xFF4CAF50))
            Text(analysis.keywords.foundKeywords.take(8).joinToString(", "), fontSize = 11.sp, color = if (isDark) Color.White.copy(0.6f) else Color.Gray)

            Spacer(Modifier.height(8.dp))

            Text("Missing Keywords", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = Color(0xFFFF9800))
            Text(analysis.keywords.missingKeywords.take(6).joinToString(", "), fontSize = 11.sp, color = if (isDark) Color.White.copy(0.6f) else Color.Gray)
        }
    }
}

@Composable
fun ReportPreviewCard(analysis: com.example.feature_student.model.ATSAnalysisResult, isDark: Boolean) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (isDark) Color(0xFF2a2a3e) else Color.White
        ),
        elevation = CardDefaults.cardElevation(8.dp)
    ) {
        Column(modifier = Modifier.fillMaxWidth().padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.Description, null, tint = Color(0xFF6C63FF), modifier = Modifier.size(20.dp))
                Spacer(Modifier.width(8.dp))
                Text("📋 Report Preview", fontSize = 16.sp, fontWeight = FontWeight.Bold)
            }

            Spacer(Modifier.height(12.dp))

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(12.dp))
                    .background(
                        Brush.verticalGradient(
                            listOf(
                                Color(0xFF6C63FF).copy(alpha = 0.05f),
                                Color(0xFF6C63FF).copy(alpha = 0.1f)
                            )
                        )
                    )
                    .border(1.dp, Color(0xFF6C63FF).copy(alpha = 0.3f), RoundedCornerShape(12.dp))
                    .padding(14.dp)
                    .heightIn(max = 180.dp)
                    .verticalScroll(rememberScrollState())
            ) {
                Column {
                    Text(
                        "ATS ANALYSIS REPORT",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF6C63FF)
                    )
                    Text(
                        "─".repeat(40),
                        fontSize = 10.sp,
                        color = Color(0xFF6C63FF).copy(alpha = 0.5f)
                    )

                    Spacer(Modifier.height(8.dp))

                    Text(
                        analysis.detailedReport,
                        fontSize = 10.sp,
                        color = if (isDark) Color.White.copy(0.75f) else Color.Gray.copy(0.8f),
                        lineHeight = 14.sp,
                        fontFamily = androidx.compose.ui.text.font.FontFamily.Monospace
                    )
                }
            }

            Spacer(Modifier.height(10.dp))

            Text(
                "⬇️ Download full report PDF to see complete analysis",
                fontSize = 9.sp,
                color = Color(0xFF6C63FF),
                fontWeight = FontWeight.Bold,
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
fun DownloadBannerCard(isDark: Boolean, onDownload: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp)),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFF6C63FF)
        ),
        elevation = CardDefaults.cardElevation(8.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text("Complete Report Ready", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = Color.White)
                Text("Get detailed PDF analysis", fontSize = 11.sp, color = Color.White.copy(0.8f))
            }

            Button(
                onClick = onDownload,
                colors = ButtonDefaults.buttonColors(containerColor = Color.White),
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier.height(40.dp)
            ) {
                Icon(Icons.Default.Download, null, tint = Color(0xFF6C63FF), modifier = Modifier.size(18.dp))
                Spacer(Modifier.width(4.dp))
                Text("Download", fontSize = 12.sp, color = Color(0xFF6C63FF), fontWeight = FontWeight.Bold)
            }
        }
    }
}

@Composable
fun ActionButtonsCard(onNavigateToSuggestions: () -> Unit, onDownloadReport: () -> Unit) {
    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
        Button(
            onClick = onNavigateToSuggestions,
            modifier = Modifier.fillMaxWidth().height(50.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF6C63FF)),
            shape = RoundedCornerShape(12.dp),
            elevation = ButtonDefaults.buttonElevation(8.dp)
        ) {
            Icon(Icons.Default.TipsAndUpdates, null, modifier = Modifier.size(20.dp))
            Spacer(Modifier.width(10.dp))
            Text("View All Suggestions", fontSize = 15.sp, fontWeight = FontWeight.Bold)
        }
    }
}

@Composable
fun DownloadDialogBox(isDark: Boolean, isDownloading: Boolean, onConfirm: () -> Unit, onDismiss: () -> Unit) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.FileDownload, null, modifier = Modifier.size(24.dp), tint = Color(0xFF6C63FF))
                Spacer(Modifier.width(8.dp))
                Text("Download PDF Report")
            }
        },
        text = { Text("Your complete ATS analysis report will be saved to Downloads folder.") },
        confirmButton = {
            Button(
                onClick = onConfirm,
                enabled = !isDownloading,
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF6C63FF))
            ) {
                if (isDownloading) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(16.dp),
                        color = Color.White,
                        strokeWidth = 2.dp
                    )
                    Spacer(Modifier.width(8.dp))
                    Text("Generating...", color = Color.White)
                } else {
                    Text("Download", color = Color.White)
                }
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss, enabled = !isDownloading) {
                Text("Cancel")
            }
        }
    )
}

@Composable
fun AnimatedCircularProgress(score: Int, isDark: Boolean) {
    var animatedProgress by remember { mutableStateOf(0f) }

    LaunchedEffect(score) {
        animatedProgress = score / 100f
    }

    val animatedValue by animateFloatAsState(
        targetValue = animatedProgress,
        animationSpec = tween(durationMillis = 1500, easing = FastOutSlowInEasing),
        label = "score_animation"
    )

    Box(modifier = Modifier.size(180.dp), contentAlignment = Alignment.Center) {
        Canvas(modifier = Modifier.size(180.dp)) {
            drawCircle(
                color = if (isDark) Color.White.copy(0.1f) else Color.LightGray.copy(0.3f),
                style = Stroke(width = 18.dp.toPx(), cap = StrokeCap.Round)
            )

            drawArc(
                brush = Brush.sweepGradient(listOf(Color(0xFF6C63FF), Color(0xFF764ba2), Color(0xFF6C63FF))),
                startAngle = -90f,
                sweepAngle = 360f * animatedValue,
                useCenter = false,
                style = Stroke(width = 18.dp.toPx(), cap = StrokeCap.Round)
            )
        }

        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text("${(animatedValue * 100).toInt()}", fontSize = 48.sp, fontWeight = FontWeight.Bold, color = Color(0xFF6C63FF))
            Text("/ 100", fontSize = 12.sp, color = if (isDark) Color.White.copy(0.6f) else Color.Gray)
        }
    }
}

@Composable
fun ScoreBreakdownItem(title: String, score: Int, isDark: Boolean) {
    Column(modifier = Modifier.padding(vertical = 6.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(title, fontSize = 12.sp, color = if (isDark) Color.White.copy(0.7f) else Color(0xFF1a1a2e))
            Text("$score%", fontSize = 12.sp, fontWeight = FontWeight.Bold)
        }
        LinearProgressIndicator(
            progress = score / 100f,
            modifier = Modifier.fillMaxWidth().height(6.dp).clip(RoundedCornerShape(3.dp)),
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
fun SectionCheckItem(name: String, isPresent: Boolean, isDark: Boolean) {
    Row(
        modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(name, fontSize = 12.sp, color = if (isDark) Color.White.copy(0.7f) else Color.Gray)
        Text(if (isPresent) "✅" else "❌", fontSize = 14.sp)
    }
}

fun getScoreMessage(score: Int): Pair<String, String> {
    return when {
        score >= 80 -> Pair("Excellent! ATS-Ready", "🎉")
        score >= 70 -> Pair("Good! Minor Improvements", "✨")
        score >= 60 -> Pair("Fair! Some Optimization", "👍")
        else -> Pair("Needs Significant Work", "📝")
    }
}