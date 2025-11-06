package com.example.feature_recruiter.screens

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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.feature_recruiter.database.entity.RecruiterResumeEntity
import com.example.feature_recruiter.viewmodel.RecruiterResumeViewModel

@Composable
fun RecruiterDashboardScreen(
    userEmail: String,
    isDark: Boolean,
    viewModel: RecruiterResumeViewModel,
    onNavigateToFilter: () -> Unit,
    modifier: Modifier = Modifier
) {
    val totalCount by viewModel.totalCount.collectAsState()
    val recentResumes by viewModel.filteredResumes.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.getRecentResumes(5)
    }

    val backgroundBrush = if (!isDark) {
        Brush.verticalGradient(
            listOf(Color(0xFF4A90E2), Color(0xFF357ABD))
        )
    } else {
        Brush.verticalGradient(
            listOf(Color(0xFF1a1a2e), Color(0xFF2D1B69))
        )
    }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(backgroundBrush)
            .padding(20.dp),
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        // Welcome Card
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(
                    containerColor = Color.White.copy(alpha = if (isDark) 0.1f else 0.95f)
                ),
                elevation = CardDefaults.cardElevation(8.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(60.dp)
                            .clip(CircleShape)
                            .background(Color(0xFF4A90E2)),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            userEmail.firstOrNull()?.uppercase() ?: "R",
                            color = Color.White,
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                    Spacer(Modifier.width(16.dp))
                    Column {
                        Text(
                            "Welcome back!",
                            fontSize = 16.sp,
                            color = if (isDark) Color.White.copy(0.7f) else Color.Gray,
                            fontWeight = FontWeight.Medium
                        )
                        Text(
                            "Talent Manager",
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Bold,
                            color = if (isDark) Color.White else Color.Black
                        )
                    }
                }
            }
        }

        // Stats Card
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(
                    containerColor = Color.White.copy(alpha = if (isDark) 0.1f else 0.95f)
                ),
                elevation = CardDefaults.cardElevation(8.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column {
                        Icon(
                            Icons.Default.Description,
                            contentDescription = null,
                            tint = Color(0xFF4A90E2),
                            modifier = Modifier.size(32.dp)
                        )
                        Spacer(Modifier.height(8.dp))
                        Text(
                            "Total Resumes",
                            fontSize = 14.sp,
                            color = if (isDark) Color.White.copy(0.7f) else Color.Gray
                        )
                        Text(
                            totalCount.toString(),
                            fontSize = 28.sp,
                            fontWeight = FontWeight.Bold,
                            color = if (isDark) Color.White else Color.Black
                        )
                    }
                    Button(
                        onClick = onNavigateToFilter,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFF4A90E2)
                        ),
                        modifier = Modifier.height(50.dp)
                    ) {
                        Icon(Icons.Default.FilterList, contentDescription = null)
                        Spacer(Modifier.width(8.dp))
                        Text("Filter")
                    }
                }
            }
        }

        // Recent Resumes Header
        item {
            Text(
                "Recent Uploads",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
        }

        // Recent Resumes List
        if (recentResumes.isEmpty()) {
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(150.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(
                            Icons.Default.CloudOff,
                            contentDescription = null,
                            modifier = Modifier.size(48.dp),
                            tint = if (isDark) Color.White.copy(0.4f) else Color.White.copy(0.7f)
                        )
                        Spacer(Modifier.height(8.dp))
                        Text(
                            "No resumes yet",
                            color = if (isDark) Color.White.copy(0.6f) else Color.White.copy(0.8f),
                            fontSize = 16.sp
                        )
                    }
                }
            }
        } else {
            items(recentResumes) { resume ->
                RecentResumeCard(resume, isDark)
            }
        }
    }
}

@Composable
fun RecentResumeCard(resume: RecruiterResumeEntity, isDark: Boolean) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp)),
        colors = CardDefaults.cardColors(
            containerColor = Color.White.copy(alpha = if (isDark) 0.1f else 0.95f)
        ),
        elevation = CardDefaults.cardElevation(6.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        resume.candidateName,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = if (isDark) Color.White else Color.Black
                    )
                    Text(
                        resume.candidateEmail,
                        fontSize = 12.sp,
                        color = if (isDark) Color.White.copy(0.6f) else Color.Gray
                    )
                }
                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = Color(0xFF4A90E2).copy(alpha = 0.2f)
                ) {
                    Text(
                        "${resume.experience}y",
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF4A90E2)
                    )
                }
            }
            Spacer(Modifier.height(8.dp))
            Text(
                resume.techStack,
                fontSize = 12.sp,
                color = if (isDark) Color.White.copy(0.7f) else Color.DarkGray,
                maxLines = 2
            )
        }
    }
}