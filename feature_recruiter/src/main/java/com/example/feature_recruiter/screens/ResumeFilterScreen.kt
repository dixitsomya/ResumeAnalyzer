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
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.feature_recruiter.database.entity.RecruiterResumeEntity
import com.example.feature_recruiter.viewmodel.RecruiterResumeViewModel

@Composable
fun ResumeFilterScreen(
    isDark: Boolean,
    viewModel: RecruiterResumeViewModel,
    modifier: Modifier = Modifier
) {
    var selectedTechStack by remember { mutableStateOf<List<String>>(emptyList()) }
    var minExperience by remember { mutableStateOf("0") }
    var maxExperience by remember { mutableStateOf("20") }

    val allTechStacks by viewModel.allTechStacks.collectAsState()
    val filteredResumes by viewModel.filteredResumes.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.filterByTechAndExperience(selectedTechStack, 0, 20)
    }

    fun applyFilter() {
        viewModel.filterByTechAndExperience(
            selectedTechStack,
            minExperience.toIntOrNull() ?: 0,
            maxExperience.toIntOrNull() ?: 20
        )
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
                "Filter Candidates",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = if (isDark) Color.White else Color.Black
            )
        }

        // Filter Card
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .shadow(8.dp, RoundedCornerShape(16.dp)),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(
                    containerColor = if (isDark) Color(0xFF1E1E2F) else Color.White
                )
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    // Tech Stack Selection
                    Text(
                        "Select Tech Stack",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = if (isDark) Color.White else Color.Black
                    )

                    FlowRow(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        if (allTechStacks.isEmpty()) {
                            Text(
                                "No technologies found",
                                fontSize = 12.sp,
                                color = Color.Gray
                            )
                        } else {
                            allTechStacks.forEach { tech ->
                                FilterChip(
                                    onClick = {
                                        selectedTechStack = if (selectedTechStack.contains(tech)) {
                                            selectedTechStack - tech
                                        } else {
                                            selectedTechStack + tech
                                        }
                                        applyFilter()
                                    },
                                    label = { Text(tech) },
                                    selected = selectedTechStack.contains(tech),
                                    leadingIcon = if (selectedTechStack.contains(tech)) {
                                        {
                                            Icon(
                                                Icons.Default.Check,
                                                contentDescription = null,
                                                modifier = Modifier.size(18.dp)
                                            )
                                        }
                                    } else null,
                                    colors = FilterChipDefaults.filterChipColors(
                                        selectedContainerColor = Color(0xFF4A90E2)
                                    )
                                )
                            }
                        }
                    }

                    Divider()

                    // Experience Range
                    Text(
                        "Experience Range (Years)",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = if (isDark) Color.White else Color.Black
                    )

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        OutlinedTextField(
                            value = minExperience,
                            onValueChange = {
                                minExperience = it
                                applyFilter()
                            },
                            label = { Text("Min") },
                            modifier = Modifier.weight(1f),
                            shape = RoundedCornerShape(8.dp),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = Color(0xFF4A90E2),
                                unfocusedBorderColor = if (isDark) Color.White.copy(0.3f) else Color.Gray.copy(0.3f)
                            )
                        )

                        Text("-", fontSize = 16.sp, fontWeight = FontWeight.Bold)

                        OutlinedTextField(
                            value = maxExperience,
                            onValueChange = {
                                maxExperience = it
                                applyFilter()
                            },
                            label = { Text("Max") },
                            modifier = Modifier.weight(1f),
                            shape = RoundedCornerShape(8.dp),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = Color(0xFF4A90E2),
                                unfocusedBorderColor = if (isDark) Color.White.copy(0.3f) else Color.Gray.copy(0.3f)
                            )
                        )
                    }

                    // Reset Button
                    Button(
                        onClick = {
                            selectedTechStack = emptyList()
                            minExperience = "0"
                            maxExperience = "20"
                            applyFilter()
                        },
                        modifier = Modifier.fillMaxWidth(),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFE0E0E0))
                    ) {
                        Text("Reset Filters", color = Color.Black)
                    }
                }
            }
        }

        // Results Count
        item {
            Text(
                "Results: ${filteredResumes.size} candidates",
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold,
                color = if (isDark) Color.White.copy(0.7f) else Color.Gray
            )
        }

        // Results
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
                            Icons.Default.SearchOff,
                            contentDescription = null,
                            modifier = Modifier.size(48.dp),
                            tint = if (isDark) Color.White.copy(0.4f) else Color.Gray
                        )
                        Spacer(Modifier.height(8.dp))
                        Text(
                            "No candidates match",
                            color = if (isDark) Color.White.copy(0.6f) else Color.Gray
                        )
                    }
                }
            }
        } else {
            items(filteredResumes) { resume ->
                RecentResumeCard(resume, isDark)
            }
        }
    }
}
