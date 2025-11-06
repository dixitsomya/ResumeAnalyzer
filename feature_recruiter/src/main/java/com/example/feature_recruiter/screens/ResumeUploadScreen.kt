//
//package com.example.feature_recruiter.screens
//
//import androidx.activity.compose.rememberLauncherForActivityResult
//import androidx.activity.result.contract.ActivityResultContracts
//import androidx.compose.foundation.background
//import androidx.compose.foundation.layout.*
//import androidx.compose.foundation.lazy.LazyColumn
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
//    var selectedFileName by remember { mutableStateOf("") }
//    var candidateName by remember { mutableStateOf("") }
//    var candidateEmail by remember { mutableStateOf("") }
//    var experience by remember { mutableStateOf("0") }
//    var extractedTechStack by remember { mutableStateOf<List<String>>(emptyList()) }
//    var errorMessage by remember { mutableStateOf<String?>(null) }
//    var isProcessing by remember { mutableStateOf(false) }
//    var uploadProgress by remember { mutableStateOf(0f) }
//    var showSuccessDialog by remember { mutableStateOf(false) }
//
//    val snackbarHostState = remember { SnackbarHostState() }
//
//    // Single File Picker
//    val singleFilePicker = rememberLauncherForActivityResult(
//        contract = ActivityResultContracts.GetContent()
//    ) { uri ->
//        if (uri != null) {
//            selectedFileName = FileUtils.getFileNameFromUri(context, uri)
//            errorMessage = null
//            isProcessing = true
//            uploadProgress = 0f
//
//            scope.launch(Dispatchers.Default) {
//                try {
//                    // Attempt to extract text from PDF
//                    val pdfText = PDFParser.extractTextFromPDF(context, uri.toString())
//
//                    // Extract tech stack from PDF content
//                    extractedTechStack = if (pdfText.isNotEmpty()) {
//                        TechStackExtractor.extractTechStack(pdfText)
//                    } else {
//                        emptyList()
//                    }
//
//                    // Extract candidate info from filename
//                    candidateName = extractCandidateName(selectedFileName)
//                    experience = extractExperienceFromFileName(selectedFileName).toString()
//
//                    uploadProgress = 1f
//                } catch (e: Exception) {
//                    e.printStackTrace()
//                    errorMessage = "Error processing file: ${e.message}"
//                }
//                isProcessing = false
//            }
//        }
//    }
//
//    // Batch Upload - Multiple Files
//    val batchUploadPicker = rememberLauncherForActivityResult(
//        contract = ActivityResultContracts.OpenMultipleDocuments()
//    ) { uris ->
//        if (uris.isNotEmpty()) {
//            isProcessing = true
//            uploadProgress = 0f
//
//            scope.launch(Dispatchers.Default) {
//                try {
//                    val resumes = mutableListOf<RecruiterResumeEntity>()
//                    val totalFiles = uris.size
//
//                    uris.forEachIndexed { index, uri ->
//                        try {
//                            val fileName = FileUtils.getFileNameFromUri(context, uri)
//
//                            // Extract text from PDF
//                            val pdfText = if (FileUtils.isPDFFile(fileName)) {
//                                PDFParser.extractTextFromPDF(context, uri.toString())
//                            } else {
//                                ""
//                            }
//
//                            // Extract tech stack
//                            val techs = if (pdfText.isNotEmpty()) {
//                                TechStackExtractor.extractTechStack(pdfText)
//                            } else {
//                                emptyList()
//                            }
//
//                            val resume = RecruiterResumeEntity(
//                                resumeId = UUID.randomUUID().toString(),
//                                fileName = fileName,
//                                candidateName = extractCandidateName(fileName),
//                                candidateEmail = extractEmailFromFileName(fileName),
//                                experience = extractExperienceFromFileName(fileName),
//                                techStack = techs.joinToString(", "),
//                                rawText = pdfText,
//                                uploadedDate = System.currentTimeMillis(),
//                                recruiterEmail = recruiterEmail
//                            )
//                            resumes.add(resume)
//
//                            uploadProgress = (index + 1).toFloat() / totalFiles
//                        } catch (e: Exception) {
//                            e.printStackTrace()
//                        }
//                    }
//
//                    if (resumes.isNotEmpty()) {
//                        viewModel.insertMultipleResumes(resumes)
//                        showSuccessDialog = true
//                    } else {
//                        errorMessage = "No valid resumes found"
//                    }
//                } catch (e: Exception) {
//                    e.printStackTrace()
//                    errorMessage = "Upload failed: ${e.message}"
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
//            // Upload Mode Selection
//            item {
//                Card(
//                    modifier = Modifier
//                        .fillMaxWidth()
//                        .shadow(8.dp, RoundedCornerShape(16.dp)),
//                    shape = RoundedCornerShape(16.dp),
//                    colors = CardDefaults.cardColors(
//                        containerColor = if (isDark) Color(0xFF1E1E2F) else Color.White
//                    )
//                ) {
//                    Column(modifier = Modifier.padding(20.dp)) {
//                        Text(
//                            "Upload Mode",
//                            fontSize = 16.sp,
//                            fontWeight = FontWeight.SemiBold,
//                            color = if (isDark) Color.White else Color.Black
//                        )
//                        Spacer(modifier = Modifier.height(12.dp))
//
//                        // Single Upload Button
//                        Button(
//                            onClick = { singleFilePicker.launch("application/pdf") },
//                            enabled = !isProcessing,
//                            modifier = Modifier
//                                .fillMaxWidth()
//                                .height(48.dp),
//                            colors = ButtonDefaults.buttonColors(
//                                containerColor = Color(0xFF4A90E2)
//                            ),
//                            shape = RoundedCornerShape(12.dp)
//                        ) {
//                            Icon(Icons.Default.Upload, contentDescription = null)
//                            Spacer(Modifier.width(8.dp))
//                            Text("Upload Single Resume")
//                        }
//
//                        Spacer(modifier = Modifier.height(12.dp))
//
//                        // Batch Upload Button
//                        Button(
//                            onClick = { batchUploadPicker.launch(arrayOf("application/pdf")) },
//                            enabled = !isProcessing,
//                            modifier = Modifier
//                                .fillMaxWidth()
//                                .height(48.dp),
//                            colors = ButtonDefaults.buttonColors(
//                                containerColor = Color(0xFFFFA726)
//                            ),
//                            shape = RoundedCornerShape(12.dp)
//                        ) {
//                            Icon(Icons.Default.FolderOpen, contentDescription = null)
//                            Spacer(Modifier.width(8.dp))
//                            Text("Upload Multiple Resumes")
//                        }
//                    }
//                }
//            }
//
//            // Selected File Info
//            if (selectedFileName.isNotEmpty()) {
//                item {
//                    Card(
//                        modifier = Modifier
//                            .fillMaxWidth()
//                            .shadow(6.dp, RoundedCornerShape(12.dp)),
//                        shape = RoundedCornerShape(12.dp),
//                        colors = CardDefaults.cardColors(
//                            containerColor = if (isDark) Color(0xFF1E1E2F) else Color.White
//                        )
//                    ) {
//                        Row(
//                            modifier = Modifier
//                                .fillMaxWidth()
//                                .padding(16.dp),
//                            verticalAlignment = Alignment.CenterVertically,
//                            horizontalArrangement = Arrangement.SpaceBetween
//                        ) {
//                            Row(
//                                modifier = Modifier.weight(1f),
//                                verticalAlignment = Alignment.CenterVertically,
//                                horizontalArrangement = Arrangement.spacedBy(12.dp)
//                            ) {
//                                Icon(
//                                    Icons.Default.FilePresent,
//                                    contentDescription = null,
//                                    tint = Color(0xFF4A90E2),
//                                    modifier = Modifier.size(32.dp)
//                                )
//                                Column {
//                                    Text(
//                                        selectedFileName,
//                                        fontSize = 14.sp,
//                                        fontWeight = FontWeight.Bold,
//                                        color = if (isDark) Color.White else Color.Black,
//                                        maxLines = 1
//                                    )
//                                    Text(
//                                        "PDF Document",
//                                        fontSize = 12.sp,
//                                        color = Color.Gray
//                                    )
//                                }
//                            }
//                            IconButton(onClick = {
//                                selectedFileName = ""
//                                extractedTechStack = emptyList()
//                                candidateName = ""
//                                candidateEmail = ""
//                                experience = "0"
//                            }) {
//                                Icon(Icons.Default.Close, contentDescription = "Remove")
//                            }
//                        }
//                    }
//                }
//            }
//
//            // Processing State
//            if (isProcessing) {
//                item {
//                    Card(
//                        modifier = Modifier.fillMaxWidth(),
//                        shape = RoundedCornerShape(12.dp)
//                    ) {
//                        Column(modifier = Modifier.padding(16.dp)) {
//                            Text(
//                                "Processing: ${(uploadProgress * 100).toInt()}%",
//                                fontSize = 14.sp,
//                                fontWeight = FontWeight.Bold
//                            )
//                            Spacer(modifier = Modifier.height(8.dp))
//                            LinearProgressIndicator(
//                                progress = uploadProgress,
//                                modifier = Modifier.fillMaxWidth()
//                            )
//                        }
//                    }
//                }
//            }
//
//            // Extracted Tech Stack
//            if (extractedTechStack.isNotEmpty()) {
//                item {
//                    Card(
//                        modifier = Modifier.fillMaxWidth(),
//                        shape = RoundedCornerShape(12.dp),
//                        colors = CardDefaults.cardColors(
//                            containerColor = if (isDark) Color(0xFF1E1E2F) else Color.White
//                        )
//                    ) {
//                        Column(modifier = Modifier.padding(16.dp)) {
//                            Text(
//                                "Detected Technologies",
//                                fontSize = 14.sp,
//                                fontWeight = FontWeight.Bold,
//                                color = if (isDark) Color.White else Color.Black
//                            )
//                            Spacer(modifier = Modifier.height(8.dp))
//                            FlowRow(
//                                modifier = Modifier.fillMaxWidth(),
//                                horizontalArrangement = Arrangement.spacedBy(8.dp)
//                            ) {
//                                extractedTechStack.forEach { tech ->
//                                    Surface(
//                                        shape = RoundedCornerShape(8.dp),
//                                        color = Color(0xFF4A90E2).copy(alpha = 0.2f)
//                                    ) {
//                                        Text(
//                                            tech,
//                                            modifier = Modifier.padding(8.dp, 4.dp),
//                                            fontSize = 12.sp,
//                                            color = Color(0xFF4A90E2)
//                                        )
//                                    }
//                                }
//                            }
//                        }
//                    }
//                }
//            }
//
//            // Manual Info Entry
//            if (selectedFileName.isNotEmpty() && !isProcessing) {
//                item {
//                    Card(
//                        modifier = Modifier
//                            .fillMaxWidth()
//                            .shadow(8.dp, RoundedCornerShape(16.dp)),
//                        shape = RoundedCornerShape(16.dp),
//                        colors = CardDefaults.cardColors(
//                            containerColor = if (isDark) Color(0xFF1E1E2F) else Color.White
//                        )
//                    ) {
//                        Column(
//                            modifier = Modifier
//                                .fillMaxWidth()
//                                .padding(20.dp),
//                            verticalArrangement = Arrangement.spacedBy(16.dp)
//                        ) {
//                            OutlinedTextField(
//                                value = candidateName,
//                                onValueChange = { candidateName = it },
//                                label = { Text("Candidate Name") },
//                                modifier = Modifier.fillMaxWidth(),
//                                shape = RoundedCornerShape(12.dp),
//                                colors = OutlinedTextFieldDefaults.colors(
//                                    focusedBorderColor = Color(0xFF4A90E2),
//                                    unfocusedBorderColor = if (isDark) Color.White.copy(0.3f) else Color.Gray.copy(0.3f),
//                                    focusedTextColor = if (isDark) Color.White else Color.Black,
//                                    unfocusedTextColor = if (isDark) Color.White else Color.Black
//                                )
//                            )
//
//                            OutlinedTextField(
//                                value = candidateEmail,
//                                onValueChange = { candidateEmail = it },
//                                label = { Text("Email Address") },
//                                modifier = Modifier.fillMaxWidth(),
//                                shape = RoundedCornerShape(12.dp),
//                                colors = OutlinedTextFieldDefaults.colors(
//                                    focusedBorderColor = Color(0xFF4A90E2),
//                                    unfocusedBorderColor = if (isDark) Color.White.copy(0.3f) else Color.Gray.copy(0.3f),
//                                    focusedTextColor = if (isDark) Color.White else Color.Black,
//                                    unfocusedTextColor = if (isDark) Color.White else Color.Black
//                                )
//                            )
//
//                            OutlinedTextField(
//                                value = experience,
//                                onValueChange = { experience = it },
//                                label = { Text("Experience (Years)") },
//                                modifier = Modifier.fillMaxWidth(),
//                                shape = RoundedCornerShape(12.dp),
//                                colors = OutlinedTextFieldDefaults.colors(
//                                    focusedBorderColor = Color(0xFF4A90E2),
//                                    unfocusedBorderColor = if (isDark) Color.White.copy(0.3f) else Color.Gray.copy(0.3f),
//                                    focusedTextColor = if (isDark) Color.White else Color.Black,
//                                    unfocusedTextColor = if (isDark) Color.White else Color.Black
//                                )
//                            )
//
//                            // Upload Button
//                            Button(
//                                onClick = {
//                                    if (candidateName.isBlank() || candidateEmail.isBlank()) {
//                                        errorMessage = "Please fill all fields"
//                                        return@Button
//                                    }
//
//                                    val resume = RecruiterResumeEntity(
//                                        resumeId = UUID.randomUUID().toString(),
//                                        fileName = selectedFileName,
//                                        candidateName = candidateName,
//                                        candidateEmail = candidateEmail,
//                                        experience = experience.toIntOrNull() ?: 0,
//                                        techStack = extractedTechStack.joinToString(", "),
//                                        rawText = "",
//                                        uploadedDate = System.currentTimeMillis(),
//                                        recruiterEmail = recruiterEmail
//                                    )
//
//                                    viewModel.insertResume(resume)
//                                    showSuccessDialog = true
//                                },
//                                modifier = Modifier
//                                    .fillMaxWidth()
//                                    .height(54.dp),
//                                colors = ButtonDefaults.buttonColors(
//                                    containerColor = Color(0xFF4A90E2)
//                                ),
//                                shape = RoundedCornerShape(12.dp)
//                            ) {
//                                Icon(Icons.Default.Upload, contentDescription = null)
//                                Spacer(Modifier.width(8.dp))
//                                Text("Upload Resume", fontWeight = FontWeight.Bold)
//                            }
//                        }
//                    }
//                }
//            }
//
//            // Error Message
//            if (errorMessage != null) {
//                item {
//                    Surface(
//                        modifier = Modifier.fillMaxWidth(),
//                        color = Color(0xFFFFEBEE),
//                        shape = RoundedCornerShape(8.dp)
//                    ) {
//                        Row(
//                            modifier = Modifier
//                                .fillMaxWidth()
//                                .padding(12.dp),
//                            verticalAlignment = Alignment.CenterVertically
//                        ) {
//                            Icon(
//                                Icons.Default.Error,
//                                contentDescription = null,
//                                tint = Color(0xFFC62828),
//                                modifier = Modifier.size(20.dp)
//                            )
//                            Spacer(Modifier.width(8.dp))
//                            Text(
//                                errorMessage ?: "",
//                                color = Color(0xFFC62828),
//                                fontSize = 12.sp
//                            )
//                        }
//                    }
//                }
//            }
//        }
//    }
//
//    // Success Dialog
//    if (showSuccessDialog) {
//        AlertDialog(
//            onDismissRequest = { showSuccessDialog = false },
//            title = { Text("Success!") },
//            text = { Text("Resume uploaded successfully") },
//            confirmButton = {
//                Button(onClick = {
//                    showSuccessDialog = false
//                    selectedFileName = ""
//                    candidateName = ""
//                    candidateEmail = ""
//                    experience = "0"
//                    extractedTechStack = emptyList()
//                    onUploadSuccess()
//                }) {
//                    Text("OK")
//                }
//            }
//        )
//    }
//}
//
//// Helper functions
//private fun extractCandidateName(fileName: String): String {
//    val name = fileName.split("_").firstOrNull() ?: fileName
//    return name.replace(".pdf", "").replace("-", " ")
//}
//
//private fun extractEmailFromFileName(fileName: String): String {
//    // Try to extract email from filename like "john_doe_john@email.com.pdf"
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
//    // Try to extract years like "5y" from filename
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
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

@OptIn(ExperimentalMaterial3Api::class)
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

    var uploadMode by remember { mutableStateOf<UploadMode?>(null) }
    var selectedFiles by remember { mutableStateOf<List<SelectedFile>>(emptyList()) }
    var showSuccessDialog by remember { mutableStateOf(false) }
    var isProcessing by remember { mutableStateOf(false) }
    var uploadProgress by remember { mutableStateOf(0f) }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    val snackbarHostState = remember { SnackbarHostState() }

    // Single File Picker
    val singleFilePicker = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri ->
        if (uri != null) {
            val fileName = FileUtils.getFileNameFromUri(context, uri)
            isProcessing = true
            uploadProgress = 0f
            errorMessage = null

            scope.launch(Dispatchers.Default) {
                try {
                    val pdfText = PDFParser.extractTextFromPDF(context, uri.toString())
                    val techStack = if (pdfText.isNotEmpty()) {
                        TechStackExtractor.extractTechStack(pdfText)
                    } else {
                        emptyList()
                    }

                    val candidateName = extractCandidateName(fileName)
                    val experience = extractExperienceFromFileName(fileName)

                    selectedFiles = listOf(
                        SelectedFile(
                            uri = uri,
                            fileName = fileName,
                            candidateName = candidateName,
                            candidateEmail = extractEmailFromFileName(fileName),
                            experience = experience,
                            detectedTechStack = techStack,
                            extractedText = pdfText
                        )
                    )
                    uploadProgress = 1f
                } catch (e: Exception) {
                    e.printStackTrace()
                    errorMessage = "Error processing file: ${e.message}"
                    selectedFiles = emptyList()
                }
                isProcessing = false
            }
        }
    }

    // Multiple Files Picker
    val multipleFilePicker = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.OpenMultipleDocuments()
    ) { uris ->
        if (uris.isNotEmpty()) {
            isProcessing = true
            uploadProgress = 0f
            errorMessage = null

            scope.launch(Dispatchers.Default) {
                try {
                    val files = mutableListOf<SelectedFile>()
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

                            val file = SelectedFile(
                                uri = uri,
                                fileName = fileName,
                                candidateName = extractCandidateName(fileName),
                                candidateEmail = extractEmailFromFileName(fileName),
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
                    errorMessage = "Error processing files: ${e.message}"
                }
                isProcessing = false
            }
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { innerPadding ->
        LazyColumn(
            modifier = modifier
                .fillMaxSize()
                .background(if (isDark) Color(0xFF121212) else Color(0xFFF5F6FA))
                .padding(innerPadding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                Text(
                    "Upload Resumes",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = if (isDark) Color.White else Color.Black
                )
                Text(
                    "Upload single or multiple resumes",
                    fontSize = 14.sp,
                    color = if (isDark) Color.White.copy(0.7f) else Color.Gray
                )
            }

            // Upload Mode Selection (Show only if no files selected)
            if (selectedFiles.isEmpty() && uploadMode == null) {
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
                        Column(modifier = Modifier.padding(20.dp)) {
                            Text(
                                "Upload Mode",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = if (isDark) Color.White else Color.Black
                            )
                            Spacer(modifier = Modifier.height(16.dp))

                            Button(
                                onClick = {
                                    uploadMode = UploadMode.SINGLE
                                    singleFilePicker.launch("application/pdf")
                                },
                                enabled = !isProcessing,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(56.dp),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = Color(0xFF4A90E2)
                                ),
                                shape = RoundedCornerShape(12.dp)
                            ) {
                                Icon(Icons.Default.Upload, contentDescription = null)
                                Spacer(Modifier.width(12.dp))
                                Text("📄 Upload Single Resume", fontWeight = FontWeight.Bold)
                            }

                            Spacer(modifier = Modifier.height(12.dp))

                            Button(
                                onClick = {
                                    uploadMode = UploadMode.MULTIPLE
                                    multipleFilePicker.launch(arrayOf("application/pdf"))
                                },
                                enabled = !isProcessing,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(56.dp),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = Color(0xFFFFA726)
                                ),
                                shape = RoundedCornerShape(12.dp)
                            ) {
                                Icon(Icons.Default.FolderOpen, contentDescription = null)
                                Spacer(Modifier.width(12.dp))
                                Text("📁 Upload Multiple Resumes", fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                }
            }

            // Processing Indicator
            if (isProcessing) {
                item {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = if (isDark) Color(0xFF1E1E2E) else Color.White
                        )
                    ) {
                        Column(modifier = Modifier.padding(20.dp)) {
                            Text(
                                "Processing: ${(uploadProgress * 100).toInt()}%",
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold,
                                color = if (isDark) Color.White else Color.Black
                            )
                            Spacer(modifier = Modifier.height(12.dp))
                            LinearProgressIndicator(
                                progress = uploadProgress,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(8.dp),
                                color = Color(0xFF4A90E2),
                                trackColor = Color.Gray.copy(alpha = 0.2f)
                            )
                        }
                    }
                }
            }

            // Error Message
            if (errorMessage != null) {
                item {
                    Surface(
                        modifier = Modifier
                            .fillMaxWidth()
                            .shadow(4.dp, RoundedCornerShape(8.dp)),
                        color = Color(0xFFFFEBEE),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(12.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Icon(
                                Icons.Default.Error,
                                contentDescription = null,
                                tint = Color(0xFFC62828),
                                modifier = Modifier.size(20.dp)
                            )
                            Text(
                                errorMessage ?: "",
                                color = Color(0xFFC62828),
                                fontSize = 12.sp
                            )
                            Spacer(modifier = Modifier.weight(1f))
                            IconButton(onClick = { errorMessage = null }, modifier = Modifier.size(24.dp)) {
                                Icon(Icons.Default.Close, contentDescription = null)
                            }
                        }
                    }
                }
            }

            // Selected Files List
            if (selectedFiles.isNotEmpty()) {
                item {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            "Selected Files: ${selectedFiles.size}",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            color = if (isDark) Color.White else Color.Black
                        )
                        Button(
                            onClick = {
                                selectedFiles = emptyList()
                                uploadMode = null
                                errorMessage = null
                            },
                            modifier = Modifier.height(36.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFE0E0E0)),
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Text("Clear", color = Color.Black, fontSize = 12.sp)
                        }
                    }
                }

                items(selectedFiles) { file ->
                    UploadFileCard(file, isDark)
                }
            }

            // Upload Button (for single resume with details form)
            if (selectedFiles.size == 1 && uploadMode == UploadMode.SINGLE) {
                item {
                    UploadSingleResumeForm(
                        file = selectedFiles[0],
                        isDark = isDark,
                        recruiterEmail = recruiterEmail,
                        viewModel = viewModel,
                        onUploadSuccess = {
                            showSuccessDialog = true
                            selectedFiles = emptyList()
                            uploadMode = null
                        }
                    )
                }
            }

            // Upload Button (for multiple resumes)
            if (selectedFiles.size > 1) {
                item {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .shadow(6.dp, RoundedCornerShape(12.dp)),
                        shape = RoundedCornerShape(12.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = if (isDark) Color(0xFF1E1E2E) else Color.White
                        )
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text(
                                "Ready to upload ${selectedFiles.size} resumes?",
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold,
                                color = if (isDark) Color.White else Color.Black
                            )
                            Spacer(modifier = Modifier.height(12.dp))
                            Button(
                                onClick = {
                                    scope.launch {
                                        val resumes = selectedFiles.map { file ->
                                            RecruiterResumeEntity(
                                                resumeId = UUID.randomUUID().toString(),
                                                fileName = file.fileName,
                                                candidateName = file.candidateName,
                                                candidateEmail = file.candidateEmail,
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
                                        uploadMode = null
                                    }
                                },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(50.dp),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = Color(0xFF4A90E2)
                                ),
                                shape = RoundedCornerShape(10.dp)
                            ) {
                                Icon(Icons.Default.Upload, contentDescription = null)
                                Spacer(Modifier.width(8.dp))
                                Text("Upload All Resumes", fontWeight = FontWeight.Bold)
                            }
                        }
                    }
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
            title = { Text("Success! 🎉") },
            text = { Text("Resume(s) uploaded successfully") },
            confirmButton = {
                Button(onClick = {
                    showSuccessDialog = false
                    onUploadSuccess()
                }) {
                    Text("OK")
                }
            }
        )
    }
}

@Composable
fun UploadFileCard(file: SelectedFile, isDark: Boolean) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(4.dp, RoundedCornerShape(12.dp)),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (isDark) Color(0xFF1E1E2E) else Color.White
        )
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Icon(
                    Icons.Default.FilePresent,
                    contentDescription = null,
                    tint = Color(0xFF4A90E2),
                    modifier = Modifier.size(32.dp)
                )
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        file.fileName,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = if (isDark) Color.White else Color.Black,
                        maxLines = 1
                    )
                    Text(
                        file.candidateName,
                        fontSize = 12.sp,
                        color = if (isDark) Color.White.copy(0.6f) else Color.Gray
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            if (file.detectedTechStack.isNotEmpty()) {
                Text(
                    "Detected Tech Stack:",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = if (isDark) Color.White else Color.Black
                )
                Spacer(modifier = Modifier.height(6.dp))
                FlowRow(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                    file.detectedTechStack.forEach { tech ->
                        Surface(
                            shape = RoundedCornerShape(6.dp),
                            color = Color(0xFF4A90E2).copy(alpha = 0.2f)
                        ) {
                            Text(
                                tech,
                                modifier = Modifier.padding(6.dp, 3.dp),
                                fontSize = 11.sp,
                                color = Color(0xFF4A90E2),
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))
            Text(
                "Experience: ${file.experience} years",
                fontSize = 11.sp,
                color = if (isDark) Color.White.copy(0.5f) else Color.Gray
            )
        }
    }
}

@Composable
fun UploadSingleResumeForm(
    file: SelectedFile,
    isDark: Boolean,
    recruiterEmail: String,
    viewModel: RecruiterResumeViewModel,
    onUploadSuccess: () -> Unit
) {
    var candidateName by remember { mutableStateOf(file.candidateName) }
    var candidateEmail by remember { mutableStateOf(file.candidateEmail) }
    var experience by remember { mutableStateOf(file.experience.toString()) }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(8.dp, RoundedCornerShape(16.dp)),
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
            Text(
                "Complete Resume Details",
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                color = if (isDark) Color.White else Color.Black
            )

            OutlinedTextField(
                value = candidateName,
                onValueChange = { candidateName = it },
                label = { Text("Candidate Name") },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Color(0xFF4A90E2),
                    unfocusedBorderColor = if (isDark) Color.White.copy(0.3f) else Color.Gray.copy(0.3f),
                    focusedTextColor = if (isDark) Color.White else Color.Black,
                    unfocusedTextColor = if (isDark) Color.White else Color.Black
                )
            )

            OutlinedTextField(
                value = candidateEmail,
                onValueChange = { candidateEmail = it },
                label = { Text("Email Address") },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Color(0xFF4A90E2),
                    unfocusedBorderColor = if (isDark) Color.White.copy(0.3f) else Color.Gray.copy(0.3f),
                    focusedTextColor = if (isDark) Color.White else Color.Black,
                    unfocusedTextColor = if (isDark) Color.White else Color.Black
                )
            )

            OutlinedTextField(
                value = experience,
                onValueChange = { experience = it },
                label = { Text("Experience (Years)") },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Color(0xFF4A90E2),
                    unfocusedBorderColor = if (isDark) Color.White.copy(0.3f) else Color.Gray.copy(0.3f),
                    focusedTextColor = if (isDark) Color.White else Color.Black,
                    unfocusedTextColor = if (isDark) Color.White else Color.Black
                )
            )

            Button(
                onClick = {
                    if (candidateName.isBlank() || candidateEmail.isBlank()) {
                        return@Button
                    }

                    val resume = RecruiterResumeEntity(
                        resumeId = UUID.randomUUID().toString(),
                        fileName = file.fileName,
                        candidateName = candidateName,
                        candidateEmail = candidateEmail,
                        experience = experience.toIntOrNull() ?: 0,
                        techStack = file.detectedTechStack.joinToString(", "),
                        rawText = file.extractedText,
                        uploadedDate = System.currentTimeMillis(),
                        recruiterEmail = recruiterEmail
                    )

                    viewModel.insertResume(resume)
                    onUploadSuccess()
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(54.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF4A90E2)
                ),
                shape = RoundedCornerShape(12.dp)
            ) {
                Icon(Icons.Default.Upload, contentDescription = null)
                Spacer(Modifier.width(8.dp))
                Text("Upload Resume", fontWeight = FontWeight.Bold, fontSize = 16.sp)
            }
        }
    }
}

enum class UploadMode {
    SINGLE, MULTIPLE
}

data class SelectedFile(
    val uri: android.net.Uri,
    val fileName: String,
    val candidateName: String,
    val candidateEmail: String,
    val experience: Int,
    val detectedTechStack: List<String>,
    val extractedText: String
)

// Helper Functions
private fun extractCandidateName(fileName: String): String {
    val name = fileName.split("_").firstOrNull() ?: fileName
    return name.replace(".pdf", "").replace("-", " ")
}

private fun extractEmailFromFileName(fileName: String): String {
    val parts = fileName.split("_", "-")
    for (part in parts) {
        if (part.contains("@")) {
            return part.replace(".pdf", "")
        }
    }
    return "candidate@example.com"
}

private fun extractExperienceFromFileName(fileName: String): Int {
    val parts = fileName.split("_", "-", ".")
    for (part in parts) {
        val years = part.replace("y", "").replace("Y", "").toIntOrNull()
        if (years != null && years > 0) return years
    }
    return 0
}