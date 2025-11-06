//package com.example.feature_recruiter.screens
//
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
//import androidx.compose.ui.text.font.FontWeight
//import androidx.compose.ui.unit.dp
//import androidx.compose.ui.unit.sp
//import com.example.feature_recruiter.database.entity.RecruiterResumeEntity
//import com.example.feature_recruiter.viewmodel.RecruiterResumeViewModel
//
//@Composable
//fun RecruiterDashboardScreen(
//    userEmail: String,
//    isDark: Boolean,
//    viewModel: RecruiterResumeViewModel,
//    onNavigateToFilter: () -> Unit,
//    modifier: Modifier = Modifier
//) {
//    val totalCount by viewModel.totalCount.collectAsState()
//    val recentResumes by viewModel.filteredResumes.collectAsState()
//
//    LaunchedEffect(Unit) {
//        viewModel.getRecentResumes(5)
//    }
//
//    val backgroundBrush = if (!isDark) {
//        Brush.verticalGradient(
//            listOf(Color(0xFF4A90E2), Color(0xFF357ABD))
//        )
//    } else {
//        Brush.verticalGradient(
//            listOf(Color(0xFF1a1a2e), Color(0xFF2D1B69))
//        )
//    }
//
//    LazyColumn(
//        modifier = modifier
//            .fillMaxSize()
//            .background(backgroundBrush)
//            .padding(20.dp),
//        verticalArrangement = Arrangement.spacedBy(20.dp)
//    ) {
//        // Welcome Card
//        item {
//            Card(
//                modifier = Modifier.fillMaxWidth(),
//                shape = RoundedCornerShape(20.dp),
//                colors = CardDefaults.cardColors(
//                    containerColor = Color.White.copy(alpha = if (isDark) 0.1f else 0.95f)
//                ),
//                elevation = CardDefaults.cardElevation(8.dp)
//            ) {
//                Row(
//                    modifier = Modifier
//                        .fillMaxWidth()
//                        .padding(20.dp),
//                    verticalAlignment = Alignment.CenterVertically
//                ) {
//                    Box(
//                        modifier = Modifier
//                            .size(60.dp)
//                            .clip(CircleShape)
//                            .background(Color(0xFF4A90E2)),
//                        contentAlignment = Alignment.Center
//                    ) {
//                        Text(
//                            userEmail.firstOrNull()?.uppercase() ?: "R",
//                            color = Color.White,
//                            fontSize = 24.sp,
//                            fontWeight = FontWeight.Bold
//                        )
//                    }
//                    Spacer(Modifier.width(16.dp))
//                    Column {
//                        Text(
//                            "Welcome back!",
//                            fontSize = 16.sp,
//                            color = if (isDark) Color.White.copy(0.7f) else Color.Gray,
//                            fontWeight = FontWeight.Medium
//                        )
//                        Text(
//                            "Talent Manager",
//                            fontSize = 24.sp,
//                            fontWeight = FontWeight.Bold,
//                            color = if (isDark) Color.White else Color.Black
//                        )
//                    }
//                }
//            }
//        }
//
//        // Stats Card
//        item {
//            Card(
//                modifier = Modifier.fillMaxWidth(),
//                shape = RoundedCornerShape(16.dp),
//                colors = CardDefaults.cardColors(
//                    containerColor = Color.White.copy(alpha = if (isDark) 0.1f else 0.95f)
//                ),
//                elevation = CardDefaults.cardElevation(8.dp)
//            ) {
//                Row(
//                    modifier = Modifier
//                        .fillMaxWidth()
//                        .padding(20.dp),
//                    verticalAlignment = Alignment.CenterVertically,
//                    horizontalArrangement = Arrangement.SpaceBetween
//                ) {
//                    Column {
//                        Icon(
//                            Icons.Default.Description,
//                            contentDescription = null,
//                            tint = Color(0xFF4A90E2),
//                            modifier = Modifier.size(32.dp)
//                        )
//                        Spacer(Modifier.height(8.dp))
//                        Text(
//                            "Total Resumes",
//                            fontSize = 14.sp,
//                            color = if (isDark) Color.White.copy(0.7f) else Color.Gray
//                        )
//                        Text(
//                            totalCount.toString(),
//                            fontSize = 28.sp,
//                            fontWeight = FontWeight.Bold,
//                            color = if (isDark) Color.White else Color.Black
//                        )
//                    }
//                    Button(
//                        onClick = onNavigateToFilter,
//                        colors = ButtonDefaults.buttonColors(
//                            containerColor = Color(0xFF4A90E2)
//                        ),
//                        modifier = Modifier.height(50.dp)
//                    ) {
//                        Icon(Icons.Default.FilterList, contentDescription = null)
//                        Spacer(Modifier.width(8.dp))
//                        Text("Filter")
//                    }
//                }
//            }
//        }
//
//        // Recent Resumes Header
//        item {
//            Text(
//                "Recent Uploads",
//                fontSize = 18.sp,
//                fontWeight = FontWeight.Bold,
//                color = Color.White
//            )
//        }
//
//        // Recent Resumes List
//        if (recentResumes.isEmpty()) {
//            item {
//                Box(
//                    modifier = Modifier
//                        .fillMaxWidth()
//                        .height(150.dp),
//                    contentAlignment = Alignment.Center
//                ) {
//                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
//                        Icon(
//                            Icons.Default.CloudOff,
//                            contentDescription = null,
//                            modifier = Modifier.size(48.dp),
//                            tint = if (isDark) Color.White.copy(0.4f) else Color.White.copy(0.7f)
//                        )
//                        Spacer(Modifier.height(8.dp))
//                        Text(
//                            "No resumes yet",
//                            color = if (isDark) Color.White.copy(0.6f) else Color.White.copy(0.8f),
//                            fontSize = 16.sp
//                        )
//                    }
//                }
//            }
//        } else {
//            items(recentResumes) { resume ->
//                RecentResumeCard(resume, isDark)
//            }
//        }
//    }
//}
//
//@Composable
//fun RecentResumeCard(resume: RecruiterResumeEntity, isDark: Boolean) {
//    Card(
//        modifier = Modifier
//            .fillMaxWidth()
//            .clip(RoundedCornerShape(12.dp)),
//        colors = CardDefaults.cardColors(
//            containerColor = Color.White.copy(alpha = if (isDark) 0.1f else 0.95f)
//        ),
//        elevation = CardDefaults.cardElevation(6.dp)
//    ) {
//        Column(
//            modifier = Modifier
//                .fillMaxWidth()
//                .padding(16.dp)
//        ) {
//            Row(
//                modifier = Modifier.fillMaxWidth(),
//                verticalAlignment = Alignment.CenterVertically,
//                horizontalArrangement = Arrangement.SpaceBetween
//            ) {
//                Column(modifier = Modifier.weight(1f)) {
//                    Text(
//                        resume.candidateName,
//                        fontSize = 16.sp,
//                        fontWeight = FontWeight.Bold,
//                        color = if (isDark) Color.White else Color.Black
//                    )
//                    Text(
//                        resume.candidateEmail,
//                        fontSize = 12.sp,
//                        color = if (isDark) Color.White.copy(0.6f) else Color.Gray
//                    )
//                }
//                Surface(
//                    shape = RoundedCornerShape(8.dp),
//                    color = Color(0xFF4A90E2).copy(alpha = 0.2f)
//                ) {
//                    Text(
//                        "${resume.experience}y",
//                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
//                        fontSize = 12.sp,
//                        fontWeight = FontWeight.Bold,
//                        color = Color(0xFF4A90E2)
//                    )
//                }
//            }
//            Spacer(Modifier.height(8.dp))
//            Text(
//                resume.techStack,
//                fontSize = 12.sp,
//                color = if (isDark) Color.White.copy(0.7f) else Color.DarkGray,
//                maxLines = 2
//            )
//        }
//    }
//}


//
//package com.example.feature_recruiter.screens
//
//import androidx.compose.animation.animateContentSize
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
//import androidx.compose.ui.draw.shadow
//import androidx.compose.ui.graphics.Brush
//import androidx.compose.ui.graphics.Color
//import androidx.compose.ui.text.font.FontWeight
//import androidx.compose.ui.unit.dp
//import androidx.compose.ui.unit.sp
//import com.example.feature_recruiter.database.entity.RecruiterResumeEntity
//import com.example.feature_recruiter.viewmodel.RecruiterResumeViewModel
//
//@Composable
//fun RecruiterDashboardScreen(
//    userEmail: String,
//    isDark: Boolean,
//    viewModel: RecruiterResumeViewModel,
//    onNavigateToFilter: () -> Unit,
//    modifier: Modifier = Modifier
//) {
//    val totalCount by viewModel.totalCount.collectAsState()
//    val recentResumes by viewModel.filteredResumes.collectAsState()
//    val isLoading by viewModel.isLoading.collectAsState()
//
//    // ✅ Load recent resumes on screen open
//    LaunchedEffect(Unit) {
//        viewModel.getRecentResumes(5)
//    }
//
//    val backgroundBrush = if (!isDark) {
//        Brush.verticalGradient(
//            listOf(Color(0xFF4A90E2), Color(0xFF357ABD))
//        )
//    } else {
//        Brush.verticalGradient(
//            listOf(Color(0xFF1a1a2e), Color(0xFF2D1B69))
//        )
//    }
//
//    LazyColumn(
//        modifier = modifier
//            .fillMaxSize()
//            .background(backgroundBrush)
//            .padding(20.dp),
//        verticalArrangement = Arrangement.spacedBy(16.dp)
//    ) {
//        // Welcome Card - Beautiful
//        item {
//            Card(
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .shadow(elevation = 12.dp, shape = RoundedCornerShape(20.dp)),
//                shape = RoundedCornerShape(20.dp),
//                colors = CardDefaults.cardColors(
//                    containerColor = Color.White.copy(alpha = if (isDark) 0.1f else 0.98f)
//                ),
//                elevation = CardDefaults.cardElevation(12.dp)
//            ) {
//                Row(
//                    modifier = Modifier
//                        .fillMaxWidth()
//                        .padding(24.dp),
//                    verticalAlignment = Alignment.CenterVertically,
//                    horizontalArrangement = Arrangement.spacedBy(16.dp)
//                ) {
//                    // Avatar
//                    Box(
//                        modifier = Modifier
//                            .size(70.dp)
//                            .clip(CircleShape)
//                            .background(
//                                Brush.verticalGradient(
//                                    listOf(Color(0xFF4A90E2), Color(0xFF357ABD))
//                                )
//                            ),
//                        contentAlignment = Alignment.Center
//                    ) {
//                        Text(
//                            userEmail.firstOrNull()?.uppercase() ?: "R",
//                            color = Color.White,
//                            fontSize = 28.sp,
//                            fontWeight = FontWeight.Bold
//                        )
//                    }
//
//                    // Text Content
//                    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
//                        Text(
//                            "Welcome back! 👋",
//                            fontSize = 14.sp,
//                            color = if (isDark) Color.White.copy(0.7f) else Color.Gray,
//                            fontWeight = FontWeight.Medium
//                        )
//                        Text(
//                            "Talent Manager",
//                            fontSize = 26.sp,
//                            fontWeight = FontWeight.ExtraBold,
//                            color = if (isDark) Color.White else Color.Black
//                        )
//                        Text(
//                            userEmail,
//                            fontSize = 12.sp,
//                            color = if (isDark) Color.White.copy(0.5f) else Color.Gray
//                        )
//                    }
//                }
//            }
//        }
//
//        // Stats Card - Beautiful
//        item {
//            Card(
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .shadow(elevation = 10.dp, shape = RoundedCornerShape(16.dp)),
//                shape = RoundedCornerShape(16.dp),
//                colors = CardDefaults.cardColors(
//                    containerColor = Color.White.copy(alpha = if (isDark) 0.1f else 0.98f)
//                ),
//                elevation = CardDefaults.cardElevation(10.dp)
//            ) {
//                Row(
//                    modifier = Modifier
//                        .fillMaxWidth()
//                        .padding(24.dp),
//                    verticalAlignment = Alignment.CenterVertically,
//                    horizontalArrangement = Arrangement.SpaceBetween
//                ) {
//                    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
//                        Box(
//                            modifier = Modifier
//                                .size(50.dp)
//                                .background(
//                                    Color(0xFF4A90E2).copy(alpha = 0.2f),
//                                    RoundedCornerShape(12.dp)
//                                ),
//                            contentAlignment = Alignment.Center
//                        ) {
//                            Icon(
//                                Icons.Default.Description,
//                                contentDescription = null,
//                                tint = Color(0xFF4A90E2),
//                                modifier = Modifier.size(28.dp)
//                            )
//                        }
//                        Text(
//                            "Total Resumes",
//                            fontSize = 13.sp,
//                            color = if (isDark) Color.White.copy(0.7f) else Color.Gray,
//                            fontWeight = FontWeight.Medium
//                        )
//                        Text(
//                            totalCount.toString(),
//                            fontSize = 32.sp,
//                            fontWeight = FontWeight.ExtraBold,
//                            color = if (isDark) Color.White else Color.Black
//                        )
//                    }
//
//                    Button(
//                        onClick = onNavigateToFilter,
//                        colors = ButtonDefaults.buttonColors(
//                            containerColor = Color(0xFF4A90E2)
//                        ),
//                        modifier = Modifier
//                            .height(60.dp)
//                            .width(140.dp)
//                            .shadow(elevation = 8.dp, shape = RoundedCornerShape(12.dp)),
//                        shape = RoundedCornerShape(12.dp)
//                    ) {
//                        Column(
//                            horizontalAlignment = Alignment.CenterHorizontally,
//                            verticalArrangement = Arrangement.spacedBy(4.dp)
//                        ) {
//                            Icon(Icons.Default.FilterList, contentDescription = null, modifier = Modifier.size(24.dp))
//                            Text("Filter", fontSize = 12.sp, fontWeight = FontWeight.Bold)
//                        }
//                    }
//                }
//            }
//        }
//
//        // Recent Resumes Header with divider
//        item {
//            Row(
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .padding(vertical = 8.dp),
//                verticalAlignment = Alignment.CenterVertically,
//                horizontalArrangement = Arrangement.spacedBy(12.dp)
//            ) {
//                Icon(
//                    Icons.Default.History,
//                    contentDescription = null,
//                    tint = Color.White,
//                    modifier = Modifier.size(24.dp)
//                )
//                Text(
//                    "Recent Uploads",
//                    fontSize = 18.sp,
//                    fontWeight = FontWeight.Bold,
//                    color = Color.White
//                )
//                Divider(
//                    modifier = Modifier
//                        .weight(1f)
//                        .height(2.dp),
//                    color = Color.White.copy(alpha = 0.3f)
//                )
//            }
//        }
//
//        // Loading State - Beautiful
//        if (isLoading) {
//            item {
//                Box(
//                    modifier = Modifier
//                        .fillMaxWidth()
//                        .height(120.dp)
//                        .shadow(elevation = 8.dp, shape = RoundedCornerShape(16.dp))
//                        .background(
//                            Color.White.copy(alpha = if (isDark) 0.1f else 0.95f),
//                            RoundedCornerShape(16.dp)
//                        ),
//                    contentAlignment = Alignment.Center
//                ) {
//                    Column(
//                        horizontalAlignment = Alignment.CenterHorizontally,
//                        verticalArrangement = Arrangement.spacedBy(12.dp)
//                    ) {
//                        CircularProgressIndicator(
//                            color = Color(0xFF4A90E2),
//                            modifier = Modifier.size(40.dp)
//                        )
//                        Text(
//                            "Loading resumes...",
//                            color = if (isDark) Color.White else Color.Black,
//                            fontSize = 14.sp,
//                            fontWeight = FontWeight.Medium
//                        )
//                    }
//                }
//            }
//        }
//
//        // Empty State - Beautiful
//        if (!isLoading && recentResumes.isEmpty()) {
//            item {
//                Box(
//                    modifier = Modifier
//                        .fillMaxWidth()
//                        .height(200.dp)
//                        .shadow(elevation = 8.dp, shape = RoundedCornerShape(16.dp))
//                        .background(
//                            Color.White.copy(alpha = if (isDark) 0.08f else 0.95f),
//                            RoundedCornerShape(16.dp)
//                        )
//                        .padding(24.dp),
//                    contentAlignment = Alignment.Center
//                ) {
//                    Column(
//                        horizontalAlignment = Alignment.CenterHorizontally,
//                        verticalArrangement = Arrangement.spacedBy(16.dp)
//                    ) {
//                        Box(
//                            modifier = Modifier
//                                .size(80.dp)
//                                .background(
//                                    Color(0xFF4A90E2).copy(alpha = 0.15f),
//                                    RoundedCornerShape(20.dp)
//                                ),
//                            contentAlignment = Alignment.Center
//                        ) {
//                            Icon(
//                                Icons.Default.CloudUpload,
//                                contentDescription = null,
//                                modifier = Modifier.size(48.dp),
//                                tint = if (isDark) Color.White.copy(0.5f) else Color.Gray.copy(0.5f)
//                            )
//                        }
//                        Text(
//                            "No Resumes Yet",
//                            color = if (isDark) Color.White else Color.Black,
//                            fontSize = 18.sp,
//                            fontWeight = FontWeight.Bold
//                        )
//                        Text(
//                            "Start by uploading your first resume",
//                            color = if (isDark) Color.White.copy(0.6f) else Color.Gray,
//                            fontSize = 13.sp,
//                            textAlign = androidx.compose.ui.text.style.TextAlign.Center
//                        )
//                    }
//                }
//            }
//        }
//
//        // Recent Resumes List - Beautiful
//        if (!isLoading && recentResumes.isNotEmpty()) {
//            items(recentResumes) { resume ->
//                RecentResumeCard(resume, isDark)
//            }
//        }
//
//        item {
//            Spacer(modifier = Modifier.height(16.dp))
//        }
//    }
//}
//
//@Composable
//fun RecentResumeCard(resume: RecruiterResumeEntity, isDark: Boolean) {
//    Card(
//        modifier = Modifier
//            .fillMaxWidth()
//            .shadow(elevation = 8.dp, shape = RoundedCornerShape(14.dp))
//            .animateContentSize(),
//        shape = RoundedCornerShape(14.dp),
//        colors = CardDefaults.cardColors(
//            containerColor = Color.White.copy(alpha = if (isDark) 0.12f else 0.97f)
//        ),
//        elevation = CardDefaults.cardElevation(8.dp)
//    ) {
//        Column(
//            modifier = Modifier
//                .fillMaxWidth()
//                .padding(18.dp)
//        ) {
//            Row(
//                modifier = Modifier.fillMaxWidth(),
//                verticalAlignment = Alignment.CenterVertically,
//                horizontalArrangement = Arrangement.SpaceBetween
//            ) {
//                Column(modifier = Modifier.weight(1f)) {
//                    Text(
//                        resume.candidateName.ifEmpty { "Unknown Candidate" },
//                        fontSize = 16.sp,
//                        fontWeight = FontWeight.Bold,
//                        color = if (isDark) Color.White else Color.Black
//                    )
//                    Spacer(modifier = Modifier.height(4.dp))
//                    Text(
//                        resume.candidateEmail.ifEmpty { "No email" },
//                        fontSize = 12.sp,
//                        color = if (isDark) Color.White.copy(0.6f) else Color.Gray
//                    )
//                }
//                Surface(
//                    shape = RoundedCornerShape(10.dp),
//                    color = Color(0xFF4A90E2).copy(alpha = 0.25f)
//                ) {
//                    Row(
//                        modifier = Modifier.padding(horizontal = 14.dp, vertical = 8.dp),
//                        horizontalArrangement = Arrangement.spacedBy(4.dp),
//                        verticalAlignment = Alignment.CenterVertically
//                    ) {
//                        Icon(
//                            Icons.Default.School,
//                            contentDescription = null,
//                            tint = Color(0xFF4A90E2),
//                            modifier = Modifier.size(14.dp)
//                        )
//                        Text(
//                            "${resume.experience}y",
//                            fontSize = 12.sp,
//                            fontWeight = FontWeight.ExtraBold,
//                            color = Color(0xFF4A90E2)
//                        )
//                    }
//                }
//            }
//
//            Spacer(modifier = Modifier.height(12.dp))
//
//            // Tech Stack Tags
//            if (resume.techStack.isNotEmpty()) {
//                FlowRow(
//                    modifier = Modifier.fillMaxWidth(),
//                    horizontalArrangement = Arrangement.spacedBy(6.dp)
//                ) {
//                    resume.techStack.split(",").take(3).forEach { tech ->
//                        Surface(
//                            shape = RoundedCornerShape(8.dp),
//                            color = Color(0xFF4A90E2).copy(alpha = 0.15f)
//                        ) {
//                            Text(
//                                tech.trim(),
//                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
//                                fontSize = 11.sp,
//                                fontWeight = FontWeight.SemiBold,
//                                color = Color(0xFF4A90E2)
//                            )
//                        }
//                    }
//                    if (resume.techStack.split(",").size > 3) {
//                        Surface(
//                            shape = RoundedCornerShape(8.dp),
//                            color = Color(0xFF4A90E2).copy(alpha = 0.15f)
//                        ) {
//                            Text(
//                                "+${resume.techStack.split(",").size - 3}",
//                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
//                                fontSize = 11.sp,
//                                fontWeight = FontWeight.SemiBold,
//                                color = Color(0xFF4A90E2)
//                            )
//                        }
//                    }
//                }
//            }
//        }
//    }
//}


package com.example.feature_recruiter.screens

import androidx.compose.animation.animateContentSize
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
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.feature_recruiter.database.entity.RecruiterResumeEntity
import com.example.feature_recruiter.viewmodel.RecruiterProfileViewModel
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
    val isLoading by viewModel.isLoading.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.getRecentResumes(5)
        viewModel.loadAllResumes()
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
        verticalArrangement = Arrangement.spacedBy(16.dp),
        contentPadding = PaddingValues(vertical = 8.dp)
    ) {
        // Welcome Card
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .shadow(elevation = 12.dp, shape = RoundedCornerShape(20.dp)),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(
                    containerColor = Color.White.copy(alpha = if (isDark) 0.1f else 0.98f)
                ),
                elevation = CardDefaults.cardElevation(12.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(24.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(70.dp)
                            .clip(CircleShape)
                            .background(
                                Brush.verticalGradient(
                                    listOf(Color(0xFF4A90E2), Color(0xFF357ABD))
                                )
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            userEmail.firstOrNull()?.uppercase() ?: "R",
                            color = Color.White,
                            fontSize = 28.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                        Text(
                            "Welcome Back! 👋",
                            fontSize = 13.sp,
                            color = if (isDark) Color.White.copy(0.7f) else Color.Gray,
                            fontWeight = FontWeight.Medium
                        )
                        Text(
                            "Talent Manager",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = if (isDark) Color.White else Color.Black
                        )
                        Text(
                            userEmail,
                            fontSize = 11.sp,
                            color = if (isDark) Color.White.copy(0.5f) else Color.Gray
                        )
                    }
                }
            }
        }

        // Stats Row
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // Total Resumes Card
                Card(
                    modifier = Modifier
                        .weight(1f)
                        .shadow(elevation = 10.dp, shape = RoundedCornerShape(16.dp)),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = Color.White.copy(alpha = if (isDark) 0.1f else 0.98f)
                    ),
                    elevation = CardDefaults.cardElevation(10.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(20.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(44.dp)
                                .background(
                                    Color(0xFF4A90E2).copy(alpha = 0.2f),
                                    RoundedCornerShape(12.dp)
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                Icons.Default.Description,
                                contentDescription = null,
                                tint = Color(0xFF4A90E2),
                                modifier = Modifier.size(24.dp)
                            )
                        }
                        Text(
                            "Total",
                            fontSize = 12.sp,
                            color = if (isDark) Color.White.copy(0.7f) else Color.Gray,
                            fontWeight = FontWeight.Medium
                        )
                        Text(
                            totalCount.toString(),
                            fontSize = 26.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = if (isDark) Color.White else Color.Black
                        )
                    }
                }

                // Filter Button Card
                Button(
                    onClick = onNavigateToFilter,
                    modifier = Modifier
                        .weight(1f)
                        .height(140.dp)
                        .shadow(elevation = 10.dp, shape = RoundedCornerShape(16.dp)),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF4A90E2)
                    ),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(44.dp)
                                .background(
                                    Color.White.copy(alpha = 0.2f),
                                    RoundedCornerShape(12.dp)
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                Icons.Default.FilterList,
                                contentDescription = null,
                                modifier = Modifier.size(24.dp),
                                tint = Color.White
                            )
                        }
                        Text("Smart", fontSize = 12.sp, color = Color.White.copy(0.8f), fontWeight = FontWeight.Medium)
                        Text("Filter", fontSize = 16.sp, fontWeight = FontWeight.ExtraBold, color = Color.White)
                    }
                }
            }
        }

        // Recent Uploads Header
        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Icon(
                    Icons.Default.History,
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.size(24.dp)
                )
                Text(
                    "Recent Uploads (${recentResumes.size})",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = Color.White
                )
                Divider(
                    modifier = Modifier
                        .weight(1f)
                        .height(2.dp),
                    color = Color.White.copy(alpha = 0.3f)
                )
            }
        }

        // Loading State
        if (isLoading) {
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(120.dp)
                        .shadow(elevation = 8.dp, shape = RoundedCornerShape(16.dp))
                        .background(
                            Color.White.copy(alpha = if (isDark) 0.1f else 0.95f),
                            RoundedCornerShape(16.dp)
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        CircularProgressIndicator(
                            color = Color.White,
                            modifier = Modifier.size(40.dp)
                        )
                        Text(
                            "Loading resumes...",
                            color = Color.White,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }
            }
        }

        // Empty State
        if (!isLoading && recentResumes.isEmpty()) {
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp)
                        .shadow(elevation = 8.dp, shape = RoundedCornerShape(16.dp))
                        .background(
                            Color.White.copy(alpha = if (isDark) 0.08f else 0.95f),
                            RoundedCornerShape(16.dp)
                        )
                        .padding(24.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(80.dp)
                                .background(
                                    Color.White.copy(alpha = 0.15f),
                                    RoundedCornerShape(20.dp)
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                Icons.Default.CloudUpload,
                                contentDescription = null,
                                modifier = Modifier.size(48.dp),
                                tint = Color.White.copy(0.5f)
                            )
                        }
                        Text(
                            "No Resumes Yet",
                            color = Color.White,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.ExtraBold,
                            textAlign = TextAlign.Center
                        )
                        Text(
                            "Start by uploading resumes to build your talent pool",
                            color = Color.White.copy(0.6f),
                            fontSize = 13.sp,
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }
        }

        // Recent Resumes List
        if (!isLoading && recentResumes.isNotEmpty()) {
            items(recentResumes) { resume ->
                RecentResumeCard(resume, isDark)
            }
        }

        item {
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

@Composable
fun RecentResumeCard(resume: RecruiterResumeEntity, isDark: Boolean) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(elevation = 8.dp, shape = RoundedCornerShape(14.dp))
            .animateContentSize(),
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White.copy(alpha = if (isDark) 0.12f else 0.97f)
        ),
        elevation = CardDefaults.cardElevation(8.dp)
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
                        resume.candidateName.ifEmpty { "Unknown Candidate" },
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        resume.candidateEmail.ifEmpty { resume.fileName },
                        fontSize = 11.sp,
                        color = Color.White.copy(0.7f),
                        maxLines = 1
                    )
                }
                Surface(
                    shape = RoundedCornerShape(10.dp),
                    color = Color.White.copy(alpha = 0.2f)
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
                        horizontalArrangement = Arrangement.spacedBy(4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            Icons.Default.School,
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier.size(14.dp)
                        )
                        Text(
                            "${resume.experience}y",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = Color.White
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            if (resume.techStack.isNotEmpty()) {
                FlowRow(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    resume.techStack.split(",").take(3).forEach { tech ->
                        Surface(
                            shape = RoundedCornerShape(6.dp),
                            color = Color.White.copy(alpha = 0.15f)
                        ) {
                            Text(
                                tech.trim(),
                                modifier = Modifier.padding(8.dp, 4.dp),
                                fontSize = 10.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = Color.White
                            )
                        }
                    }
                    if (resume.techStack.split(",").size > 3) {
                        Surface(
                            shape = RoundedCornerShape(6.dp),
                            color = Color.White.copy(alpha = 0.15f)
                        ) {
                            Text(
                                "+${resume.techStack.split(",").size - 3}",
                                modifier = Modifier.padding(8.dp, 4.dp),
                                fontSize = 10.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = Color.White
                            )
                        }
                    }
                }
            }
        }
    }
}