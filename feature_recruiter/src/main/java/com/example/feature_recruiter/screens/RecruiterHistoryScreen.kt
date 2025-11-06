package com.example.feature_recruiter.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.feature_recruiter.database.entity.RecruiterResumeEntity
import com.example.feature_recruiter.viewmodel.RecruiterResumeViewModel
import java.text.SimpleDateFormat
import java.util.*
import kotlin.String

@Composable
fun RecruiterHistoryScreen(
    isDark: Boolean,
    viewModel: RecruiterResumeViewModel,
    modifier: Modifier = Modifier
) {
    var searchQuery by remember { mutableStateOf("") }

    val allResumes by viewModel.allResumes.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()

    val filteredResumes = if (searchQuery.isBlank()) {
        allResumes
    } else {
        allResumes.filter { resume ->
            resume.candidateName.contains(searchQuery, ignoreCase = true) ||
                    resume.candidateEmail.contains(searchQuery, ignoreCase = true) ||
                    resume.techStack.contains(searchQuery, ignoreCase = true)
        }
    }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(if (isDark) Color(0xFF121212) else Color(0xFFF5F6FA))
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Text(
                "Upload History",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = if (isDark) Color.White else Color.Black
            )
        }

        // Search Bar
        item {
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                label = { Text("Search by name, email, or tech") },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
                trailingIcon = if (searchQuery.isNotEmpty()) {
                    {
                        IconButton(onClick = { searchQuery = "" }) {
                            Icon(Icons.Default.Close, contentDescription = "Clear")
                        }
                    }
                } else null,
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Color(0xFF4A90E2),
                    unfocusedBorderColor = if (isDark) Color.White.copy(0.3f) else Color.Gray.copy(0.3f)
                )
            )
        }

        // Count
        item {
            Text(
                "Total: ${filteredResumes.size} resumes",
                fontSize = 14.sp,
                color = if (isDark) Color.White.copy(0.7f) else Color.Gray
            )
        }

        // History List
        if (isLoading) {
            item {
                Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }
            }
        } else if (filteredResumes.isEmpty()) {
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(150.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(
                            Icons.Default.HistoryToggleOff,
                            contentDescription = null,
                            modifier = Modifier.size(48.dp),
                            tint = if (isDark) Color.White.copy(0.4f) else Color.Gray
                        )
                        Spacer(Modifier.height(8.dp))
                        Text(
                            "No history found",
                            color = if (isDark) Color.White.copy(0.6f) else Color.Gray
                        )
                    }
                }
            }
        } else {
            items(filteredResumes) { resume ->
                HistoryResumeCard(resume, isDark)
            }
        }
    }
}

@Composable
fun HistoryResumeCard(resume: RecruiterResumeEntity, isDark: Boolean) {
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
                    Text(
                        formatDate(resume.uploadedDate),
                        fontSize = 11.sp,
                        color = if (isDark) Color.White.copy(0.5f) else Color.Gray
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
            Spacer(Modifier.height(8.dp))
            Text(
                "File: ${resume.fileName}",
                fontSize = 11.sp,
                color = if (isDark) Color.White.copy(0.5f) else Color.Gray,
                maxLines = 1
            )
        }
    }
}

private fun formatDate(timestamp: Long): String {
    val sdf = SimpleDateFormat("MMM dd, yyyy - HH:mm", Locale.getDefault())
    return sdf.format(Date(timestamp))
}