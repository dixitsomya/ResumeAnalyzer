
package com.example.feature_student.history

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.feature_student.model.Resume
import com.example.feature_student.data.LocalResumeDatabase
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun HistoryScreen(
    modifier: Modifier = Modifier,
    isDark: Boolean,
    viewModel: HistoryViewModel = viewModel(),
    onNavigateToSuggestions: () -> Unit = {},
    onNavigateToATS: () -> Unit = {}
) {
    val resumes by viewModel.resumes.collectAsState()
    val averageScore by viewModel.averageScore.collectAsState()

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
        if (resumes.isEmpty()) {
            EmptyHistoryState(isDark = isDark)
        } else {
            HistoryContent(
                resumes = resumes,
                averageScore = averageScore,
                isDark = isDark,
                onResumeClick = { resume ->
                    // Set selected resume and navigate to suggestions
                    LocalResumeDatabase.setSelectedResume(resume.id)
                    onNavigateToSuggestions()
                },
                onATSClick = { resume ->
                    // Set selected resume and navigate to ATS
                    LocalResumeDatabase.setSelectedResume(resume.id)
                    onNavigateToATS()
                }
            )
        }
    }
}

@Composable
fun EmptyHistoryState(isDark: Boolean) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            Icons.Default.History,
            contentDescription = null,
            modifier = Modifier.size(100.dp),
            tint = if (isDark) Color(0xFF6C63FF).copy(0.5f) else Color(0xFF6C63FF)
        )

        Spacer(Modifier.height(24.dp))

        Text(
            "No History Yet",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = if (isDark) Color.White else Color(0xFF1a1a2e)
        )

        Spacer(Modifier.height(12.dp))

        Text(
            "Your analyzed resumes will\nappear here",
            fontSize = 16.sp,
            color = if (isDark) Color.White.copy(0.6f) else Color.Gray,
            textAlign = androidx.compose.ui.text.style.TextAlign.Center
        )
    }
}

@Composable
fun HistoryContent(
    resumes: List<Resume>,
    averageScore: Double,
    isDark: Boolean,
    onResumeClick: (Resume) -> Unit,
    onATSClick: (Resume) -> Unit
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        contentPadding = PaddingValues(bottom = 20.dp)
    ) {
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(
                    containerColor = if (isDark) Color(0xFF2a2a3e) else Color.White
                ),
                elevation = CardDefaults.cardElevation(12.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(50.dp)
                                .clip(CircleShape)
                                .background(
                                    Brush.radialGradient(
                                        listOf(Color(0xFF6C63FF), Color(0xFF764ba2))
                                    )
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                Icons.Default.Insights,
                                contentDescription = null,
                                tint = Color.White,
                                modifier = Modifier.size(28.dp)
                            )
                        }

                        Spacer(Modifier.width(16.dp))

                        Column {
                            Text(
                                "Analysis History",
                                fontSize = 20.sp,
                                fontWeight = FontWeight.Bold,
                                color = if (isDark) Color.White else Color.Black
                            )
                            Text(
                                "${resumes.size} resumes analyzed",
                                fontSize = 14.sp,
                                color = if (isDark) Color.White.copy(0.6f) else Color.Gray
                            )
                        }
                    }

                    Spacer(Modifier.height(20.dp))

                    Divider(color = if (isDark) Color.White.copy(0.1f) else Color.LightGray)

                    Spacer(Modifier.height(16.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceAround
                    ) {
                        StatItem(
                            label = "Total",
                            value = "${resumes.size}",
                            icon = Icons.Default.Description,
                            color = Color(0xFF6C63FF),
                            isDark = isDark
                        )

                        StatItem(
                            label = "Avg Score",
                            value = if (averageScore > 0) String.format("%.1f", averageScore) else "N/A",
                            icon = Icons.Default.Star,
                            color = Color(0xFFFFD700),
                            isDark = isDark
                        )

                        StatItem(
                            label = "Best Score",
                            value = "${resumes.maxOfOrNull { it.atsScore ?: 0 } ?: 0}",
                            icon = Icons.Default.TrendingUp,
                            color = Color(0xFF4CAF50),
                            isDark = isDark
                        )
                    }
                }
            }
        }

        item {
            Text(
                "All Resumes",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = if (isDark) Color.White else Color(0xFF1a1a2e),
                modifier = Modifier.padding(horizontal = 4.dp)
            )
        }

        items(resumes) { resume ->
            ResumeHistoryCard(
                resume = resume,
                isDark = isDark,
                onResumeClick = { onResumeClick(resume) },
                onATSClick = { onATSClick(resume) }
            )
        }
    }
}

@Composable
fun StatItem(
    label: String,
    value: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    color: Color,
    isDark: Boolean
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            icon,
            contentDescription = null,
            tint = color,
            modifier = Modifier.size(32.dp)
        )
        Spacer(Modifier.height(8.dp))
        Text(
            value,
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = if (isDark) Color.White else Color.Black
        )
        Text(
            label,
            fontSize = 12.sp,
            color = if (isDark) Color.White.copy(0.6f) else Color.Gray
        )
    }
}

@Composable
fun ResumeHistoryCard(
    resume: Resume,
    isDark: Boolean,
    onResumeClick: () -> Unit,
    onATSClick: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (isDark) Color(0xFF2a2a3e) else Color.White
        ),
        elevation = CardDefaults.cardElevation(6.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(56.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(
                            Brush.linearGradient(
                                listOf(
                                    Color(0xFF6C63FF).copy(0.2f),
                                    Color(0xFF764ba2).copy(0.2f)
                                )
                            )
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        Icons.Default.Description,
                        contentDescription = null,
                        tint = Color(0xFF6C63FF),
                        modifier = Modifier.size(32.dp)
                    )
                }

                Spacer(Modifier.width(16.dp))

                Column(
                    modifier = Modifier.weight(1f)
                ) {
                    Text(
                        resume.fileName,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold,
                        color = if (isDark) Color.White else Color.Black,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )

                    Spacer(Modifier.height(4.dp))

                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            Icons.Default.CalendarToday,
                            contentDescription = null,
                            tint = if (isDark) Color.White.copy(0.5f) else Color.Gray,
                            modifier = Modifier.size(14.dp)
                        )
                        Spacer(Modifier.width(4.dp))
                        Text(
                            formatDate(resume.uploadedDate),
                            fontSize = 12.sp,
                            color = if (isDark) Color.White.copy(0.6f) else Color.Gray
                        )
                    }

                    Spacer(Modifier.height(4.dp))

                    Text(
                        formatFileSize(resume.fileSize),
                        fontSize = 12.sp,
                        color = if (isDark) Color.White.copy(0.5f) else Color.Gray
                    )
                }

                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = getScoreColor(resume.atsScore ?: 0).copy(alpha = 0.2f),
                    modifier = Modifier.size(60.dp)
                ) {
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Text(
                            "${resume.atsScore ?: 0}",
                            fontSize = 22.sp,
                            fontWeight = FontWeight.Bold,
                            color = getScoreColor(resume.atsScore ?: 0)
                        )
                        Text(
                            "Score",
                            fontSize = 10.sp,
                            color = getScoreColor(resume.atsScore ?: 0).copy(0.8f)
                        )
                    }
                }
            }

            Divider(
                color = if (isDark) Color.White.copy(0.1f) else Color.LightGray,
                modifier = Modifier.padding(horizontal = 16.dp)
            )

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(12.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Button(
                    onClick = onResumeClick,
                    modifier = Modifier
                        .weight(1f)
                        .height(40.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF6C63FF)
                    ),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Icon(
                        Icons.Default.TipsAndUpdates,
                        contentDescription = null,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(Modifier.width(4.dp))
                    Text(
                        "Suggestions",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                OutlinedButton(
                    onClick = onATSClick,
                    modifier = Modifier
                        .weight(1f)
                        .height(40.dp),
                    shape = RoundedCornerShape(12.dp),
                    border = ButtonDefaults.outlinedButtonBorder.copy(
                        brush = Brush.linearGradient(listOf(Color(0xFF6C63FF), Color(0xFF6C63FF)))
                    )
                ) {
                    Icon(
                        Icons.Default.Assessment,
                        contentDescription = null,
                        modifier = Modifier.size(16.dp),
                        tint = Color(0xFF6C63FF)
                    )
                    Spacer(Modifier.width(4.dp))
                    Text(
                        "View ATS",
                        fontSize = 12.sp,
                        color = Color(0xFF6C63FF),
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}

fun getScoreColor(score: Int): Color {
    return when {
        score >= 80 -> Color(0xFF4CAF50)
        score >= 60 -> Color(0xFFFF9800)
        else -> Color(0xFFF44336)
    }
}

fun formatDate(timestamp: Long): String {
    val sdf = SimpleDateFormat("MMM dd, yyyy", Locale.getDefault())
    return sdf.format(Date(timestamp))
}

fun formatFileSize(bytes: Long): String {
    return when {
        bytes < 1024 -> "$bytes B"
        bytes < 1024 * 1024 -> "${bytes / 1024} KB"
        else -> String.format("%.2f MB", bytes / (1024.0 * 1024.0))
    }
}