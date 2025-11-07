package com.example.feature_recruiter.screens

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import com.example.feature_recruiter.database.entity.SearchHistoryEntity
import com.example.feature_recruiter.viewmodel.RecruiterResumeViewModel
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun RecruiterHistoryScreen(
    isDark: Boolean,
    viewModel: RecruiterResumeViewModel,
    modifier: Modifier = Modifier
) {
    var searchQuery by remember { mutableStateOf("") }
    var selectedTab by remember { mutableStateOf(0) }  // 0 = Resumes, 1 = Search History

    val allResumes by viewModel.allResumes.collectAsState()
    val searchHistory by viewModel.searchHistory.collectAsState()
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
                "History",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = if (isDark) Color.White else Color.Black
            )
        }

        // Tab Row
        item {
            TabRow(
                selectedTabIndex = selectedTab,
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(12.dp)),
                containerColor = if (isDark) Color(0xFF1E1E2E) else Color.White,
                indicator = { }
            ) {
                Tab(
                    selected = selectedTab == 0,
                    onClick = { selectedTab = 0 },
                    text = { Text("📄 Resumes (${allResumes.size})") },
                    selectedContentColor = Color.White,
                    unselectedContentColor = if (isDark) Color.White.copy(0.6f) else Color.Gray,
                    modifier = if (selectedTab == 0) {
                        Modifier.background(Color(0xFF4A90E2))
                    } else {
                        Modifier.background(Color.Transparent)
                    }
                )
                Tab(
                    selected = selectedTab == 1,
                    onClick = { selectedTab = 1 },
                    text = { Text("🔍 Searches (${searchHistory.size})") },
                    selectedContentColor = Color.White,
                    unselectedContentColor = if (isDark) Color.White.copy(0.6f) else Color.Gray,
                    modifier = if (selectedTab == 1) {
                        Modifier.background(Color(0xFF4A90E2))
                    } else {
                        Modifier.background(Color.Transparent)
                    }
                )
            }
        }

        // Tab 0: Resumes Upload History
        if (selectedTab == 0) {
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

            item {
                Text(
                    "Total: ${filteredResumes.size} resumes",
                    fontSize = 14.sp,
                    color = if (isDark) Color.White.copy(0.7f) else Color.Gray
                )
            }

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
                                "No resumes found",
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

        // Tab 1: Search History - ✅ UPDATED WITH COLLAPSIBLE CARDS
        if (selectedTab == 1) {
            item {
                Text(
                    "Filter Search History",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = if (isDark) Color.White else Color.Black
                )
            }

            if (isLoading) {
                item {
                    Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator()
                    }
                }
            } else if (searchHistory.isEmpty()) {
                item {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(150.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Icon(
                                Icons.Default.SearchOff,
                                contentDescription = null,
                                modifier = Modifier.size(48.dp),
                                tint = if (isDark) Color.White.copy(0.4f) else Color.Gray
                            )
                            Spacer(Modifier.height(8.dp))
                            Text(
                                "No search history yet",
                                color = if (isDark) Color.White.copy(0.6f) else Color.Gray
                            )
                        }
                    }
                }
            } else {
                items(searchHistory) { history ->
                    SearchHistoryCardCollapsible(history, isDark)
                }
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
            containerColor = if (isDark) Color(0xFF1E1E2E) else Color.White
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

// ✅ NEW: Collapsible Search History Card
@Composable
fun SearchHistoryCardCollapsible(history: SearchHistoryEntity, isDark: Boolean) {
    var isExpanded by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .animateContentSize()
            .clickable { isExpanded = !isExpanded },
        colors = CardDefaults.cardColors(
            containerColor = if (isDark) Color(0xFF1E1E2E) else Color.White
        ),
        elevation = CardDefaults.cardElevation(6.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            // ✅ Collapsed View - Always Visible
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        "🔍 ${history.techStack}",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = if (isDark) Color.White else Color.Black,
                        maxLines = 1
                    )
                    Text(
                        formatDateTimeShort(history.searchedAt),  // ✅ Short date/time
                        fontSize = 11.sp,
                        color = if (isDark) Color.White.copy(0.6f) else Color.Gray
                    )
                }
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = Color(0xFF4CAF50).copy(alpha = 0.2f)
                    ) {
                        Text(
                            "${history.resultCount} matches",
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF4CAF50)
                        )
                    }
                    Icon(
                        if (isExpanded) Icons.Default.ExpandLess else Icons.Default.ExpandMore,
                        contentDescription = null,
                        tint = if (isDark) Color.White.copy(0.6f) else Color.Gray,
                        modifier = Modifier.size(24.dp)
                    )
                }
            }

            // ✅ Expanded View - Detailed Information
            if (isExpanded) {
                Divider(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 12.dp),
                    color = if (isDark) Color.White.copy(0.1f) else Color.Gray.copy(0.2f)
                )

                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    DetailRow("Technologies", history.techStack, isDark)
                    DetailRow(
                        "Experience Range",
                        "${history.minExperience} - ${history.maxExperience} years",
                        isDark
                    )
                    DetailRow("Matches Found", history.resultCount.toString(), isDark)
                    DetailRow("Date & Time", formatDateTimeFull(history.searchedAt), isDark)
                }
            }
        }
    }
}

@Composable
fun DetailRow(label: String, value: String, isDark: Boolean) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            label,
            fontSize = 12.sp,
            color = if (isDark) Color.White.copy(0.6f) else Color.Gray,
            fontWeight = FontWeight.Medium
        )
        Text(
            value,
            fontSize = 12.sp,
            fontWeight = FontWeight.SemiBold,
            color = if (isDark) Color.White else Color.Black
        )
    }
}

// ✅ Helper Functions
private fun formatDate(timestamp: Long): String {
    val sdf = SimpleDateFormat("MMM dd, yyyy - HH:mm", Locale.getDefault())
    return sdf.format(Date(timestamp))
}

private fun formatDateTimeShort(timestamp: Long): String {
    val sdf = SimpleDateFormat("MMM dd, HH:mm", Locale.getDefault())
    return sdf.format(Date(timestamp))
}

private fun formatDateTimeFull(timestamp: Long): String {
    val sdf = SimpleDateFormat("MMM dd, yyyy - HH:mm:ss", Locale.getDefault())
    return sdf.format(Date(timestamp))
}