//package com.example.feature_recruiter.screens
//
//import androidx.activity.compose.rememberLauncherForActivityResult
//import androidx.activity.result.contract.ActivityResultContracts
//import androidx.compose.animation.animateContentSize
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
//import androidx.compose.ui.draw.clip
//import androidx.compose.ui.draw.shadow
//import androidx.compose.ui.graphics.Brush
//import androidx.compose.ui.graphics.Color
//import androidx.compose.ui.platform.LocalContext
//import androidx.compose.ui.text.font.FontWeight
//import androidx.compose.ui.text.style.TextAlign
//import androidx.compose.ui.unit.dp
//import androidx.compose.ui.unit.sp
//import com.example.feature_recruiter.database.entity.RecruiterResumeEntity
//import com.example.feature_recruiter.util.FileUtils
//import com.example.feature_recruiter.util.PDFParser
//import com.example.feature_recruiter.util.TechStackExtractor
//import com.example.feature_recruiter.viewmodel.RecruiterResumeViewModel
//import kotlinx.coroutines.Dispatchers
//import kotlinx.coroutines.launch
//import java.util.*
//
//@Composable
//fun ResumeUploadScreen(
//    recruiterEmail: String,
//    isDark: Boolean,
//    viewModel: RecruiterResumeViewModel,
//    onUploadSuccess: () -> Unit,
//    modifier: Modifier = Modifier
//) {
//    val context = LocalContext.current
//    val scope = rememberCoroutineScope()
//
//    var selectedFiles by remember { mutableStateOf<List<SelectedResumeFile>>(emptyList()) }
//    var isProcessing by remember { mutableStateOf(false) }
//    var uploadProgress by remember { mutableStateOf(0f) }
//    var showSuccessDialog by remember { mutableStateOf(false) }
//
//    // ✅ MULTIPLE FILES ONLY
//    val multipleFilePicker = rememberLauncherForActivityResult(
//        contract = ActivityResultContracts.OpenMultipleDocuments()
//    ) { uris ->
//        if (uris.isNotEmpty()) {
//            isProcessing = true
//            uploadProgress = 0f
//
//            scope.launch(Dispatchers.Default) {
//                try {
//                    val files = mutableListOf<SelectedResumeFile>()
//                    val totalFiles = uris.size
//
//                    uris.forEachIndexed { index, uri ->
//                        try {
//                            val fileName = FileUtils.getFileNameFromUri(context, uri)
//                            val pdfText = if (FileUtils.isPDFFile(fileName)) {
//                                PDFParser.extractTextFromPDF(context, uri.toString())
//                            } else {
//                                ""
//                            }
//
//                            val techStack = if (pdfText.isNotEmpty()) {
//                                TechStackExtractor.extractTechStack(pdfText)
//                            } else {
//                                emptyList()
//                            }
//
//                            val file = SelectedResumeFile(
//                                uri = uri,
//                                fileName = fileName,
//                                candidateName = extractCandidateName(fileName),
//                                experience = extractExperienceFromFileName(fileName),
//                                detectedTechStack = techStack,
//                                extractedText = pdfText
//                            )
//                            files.add(file)
//                            uploadProgress = (index + 1).toFloat() / totalFiles
//                        } catch (e: Exception) {
//                            e.printStackTrace()
//                        }
//                    }
//
//                    selectedFiles = files
//                } catch (e: Exception) {
//                    e.printStackTrace()
//                }
//                isProcessing = false
//            }
//        }
//    }
//
//    LazyColumn(
//        modifier = modifier
//            .fillMaxSize()
//            .background(if (isDark) Color(0xFF121212) else Color(0xFFF5F6FA))
//            .padding(16.dp),
//        verticalArrangement = Arrangement.spacedBy(20.dp),
//        contentPadding = PaddingValues(vertical = 8.dp)
//    ) {
//        // Empty State - Upload Prompt
//        if (selectedFiles.isEmpty() && !isProcessing) {
//            item {
//                Card(
//                    modifier = Modifier
//                        .fillMaxWidth()
//                        .shadow(12.dp, RoundedCornerShape(24.dp)),
//                    shape = RoundedCornerShape(24.dp),
//                    colors = CardDefaults.cardColors(
//                        containerColor = if (isDark) Color(0xFF1E1E2E) else Color.White
//                    )
//                ) {
//                    Box(
//                        modifier = Modifier
//                            .fillMaxWidth()
//                            .background(
//                                Brush.verticalGradient(
//                                    listOf(Color(0xFF4A90E2), Color(0xFF357ABD))
//                                )
//                            )
//                            .padding(40.dp),
//                        contentAlignment = Alignment.Center
//                    ) {
//                        Column(
//                            horizontalAlignment = Alignment.CenterHorizontally,
//                            verticalArrangement = Arrangement.spacedBy(24.dp)
//                        ) {
//                            Box(
//                                modifier = Modifier
//                                    .size(100.dp)
//                                    .background(
//                                        Color.White.copy(alpha = 0.15f),
//                                        RoundedCornerShape(28.dp)
//                                    ),
//                                contentAlignment = Alignment.Center
//                            ) {
//                                Icon(
//                                    Icons.Default.CloudUpload,
//                                    contentDescription = null,
//                                    modifier = Modifier.size(56.dp),
//                                    tint = Color.White
//                                )
//                            }
//
//                            Column(
//                                horizontalAlignment = Alignment.CenterHorizontally,
//                                verticalArrangement = Arrangement.spacedBy(8.dp)
//                            ) {
//                                Text(
//                                    "Upload Resumes",
//                                    fontSize = 28.sp,
//                                    fontWeight = FontWeight.ExtraBold,
//                                    color = Color.White,
//                                    textAlign = TextAlign.Center
//                                )
//                                Text(
//                                    "Upload multiple PDF resumes at once\nOur system will automatically extract information",
//                                    fontSize = 13.sp,
//                                    color = Color.White.copy(alpha = 0.9f),
//                                    textAlign = TextAlign.Center,
//                                    lineHeight = 18.sp
//                                )
//                            }
//
//                            Button(
//                                onClick = { multipleFilePicker.launch(arrayOf("application/pdf")) },
//                                modifier = Modifier
//                                    .fillMaxWidth()
//                                    .height(56.dp)
//                                    .shadow(8.dp, RoundedCornerShape(14.dp)),
//                                colors = ButtonDefaults.buttonColors(
//                                    containerColor = Color.White
//                                ),
//                                shape = RoundedCornerShape(14.dp)
//                            ) {
//                                Icon(
//                                    Icons.Default.FolderOpen,
//                                    contentDescription = null,
//                                    modifier = Modifier.size(22.dp),
//                                    tint = Color(0xFF4A90E2)
//                                )
//                                Spacer(Modifier.width(12.dp))
//                                Text(
//                                    "Select PDFs",
//                                    fontWeight = FontWeight.ExtraBold,
//                                    fontSize = 16.sp,
//                                    color = Color(0xFF4A90E2)
//                                )
//                            }
//                        }
//                    }
//                }
//            }
//        }
//
//        // Processing Progress
//        if (isProcessing) {
//            item {
//                Card(
//                    modifier = Modifier
//                        .fillMaxWidth()
//                        .shadow(8.dp, RoundedCornerShape(16.dp)),
//                    shape = RoundedCornerShape(16.dp),
//                    colors = CardDefaults.cardColors(
//                        containerColor = if (isDark) Color(0xFF1E1E2E) else Color.White
//                    )
//                ) {
//                    Column(modifier = Modifier.padding(24.dp), verticalArrangement = Arrangement.spacedBy(16.dp)) {
//                        Row(
//                            verticalAlignment = Alignment.CenterVertically,
//                            horizontalArrangement = Arrangement.spacedBy(12.dp)
//                        ) {
//                            CircularProgressIndicator(
//                                modifier = Modifier.size(32.dp),
//                                color = Color(0xFF4A90E2),
//                                strokeWidth = 3.dp
//                            )
//                            Column {
//                                Text(
//                                    "Processing Files",
//                                    fontSize = 16.sp,
//                                    fontWeight = FontWeight.Bold,
//                                    color = if (isDark) Color.White else Color.Black
//                                )
//                                Text(
//                                    "${(uploadProgress * 100).toInt()}%",
//                                    fontSize = 12.sp,
//                                    color = if (isDark) Color.White.copy(0.6f) else Color.Gray
//                                )
//                            }
//                        }
//                        LinearProgressIndicator(
//                            progress = uploadProgress,
//                            modifier = Modifier
//                                .fillMaxWidth()
//                                .height(6.dp)
//                                .clip(RoundedCornerShape(3.dp)),
//                            color = Color(0xFF4A90E2),
//                            trackColor = if (isDark) Color.White.copy(0.1f) else Color.Gray.copy(0.2f)
//                        )
//                    }
//                }
//            }
//        }
//
//        // Selected Files Header
//        if (selectedFiles.isNotEmpty()) {
//            item {
//                Row(
//                    modifier = Modifier
//                        .fillMaxWidth()
//                        .padding(vertical = 8.dp),
//                    verticalAlignment = Alignment.CenterVertically,
//                    horizontalArrangement = Arrangement.SpaceBetween
//                ) {
//                    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
//                        Icon(
//                            Icons.Default.CheckCircle,
//                            contentDescription = null,
//                            tint = Color(0xFF4CAF50),
//                            modifier = Modifier.size(28.dp)
//                        )
//                        Column {
//                            Text(
//                                "Ready to Upload",
//                                fontSize = 16.sp,
//                                fontWeight = FontWeight.ExtraBold,
//                                color = if (isDark) Color.White else Color.Black
//                            )
//                            Text(
//                                "${selectedFiles.size} file(s) selected",
//                                fontSize = 12.sp,
//                                color = if (isDark) Color.White.copy(0.6f) else Color.Gray
//                            )
//                        }
//                    }
//                    Button(
//                        onClick = { selectedFiles = emptyList() },
//                        modifier = Modifier.height(36.dp),
//                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFE0E0E0)),
//                        shape = RoundedCornerShape(8.dp)
//                    ) {
//                        Text("Clear", color = Color.Black, fontSize = 12.sp, fontWeight = FontWeight.Bold)
//                    }
//                }
//            }
//        }
//
//        // Resume Files List
//        items(selectedFiles) { file ->
//            ResumeFileCard(file, isDark)
//        }
//
//        // Upload Button
//        if (selectedFiles.isNotEmpty()) {
//            item {
//                Button(
//                    onClick = {
//                        scope.launch {
//                            val resumes = selectedFiles.map { file ->
//                                RecruiterResumeEntity(
//                                    resumeId = UUID.randomUUID().toString(),
//                                    fileName = file.fileName,
//                                    candidateName = file.candidateName,
//                                    candidateEmail = "",
//                                    experience = file.experience,
//                                    techStack = file.detectedTechStack.joinToString(", "),
//                                    rawText = file.extractedText,
//                                    uploadedDate = System.currentTimeMillis(),
//                                    recruiterEmail = recruiterEmail
//                                )
//                            }
//                            viewModel.insertMultipleResumes(resumes)
//                            showSuccessDialog = true
//                            selectedFiles = emptyList()
//                        }
//                    },
//                    modifier = Modifier
//                        .fillMaxWidth()
//                        .height(56.dp)
//                        .shadow(8.dp, RoundedCornerShape(14.dp)),
//                    colors = ButtonDefaults.buttonColors(
//                        containerColor = Color(0xFF4CAF50)
//                    ),
//                    shape = RoundedCornerShape(14.dp)
//                ) {
//                    Icon(Icons.Default.CloudUpload, contentDescription = null, modifier = Modifier.size(24.dp))
//                    Spacer(Modifier.width(12.dp))
//                    Text("Upload All (${selectedFiles.size})", fontWeight = FontWeight.ExtraBold, fontSize = 16.sp)
//                }
//            }
//
//            item { Spacer(modifier = Modifier.height(16.dp)) }
//        }
//    }
//
//    // Success Dialog
//    if (showSuccessDialog) {
//        AlertDialog(
//            onDismissRequest = { showSuccessDialog = false },
//            icon = {
//                Icon(
//                    Icons.Default.CheckCircle,
//                    contentDescription = null,
//                    tint = Color(0xFF4CAF50),
//                    modifier = Modifier.size(48.dp)
//                )
//            },
//            title = { Text("✅ Success!", fontSize = 20.sp, fontWeight = FontWeight.ExtraBold) },
//            text = { Text("${selectedFiles.size} resume(s) uploaded successfully!", fontSize = 14.sp) },
//            confirmButton = {
//                Button(
//                    onClick = {
//                        showSuccessDialog = false
//                        viewModel.loadAllResumes()
//                        onUploadSuccess()
//                    },
//                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4CAF50)),
//                    shape = RoundedCornerShape(8.dp)
//                ) {
//                    Text("Go to Dashboard", fontWeight = FontWeight.Bold)
//                }
//            }
//        )
//    }
//}
//
//@Composable
//fun ResumeFileCard(file: SelectedResumeFile, isDark: Boolean) {
//    Card(
//        modifier = Modifier
//            .fillMaxWidth()
//            .shadow(6.dp, RoundedCornerShape(14.dp))
//            .animateContentSize(),
//        shape = RoundedCornerShape(14.dp),
//        colors = CardDefaults.cardColors(
//            containerColor = if (isDark) Color(0xFF1E1E2E) else Color.White
//        )
//    ) {
//        Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
//            Row(
//                modifier = Modifier.fillMaxWidth(),
//                verticalAlignment = Alignment.CenterVertically,
//                horizontalArrangement = Arrangement.spacedBy(12.dp)
//            ) {
//                Icon(
//                    Icons.Default.FilePresent,
//                    contentDescription = null,
//                    tint = Color(0xFF4A90E2),
//                    modifier = Modifier.size(36.dp)
//                )
//                Column(modifier = Modifier.weight(1f)) {
//                    Text(
//                        file.candidateName,
//                        fontSize = 15.sp,
//                        fontWeight = FontWeight.Bold,
//                        color = if (isDark) Color.White else Color.Black
//                    )
//                    Text(
//                        file.fileName,
//                        fontSize = 11.sp,
//                        color = if (isDark) Color.White.copy(0.6f) else Color.Gray,
//                        maxLines = 1
//                    )
//                }
//            }
//
//            Row(
//                modifier = Modifier.fillMaxWidth(),
//                horizontalArrangement = Arrangement.spacedBy(12.dp),
//                verticalAlignment = Alignment.CenterVertically
//            ) {
//                Surface(
//                    shape = RoundedCornerShape(8.dp),
//                    color = Color(0xFF4A90E2).copy(alpha = 0.15f)
//                ) {
//                    Row(
//                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
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
//                            "${file.experience} years",
//                            fontSize = 11.sp,
//                            fontWeight = FontWeight.Bold,
//                            color = Color(0xFF4A90E2)
//                        )
//                    }
//                }
//
//                if (file.detectedTechStack.isNotEmpty()) {
//                    Surface(
//                        shape = RoundedCornerShape(8.dp),
//                        color = Color(0xFF9C27B0).copy(alpha = 0.15f)
//                    ) {
//                        Text(
//                            "+${file.detectedTechStack.size} tech",
//                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
//                            fontSize = 11.sp,
//                            fontWeight = FontWeight.Bold,
//                            color = Color(0xFF9C27B0)
//                        )
//                    }
//                }
//            }
//
//            if (file.detectedTechStack.isNotEmpty()) {
//                FlowRow(
//                    modifier = Modifier.fillMaxWidth(),
//                    horizontalArrangement = Arrangement.spacedBy(6.dp)
//                ) {
//                    file.detectedTechStack.take(5).forEach { tech ->
//                        Surface(
//                            shape = RoundedCornerShape(6.dp),
//                            color = Color(0xFF4A90E2).copy(alpha = 0.15f)
//                        ) {
//                            Text(
//                                tech,
//                                modifier = Modifier.padding(8.dp, 4.dp),
//                                fontSize = 10.sp,
//                                color = Color(0xFF4A90E2),
//                                fontWeight = FontWeight.SemiBold
//                            )
//                        }
//                    }
//                    if (file.detectedTechStack.size > 5) {
//                        Surface(
//                            shape = RoundedCornerShape(6.dp),
//                            color = Color(0xFF4A90E2).copy(alpha = 0.15f)
//                        ) {
//                            Text(
//                                "+${file.detectedTechStack.size - 5}",
//                                modifier = Modifier.padding(8.dp, 4.dp),
//                                fontSize = 10.sp,
//                                color = Color(0xFF4A90E2),
//                                fontWeight = FontWeight.Bold
//                            )
//                        }
//                    }
//                }
//            }
//        }
//    }
//}
//
//data class SelectedResumeFile(
//    val uri: android.net.Uri,
//    val fileName: String,
//    val candidateName: String,
//    val experience: Int,
//    val detectedTechStack: List<String>,
//    val extractedText: String
//)
//
//private fun extractCandidateName(fileName: String): String {
//    val name = fileName.split("_", "-").firstOrNull() ?: fileName
//    return name.replace(".pdf", "").replace(".PDF", "").trim()
//}
//
//private fun extractExperienceFromFileName(fileName: String): Int {
//    val parts = fileName.split("_", "-", ".")
//    for (part in parts) {
//        val cleaned = part.replace("y", "").replace("Y", "").trim()
//        val years = cleaned.toIntOrNull()
//        if (years != null && years in 0..100) return years
//    }
//    return 0
//}