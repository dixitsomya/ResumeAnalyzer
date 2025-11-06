//package com.example.feature_recruiter.screens
//
//import androidx.compose.foundation.background
//import androidx.compose.foundation.layout.*
//import androidx.compose.foundation.lazy.LazyColumn
//import androidx.compose.foundation.lazy.items
//import androidx.compose.foundation.shape.RoundedCornerShape
//import androidx.compose.material.icons.Icons
//import androidx.compose.material.icons.filled.*
//import androidx.compose.material3.*
//import androidx.compose.runtime.*
//import androidx.compose.ui.Alignment
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.draw.shadow
//import androidx.compose.ui.graphics.Color
//import androidx.compose.ui.text.font.FontWeight
//import androidx.compose.ui.unit.dp
//import androidx.compose.ui.unit.sp
//import com.example.feature_recruiter.database.entity.RecruiterResumeEntity
//import com.example.feature_recruiter.viewmodel.RecruiterResumeViewModel
//
//@Composable
//fun ResumeFilterScreen(
//    isDark: Boolean,
//    viewModel: RecruiterResumeViewModel,
//    modifier: Modifier = Modifier
//) {
//    var selectedTechStack by remember { mutableStateOf<List<String>>(emptyList()) }
//    var minExperience by remember { mutableStateOf("0") }
//    var maxExperience by remember { mutableStateOf("20") }
//
//    val allTechStacks by viewModel.allTechStacks.collectAsState()
//    val filteredResumes by viewModel.filteredResumes.collectAsState()
//    val isLoading by viewModel.isLoading.collectAsState()
//
//    LaunchedEffect(Unit) {
//        viewModel.filterByTechAndExperience(selectedTechStack, 0, 20)
//    }
//
//    fun applyFilter() {
//        viewModel.filterByTechAndExperience(
//            selectedTechStack,
//            minExperience.toIntOrNull() ?: 0,
//            maxExperience.toIntOrNull() ?: 20
//        )
//    }
//
//    LazyColumn(
//        modifier = modifier
//            .fillMaxSize()
//            .background(if (isDark) Color(0xFF121212) else Color(0xFFF5F6FA))
//            .padding(16.dp),
//        verticalArrangement = Arrangement.spacedBy(16.dp)
//    ) {
//        item {
//            Text(
//                "Filter Candidates",
//                fontSize = 24.sp,
//                fontWeight = FontWeight.Bold,
//                color = if (isDark) Color.White else Color.Black
//            )
//        }
//
//        // Filter Card
//        item {
//            Card(
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .shadow(8.dp, RoundedCornerShape(16.dp)),
//                shape = RoundedCornerShape(16.dp),
//                colors = CardDefaults.cardColors(
//                    containerColor = if (isDark) Color(0xFF1E1E2F) else Color.White
//                )
//            ) {
//                Column(
//                    modifier = Modifier
//                        .fillMaxWidth()
//                        .padding(20.dp),
//                    verticalArrangement = Arrangement.spacedBy(16.dp)
//                ) {
//                    // Tech Stack Selection
//                    Text(
//                        "Select Tech Stack",
//                        fontSize = 16.sp,
//                        fontWeight = FontWeight.SemiBold,
//                        color = if (isDark) Color.White else Color.Black
//                    )
//
//                    FlowRow(
//                        modifier = Modifier.fillMaxWidth(),
//                        horizontalArrangement = Arrangement.spacedBy(8.dp)
//                    ) {
//                        if (allTechStacks.isEmpty()) {
//                            Text(
//                                "No technologies found",
//                                fontSize = 12.sp,
//                                color = Color.Gray
//                            )
//                        } else {
//                            allTechStacks.forEach { tech ->
//                                FilterChip(
//                                    onClick = {
//                                        selectedTechStack = if (selectedTechStack.contains(tech)) {
//                                            selectedTechStack - tech
//                                        } else {
//                                            selectedTechStack + tech
//                                        }
//                                        applyFilter()
//                                    },
//                                    label = { Text(tech) },
//                                    selected = selectedTechStack.contains(tech),
//                                    leadingIcon = if (selectedTechStack.contains(tech)) {
//                                        {
//                                            Icon(
//                                                Icons.Default.Check,
//                                                contentDescription = null,
//                                                modifier = Modifier.size(18.dp)
//                                            )
//                                        }
//                                    } else null,
//                                    colors = FilterChipDefaults.filterChipColors(
//                                        selectedContainerColor = Color(0xFF4A90E2)
//                                    )
//                                )
//                            }
//                        }
//                    }
//
//                    Divider()
//
//                    // Experience Range
//                    Text(
//                        "Experience Range (Years)",
//                        fontSize = 16.sp,
//                        fontWeight = FontWeight.SemiBold,
//                        color = if (isDark) Color.White else Color.Black
//                    )
//
//                    Row(
//                        modifier = Modifier.fillMaxWidth(),
//                        horizontalArrangement = Arrangement.spacedBy(12.dp),
//                        verticalAlignment = Alignment.CenterVertically
//                    ) {
//                        OutlinedTextField(
//                            value = minExperience,
//                            onValueChange = {
//                                minExperience = it
//                                applyFilter()
//                            },
//                            label = { Text("Min") },
//                            modifier = Modifier.weight(1f),
//                            shape = RoundedCornerShape(8.dp),
//                            colors = OutlinedTextFieldDefaults.colors(
//                                focusedBorderColor = Color(0xFF4A90E2),
//                                unfocusedBorderColor = if (isDark) Color.White.copy(0.3f) else Color.Gray.copy(0.3f)
//                            )
//                        )
//
//                        Text("-", fontSize = 16.sp, fontWeight = FontWeight.Bold)
//
//                        OutlinedTextField(
//                            value = maxExperience,
//                            onValueChange = {
//                                maxExperience = it
//                                applyFilter()
//                            },
//                            label = { Text("Max") },
//                            modifier = Modifier.weight(1f),
//                            shape = RoundedCornerShape(8.dp),
//                            colors = OutlinedTextFieldDefaults.colors(
//                                focusedBorderColor = Color(0xFF4A90E2),
//                                unfocusedBorderColor = if (isDark) Color.White.copy(0.3f) else Color.Gray.copy(0.3f)
//                            )
//                        )
//                    }
//
//                    // Reset Button
//                    Button(
//                        onClick = {
//                            selectedTechStack = emptyList()
//                            minExperience = "0"
//                            maxExperience = "20"
//                            applyFilter()
//                        },
//                        modifier = Modifier.fillMaxWidth(),
//                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFE0E0E0))
//                    ) {
//                        Text("Reset Filters", color = Color.Black)
//                    }
//                }
//            }
//        }
//
//        // Results Count
//        item {
//            Text(
//                "Results: ${filteredResumes.size} candidates",
//                fontSize = 16.sp,
//                fontWeight = FontWeight.SemiBold,
//                color = if (isDark) Color.White.copy(0.7f) else Color.Gray
//            )
//        }
//
//        // Results
//        if (isLoading) {
//            item {
//                Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
//                    CircularProgressIndicator()
//                }
//            }
//        } else if (filteredResumes.isEmpty()) {
//            item {
//                Box(
//                    modifier = Modifier
//                        .fillMaxWidth()
//                        .height(150.dp),
//                    contentAlignment = Alignment.Center
//                ) {
//                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
//                        Icon(
//                            Icons.Default.SearchOff,
//                            contentDescription = null,
//                            modifier = Modifier.size(48.dp),
//                            tint = if (isDark) Color.White.copy(0.4f) else Color.Gray
//                        )
//                        Spacer(Modifier.height(8.dp))
//                        Text(
//                            "No candidates match",
//                            color = if (isDark) Color.White.copy(0.6f) else Color.Gray
//                        )
//                    }
//                }
//            }
//        } else {
//            items(filteredResumes) { resume ->
//                RecentResumeCard(resume, isDark)
//            }
//        }
//    }
//}

//
//package com.example.feature_recruiter.screens
//
//import androidx.compose.animation.animateContentSize
//import androidx.compose.foundation.background
//import androidx.compose.foundation.clickable
//import androidx.compose.foundation.layout.*
//import androidx.compose.foundation.lazy.LazyColumn
//import androidx.compose.foundation.lazy.items
//import androidx.compose.foundation.shape.RoundedCornerShape
//import androidx.compose.foundation.text.KeyboardOptions
//import androidx.compose.material.icons.Icons
//import androidx.compose.material.icons.filled.*
//import androidx.compose.material3.*
//import androidx.compose.runtime.*
//import androidx.compose.ui.Alignment
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.draw.clip
//import androidx.compose.ui.draw.shadow
//import androidx.compose.ui.graphics.Color
//import androidx.compose.ui.text.font.FontWeight
//import androidx.compose.ui.text.input.KeyboardType
//import androidx.compose.ui.unit.dp
//import androidx.compose.ui.unit.sp
//import com.example.feature_recruiter.database.entity.RecruiterResumeEntity
//import com.example.feature_recruiter.viewmodel.RecruiterResumeViewModel
//
//@Composable
//fun ResumeFilterScreen(
//    isDark: Boolean,
//    viewModel: RecruiterResumeViewModel,
//    modifier: Modifier = Modifier
//) {
//    var selectedTechs by remember { mutableStateOf<List<String>>(emptyList()) }
//    var minExperience by remember { mutableStateOf("0") }
//    var maxExperience by remember { mutableStateOf("20") }
//    var hasFiltered by remember { mutableStateOf(false) }
//
//    val allTechStacks by viewModel.allTechStacks.collectAsState()
//    val filteredResumes by viewModel.filteredResumes.collectAsState()
//    val isLoading by viewModel.isLoading.collectAsState()
//
//    // Categorized tech stacks
//    val categories = remember(allTechStacks) {
//        mapOf(
//            "Languages" to listOf("Kotlin", "Java", "Python", "JavaScript", "TypeScript", "Swift", "Go", "Rust", "C++", "C#"),
//            "Mobile" to listOf("Android", "iOS", "Flutter", "React Native"),
//            "Frontend" to listOf("React", "Vue", "Angular", "HTML", "CSS", "Jetpack Compose"),
//            "Backend" to listOf("Spring", "Node.js", "Django", "Flask", "Express"),
//            "Databases" to listOf("Firebase", "MySQL", "PostgreSQL", "MongoDB", "SQLite", "Realm"),
//            "Tools" to listOf("Git", "Docker", "Kubernetes", "AWS", "GCP", "Azure", "Jenkins", "Gradle", "Maven", "API", "REST", "GraphQL")
//        ).mapValues { (_, techs) -> techs.filter { it in allTechStacks } }
//    }
//
//    LazyColumn(
//        modifier = modifier
//            .fillMaxSize()
//            .background(if (isDark) Color(0xFF121212) else Color(0xFFF5F6FA))
//            .padding(16.dp),
//        verticalArrangement = Arrangement.spacedBy(16.dp)
//    ) {
//        item {
//            Text(
//                "🔍 Filter Candidates",
//                fontSize = 24.sp,
//                fontWeight = FontWeight.Bold,
//                color = if (isDark) Color.White else Color.Black
//            )
//        }
//
//        // Filter Card
//        item {
//            Card(
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .shadow(8.dp, RoundedCornerShape(16.dp))
//                    .animateContentSize(),
//                shape = RoundedCornerShape(16.dp),
//                colors = CardDefaults.cardColors(
//                    containerColor = if (isDark) Color(0xFF1E1E2E) else Color.White
//                )
//            ) {
//                Column(
//                    modifier = Modifier
//                        .fillMaxWidth()
//                        .padding(20.dp),
//                    verticalArrangement = Arrangement.spacedBy(16.dp)
//                ) {
//                    // Tech Stack Selection
//                    Text(
//                        "🛠️ Select Technologies",
//                        fontSize = 16.sp,
//                        fontWeight = FontWeight.Bold,
//                        color = if (isDark) Color.White else Color.Black
//                    )
//
//                    // Categorized Checkboxes
//                    categories.forEach { (category, techs) ->
//                        if (techs.isNotEmpty()) {
//                            TechCategoryCheckbox(
//                                category = category,
//                                techs = techs,
//                                selectedTechs = selectedTechs,
//                                isDark = isDark,
//                                onSelectionChange = { tech, isSelected ->
//                                    selectedTechs = if (isSelected) {
//                                        selectedTechs + tech
//                                    } else {
//                                        selectedTechs - tech
//                                    }
//                                }
//                            )
//                        }
//                    }
//
//                    Divider(color = if (isDark) Color.White.copy(0.2f) else Color.Gray.copy(0.2f))
//
//                    // Experience Range
//                    Text(
//                        "📚 Experience Range (Years)",
//                        fontSize = 16.sp,
//                        fontWeight = FontWeight.Bold,
//                        color = if (isDark) Color.White else Color.Black
//                    )
//
//                    Row(
//                        modifier = Modifier.fillMaxWidth(),
//                        horizontalArrangement = Arrangement.spacedBy(12.dp),
//                        verticalAlignment = Alignment.CenterVertically
//                    ) {
//                        OutlinedTextField(
//                            value = minExperience,
//                            onValueChange = { minExperience = it },
//                            label = { Text("Min") },
//                            modifier = Modifier.weight(1f),
//                            shape = RoundedCornerShape(8.dp),
//                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
//                            colors = OutlinedTextFieldDefaults.colors(
//                                focusedBorderColor = Color(0xFF4A90E2),
//                                unfocusedBorderColor = if (isDark) Color.White.copy(0.3f) else Color.Gray.copy(0.3f),
//                                focusedTextColor = if (isDark) Color.White else Color.Black,
//                                unfocusedTextColor = if (isDark) Color.White else Color.Black
//                            ),
//                            textStyle = androidx.compose.ui.text.TextStyle(
//                                fontSize = 14.sp,
//                                color = if (isDark) Color.White else Color.Black
//                            )
//                        )
//
//                        Text("-", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = if (isDark) Color.White else Color.Black)
//
//                        OutlinedTextField(
//                            value = maxExperience,
//                            onValueChange = { maxExperience = it },
//                            label = { Text("Max") },
//                            modifier = Modifier.weight(1f),
//                            shape = RoundedCornerShape(8.dp),
//                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
//                            colors = OutlinedTextFieldDefaults.colors(
//                                focusedBorderColor = Color(0xFF4A90E2),
//                                unfocusedBorderColor = if (isDark) Color.White.copy(0.3f) else Color.Gray.copy(0.3f),
//                                focusedTextColor = if (isDark) Color.White else Color.Black,
//                                unfocusedTextColor = if (isDark) Color.White else Color.Black
//                            ),
//                            textStyle = androidx.compose.ui.text.TextStyle(
//                                fontSize = 14.sp,
//                                color = if (isDark) Color.White else Color.Black
//                            )
//                        )
//                    }
//
//                    // Filter & Reset Buttons
//                    Row(
//                        modifier = Modifier.fillMaxWidth(),
//                        horizontalArrangement = Arrangement.spacedBy(12.dp)
//                    ) {
//                        Button(
//                            onClick = {
//                                hasFiltered = true
//                                viewModel.filterByTechAndExperience(
//                                    selectedTechs,
//                                    minExperience.toIntOrNull() ?: 0,
//                                    maxExperience.toIntOrNull() ?: 20
//                                )
//                            },
//                            modifier = Modifier
//                                .weight(1f)
//                                .height(50.dp)
//                                .shadow(6.dp, RoundedCornerShape(10.dp)),
//                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4CAF50)),
//                            shape = RoundedCornerShape(10.dp)
//                        ) {
//                            Icon(Icons.Default.Search, contentDescription = null, modifier = Modifier.size(20.dp))
//                            Spacer(Modifier.width(8.dp))
//                            Text("Filter", fontWeight = FontWeight.Bold)
//                        }
//
//                        Button(
//                            onClick = {
//                                selectedTechs = emptyList()
//                                minExperience = "0"
//                                maxExperience = "20"
//                                hasFiltered = false
//                            },
//                            modifier = Modifier
//                                .weight(1f)
//                                .height(50.dp),
//                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFE0E0E0)),
//                            shape = RoundedCornerShape(10.dp)
//                        ) {
//                            Icon(Icons.Default.RestartAlt, contentDescription = null, modifier = Modifier.size(20.dp), tint = Color.Black)
//                            Spacer(Modifier.width(8.dp))
//                            Text("Reset", fontWeight = FontWeight.Bold, color = Color.Black)
//                        }
//                    }
//                }
//            }
//        }
//
//        // Results Count
//        if (hasFiltered) {
//            item {
//                Text(
//                    "✨ Found ${filteredResumes.size} candidate(s)",
//                    fontSize = 15.sp,
//                    fontWeight = FontWeight.Bold,
//                    color = if (isDark) Color.White else Color.Black
//                )
//            }
//
//            if (isLoading) {
//                item {
//                    Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
//                        CircularProgressIndicator(color = Color(0xFF4A90E2))
//                    }
//                }
//            } else if (filteredResumes.isEmpty()) {
//                item {
//                    Box(
//                        modifier = Modifier
//                            .fillMaxWidth()
//                            .height(150.dp),
//                        contentAlignment = Alignment.Center
//                    ) {
//                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
//                            Icon(
//                                Icons.Default.SearchOff,
//                                contentDescription = null,
//                                modifier = Modifier.size(48.dp),
//                                tint = if (isDark) Color.White.copy(0.4f) else Color.Gray
//                            )
//                            Spacer(Modifier.height(8.dp))
//                            Text(
//                                "No candidates match your filters",
//                                color = if (isDark) Color.White.copy(0.6f) else Color.Gray,
//                                fontSize = 14.sp
//                            )
//                        }
//                    }
//                }
//            } else {
//                items(filteredResumes) { resume ->
//                    FilterResultCard(resume, isDark)
//                }
//            }
//        }
//    }
//}
//
//@Composable
//fun TechCategoryCheckbox(
//    category: String,
//    techs: List<String>,
//    selectedTechs: List<String>,
//    isDark: Boolean,
//    onSelectionChange: (String, Boolean) -> Unit
//) {
//    var expanded by remember { mutableStateOf(false) }
//    val categorySelected = techs.any { it in selectedTechs }
//
//    Column(
//        modifier = Modifier
//            .fillMaxWidth()
//            .animateContentSize()
//    ) {
//        // Main Category Header
//        Row(
//            modifier = Modifier
//                .fillMaxWidth()
//                .clickable { expanded = !expanded }
//                .padding(vertical = 8.dp),
//            verticalAlignment = Alignment.CenterVertically,
//            horizontalArrangement = Arrangement.spacedBy(10.dp)
//        ) {
//            Checkbox(
//                checked = categorySelected,
//                onCheckedChange = { isChecked ->
//                    techs.forEach { tech ->
//                        onSelectionChange(tech, isChecked)
//                    }
//                },
//                colors = CheckboxDefaults.colors(
//                    checkedColor = Color(0xFF4A90E2),
//                    uncheckedColor = Color.Gray
//                )
//            )
//
//            Text(
//                category,
//                fontSize = 14.sp,
//                fontWeight = FontWeight.SemiBold,
//                color = if (isDark) Color.White else Color.Black,
//                modifier = Modifier.weight(1f)
//            )
//
//            Icon(
//                if (expanded) Icons.Default.ExpandLess else Icons.Default.ExpandMore,
//                contentDescription = null,
//                tint = if (isDark) Color.White else Color.Black,
//                modifier = Modifier.size(24.dp)
//            )
//        }
//
//        // Expanded Options
//        if (expanded) {
//            Column(
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .background(
//                        if (isDark) Color(0xFF2A2A3E) else Color(0xFFF5F5F5),
//                        RoundedCornerShape(8.dp)
//                    )
//                    .padding(12.dp),
//                verticalArrangement = Arrangement.spacedBy(8.dp)
//            ) {
//                techs.forEach { tech ->
//                    Row(
//                        modifier = Modifier
//                            .fillMaxWidth()
//                            .clickable {
//                                onSelectionChange(tech, tech !in selectedTechs)
//                            }
//                            .padding(vertical = 4.dp),
//                        verticalAlignment = Alignment.CenterVertically,
//                        horizontalArrangement = Arrangement.spacedBy(10.dp)
//                    ) {
//                        Checkbox(
//                            checked = tech in selectedTechs,
//                            onCheckedChange = { isChecked ->
//                                onSelectionChange(tech, isChecked)
//                            },
//                            modifier = Modifier.size(20.dp),
//                            colors = CheckboxDefaults.colors(
//                                checkedColor = Color(0xFF4A90E2)
//                            )
//                        )
//                        Text(
//                            tech,
//                            fontSize = 13.sp,
//                            color = if (isDark) Color.White else Color.Black
//                        )
//                    }
//                }
//            }
//        }
//    }
//}
//
//@Composable
//fun FilterResultCard(resume: RecruiterResumeEntity, isDark: Boolean) {
//    Card(
//        modifier = Modifier
//            .fillMaxWidth()
//            .shadow(4.dp, RoundedCornerShape(12.dp)),
//        shape = RoundedCornerShape(12.dp),
//        colors = CardDefaults.cardColors(
//            containerColor = if (isDark) Color(0xFF1E1E2E) else Color.White
//        )
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
//                        resume.candidateName.ifEmpty { "Unknown" },
//                        fontSize = 15.sp,
//                        fontWeight = FontWeight.Bold,
//                        color = if (isDark) Color.White else Color.Black
//                    )
//                    Text(
//                        "${resume.experience} years experience",
//                        fontSize = 12.sp,
//                        color = if (isDark) Color.White.copy(0.6f) else Color.Gray
//                    )
//                }
//                Surface(
//                    shape = RoundedCornerShape(8.dp),
//                    color = Color(0xFF4A90E2).copy(alpha = 0.2f)
//                ) {
//                    Text(
//                        "✅ Match",
//                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
//                        fontSize = 12.sp,
//                        fontWeight = FontWeight.Bold,
//                        color = Color(0xFF4A90E2)
//                    )
//                }
//            }
//
//            Spacer(Modifier.height(8.dp))
//
//            FlowRow(
//                modifier = Modifier.fillMaxWidth(),
//                horizontalArrangement = Arrangement.spacedBy(4.dp)
//            ) {
//                resume.techStack.split(",").take(3).forEach { tech ->
//                    Surface(
//                        shape = RoundedCornerShape(6.dp),
//                        color = Color(0xFF4A90E2).copy(alpha = 0.15f)
//                    ) {
//                        Text(
//                            tech.trim(),
//                            modifier = Modifier.padding(6.dp, 3.dp),
//                            fontSize = 10.sp,
//                            color = Color(0xFF4A90E2),
//                            fontWeight = FontWeight.Bold
//                        )
//                    }
//                }
//            }
//        }
//    }
//}


package com.example.feature_recruiter.screens

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.feature_recruiter.database.entity.RecruiterResumeEntity
import com.example.feature_recruiter.util.FileUtils
import com.example.feature_recruiter.util.PDFParser
import com.example.feature_recruiter.util.TechStackExtractor
import com.example.feature_recruiter.viewmodel.RecruiterResumeViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.*

// ✅ DATA CLASSES
data class SelectedResumeFileForFilter(
    val uri: android.net.Uri,
    val fileName: String,
    val candidateName: String,
    val experience: Int,
    val detectedTechStack: List<String>,
    val extractedText: String
)

data class UploadedResumeWithMatch(
    val candidateName: String,
    val fileName: String,
    val experience: Int,
    val detectedTechStack: List<String>,
    val matchedTechs: List<String>,
    val isMatch: Boolean,
    val matchPercentage: Int
)

// ✅ MAIN FLOW SCREEN
@Composable
fun ResumeFilterScreenWithUpload(
    isDark: Boolean,
    viewModel: RecruiterResumeViewModel,
    recruiterEmail: String,
    modifier: Modifier = Modifier
) {
    var selectedCategory by remember { mutableStateOf<String?>(null) }
    var selectedTechs by remember { mutableStateOf<List<String>>(emptyList()) }
    var minExperience by remember { mutableStateOf("0") }
    var maxExperience by remember { mutableStateOf("20") }
    var currentStep by remember { mutableStateOf("filter") }
    var uploadedResumes by remember { mutableStateOf<List<UploadedResumeWithMatch>>(emptyList()) }

    val allTechStacks by viewModel.allTechStacks.collectAsState()

    val categories = remember {
        mapOf(
            "Languages" to listOf("Kotlin", "Java", "Python", "JavaScript", "TypeScript", "Swift", "Go", "Rust", "C++", "C#"),
            "Mobile" to listOf("Android", "iOS", "Flutter", "React Native"),
            "Frontend" to listOf("React", "Vue", "Angular", "HTML", "CSS", "Jetpack Compose"),
            "Backend" to listOf("Spring", "Node.js", "Django", "Flask", "Express"),
            "Databases" to listOf("Firebase", "MySQL", "PostgreSQL", "MongoDB", "SQLite", "Realm"),
            "Tools" to listOf("Git", "Docker", "Kubernetes", "AWS", "GCP", "Azure", "Jenkins", "Gradle", "Maven", "API", "REST", "GraphQL")
        )
    }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(if (isDark) Color(0xFF121212) else Color(0xFFF5F6FA))
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        contentPadding = PaddingValues(vertical = 8.dp)
    ) {
        // ============ STEP 1: FILTER SELECTION ============
        if (currentStep == "filter") {
            item {
                Text(
                    "🔍 Step 1: Set Your Criteria",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = if (isDark) Color.White else Color.Black
                )
            }

            item {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .shadow(10.dp, RoundedCornerShape(16.dp))
                        .animateContentSize(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = if (isDark) Color(0xFF1E1E2E) else Color.White
                    )
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(20.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        // Category Selection (Main Dropdown)
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Icon(
                                Icons.Default.Category,
                                contentDescription = null,
                                tint = Color(0xFF4A90E2),
                                modifier = Modifier.size(24.dp)
                            )
                            Text(
                                "Select Category",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                color = if (isDark) Color.White else Color.Black
                            )
                        }

                        // Main Category Dropdown
                        DropdownMenuBox(
                            selectedCategory = selectedCategory,
                            categories = categories.keys.toList(),
                            isDark = isDark,
                            onCategorySelect = { category ->
                                selectedCategory = category
                                selectedTechs = emptyList()
                            }
                        )

                        // Tech Selection (Multiple Checkboxes)
                        if (selectedCategory != null) {
                            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                                Text(
                                    "✅ Select Technologies (${selectedTechs.size} selected)",
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFF4A90E2)
                                )

                                categories[selectedCategory]?.forEach { tech ->
                                    Row(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .clickable {
                                                selectedTechs = if (tech in selectedTechs) {
                                                    selectedTechs - tech
                                                } else {
                                                    selectedTechs + tech
                                                }
                                            }
                                            .padding(vertical = 4.dp),
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                                    ) {
                                        Checkbox(
                                            checked = tech in selectedTechs,
                                            onCheckedChange = { isChecked ->
                                                selectedTechs = if (isChecked) {
                                                    selectedTechs + tech
                                                } else {
                                                    selectedTechs - tech
                                                }
                                            },
                                            colors = CheckboxDefaults.colors(
                                                checkedColor = Color(0xFF4A90E2)
                                            ),
                                            modifier = Modifier.size(20.dp)
                                        )
                                        Text(
                                            tech,
                                            fontSize = 13.sp,
                                            color = if (isDark) Color.White else Color.Black
                                        )
                                    }
                                }
                            }
                        }

                        Divider(color = if (isDark) Color.White.copy(0.1f) else Color.Gray.copy(0.2f), thickness = 2.dp)

                        // Experience Range
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Icon(
                                Icons.Default.TrendingUp,
                                contentDescription = null,
                                tint = Color(0xFF4CAF50),
                                modifier = Modifier.size(24.dp)
                            )
                            Text(
                                "Experience Range",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                color = if (isDark) Color.White else Color.Black
                            )
                        }

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(12.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            OutlinedTextField(
                                value = minExperience,
                                onValueChange = { minExperience = it },
                                label = { Text("Min Years") },
                                modifier = Modifier.weight(1f),
                                shape = RoundedCornerShape(10.dp),
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedBorderColor = Color(0xFF4A90E2),
                                    unfocusedBorderColor = if (isDark) Color.White.copy(0.2f) else Color.Gray.copy(0.3f),
                                    focusedTextColor = if (isDark) Color.White else Color.Black,
                                    unfocusedTextColor = if (isDark) Color.White else Color.Black
                                )
                            )

                            Text("-", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = if (isDark) Color.White else Color.Black)

                            OutlinedTextField(
                                value = maxExperience,
                                onValueChange = { maxExperience = it },
                                label = { Text("Max Years") },
                                modifier = Modifier.weight(1f),
                                shape = RoundedCornerShape(10.dp),
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedBorderColor = Color(0xFF4A90E2),
                                    unfocusedBorderColor = if (isDark) Color.White.copy(0.2f) else Color.Gray.copy(0.3f),
                                    focusedTextColor = if (isDark) Color.White else Color.Black,
                                    unfocusedTextColor = if (isDark) Color.White else Color.Black
                                )
                            )
                        }

                        // Filter Summary
                        if (selectedTechs.isNotEmpty()) {
                            Surface(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(12.dp)),
                                color = Color(0xFF4A90E2).copy(alpha = 0.15f)
                            ) {
                                Column(
                                    modifier = Modifier.padding(12.dp),
                                    verticalArrangement = Arrangement.spacedBy(6.dp)
                                ) {
                                    Text(
                                        "📋 Your Criteria:",
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = Color(0xFF4A90E2)
                                    )
                                    Text(
                                        "Category: $selectedCategory",
                                        fontSize = 11.sp,
                                        color = if (isDark) Color.White else Color.Black
                                    )
                                    Text(
                                        "Tech: ${selectedTechs.joinToString(", ")}",
                                        fontSize = 11.sp,
                                        color = if (isDark) Color.White else Color.Black
                                    )
                                    Text(
                                        "Experience: $minExperience - $maxExperience years",
                                        fontSize = 11.sp,
                                        color = if (isDark) Color.White else Color.Black
                                    )
                                }
                            }
                        }

                        // Action Buttons
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            Button(
                                onClick = {
                                    currentStep = "upload"
                                    uploadedResumes = emptyList()
                                },
                                modifier = Modifier
                                    .weight(1f)
                                    .height(50.dp)
                                    .shadow(6.dp, RoundedCornerShape(12.dp)),
                                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4CAF50)),
                                shape = RoundedCornerShape(12.dp),
                                enabled = selectedTechs.isNotEmpty()
                            ) {
                                Icon(Icons.Default.CloudUpload, contentDescription = null, modifier = Modifier.size(20.dp))
                                Spacer(Modifier.width(8.dp))
                                Text("Next: Upload", fontWeight = FontWeight.ExtraBold, fontSize = 14.sp)
                            }

                            Button(
                                onClick = {
                                    selectedCategory = null
                                    selectedTechs = emptyList()
                                    minExperience = "0"
                                    maxExperience = "20"
                                },
                                modifier = Modifier
                                    .weight(1f)
                                    .height(50.dp),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = if (isDark) Color(0xFF2A2A3E) else Color(0xFFE0E0E0)
                                ),
                                shape = RoundedCornerShape(12.dp)
                            ) {
                                Icon(Icons.Default.RestartAlt, contentDescription = null, modifier = Modifier.size(20.dp), tint = if (isDark) Color.White else Color.Black)
                                Spacer(Modifier.width(8.dp))
                                Text("Reset", fontWeight = FontWeight.ExtraBold, fontSize = 14.sp, color = if (isDark) Color.White else Color.Black)
                            }
                        }
                    }
                }
            }
        }

        // ============ STEP 2: UPLOAD ============
        if (currentStep == "upload") {
            item {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Button(
                        onClick = { currentStep = "filter" },
                        modifier = Modifier.size(40.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = if (isDark) Color(0xFF2A2A3E) else Color(0xFFE0E0E0)),
                        shape = RoundedCornerShape(8.dp),
                        contentPadding = PaddingValues(0.dp)
                    ) {
                        Icon(Icons.Default.ArrowBack, contentDescription = null, modifier = Modifier.size(20.dp), tint = if (isDark) Color.White else Color.Black)
                    }
                    Column {
                        Text(
                            "📤 Step 2: Upload Resumes",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = if (isDark) Color.White else Color.Black
                        )
                        Text(
                            "Your filter will be applied automatically",
                            fontSize = 12.sp,
                            color = if (isDark) Color.White.copy(0.6f) else Color.Gray
                        )
                    }
                }
            }

            item {
                UploadSectionWithFilterFixed(
                    isDark = isDark,
                    recruiterEmail = recruiterEmail,
                    viewModel = viewModel,
                    selectedTechs = selectedTechs,
                    minExp = minExperience.toIntOrNull() ?: 0,
                    maxExp = maxExperience.toIntOrNull() ?: 20,
                    onUploadComplete = { results ->
                        uploadedResumes = results
                        currentStep = "results"
                    }
                )
            }
        }

        // ============ STEP 3: RESULTS ============
        if (currentStep == "results") {
            item {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Button(
                        onClick = {
                            currentStep = "upload"
                            uploadedResumes = emptyList()
                        },
                        modifier = Modifier.size(40.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = if (isDark) Color(0xFF2A2A3E) else Color(0xFFE0E0E0)),
                        shape = RoundedCornerShape(8.dp),
                        contentPadding = PaddingValues(0.dp)
                    ) {
                        Icon(Icons.Default.ArrowBack, contentDescription = null, modifier = Modifier.size(20.dp), tint = if (isDark) Color.White else Color.Black)
                    }
                    Column {
                        Text(
                            "✨ Step 3: Results",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = if (isDark) Color.White else Color.Black
                        )
                        val matched = uploadedResumes.count { it.isMatch }
                        val total = uploadedResumes.size
                        Text(
                            "$matched out of $total resumes matched",
                            fontSize = 12.sp,
                            color = if (isDark) Color.White.copy(0.6f) else Color.Gray
                        )
                    }
                }
            }

            // Matched Section
            if (uploadedResumes.any { it.isMatch }) {
                item {
                    Text(
                        "✅ Perfect Matches (${uploadedResumes.count { it.isMatch }})",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = Color(0xFF4CAF50)
                    )
                }
                items(uploadedResumes.filter { it.isMatch }) { resume ->
                    ResultResumeCard(resume, isDark, matched = true)
                }
            }

            // Unmatched Section
            if (uploadedResumes.any { !it.isMatch }) {
                item {
                    Spacer(Modifier.height(12.dp))
                    Text(
                        "❌ Not Matched (${uploadedResumes.count { !it.isMatch }})",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = Color(0xFFFF6B6B)
                    )
                }
                items(uploadedResumes.filter { !it.isMatch }) { resume ->
                    ResultResumeCard(resume, isDark, matched = false)
                }
            }

            item {
                Spacer(Modifier.height(12.dp))
                Button(
                    onClick = {
                        currentStep = "filter"
                        uploadedResumes = emptyList()
                        selectedCategory = null
                        selectedTechs = emptyList()
                        minExperience = "0"
                        maxExperience = "20"
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp)
                        .shadow(6.dp, RoundedCornerShape(12.dp)),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4A90E2)),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Icon(Icons.Default.RestartAlt, contentDescription = null, modifier = Modifier.size(20.dp))
                    Spacer(Modifier.width(8.dp))
                    Text("Start Over", fontWeight = FontWeight.ExtraBold, fontSize = 14.sp)
                }
            }
        }

        item { Spacer(modifier = Modifier.height(16.dp)) }
    }
}

// ✅ DROPDOWN COMPONENT
@Composable
fun DropdownMenuBox(
    selectedCategory: String?,
    categories: List<String>,
    isDark: Boolean,
    onCategorySelect: (String) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(10.dp))
            .background(if (isDark) Color(0xFF2A2A3E) else Color.White)
            .clickable { expanded = !expanded }
            .padding(12.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                selectedCategory ?: "Select a category...",
                fontSize = 14.sp,
                color = if (isDark) Color.White else Color.Black,
                fontWeight = FontWeight.SemiBold
            )
            Icon(
                if (expanded) Icons.Default.ExpandLess else Icons.Default.ExpandMore,
                contentDescription = null,
                tint = Color(0xFF4A90E2)
            )
        }

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = Modifier
                .fillMaxWidth()
                .background(if (isDark) Color(0xFF1E1E2E) else Color.White)
        ) {
            categories.forEach { category ->
                DropdownMenuItem(
                    text = {
                        Text(
                            category,
                            fontSize = 14.sp,
                            color = if (isDark) Color.White else Color.Black
                        )
                    },
                    onClick = {
                        onCategorySelect(category)
                        expanded = false
                    }
                )
            }
        }
    }
}

// ✅ UPLOAD SECTION
@Composable
fun UploadSectionWithFilterFixed(
    isDark: Boolean,
    recruiterEmail: String,
    viewModel: RecruiterResumeViewModel,
    selectedTechs: List<String>,
    minExp: Int,
    maxExp: Int,
    onUploadComplete: (List<UploadedResumeWithMatch>) -> Unit
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    var selectedFiles by remember { mutableStateOf<List<SelectedResumeFileForFilter>>(emptyList()) }
    var isProcessing by remember { mutableStateOf(false) }
    var uploadProgress by remember { mutableStateOf(0f) }

    val multipleFilePicker = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.OpenMultipleDocuments()
    ) { uris ->
        if (uris.isNotEmpty()) {
            isProcessing = true
            uploadProgress = 0f

            scope.launch(Dispatchers.Default) {
                try {
                    val files = mutableListOf<SelectedResumeFileForFilter>()
                    val totalFiles = uris.size

                    uris.forEachIndexed { index, uri ->
                        try {
                            val fileName = FileUtils.getFileNameFromUri(context, uri)
                            val pdfText = if (FileUtils.isPDFFile(fileName)) {
                                PDFParser.extractTextFromPDF(context, uri.toString())
                            } else {
                                ""
                            }

                            val techStack = if (pdfText.isNotEmpty()) {
                                TechStackExtractor.extractTechStack(pdfText)
                            } else {
                                emptyList()
                            }

                            val experience = extractExperienceFromFileName(fileName)

                            val file = SelectedResumeFileForFilter(
                                uri = uri,
                                fileName = fileName,
                                candidateName = extractCandidateName(fileName),
                                experience = experience,
                                detectedTechStack = techStack,
                                extractedText = pdfText
                            )
                            files.add(file)
                            uploadProgress = (index + 1).toFloat() / totalFiles
                        } catch (e: Exception) {
                            e.printStackTrace()
                        }
                    }

                    selectedFiles = files
                } catch (e: Exception) {
                    e.printStackTrace()
                }
                isProcessing = false
            }
        }
    }

    if (selectedFiles.isEmpty() && !isProcessing) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .shadow(12.dp, RoundedCornerShape(16.dp)),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(
                containerColor = if (isDark) Color(0xFF1E1E2E) else Color.White
            )
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Icon(
                    Icons.Default.CloudUpload,
                    contentDescription = null,
                    modifier = Modifier.size(64.dp),
                    tint = Color(0xFF4A90E2)
                )
                Text(
                    "Select PDFs to Upload",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = if (isDark) Color.White else Color.Black
                )

                Button(
                    onClick = { multipleFilePicker.launch(arrayOf("application/pdf")) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp)
                        .shadow(6.dp, RoundedCornerShape(12.dp)),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4A90E2)),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Icon(Icons.Default.FolderOpen, contentDescription = null, modifier = Modifier.size(20.dp))
                    Spacer(Modifier.width(8.dp))
                    Text("Select PDFs", fontWeight = FontWeight.ExtraBold, fontSize = 14.sp)
                }
            }
        }
    }

    if (isProcessing) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .shadow(8.dp, RoundedCornerShape(16.dp)),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(
                containerColor = if (isDark) Color(0xFF1E1E2E) else Color.White
            )
        ) {
            Column(modifier = Modifier.padding(20.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(28.dp),
                        color = Color(0xFF4A90E2),
                        strokeWidth = 2.dp
                    )
                    Column {
                        Text("Processing Files", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = if (isDark) Color.White else Color.Black)
                        Text("${(uploadProgress * 100).toInt()}%", fontSize = 11.sp, color = if (isDark) Color.White.copy(0.6f) else Color.Gray)
                    }
                }
                LinearProgressIndicator(
                    progress = uploadProgress,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(6.dp)
                        .clip(RoundedCornerShape(3.dp)),
                    color = Color(0xFF4A90E2)
                )
            }
        }
    }

    if (selectedFiles.isNotEmpty()) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text("${selectedFiles.size} file(s) selected", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = if (isDark) Color.White else Color.Black)
            Button(
                onClick = { selectedFiles = emptyList() },
                modifier = Modifier.height(36.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFE0E0E0)),
                shape = RoundedCornerShape(8.dp)
            ) {
                Text("Clear", color = Color.Black, fontSize = 12.sp, fontWeight = FontWeight.Bold)
            }
        }

        selectedFiles.forEach { file ->
            ResumeFileCardForUpload(file, isDark)
        }

        Button(
            onClick = {
                scope.launch {
                    val resumes = selectedFiles.map { file ->
                        RecruiterResumeEntity(
                            resumeId = UUID.randomUUID().toString(),
                            fileName = file.fileName,
                            candidateName = file.candidateName,
                            candidateEmail = "",
                            experience = file.experience,
                            techStack = file.detectedTechStack.joinToString(", "),
                            rawText = file.extractedText,
                            uploadedDate = System.currentTimeMillis(),
                            recruiterEmail = recruiterEmail
                        )
                    }
                    viewModel.insertMultipleResumes(resumes)

                    val results = selectedFiles.map { file ->
                        val matchedTechs = file.detectedTechStack.filter { it in selectedTechs }
                        val isExpMatch = file.experience >= minExp && file.experience <= maxExp
                        val isTechMatch = matchedTechs.isNotEmpty()
                        val isMatch = isTechMatch && isExpMatch

                        UploadedResumeWithMatch(
                            candidateName = file.candidateName,
                            fileName = file.fileName,
                            experience = file.experience,
                            detectedTechStack = file.detectedTechStack,
                            matchedTechs = matchedTechs,
                            isMatch = isMatch,
                            matchPercentage = if (selectedTechs.isEmpty()) 0 else (matchedTechs.size * 100) / selectedTechs.size
                        )
                    }

                    viewModel.filterSearchHistory(
                        selectedTechs,
                        minExp,
                        maxExp,
                        results.count { it.isMatch }
                    )

                    onUploadComplete(results)
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp)
                .shadow(8.dp, RoundedCornerShape(12.dp)),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4CAF50)),
            shape = RoundedCornerShape(12.dp)
        ) {
            Icon(Icons.Default.Check, contentDescription = null, modifier = Modifier.size(20.dp))
            Spacer(Modifier.width(8.dp))
            Text("✅ Upload & Parse (${selectedFiles.size})", fontWeight = FontWeight.ExtraBold, fontSize = 14.sp)
        }
    }
}

// ✅ RESULT CARD
@Composable
fun ResultResumeCard(resume: UploadedResumeWithMatch, isDark: Boolean, matched: Boolean) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(6.dp, RoundedCornerShape(14.dp))
            .animateContentSize(),
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (isDark) Color(0xFF1E1E2E) else Color.White
        ),
        border = if (matched) {
            androidx.compose.foundation.BorderStroke(2.dp, Color(0xFF4CAF50))
        } else {
            androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFFF6B6B).copy(alpha = 0.3f))
        }
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        resume.candidateName,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold,
                        color = if (isDark) Color.White else Color.Black
                    )
                    Text(
                        "${resume.experience} years exp",
                        fontSize = 12.sp,
                        color = if (isDark) Color.White.copy(0.6f) else Color.Gray
                    )
                }

                Surface(
                    shape = RoundedCornerShape(10.dp),
                    color = if (matched) Color(0xFF4CAF50).copy(alpha = 0.2f) else Color(0xFFFF6B6B).copy(alpha = 0.2f)
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.padding(12.dp, 8.dp)
                    ) {
                        Text(
                            if (matched) "✅ Match" else "❌ No Match",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = if (matched) Color(0xFF4CAF50) else Color(0xFFFF6B6B)
                        )
                        if (matched && resume.matchPercentage > 0) {
                            Text(
                                "${resume.matchPercentage}%",
                                fontSize = 10.sp,
                                color = if (matched) Color(0xFF4CAF50) else Color(0xFFFF6B6B)
                            )
                        }
                    }
                }
            }

            if (resume.matchedTechs.isNotEmpty()) {
                Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                    Text(
                        "🎯 Matched Tech (${resume.matchedTechs.size})",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF4CAF50)
                    )
                    FlowRow(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        resume.matchedTechs.forEach { tech ->
                            Surface(
                                shape = RoundedCornerShape(6.dp),
                                color = Color(0xFF4CAF50).copy(alpha = 0.15f)
                            ) {
                                Text(
                                    tech,
                                    modifier = Modifier.padding(8.dp, 4.dp),
                                    fontSize = 10.sp,
                                    color = Color(0xFF4CAF50),
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }
                }
            }

            if (resume.detectedTechStack.isNotEmpty()) {
                Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                    Text(
                        "🔧 Detected Tech (${resume.detectedTechStack.size})",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = if (isDark) Color.White.copy(0.7f) else Color.Gray
                    )
                    FlowRow(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        resume.detectedTechStack.take(6).forEach { tech ->
                            Surface(
                                shape = RoundedCornerShape(6.dp),
                                color = Color(0xFF4A90E2).copy(alpha = 0.15f)
                            ) {
                                Text(
                                    tech,
                                    modifier = Modifier.padding(8.dp, 4.dp),
                                    fontSize = 10.sp,
                                    color = Color(0xFF4A90E2),
                                    fontWeight = FontWeight.SemiBold
                                )
                            }
                        }
                        if (resume.detectedTechStack.size > 6) {
                            Surface(
                                shape = RoundedCornerShape(6.dp),
                                color = Color(0xFF4A90E2).copy(alpha = 0.15f)
                            ) {
                                Text(
                                    "+${resume.detectedTechStack.size - 6}",
                                    modifier = Modifier.padding(8.dp, 4.dp),
                                    fontSize = 10.sp,
                                    color = Color(0xFF4A90E2),
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

// ✅ FILE CARD
@Composable
fun ResumeFileCardForUpload(file: SelectedResumeFileForFilter, isDark: Boolean) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(4.dp, RoundedCornerShape(12.dp)),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (isDark) Color(0xFF1E1E2E) else Color.White
        )
    ) {
        Column(modifier = Modifier.padding(12.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Icon(
                    Icons.Default.FilePresent,
                    contentDescription = null,
                    tint = Color(0xFF4A90E2),
                    modifier = Modifier.size(32.dp)
                )
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        file.candidateName,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        color = if (isDark) Color.White else Color.Black
                    )
                    Text(
                        file.fileName,
                        fontSize = 10.sp,
                        color = if (isDark) Color.White.copy(0.6f) else Color.Gray,
                        maxLines = 1
                    )
                }
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Surface(
                    shape = RoundedCornerShape(6.dp),
                    color = Color(0xFF4A90E2).copy(alpha = 0.15f)
                ) {
                    Row(
                        modifier = Modifier.padding(8.dp, 4.dp),
                        horizontalArrangement = Arrangement.spacedBy(4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(Icons.Default.School, contentDescription = null, tint = Color(0xFF4A90E2), modifier = Modifier.size(12.dp))
                        Text("${file.experience}y", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = Color(0xFF4A90E2))
                    }
                }

                if (file.detectedTechStack.isNotEmpty()) {
                    Surface(
                        shape = RoundedCornerShape(6.dp),
                        color = Color(0xFF9C27B0).copy(alpha = 0.15f)
                    ) {
                        Text("+${file.detectedTechStack.size} tech", modifier = Modifier.padding(8.dp, 4.dp), fontSize = 10.sp, fontWeight = FontWeight.Bold, color = Color(0xFF9C27B0))
                    }
                }
            }
        }
    }
}

// ✅ HELPER FUNCTIONS
private fun extractCandidateName(fileName: String): String {
    val name = fileName.split("_", "-").firstOrNull() ?: fileName
    return name.replace(".pdf", "").replace(".PDF", "").trim()
}

private fun extractExperienceFromFileName(fileName: String): Int {
    val parts = fileName.split("_", "-", ".")
    for (part in parts) {
        val cleaned = part.replace("y", "").replace("Y", "").trim()
        val years = cleaned.toIntOrNull()
        if (years != null && years in 0..100) return years
    }
    return 0
}