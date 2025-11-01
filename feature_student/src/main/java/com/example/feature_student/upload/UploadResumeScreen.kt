package com.example.feature_student.upload

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.feature_student.data.LocalResumeDatabase

@Composable
fun UploadResumeScreen(
    modifier: Modifier = Modifier,
    isDark: Boolean,
    viewModel: UploadViewModel = viewModel(),
    onNavigateToHome: () -> Unit = {},
    onNavigateToSuggestions: () -> Unit = {}
) {
    val context = LocalContext.current

    val filePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let {
            val fileName = getFileName(context, it)
            viewModel.uploadResume(context, it, fileName)
        }
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
            .padding(20.dp)
    ) {
        when (val state = viewModel.uploadState) {
            is UploadState.Idle -> {
                EmptyUploadState(
                    isDark = isDark,
                    onUploadClick = { filePickerLauncher.launch("application/pdf") }
                )
            }
            is UploadState.Uploading -> {
                LoadingState(
                    message = "Uploading your resume...",
                    isDark = isDark
                )
            }
            is UploadState.Success -> {
                UploadSuccessState(
                    resume = state.resume,
                    isDark = isDark,
                    onAnalyzeClick = {
                        viewModel.analyzeResume { result ->
                            // Store resume ID for navigation
                            LocalResumeDatabase.setSelectedResume(result.resumeId)
                        }
                    },
                    onUploadNew = {
                        viewModel.resetState()
                    },
                    getFileSize = { viewModel.getFileSize(it) }
                )
            }
            is UploadState.Analyzing -> {
                LoadingState(
                    message = "Analyzing your resume with AI...",
                    subMessage = "Extracting text and calculating ATS score...",
                    isDark = isDark
                )
            }
            is UploadState.Analyzed -> {
                AnalyzedState(
                    result = state.result,
                    isDark = isDark,
                    onUploadNew = { viewModel.resetState() },
                    onViewSuggestions = onNavigateToSuggestions,
                    onGoHome = onNavigateToHome,
                    getFileSize = { viewModel.getFileSize(it) }
                )
            }
            is UploadState.Error -> {
                ErrorState(
                    message = state.message,
                    isDark = isDark,
                    onRetry = { viewModel.resetState() }
                )
            }
        }
    }
}

@Composable
fun EmptyUploadState(
    isDark: Boolean,
    onUploadClick: () -> Unit
) {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        val infiniteTransition = rememberInfiniteTransition(label = "upload_anim")
        val scale by infiniteTransition.animateFloat(
            initialValue = 1f,
            targetValue = 1.1f,
            animationSpec = infiniteRepeatable(
                animation = tween(1000),
                repeatMode = RepeatMode.Reverse
            ),
            label = "scale"
        )

        Card(
            modifier = Modifier
                .size(180.dp)
                .scale(scale),
            shape = CircleShape,
            colors = CardDefaults.cardColors(
                containerColor = if (isDark) Color(0xFF6C63FF).copy(0.2f) else Color(0xFF6C63FF).copy(0.1f)
            ),
            elevation = CardDefaults.cardElevation(8.dp)
        ) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    Icons.Default.CloudUpload,
                    contentDescription = null,
                    modifier = Modifier.size(80.dp),
                    tint = Color(0xFF6C63FF)
                )
            }
        }

        Spacer(Modifier.height(32.dp))

        Text(
            "Upload Your Resume",
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold,
            color = if (isDark) Color.White else Color(0xFF1a1a2e)
        )

        Spacer(Modifier.height(12.dp))

        Text(
            "Get FREE ATS Score & Suggestions",
            fontSize = 16.sp,
            color = Color(0xFF6C63FF),
            fontWeight = FontWeight.Medium
        )

        Spacer(Modifier.height(8.dp))

        Text(
            "Support formats: PDF",
            fontSize = 14.sp,
            color = if (isDark) Color.White.copy(0.6f) else Color.Gray,
            textAlign = TextAlign.Center
        )

        Spacer(Modifier.height(8.dp))

        Text(
            "Maximum file size: 5MB",
            fontSize = 14.sp,
            color = if (isDark) Color.White.copy(0.6f) else Color.Gray
        )

        Spacer(Modifier.height(40.dp))

        Button(
            onClick = onUploadClick,
            modifier = Modifier
                .fillMaxWidth(0.8f)
                .height(56.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFF6C63FF)
            ),
            shape = RoundedCornerShape(16.dp),
            elevation = ButtonDefaults.buttonElevation(8.dp)
        ) {
            Icon(
                Icons.Default.UploadFile,
                contentDescription = null,
                modifier = Modifier.size(24.dp)
            )
            Spacer(Modifier.width(12.dp))
            Text(
                "Choose File",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Composable
fun LoadingState(
    message: String,
    subMessage: String = "",
    isDark: Boolean
) {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        CircularProgressIndicator(
            modifier = Modifier.size(80.dp),
            color = Color(0xFF6C63FF),
            strokeWidth = 6.dp
        )

        Spacer(Modifier.height(24.dp))

        Text(
            message,
            fontSize = 18.sp,
            fontWeight = FontWeight.Medium,
            color = if (isDark) Color.White else Color(0xFF1a1a2e),
            textAlign = TextAlign.Center
        )

        if (subMessage.isNotEmpty()) {
            Spacer(Modifier.height(8.dp))
            Text(
                subMessage,
                fontSize = 14.sp,
                color = if (isDark) Color.White.copy(0.6f) else Color.Gray,
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
fun UploadSuccessState(
    resume: com.example.feature_student.model.Resume,
    isDark: Boolean,
    onAnalyzeClick: () -> Unit,
    onUploadNew: () -> Unit,
    getFileSize: (Long) -> String
) {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Card(
            modifier = Modifier.size(120.dp),
            shape = CircleShape,
            colors = CardDefaults.cardColors(
                containerColor = Color(0xFF4CAF50).copy(0.2f)
            )
        ) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    Icons.Default.CheckCircle,
                    contentDescription = null,
                    modifier = Modifier.size(60.dp),
                    tint = Color(0xFF4CAF50)
                )
            }
        }

        Spacer(Modifier.height(24.dp))

        Text(
            "Upload Successful!",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = if (isDark) Color.White else Color(0xFF1a1a2e)
        )

        Spacer(Modifier.height(32.dp))

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
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        Icons.Default.Description,
                        contentDescription = null,
                        tint = Color(0xFF6C63FF),
                        modifier = Modifier.size(40.dp)
                    )
                    Spacer(Modifier.width(16.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            resume.fileName,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = if (isDark) Color.White else Color.Black,
                            maxLines = 2
                        )
                        Spacer(Modifier.height(4.dp))
                        Text(
                            getFileSize(resume.fileSize),
                            fontSize = 14.sp,
                            color = if (isDark) Color.White.copy(0.6f) else Color.Gray
                        )
                    }
                }
            }
        }

        Spacer(Modifier.height(32.dp))

        Button(
            onClick = onAnalyzeClick,
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFF6C63FF)
            ),
            shape = RoundedCornerShape(16.dp)
        ) {
            Icon(Icons.Default.Analytics, contentDescription = null)
            Spacer(Modifier.width(12.dp))
            Text("Analyze Resume with AI", fontSize = 16.sp, fontWeight = FontWeight.Bold)
        }

        Spacer(Modifier.height(12.dp))

        OutlinedButton(
            onClick = onUploadNew,
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            shape = RoundedCornerShape(16.dp),
            border = ButtonDefaults.outlinedButtonBorder.copy(
                brush = Brush.linearGradient(listOf(Color(0xFF6C63FF), Color(0xFF6C63FF)))
            )
        ) {
            Icon(
                Icons.Default.UploadFile,
                contentDescription = null,
                tint = Color(0xFF6C63FF)
            )
            Spacer(Modifier.width(12.dp))
            Text(
                "Upload Another Resume",
                fontSize = 16.sp,
                color = Color(0xFF6C63FF),
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Composable
fun AnalyzedState(
    result: com.example.feature_student.model.ATSAnalysisResult,
    isDark: Boolean,
    onUploadNew: () -> Unit,
    onViewSuggestions: () -> Unit,
    onGoHome: () -> Unit,
    getFileSize: (Long) -> String
) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
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
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    "Analysis Complete! 🎉",
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                    color = if (isDark) Color.White else Color(0xFF1a1a2e)
                )

                Spacer(Modifier.height(16.dp))

                Box(
                    modifier = Modifier
                        .size(160.dp)
                        .clip(CircleShape)
                        .background(
                            Brush.radialGradient(
                                listOf(
                                    Color(0xFF6C63FF).copy(0.3f),
                                    Color(0xFF6C63FF).copy(0.1f)
                                )
                            )
                        )
                        .border(8.dp, Color(0xFF6C63FF), CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            "${result.overallScore}",
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

                Spacer(Modifier.height(16.dp))

                val scoreMessage = when {
                    result.overallScore >= 80 -> "Excellent! Your resume is ATS-ready 🎉"
                    result.overallScore >= 60 -> "Good, but can be improved 👍"
                    else -> "Needs significant improvement 💪"
                }

                Text(
                    scoreMessage,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium,
                    color = if (isDark) Color.White else Color(0xFF1a1a2e),
                    textAlign = TextAlign.Center
                )

                Spacer(Modifier.height(16.dp))

                Text(
                    "${result.suggestions.size} suggestions found",
                    fontSize = 14.sp,
                    color = Color(0xFF6C63FF),
                    fontWeight = FontWeight.Medium
                )
            }
        }

        Spacer(Modifier.weight(1f))

        Button(
            onClick = onViewSuggestions,
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFF6C63FF)
            ),
            shape = RoundedCornerShape(16.dp),
            elevation = ButtonDefaults.buttonElevation(8.dp)
        ) {
            Icon(Icons.Default.TipsAndUpdates, contentDescription = null)
            Spacer(Modifier.width(12.dp))
            Text("View Suggestions", fontSize = 16.sp, fontWeight = FontWeight.Bold)
        }

        Spacer(Modifier.height(12.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            OutlinedButton(
                onClick = onGoHome,
                modifier = Modifier
                    .weight(1f)
                    .height(56.dp),
                shape = RoundedCornerShape(16.dp),
                border = ButtonDefaults.outlinedButtonBorder.copy(
                    brush = Brush.linearGradient(listOf(Color(0xFF6C63FF), Color(0xFF6C63FF)))
                )
            ) {
                Icon(
                    Icons.Default.Home,
                    contentDescription = null,
                    tint = Color(0xFF6C63FF)
                )
                Spacer(Modifier.width(8.dp))
                Text(
                    "Go Home",
                    fontSize = 15.sp,
                    color = Color(0xFF6C63FF),
                    fontWeight = FontWeight.Bold
                )
            }

            OutlinedButton(
                onClick = onUploadNew,
                modifier = Modifier
                    .weight(1f)
                    .height(56.dp),
                shape = RoundedCornerShape(16.dp),
                border = ButtonDefaults.outlinedButtonBorder.copy(
                    brush = Brush.linearGradient(listOf(Color(0xFF6C63FF), Color(0xFF6C63FF)))
                )
            ) {
                Icon(
                    Icons.Default.UploadFile,
                    contentDescription = null,
                    tint = Color(0xFF6C63FF)
                )
                Spacer(Modifier.width(8.dp))
                Text(
                    "Upload New",
                    fontSize = 15.sp,
                    color = Color(0xFF6C63FF),
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

@Composable
fun ErrorState(
    message: String,
    isDark: Boolean,
    onRetry: () -> Unit
) {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            Icons.Default.ErrorOutline,
            contentDescription = null,
            modifier = Modifier.size(80.dp),
            tint = Color(0xFFF44336)
        )

        Spacer(Modifier.height(24.dp))

        Text(
            "Oops! Something went wrong",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = if (isDark) Color.White else Color(0xFF1a1a2e)
        )

        Spacer(Modifier.height(12.dp))

        Text(
            message,
            fontSize = 14.sp,
            color = if (isDark) Color.White.copy(0.6f) else Color.Gray,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(horizontal = 32.dp)
        )

        Spacer(Modifier.height(32.dp))

        Button(
            onClick = onRetry,
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFF6C63FF)
            ),
            shape = RoundedCornerShape(16.dp)
        ) {
            Icon(Icons.Default.Refresh, contentDescription = null)
            Spacer(Modifier.width(8.dp))
            Text("Try Again")
        }
    }
}

fun getFileName(context: android.content.Context, uri: android.net.Uri): String {
    var result = "resume.pdf"
    val cursor = context.contentResolver.query(uri, null, null, null, null)
    cursor?.use {
        if (it.moveToFirst()) {
            val nameIndex = it.getColumnIndex(android.provider.OpenableColumns.DISPLAY_NAME)
            if (nameIndex != -1) {
                result = it.getString(nameIndex)
            }
        }
    }
    return result
}