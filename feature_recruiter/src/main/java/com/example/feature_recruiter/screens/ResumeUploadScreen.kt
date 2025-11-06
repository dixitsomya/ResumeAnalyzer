//
//package com.example.feature_recruiter.screens
//
//import androidx.activity.compose.rememberLauncherForActivityResult
//import androidx.activity.result.contract.ActivityResultContracts
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
//import androidx.compose.ui.platform.LocalContext
//import androidx.compose.ui.text.font.FontWeight
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
//@OptIn(ExperimentalMaterial3Api::class)
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
//    var uploadMode by remember { mutableStateOf<UploadMode?>(null) }
//    var selectedFiles by remember { mutableStateOf<List<SelectedFile>>(emptyList()) }
//    var showSuccessDialog by remember { mutableStateOf(false) }
//    var isProcessing by remember { mutableStateOf(false) }
//    var uploadProgress by remember { mutableStateOf(0f) }
//    var errorMessage by remember { mutableStateOf<String?>(null) }
//
//    val snackbarHostState = remember { SnackbarHostState() }
//
//    // Single File Picker
//    val singleFilePicker = rememberLauncherForActivityResult(
//        contract = ActivityResultContracts.GetContent()
//    ) { uri ->
//        if (uri != null) {
//            val fileName = FileUtils.getFileNameFromUri(context, uri)
//            isProcessing = true
//            uploadProgress = 0f
//            errorMessage = null
//
//            scope.launch(Dispatchers.Default) {
//                try {
//                    val pdfText = PDFParser.extractTextFromPDF(context, uri.toString())
//                    val techStack = if (pdfText.isNotEmpty()) {
//                        TechStackExtractor.extractTechStack(pdfText)
//                    } else {
//                        emptyList()
//                    }
//
//                    val candidateName = extractCandidateName(fileName)
//                    val experience = extractExperienceFromFileName(fileName)
//
//                    selectedFiles = listOf(
//                        SelectedFile(
//                            uri = uri,
//                            fileName = fileName,
//                            candidateName = candidateName,
//                            candidateEmail = extractEmailFromFileName(fileName),
//                            experience = experience,
//                            detectedTechStack = techStack,
//                            extractedText = pdfText
//                        )
//                    )
//                    uploadProgress = 1f
//                } catch (e: Exception) {
//                    e.printStackTrace()
//                    errorMessage = "Error processing file: ${e.message}"
//                    selectedFiles = emptyList()
//                }
//                isProcessing = false
//            }
//        }
//    }
//
//    // Multiple Files Picker
//    val multipleFilePicker = rememberLauncherForActivityResult(
//        contract = ActivityResultContracts.OpenMultipleDocuments()
//    ) { uris ->
//        if (uris.isNotEmpty()) {
//            isProcessing = true
//            uploadProgress = 0f
//            errorMessage = null
//
//            scope.launch(Dispatchers.Default) {
//                try {
//                    val files = mutableListOf<SelectedFile>()
//                    val totalFiles = uris.size
//
//                    uris.forEachIndexed { index, uri ->
//                        try {
//                            val fileName = FileUtils.getFileNameFromUri(context, uri)
//
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
//                            val file = SelectedFile(
//                                uri = uri,
//                                fileName = fileName,
//                                candidateName = extractCandidateName(fileName),
//                                candidateEmail = extractEmailFromFileName(fileName),
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
//                    errorMessage = "Error processing files: ${e.message}"
//                }
//                isProcessing = false
//            }
//        }
//    }
//
//    Scaffold(
//        snackbarHost = { SnackbarHost(snackbarHostState) }
//    ) { innerPadding ->
//        LazyColumn(
//            modifier = modifier
//                .fillMaxSize()
//                .background(if (isDark) Color(0xFF121212) else Color(0xFFF5F6FA))
//                .padding(innerPadding)
//                .padding(16.dp),
//            verticalArrangement = Arrangement.spacedBy(16.dp)
//        ) {
//            item {
//                Text(
//                    "Upload Resumes",
//                    fontSize = 24.sp,
//                    fontWeight = FontWeight.Bold,
//                    color = if (isDark) Color.White else Color.Black
//                )
//                Text(
//                    "Upload single or multiple resumes",
//                    fontSize = 14.sp,
//                    color = if (isDark) Color.White.copy(0.7f) else Color.Gray
//                )
//            }
//
//            // Upload Mode Selection (Show only if no files selected)
//            if (selectedFiles.isEmpty() && uploadMode == null) {
//                item {
//                    Card(
//                        modifier = Modifier
//                            .fillMaxWidth()
//                            .shadow(8.dp, RoundedCornerShape(16.dp)),
//                        shape = RoundedCornerShape(16.dp),
//                        colors = CardDefaults.cardColors(
//                            containerColor = if (isDark) Color(0xFF1E1E2E) else Color.White
//                        )
//                    ) {
//                        Column(modifier = Modifier.padding(20.dp)) {
//                            Text(
//                                "Upload Mode",
//                                fontSize = 16.sp,
//                                fontWeight = FontWeight.SemiBold,
//                                color = if (isDark) Color.White else Color.Black
//                            )
//                            Spacer(modifier = Modifier.height(16.dp))
//
//                            Button(
//                                onClick = {
//                                    uploadMode = UploadMode.SINGLE
//                                    singleFilePicker.launch("application/pdf")
//                                },
//                                enabled = !isProcessing,
//                                modifier = Modifier
//                                    .fillMaxWidth()
//                                    .height(56.dp),
//                                colors = ButtonDefaults.buttonColors(
//                                    containerColor = Color(0xFF4A90E2)
//                                ),
//                                shape = RoundedCornerShape(12.dp)
//                            ) {
//                                Icon(Icons.Default.Upload, contentDescription = null)
//                                Spacer(Modifier.width(12.dp))
//                                Text("📄 Upload Single Resume", fontWeight = FontWeight.Bold)
//                            }
//
//                            Spacer(modifier = Modifier.height(12.dp))
//
//                            Button(
//                                onClick = {
//                                    uploadMode = UploadMode.MULTIPLE
//                                    multipleFilePicker.launch(arrayOf("application/pdf"))
//                                },
//                                enabled = !isProcessing,
//                                modifier = Modifier
//                                    .fillMaxWidth()
//                                    .height(56.dp),
//                                colors = ButtonDefaults.buttonColors(
//                                    containerColor = Color(0xFFFFA726)
//                                ),
//                                shape = RoundedCornerShape(12.dp)
//                            ) {
//                                Icon(Icons.Default.FolderOpen, contentDescription = null)
//                                Spacer(Modifier.width(12.dp))
//                                Text("📁 Upload Multiple Resumes", fontWeight = FontWeight.Bold)
//                            }
//                        }
//                    }
//                }
//            }
//
//            // Processing Indicator
//            if (isProcessing) {
//                item {
//                    Card(
//                        modifier = Modifier.fillMaxWidth(),
//                        shape = RoundedCornerShape(12.dp),
//                        colors = CardDefaults.cardColors(
//                            containerColor = if (isDark) Color(0xFF1E1E2E) else Color.White
//                        )
//                    ) {
//                        Column(modifier = Modifier.padding(20.dp)) {
//                            Text(
//                                "Processing: ${(uploadProgress * 100).toInt()}%",
//                                fontSize = 14.sp,
//                                fontWeight = FontWeight.Bold,
//                                color = if (isDark) Color.White else Color.Black
//                            )
//                            Spacer(modifier = Modifier.height(12.dp))
//                            LinearProgressIndicator(
//                                progress = uploadProgress,
//                                modifier = Modifier
//                                    .fillMaxWidth()
//                                    .height(8.dp),
//                                color = Color(0xFF4A90E2),
//                                trackColor = Color.Gray.copy(alpha = 0.2f)
//                            )
//                        }
//                    }
//                }
//            }
//
//            // Error Message
//            if (errorMessage != null) {
//                item {
//                    Surface(
//                        modifier = Modifier
//                            .fillMaxWidth()
//                            .shadow(4.dp, RoundedCornerShape(8.dp)),
//                        color = Color(0xFFFFEBEE),
//                        shape = RoundedCornerShape(8.dp)
//                    ) {
//                        Row(
//                            modifier = Modifier
//                                .fillMaxWidth()
//                                .padding(12.dp),
//                            verticalAlignment = Alignment.CenterVertically,
//                            horizontalArrangement = Arrangement.spacedBy(8.dp)
//                        ) {
//                            Icon(
//                                Icons.Default.Error,
//                                contentDescription = null,
//                                tint = Color(0xFFC62828),
//                                modifier = Modifier.size(20.dp)
//                            )
//                            Text(
//                                errorMessage ?: "",
//                                color = Color(0xFFC62828),
//                                fontSize = 12.sp
//                            )
//                            Spacer(modifier = Modifier.weight(1f))
//                            IconButton(onClick = { errorMessage = null }, modifier = Modifier.size(24.dp)) {
//                                Icon(Icons.Default.Close, contentDescription = null)
//                            }
//                        }
//                    }
//                }
//            }
//
//            // Selected Files List
//            if (selectedFiles.isNotEmpty()) {
//                item {
//                    Row(
//                        modifier = Modifier
//                            .fillMaxWidth()
//                            .padding(vertical = 8.dp),
//                        verticalAlignment = Alignment.CenterVertically,
//                        horizontalArrangement = Arrangement.SpaceBetween
//                    ) {
//                        Text(
//                            "Selected Files: ${selectedFiles.size}",
//                            fontSize = 14.sp,
//                            fontWeight = FontWeight.Bold,
//                            color = if (isDark) Color.White else Color.Black
//                        )
//                        Button(
//                            onClick = {
//                                selectedFiles = emptyList()
//                                uploadMode = null
//                                errorMessage = null
//                            },
//                            modifier = Modifier.height(36.dp),
//                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFE0E0E0)),
//                            shape = RoundedCornerShape(8.dp)
//                        ) {
//                            Text("Clear", color = Color.Black, fontSize = 12.sp)
//                        }
//                    }
//                }
//
//                items(selectedFiles) { file ->
//                    UploadFileCard(file, isDark)
//                }
//            }
//
//            // Upload Button (for single resume with details form)
//            if (selectedFiles.size == 1 && uploadMode == UploadMode.SINGLE) {
//                item {
//                    UploadSingleResumeForm(
//                        file = selectedFiles[0],
//                        isDark = isDark,
//                        recruiterEmail = recruiterEmail,
//                        viewModel = viewModel,
//                        onUploadSuccess = {
//                            showSuccessDialog = true
//                            selectedFiles = emptyList()
//                            uploadMode = null
//                        }
//                    )
//                }
//            }
//
//            // Upload Button (for multiple resumes)
//            if (selectedFiles.size > 1) {
//                item {
//                    Card(
//                        modifier = Modifier
//                            .fillMaxWidth()
//                            .shadow(6.dp, RoundedCornerShape(12.dp)),
//                        shape = RoundedCornerShape(12.dp),
//                        colors = CardDefaults.cardColors(
//                            containerColor = if (isDark) Color(0xFF1E1E2E) else Color.White
//                        )
//                    ) {
//                        Column(modifier = Modifier.padding(16.dp)) {
//                            Text(
//                                "Ready to upload ${selectedFiles.size} resumes?",
//                                fontSize = 14.sp,
//                                fontWeight = FontWeight.Bold,
//                                color = if (isDark) Color.White else Color.Black
//                            )
//                            Spacer(modifier = Modifier.height(12.dp))
//                            Button(
//                                onClick = {
//                                    scope.launch {
//                                        val resumes = selectedFiles.map { file ->
//                                            RecruiterResumeEntity(
//                                                resumeId = UUID.randomUUID().toString(),
//                                                fileName = file.fileName,
//                                                candidateName = file.candidateName,
//                                                candidateEmail = file.candidateEmail,
//                                                experience = file.experience,
//                                                techStack = file.detectedTechStack.joinToString(", "),
//                                                rawText = file.extractedText,
//                                                uploadedDate = System.currentTimeMillis(),
//                                                recruiterEmail = recruiterEmail
//                                            )
//                                        }
//                                        viewModel.insertMultipleResumes(resumes)
//                                        showSuccessDialog = true
//                                        selectedFiles = emptyList()
//                                        uploadMode = null
//                                    }
//                                },
//                                modifier = Modifier
//                                    .fillMaxWidth()
//                                    .height(50.dp),
//                                colors = ButtonDefaults.buttonColors(
//                                    containerColor = Color(0xFF4A90E2)
//                                ),
//                                shape = RoundedCornerShape(10.dp)
//                            ) {
//                                Icon(Icons.Default.Upload, contentDescription = null)
//                                Spacer(Modifier.width(8.dp))
//                                Text("Upload All Resumes", fontWeight = FontWeight.Bold)
//                            }
//                        }
//                    }
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
//            title = { Text("Success! 🎉") },
//            text = { Text("Resume(s) uploaded successfully") },
//            confirmButton = {
//                Button(onClick = {
//                    showSuccessDialog = false
//                    onUploadSuccess()
//                }) {
//                    Text("OK")
//                }
//            }
//        )
//    }
//}
//
//@Composable
//fun UploadFileCard(file: SelectedFile, isDark: Boolean) {
//    Card(
//        modifier = Modifier
//            .fillMaxWidth()
//            .shadow(4.dp, RoundedCornerShape(12.dp)),
//        shape = RoundedCornerShape(12.dp),
//        colors = CardDefaults.cardColors(
//            containerColor = if (isDark) Color(0xFF1E1E2E) else Color.White
//        )
//    ) {
//        Column(modifier = Modifier.padding(16.dp)) {
//            Row(
//                modifier = Modifier.fillMaxWidth(),
//                verticalAlignment = Alignment.CenterVertically,
//                horizontalArrangement = Arrangement.spacedBy(12.dp)
//            ) {
//                Icon(
//                    Icons.Default.FilePresent,
//                    contentDescription = null,
//                    tint = Color(0xFF4A90E2),
//                    modifier = Modifier.size(32.dp)
//                )
//                Column(modifier = Modifier.weight(1f)) {
//                    Text(
//                        file.fileName,
//                        fontSize = 14.sp,
//                        fontWeight = FontWeight.Bold,
//                        color = if (isDark) Color.White else Color.Black,
//                        maxLines = 1
//                    )
//                    Text(
//                        file.candidateName,
//                        fontSize = 12.sp,
//                        color = if (isDark) Color.White.copy(0.6f) else Color.Gray
//                    )
//                }
//            }
//
//            Spacer(modifier = Modifier.height(12.dp))
//
//            if (file.detectedTechStack.isNotEmpty()) {
//                Text(
//                    "Detected Tech Stack:",
//                    fontSize = 12.sp,
//                    fontWeight = FontWeight.SemiBold,
//                    color = if (isDark) Color.White else Color.Black
//                )
//                Spacer(modifier = Modifier.height(6.dp))
//                FlowRow(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(4.dp)) {
//                    file.detectedTechStack.forEach { tech ->
//                        Surface(
//                            shape = RoundedCornerShape(6.dp),
//                            color = Color(0xFF4A90E2).copy(alpha = 0.2f)
//                        ) {
//                            Text(
//                                tech,
//                                modifier = Modifier.padding(6.dp, 3.dp),
//                                fontSize = 11.sp,
//                                color = Color(0xFF4A90E2),
//                                fontWeight = FontWeight.Bold
//                            )
//                        }
//                    }
//                }
//            }
//
//            Spacer(modifier = Modifier.height(8.dp))
//            Text(
//                "Experience: ${file.experience} years",
//                fontSize = 11.sp,
//                color = if (isDark) Color.White.copy(0.5f) else Color.Gray
//            )
//        }
//    }
//}
//
//@Composable
//fun UploadSingleResumeForm(
//    file: SelectedFile,
//    isDark: Boolean,
//    recruiterEmail: String,
//    viewModel: RecruiterResumeViewModel,
//    onUploadSuccess: () -> Unit
//) {
//    var candidateName by remember { mutableStateOf(file.candidateName) }
//    var candidateEmail by remember { mutableStateOf(file.candidateEmail) }
//    var experience by remember { mutableStateOf(file.experience.toString()) }
//
//    Card(
//        modifier = Modifier
//            .fillMaxWidth()
//            .shadow(8.dp, RoundedCornerShape(16.dp)),
//        shape = RoundedCornerShape(16.dp),
//        colors = CardDefaults.cardColors(
//            containerColor = if (isDark) Color(0xFF1E1E2E) else Color.White
//        )
//    ) {
//        Column(
//            modifier = Modifier
//                .fillMaxWidth()
//                .padding(20.dp),
//            verticalArrangement = Arrangement.spacedBy(16.dp)
//        ) {
//            Text(
//                "Complete Resume Details",
//                fontSize = 14.sp,
//                fontWeight = FontWeight.Bold,
//                color = if (isDark) Color.White else Color.Black
//            )
//
//            OutlinedTextField(
//                value = candidateName,
//                onValueChange = { candidateName = it },
//                label = { Text("Candidate Name") },
//                modifier = Modifier.fillMaxWidth(),
//                shape = RoundedCornerShape(12.dp),
//                colors = OutlinedTextFieldDefaults.colors(
//                    focusedBorderColor = Color(0xFF4A90E2),
//                    unfocusedBorderColor = if (isDark) Color.White.copy(0.3f) else Color.Gray.copy(0.3f),
//                    focusedTextColor = if (isDark) Color.White else Color.Black,
//                    unfocusedTextColor = if (isDark) Color.White else Color.Black
//                )
//            )
//
//            OutlinedTextField(
//                value = candidateEmail,
//                onValueChange = { candidateEmail = it },
//                label = { Text("Email Address") },
//                modifier = Modifier.fillMaxWidth(),
//                shape = RoundedCornerShape(12.dp),
//                colors = OutlinedTextFieldDefaults.colors(
//                    focusedBorderColor = Color(0xFF4A90E2),
//                    unfocusedBorderColor = if (isDark) Color.White.copy(0.3f) else Color.Gray.copy(0.3f),
//                    focusedTextColor = if (isDark) Color.White else Color.Black,
//                    unfocusedTextColor = if (isDark) Color.White else Color.Black
//                )
//            )
//
//            OutlinedTextField(
//                value = experience,
//                onValueChange = { experience = it },
//                label = { Text("Experience (Years)") },
//                modifier = Modifier.fillMaxWidth(),
//                shape = RoundedCornerShape(12.dp),
//                colors = OutlinedTextFieldDefaults.colors(
//                    focusedBorderColor = Color(0xFF4A90E2),
//                    unfocusedBorderColor = if (isDark) Color.White.copy(0.3f) else Color.Gray.copy(0.3f),
//                    focusedTextColor = if (isDark) Color.White else Color.Black,
//                    unfocusedTextColor = if (isDark) Color.White else Color.Black
//                )
//            )
//
//            Button(
//                onClick = {
//                    if (candidateName.isBlank() || candidateEmail.isBlank()) {
//                        return@Button
//                    }
//
//                    val resume = RecruiterResumeEntity(
//                        resumeId = UUID.randomUUID().toString(),
//                        fileName = file.fileName,
//                        candidateName = candidateName,
//                        candidateEmail = candidateEmail,
//                        experience = experience.toIntOrNull() ?: 0,
//                        techStack = file.detectedTechStack.joinToString(", "),
//                        rawText = file.extractedText,
//                        uploadedDate = System.currentTimeMillis(),
//                        recruiterEmail = recruiterEmail
//                    )
//
//                    viewModel.insertResume(resume)
//                    onUploadSuccess()
//                },
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .height(54.dp),
//                colors = ButtonDefaults.buttonColors(
//                    containerColor = Color(0xFF4A90E2)
//                ),
//                shape = RoundedCornerShape(12.dp)
//            ) {
//                Icon(Icons.Default.Upload, contentDescription = null)
//                Spacer(Modifier.width(8.dp))
//                Text("Upload Resume", fontWeight = FontWeight.Bold, fontSize = 16.sp)
//            }
//        }
//    }
//}
//
//enum class UploadMode {
//    SINGLE, MULTIPLE
//}
//
//data class SelectedFile(
//    val uri: android.net.Uri,
//    val fileName: String,
//    val candidateName: String,
//    val candidateEmail: String,
//    val experience: Int,
//    val detectedTechStack: List<String>,
//    val extractedText: String
//)
//
//// Helper Functions
//private fun extractCandidateName(fileName: String): String {
//    val name = fileName.split("_").firstOrNull() ?: fileName
//    return name.replace(".pdf", "").replace("-", " ")
//}
//
//private fun extractEmailFromFileName(fileName: String): String {
//    val parts = fileName.split("_", "-")
//    for (part in parts) {
//        if (part.contains("@")) {
//            return part.replace(".pdf", "")
//        }
//    }
//    return "candidate@example.com"
//}
//
//private fun extractExperienceFromFileName(fileName: String): Int {
//    val parts = fileName.split("_", "-", ".")
//    for (part in parts) {
//        val years = part.replace("y", "").replace("Y", "").toIntOrNull()
//        if (years != null && years > 0) return years
//    }
//    return 0
//}

//
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
//import androidx.compose.ui.graphics.Color
//import androidx.compose.ui.platform.LocalContext
//import androidx.compose.ui.text.font.FontWeight
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
//    // ✅ Single File Picker
//    val singleFilePicker = rememberLauncherForActivityResult(
//        contract = ActivityResultContracts.GetContent()
//    ) { uri ->
//        if (uri != null) {
//            val fileName = FileUtils.getFileNameFromUri(context, uri)
//            isProcessing = true
//            uploadProgress = 0f
//
//            scope.launch(Dispatchers.Default) {
//                try {
//                    val pdfText = PDFParser.extractTextFromPDF(context, uri.toString())
//                    val techStack = if (pdfText.isNotEmpty()) {
//                        TechStackExtractor.extractTechStack(pdfText)
//                    } else {
//                        emptyList()
//                    }
//
//                    val candidateName = extractCandidateName(fileName)
//                    val experience = extractExperienceFromFileName(fileName)
//
//                    selectedFiles = listOf(
//                        SelectedResumeFile(
//                            uri = uri,
//                            fileName = fileName,
//                            candidateName = candidateName,
//                            experience = experience,
//                            detectedTechStack = techStack,
//                            extractedText = pdfText
//                        )
//                    )
//                    uploadProgress = 1f
//                } catch (e: Exception) {
//                    e.printStackTrace()
//                }
//                isProcessing = false
//            }
//        }
//    }
//
//    // ✅ Multiple Files Picker
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
//        verticalArrangement = Arrangement.spacedBy(16.dp)
//    ) {
//        item {
//            Text(
//                "📤 Upload Resumes",
//                fontSize = 24.sp,
//                fontWeight = FontWeight.Bold,
//                color = if (isDark) Color.White else Color.Black
//            )
//        }
//
//        // Upload Buttons (Show only if no files selected)
//        if (selectedFiles.isEmpty() && !isProcessing) {
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
//                    Column(modifier = Modifier.padding(20.dp)) {
//                        Text(
//                            "Choose Upload Mode",
//                            fontSize = 16.sp,
//                            fontWeight = FontWeight.Bold,
//                            color = if (isDark) Color.White else Color.Black
//                        )
//                        Spacer(modifier = Modifier.height(16.dp))
//
//                        Button(
//                            onClick = { singleFilePicker.launch("application/pdf") },
//                            modifier = Modifier
//                                .fillMaxWidth()
//                                .height(56.dp),
//                            colors = ButtonDefaults.buttonColors(
//                                containerColor = Color(0xFF4A90E2)
//                            ),
//                            shape = RoundedCornerShape(12.dp)
//                        ) {
//                            Icon(Icons.Default.Upload, contentDescription = null, modifier = Modifier.size(22.dp))
//                            Spacer(Modifier.width(12.dp))
//                            Text("📄 Upload Single Resume", fontWeight = FontWeight.Bold, fontSize = 15.sp)
//                        }
//
//                        Spacer(modifier = Modifier.height(12.dp))
//
//                        Button(
//                            onClick = { multipleFilePicker.launch(arrayOf("application/pdf")) },
//                            modifier = Modifier
//                                .fillMaxWidth()
//                                .height(56.dp),
//                            colors = ButtonDefaults.buttonColors(
//                                containerColor = Color(0xFFFFA726)
//                            ),
//                            shape = RoundedCornerShape(12.dp)
//                        ) {
//                            Icon(Icons.Default.FolderOpen, contentDescription = null, modifier = Modifier.size(22.dp))
//                            Spacer(Modifier.width(12.dp))
//                            Text("📁 Upload Multiple Resumes", fontWeight = FontWeight.Bold, fontSize = 15.sp)
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
//                        .shadow(6.dp, RoundedCornerShape(12.dp)),
//                    shape = RoundedCornerShape(12.dp),
//                    colors = CardDefaults.cardColors(
//                        containerColor = if (isDark) Color(0xFF1E1E2E) else Color.White
//                    )
//                ) {
//                    Column(modifier = Modifier.padding(20.dp)) {
//                        Text(
//                            "Processing: ${(uploadProgress * 100).toInt()}%",
//                            fontSize = 14.sp,
//                            fontWeight = FontWeight.Bold,
//                            color = if (isDark) Color.White else Color.Black
//                        )
//                        Spacer(modifier = Modifier.height(12.dp))
//                        LinearProgressIndicator(
//                            progress = uploadProgress,
//                            modifier = Modifier
//                                .fillMaxWidth()
//                                .height(8.dp),
//                            color = Color(0xFF4A90E2)
//                        )
//                    }
//                }
//            }
//        }
//
//        // Selected Files List
//        if (selectedFiles.isNotEmpty()) {
//            item {
//                Row(
//                    modifier = Modifier
//                        .fillMaxWidth()
//                        .padding(vertical = 8.dp),
//                    verticalAlignment = Alignment.CenterVertically,
//                    horizontalArrangement = Arrangement.SpaceBetween
//                ) {
//                    Text(
//                        "📋 Selected: ${selectedFiles.size} file(s)",
//                        fontSize = 15.sp,
//                        fontWeight = FontWeight.Bold,
//                        color = if (isDark) Color.White else Color.Black
//                    )
//                    Button(
//                        onClick = { selectedFiles = emptyList() },
//                        modifier = Modifier.height(36.dp),
//                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFE0E0E0)),
//                        shape = RoundedCornerShape(8.dp)
//                    ) {
//                        Text("Clear", color = Color.Black, fontSize = 12.sp)
//                    }
//                }
//            }
//
//            items(selectedFiles) { file ->
//                ResumeFileCard(file, isDark)
//            }
//
//            // Upload Button
//            item {
//                Button(
//                    onClick = {
//                        scope.launch {
//                            val resumes = selectedFiles.map { file ->
//                                RecruiterResumeEntity(
//                                    resumeId = UUID.randomUUID().toString(),
//                                    fileName = file.fileName,
//                                    candidateName = file.candidateName,
//                                    candidateEmail = "", // ✅ No email needed
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
//                        .height(54.dp)
//                        .shadow(6.dp, RoundedCornerShape(12.dp)),
//                    colors = ButtonDefaults.buttonColors(
//                        containerColor = Color(0xFF4CAF50)
//                    ),
//                    shape = RoundedCornerShape(12.dp)
//                ) {
//                    Icon(Icons.Default.Check, contentDescription = null, modifier = Modifier.size(22.dp))
//                    Spacer(Modifier.width(12.dp))
//                    Text("✅ Upload All (${selectedFiles.size})", fontWeight = FontWeight.Bold, fontSize = 15.sp)
//                }
//            }
//        }
//
//        item { Spacer(modifier = Modifier.height(16.dp)) }
//    }
//
//    // ✅ Success Dialog
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
//            title = { Text("🎉 Success!", fontSize = 20.sp, fontWeight = FontWeight.Bold) },
//            text = { Text("Resume(s) uploaded successfully!", fontSize = 14.sp) },
//            confirmButton = {
//                Button(
//                    onClick = {
//                        showSuccessDialog = false
//                        viewModel.loadAllResumes()  // ✅ Reload data
//                        // Navigate to filter after success
//                    },
//                    colors = ButtonDefaults.buttonColors(
//                        containerColor = Color(0xFF4CAF50)
//                    ),
//                    shape = RoundedCornerShape(8.dp)
//                ) {
//                    Text("Go to Filter", fontWeight = FontWeight.Bold)
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
//            .shadow(4.dp, RoundedCornerShape(12.dp))
//            .animateContentSize(),
//        shape = RoundedCornerShape(12.dp),
//        colors = CardDefaults.cardColors(
//            containerColor = if (isDark) Color(0xFF1E1E2E) else Color.White
//        )
//    ) {
//        Column(modifier = Modifier.padding(16.dp)) {
//            Row(
//                modifier = Modifier.fillMaxWidth(),
//                verticalAlignment = Alignment.CenterVertically,
//                horizontalArrangement = Arrangement.spacedBy(12.dp)
//            ) {
//                Icon(
//                    Icons.Default.FilePresent,
//                    contentDescription = null,
//                    tint = Color(0xFF4A90E2),
//                    modifier = Modifier.size(32.dp)
//                )
//                Column(modifier = Modifier.weight(1f)) {
//                    Text(
//                        file.fileName,
//                        fontSize = 14.sp,
//                        fontWeight = FontWeight.Bold,
//                        color = if (isDark) Color.White else Color.Black,
//                        maxLines = 1
//                    )
//                    Text(
//                        file.candidateName,
//                        fontSize = 12.sp,
//                        color = if (isDark) Color.White.copy(0.6f) else Color.Gray
//                    )
//                }
//            }
//
//            Spacer(modifier = Modifier.height(12.dp))
//
//            Text(
//                "Experience: ${file.experience} years",
//                fontSize = 12.sp,
//                fontWeight = FontWeight.SemiBold,
//                color = if (isDark) Color.White else Color.Black
//            )
//
//            if (file.detectedTechStack.isNotEmpty()) {
//                Spacer(modifier = Modifier.height(8.dp))
//                FlowRow(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(4.dp)) {
//                    file.detectedTechStack.take(4).forEach { tech ->
//                        Surface(
//                            shape = RoundedCornerShape(6.dp),
//                            color = Color(0xFF4A90E2).copy(alpha = 0.2f)
//                        ) {
//                            Text(
//                                tech,
//                                modifier = Modifier.padding(6.dp, 3.dp),
//                                fontSize = 10.sp,
//                                color = Color(0xFF4A90E2),
//                                fontWeight = FontWeight.Bold
//                            )
//                        }
//                    }
//                    if (file.detectedTechStack.size > 4) {
//                        Surface(
//                            shape = RoundedCornerShape(6.dp),
//                            color = Color(0xFF4A90E2).copy(alpha = 0.2f)
//                        ) {
//                            Text(
//                                "+${file.detectedTechStack.size - 4}",
//                                modifier = Modifier.padding(6.dp, 3.dp),
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
//    val name = fileName.split("_").firstOrNull() ?: fileName
//    return name.replace(".pdf", "").replace("-", " ")
//}
//
//private fun extractExperienceFromFileName(fileName: String): Int {
//    val parts = fileName.split("_", "-", ".")
//    for (part in parts) {
//        val years = part.replace("y", "").replace("Y", "").toIntOrNull()
//        if (years != null && years > 0) return years
//    }
//    return 0
//}


package com.example.feature_recruiter.screens

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.animateContentSize
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
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
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

@Composable
fun ResumeUploadScreen(
    recruiterEmail: String,
    isDark: Boolean,
    viewModel: RecruiterResumeViewModel,
    onUploadSuccess: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    var selectedFiles by remember { mutableStateOf<List<SelectedResumeFile>>(emptyList()) }
    var isProcessing by remember { mutableStateOf(false) }
    var uploadProgress by remember { mutableStateOf(0f) }
    var showSuccessDialog by remember { mutableStateOf(false) }

    // ✅ MULTIPLE FILES ONLY
    val multipleFilePicker = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.OpenMultipleDocuments()
    ) { uris ->
        if (uris.isNotEmpty()) {
            isProcessing = true
            uploadProgress = 0f

            scope.launch(Dispatchers.Default) {
                try {
                    val files = mutableListOf<SelectedResumeFile>()
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

                            val file = SelectedResumeFile(
                                uri = uri,
                                fileName = fileName,
                                candidateName = extractCandidateName(fileName),
                                experience = extractExperienceFromFileName(fileName),
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

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(if (isDark) Color(0xFF121212) else Color(0xFFF5F6FA))
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(20.dp),
        contentPadding = PaddingValues(vertical = 8.dp)
    ) {
        // Empty State - Upload Prompt
        if (selectedFiles.isEmpty() && !isProcessing) {
            item {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .shadow(12.dp, RoundedCornerShape(24.dp)),
                    shape = RoundedCornerShape(24.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = if (isDark) Color(0xFF1E1E2E) else Color.White
                    )
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(
                                Brush.verticalGradient(
                                    listOf(Color(0xFF4A90E2), Color(0xFF357ABD))
                                )
                            )
                            .padding(40.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.spacedBy(24.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(100.dp)
                                    .background(
                                        Color.White.copy(alpha = 0.15f),
                                        RoundedCornerShape(28.dp)
                                    ),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    Icons.Default.CloudUpload,
                                    contentDescription = null,
                                    modifier = Modifier.size(56.dp),
                                    tint = Color.White
                                )
                            }

                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                Text(
                                    "Upload Resumes",
                                    fontSize = 28.sp,
                                    fontWeight = FontWeight.ExtraBold,
                                    color = Color.White,
                                    textAlign = TextAlign.Center
                                )
                                Text(
                                    "Upload multiple PDF resumes at once\nOur system will automatically extract information",
                                    fontSize = 13.sp,
                                    color = Color.White.copy(alpha = 0.9f),
                                    textAlign = TextAlign.Center,
                                    lineHeight = 18.sp
                                )
                            }

                            Button(
                                onClick = { multipleFilePicker.launch(arrayOf("application/pdf")) },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(56.dp)
                                    .shadow(8.dp, RoundedCornerShape(14.dp)),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = Color.White
                                ),
                                shape = RoundedCornerShape(14.dp)
                            ) {
                                Icon(
                                    Icons.Default.FolderOpen,
                                    contentDescription = null,
                                    modifier = Modifier.size(22.dp),
                                    tint = Color(0xFF4A90E2)
                                )
                                Spacer(Modifier.width(12.dp))
                                Text(
                                    "Select PDFs",
                                    fontWeight = FontWeight.ExtraBold,
                                    fontSize = 16.sp,
                                    color = Color(0xFF4A90E2)
                                )
                            }
                        }
                    }
                }
            }
        }

        // Processing Progress
        if (isProcessing) {
            item {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .shadow(8.dp, RoundedCornerShape(16.dp)),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = if (isDark) Color(0xFF1E1E2E) else Color.White
                    )
                ) {
                    Column(modifier = Modifier.padding(24.dp), verticalArrangement = Arrangement.spacedBy(16.dp)) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(32.dp),
                                color = Color(0xFF4A90E2),
                                strokeWidth = 3.dp
                            )
                            Column {
                                Text(
                                    "Processing Files",
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = if (isDark) Color.White else Color.Black
                                )
                                Text(
                                    "${(uploadProgress * 100).toInt()}%",
                                    fontSize = 12.sp,
                                    color = if (isDark) Color.White.copy(0.6f) else Color.Gray
                                )
                            }
                        }
                        LinearProgressIndicator(
                            progress = uploadProgress,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(6.dp)
                                .clip(RoundedCornerShape(3.dp)),
                            color = Color(0xFF4A90E2),
                            trackColor = if (isDark) Color.White.copy(0.1f) else Color.Gray.copy(0.2f)
                        )
                    }
                }
            }
        }

        // Selected Files Header
        if (selectedFiles.isNotEmpty()) {
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        Icon(
                            Icons.Default.CheckCircle,
                            contentDescription = null,
                            tint = Color(0xFF4CAF50),
                            modifier = Modifier.size(28.dp)
                        )
                        Column {
                            Text(
                                "Ready to Upload",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.ExtraBold,
                                color = if (isDark) Color.White else Color.Black
                            )
                            Text(
                                "${selectedFiles.size} file(s) selected",
                                fontSize = 12.sp,
                                color = if (isDark) Color.White.copy(0.6f) else Color.Gray
                            )
                        }
                    }
                    Button(
                        onClick = { selectedFiles = emptyList() },
                        modifier = Modifier.height(36.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFE0E0E0)),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text("Clear", color = Color.Black, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }

        // Resume Files List
        items(selectedFiles) { file ->
            ResumeFileCard(file, isDark)
        }

        // Upload Button
        if (selectedFiles.isNotEmpty()) {
            item {
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
                            showSuccessDialog = true
                            selectedFiles = emptyList()
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp)
                        .shadow(8.dp, RoundedCornerShape(14.dp)),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF4CAF50)
                    ),
                    shape = RoundedCornerShape(14.dp)
                ) {
                    Icon(Icons.Default.CloudUpload, contentDescription = null, modifier = Modifier.size(24.dp))
                    Spacer(Modifier.width(12.dp))
                    Text("Upload All (${selectedFiles.size})", fontWeight = FontWeight.ExtraBold, fontSize = 16.sp)
                }
            }

            item { Spacer(modifier = Modifier.height(16.dp)) }
        }
    }

    // Success Dialog
    if (showSuccessDialog) {
        AlertDialog(
            onDismissRequest = { showSuccessDialog = false },
            icon = {
                Icon(
                    Icons.Default.CheckCircle,
                    contentDescription = null,
                    tint = Color(0xFF4CAF50),
                    modifier = Modifier.size(48.dp)
                )
            },
            title = { Text("✅ Success!", fontSize = 20.sp, fontWeight = FontWeight.ExtraBold) },
            text = { Text("${selectedFiles.size} resume(s) uploaded successfully!", fontSize = 14.sp) },
            confirmButton = {
                Button(
                    onClick = {
                        showSuccessDialog = false
                        viewModel.loadAllResumes()
                        onUploadSuccess()
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4CAF50)),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text("Go to Dashboard", fontWeight = FontWeight.Bold)
                }
            }
        )
    }
}

@Composable
fun ResumeFileCard(file: SelectedResumeFile, isDark: Boolean) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(6.dp, RoundedCornerShape(14.dp))
            .animateContentSize(),
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (isDark) Color(0xFF1E1E2E) else Color.White
        )
    ) {
        Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Icon(
                    Icons.Default.FilePresent,
                    contentDescription = null,
                    tint = Color(0xFF4A90E2),
                    modifier = Modifier.size(36.dp)
                )
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        file.candidateName,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold,
                        color = if (isDark) Color.White else Color.Black
                    )
                    Text(
                        file.fileName,
                        fontSize = 11.sp,
                        color = if (isDark) Color.White.copy(0.6f) else Color.Gray,
                        maxLines = 1
                    )
                }
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = Color(0xFF4A90E2).copy(alpha = 0.15f)
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                        horizontalArrangement = Arrangement.spacedBy(4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            Icons.Default.School,
                            contentDescription = null,
                            tint = Color(0xFF4A90E2),
                            modifier = Modifier.size(14.dp)
                        )
                        Text(
                            "${file.experience} years",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF4A90E2)
                        )
                    }
                }

                if (file.detectedTechStack.isNotEmpty()) {
                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = Color(0xFF9C27B0).copy(alpha = 0.15f)
                    ) {
                        Text(
                            "+${file.detectedTechStack.size} tech",
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF9C27B0)
                        )
                    }
                }
            }

            if (file.detectedTechStack.isNotEmpty()) {
                FlowRow(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    file.detectedTechStack.take(5).forEach { tech ->
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
                    if (file.detectedTechStack.size > 5) {
                        Surface(
                            shape = RoundedCornerShape(6.dp),
                            color = Color(0xFF4A90E2).copy(alpha = 0.15f)
                        ) {
                            Text(
                                "+${file.detectedTechStack.size - 5}",
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

data class SelectedResumeFile(
    val uri: android.net.Uri,
    val fileName: String,
    val candidateName: String,
    val experience: Int,
    val detectedTechStack: List<String>,
    val extractedText: String
)

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