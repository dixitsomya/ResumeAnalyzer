//package com.example.feature_recruiter.screens
//
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
//import androidx.compose.ui.text.font.FontWeight
//import androidx.compose.ui.unit.dp
//import androidx.compose.ui.unit.sp
//import com.example.feature_recruiter.data.RecruiterResumeDatabase
//import com.example.feature_recruiter.model.RecruiterResume
//import java.util.*
//
//@OptIn(ExperimentalMaterial3Api::class)
//@Composable
//fun ResumeUploadScreenRecruiter(
//    modifier: Modifier = Modifier,
//    isDark: Boolean,
//    onUploadSuccess: () -> Unit = {}
//) {
//    var candidateName by remember { mutableStateOf("") }
//    var candidateEmail by remember { mutableStateOf("") }
//    var experience by remember { mutableStateOf("0") }
//    var techStackInput by remember { mutableStateOf("") }
//    var fileName by remember { mutableStateOf("") }
//    var showSuccessDialog by remember { mutableStateOf(false) }
//    var errorMessage by remember { mutableStateOf<String?>(null) }
//
//    val snackbarHostState = remember { SnackbarHostState() }
//
//    fun validateInput(): Boolean {
//        return when {
//            candidateName.isBlank() -> {
//                errorMessage = "Candidate name required"
//                false
//            }
//            candidateEmail.isBlank() -> {
//                errorMessage = "Email required"
//                false
//            }
//            experience.toIntOrNull() == null -> {
//                errorMessage = "Valid experience required"
//                false
//            }
//            techStackInput.isBlank() -> {
//                errorMessage = "Tech stack required"
//                false
//            }
//            fileName.isBlank() -> {
//                errorMessage = "File name required"
//                false
//            }
//            else -> true
//        }
//    }
//
//    Scaffold(
//        snackbarHost = { SnackbarHost(snackbarHostState) }
//    ) { innerPadding ->
//        LazyColumn(
//            modifier = modifier
//                .fillMaxSize()
//                .background(
//                    if (isDark) Color(0xFF121212) else Color(0xFFF5F6FA)
//                )
//                .padding(innerPadding)
//                .padding(16.dp),
//            verticalArrangement = Arrangement.spacedBy(16.dp)
//        ) {
//            item {
//                Text(
//                    "Add Candidate Resume",
//                    fontSize = 24.sp,
//                    fontWeight = FontWeight.Bold,
//                    color = if (isDark) Color.White else Color.Black
//                )
//            }
//
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
//                    Column(
//                        modifier = Modifier
//                            .fillMaxWidth()
//                            .padding(20.dp),
//                        verticalArrangement = Arrangement.spacedBy(16.dp)
//                    ) {
//                        // Candidate Name
//                        OutlinedTextField(
//                            value = candidateName,
//                            onValueChange = { candidateName = it },
//                            label = { Text("Candidate Name") },
//                            modifier = Modifier.fillMaxWidth(),
//                            shape = RoundedCornerShape(12.dp),
//                            colors = OutlinedTextFieldDefaults.colors(
//                                focusedBorderColor = Color(0xFF4A90E2),
//                                unfocusedBorderColor = if (isDark) Color.White.copy(0.3f) else Color.Gray.copy(0.3f),
//                                focusedTextColor = if (isDark) Color.White else Color.Black,
//                                unfocusedTextColor = if (isDark) Color.White else Color.Black
//                            ),
//                            leadingIcon = {
//                                Icon(Icons.Default.Person, contentDescription = null)
//                            }
//                        )
//
//                        // Candidate Email
//                        OutlinedTextField(
//                            value = candidateEmail,
//                            onValueChange = { candidateEmail = it },
//                            label = { Text("Email Address") },
//                            modifier = Modifier.fillMaxWidth(),
//                            shape = RoundedCornerShape(12.dp),
//                            colors = OutlinedTextFieldDefaults.colors(
//                                focusedBorderColor = Color(0xFF4A90E2),
//                                unfocusedBorderColor = if (isDark) Color.White.copy(0.3f) else Color.Gray.copy(0.3f),
//                                focusedTextColor = if (isDark) Color.White else Color.Black,
//                                unfocusedTextColor = if (isDark) Color.White else Color.Black
//                            ),
//                            leadingIcon = {
//                                Icon(Icons.Default.Email, contentDescription = null)
//                            }
//                        )
//
//                        // Experience
//                        OutlinedTextField(
//                            value = experience,
//                            onValueChange = { experience = it },
//                            label = { Text("Experience (Years)") },
//                            modifier = Modifier.fillMaxWidth(),
//                            shape = RoundedCornerShape(12.dp),
//                            colors = OutlinedTextFieldDefaults.colors(
//                                focusedBorderColor = Color(0xFF4A90E2),
//                                unfocusedBorderColor = if (isDark) Color.White.copy(0.3f) else Color.Gray.copy(0.3f),
//                                focusedTextColor = if (isDark) Color.White else Color.Black,
//                                unfocusedTextColor = if (isDark) Color.White else Color.Black
//                            ),
//                            leadingIcon = {
//                                Icon(Icons.Default.WorkHistory, contentDescription = null)
//                            }
//                        )
//
//                        // Tech Stack (comma separated)
//                        OutlinedTextField(
//                            value = techStackInput,
//                            onValueChange = { techStackInput = it },
//                            label = { Text("Tech Stack (comma separated)") },
//                            modifier = Modifier
//                                .fillMaxWidth()
//                                .heightIn(min = 80.dp),
//                            shape = RoundedCornerShape(12.dp),
//                            colors = OutlinedTextFieldDefaults.colors(
//                                focusedBorderColor = Color(0xFF4A90E2),
//                                unfocusedBorderColor = if (isDark) Color.White.copy(0.3f) else Color.Gray.copy(0.3f),
//                                focusedTextColor = if (isDark) Color.White else Color.Black,
//                                unfocusedTextColor = if (isDark) Color.White else Color.Black
//                            ),
//                            placeholder = { Text("e.g., Kotlin, Java, Compose, Firebase") }
//                        )
//
//                        // File Name
//                        OutlinedTextField(
//                            value = fileName,
//                            onValueChange = { fileName = it },
//                            label = { Text("Resume File Name") },
//                            modifier = Modifier.fillMaxWidth(),
//                            shape = RoundedCornerShape(12.dp),
//                            colors = OutlinedTextFieldDefaults.colors(
//                                focusedBorderColor = Color(0xFF4A90E2),
//                                unfocusedBorderColor = if (isDark) Color.White.copy(0.3f) else Color.Gray.copy(0.3f),
//                                focusedTextColor = if (isDark) Color.White else Color.Black,
//                                unfocusedTextColor = if (isDark) Color.White else Color.Black
//                            ),
//                            leadingIcon = {
//                                Icon(Icons.Default.FilePresent, contentDescription = null)
//                            }
//                        )
//
//                        // Error message display
//                        if (errorMessage != null) {
//                            Surface(
//                                modifier = Modifier.fillMaxWidth(),
//                                color = Color(0xFFFFEBEE),
//                                shape = RoundedCornerShape(8.dp)
//                            ) {
//                                Row(
//                                    modifier = Modifier
//                                        .fillMaxWidth()
//                                        .padding(12.dp),
//                                    verticalAlignment = Alignment.CenterVertically
//                                ) {
//                                    Icon(
//                                        Icons.Default.Error,
//                                        contentDescription = null,
//                                        tint = Color(0xFFC62828),
//                                        modifier = Modifier.size(20.dp)
//                                    )
//                                    Spacer(Modifier.width(8.dp))
//                                    Text(
//                                        errorMessage ?: "",
//                                        color = Color(0xFFC62828),
//                                        fontSize = 12.sp
//                                    )
//                                }
//                            }
//                        }
//
//                        // Submit Button
//                        Button(
//                            onClick = {
//                                if (validateInput()) {
//                                    val resume = RecruiterResume(
//                                        id = UUID.randomUUID().toString(),
//                                        fileName = fileName,
//                                        uploadedDate = System.currentTimeMillis(),
//                                        techStack = techStackInput.split(",").map { it.trim() },
//                                        candidateName = candidateName,
//                                        candidateEmail = candidateEmail,
//                                        experience = experience.toInt()
//                                    )
//                                    RecruiterResumeDatabase.addResume(resume)
//                                    showSuccessDialog = true
//                                    errorMessage = null
//
//                                    // Clear fields
//                                    candidateName = ""
//                                    candidateEmail = ""
//                                    experience = "0"
//                                    techStackInput = ""
//                                    fileName = ""
//                                }
//                            },
//                            modifier = Modifier
//                                .fillMaxWidth()
//                                .height(54.dp)
//                                .shadow(8.dp, RoundedCornerShape(12.dp)),
//                            colors = ButtonDefaults.buttonColors(
//                                containerColor = Color(0xFF4A90E2)
//                            ),
//                            shape = RoundedCornerShape(12.dp)
//                        ) {
//                            Icon(Icons.Default.Upload, contentDescription = null)
//                            Spacer(Modifier.width(8.dp))
//                            Text("Upload Resume", fontWeight = FontWeight.Bold)
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
//                    onUploadSuccess()
//                }) {
//                    Text("OK")
//                }
//            }
//        )
//    }
//}

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
//import kotlinx.coroutines.withContext
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
//    var selectedFileName by remember { mutableStateOf("") }
//    var candidateName by remember { mutableStateOf("") }
//    var candidateEmail by remember { mutableStateOf("") }
//    var experience by remember { mutableStateOf("0") }
//    var errorMessage by remember { mutableStateOf<String?>(null) }
//    var uploadProgress by remember { mutableStateOf(0f) }
//    var isUploading by remember { mutableStateOf(false) }
//    var showSuccessDialog by remember { mutableStateOf(false) }
//
//    val snackbarHostState = remember { SnackbarHostState() }
//
//    // File Picker - Single file
//    val singleFilePicker = rememberLauncherForActivityResult(
//        contract = ActivityResultContracts.GetContent()
//    ) { uri ->
//        if (uri != null) {
//            selectedFileName = FileUtils.getFileNameFromUri(context, uri)
//            errorMessage = null
//        }
//    }
//
//    // Folder Picker - Multiple files
//    val folderPicker = rememberLauncherForActivityResult(
//        contract = ActivityResultContracts.OpenMultipleDocuments()
//    ) { uris ->
//        if (uris.isNotEmpty()) {
//            isUploading = true
//            uploadProgress = 0f
//
//            val resumes = mutableListOf<RecruiterResumeEntity>()
//            val totalFiles = uris.size
//
//            uris.forEachIndexed { index, uri ->
//                try {
//                    val fileName = FileUtils.getFileNameFromUri(context, uri)
//                    val text = if (FileUtils.isPDFFile(fileName)) {
//                        // For demo, we'll use filename-based extraction
//                        extractInfoFromFileName(fileName)
//                    } else {
//                        ""
//                    }
//
//                    val techs = TechStackExtractor.extractTechStack(text)
//
//                    val resume = RecruiterResumeEntity(
//                        resumeId = UUID.randomUUID().toString(),
//                        fileName = fileName,
//                        candidateName = extractCandidateName(fileName),
//                        candidateEmail = "candidate@example.com",
//                        experience = extractExperienceFromFileName(fileName),
//                        techStack = techs.joinToString(", "),
//                        rawText = text,
//                        uploadedDate = System.currentTimeMillis(),
//                        recruiterEmail = recruiterEmail
//                    )
//                    resumes.add(resume)
//
//                    uploadProgress = (index + 1).toFloat() / totalFiles
//                } catch (e: Exception) {
//                    e.printStackTrace()
//                }
//            }
//
//            if (resumes.isNotEmpty()) {
//                viewModel.insertMultipleResumes(resumes)
//                showSuccessDialog = true
//            } else {
//                errorMessage = "No valid resumes found"
//            }
//            isUploading = false
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
//                            onClick = { folderPicker.launch(arrayOf("application/pdf")) },
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
//                            Text("Upload Folder")
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
//                            IconButton(onClick = { selectedFileName = "" }) {
//                                Icon(Icons.Default.Close, contentDescription = "Remove")
//                            }
//                        }
//                    }
//                }
//            }
//
//            // Manual Info Entry (for single upload)
//            if (selectedFileName.isNotEmpty()) {
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
//
//            // Upload Progress
//            if (isUploading && uploadProgress > 0f) {
//                item {
//                    Card(
//                        modifier = Modifier.fillMaxWidth(),
//                        shape = RoundedCornerShape(12.dp)
//                    ) {
//                        Column(modifier = Modifier.padding(16.dp)) {
//                            Text(
//                                "Uploading: ${(uploadProgress * 100).toInt()}%",
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
//        }
//    }
//
//    // Success Dialog
//    if (showSuccessDialog) {
//        AlertDialog(
//            onDismissRequest = { showSuccessDialog = false },
//            title = { Text("Success!") },
//            text = { Text("Resumes uploaded successfully") },
//            confirmButton = {
//                Button(onClick = {
//                    showSuccessDialog = false
//                    selectedFileName = ""
//                    candidateName = ""
//                    candidateEmail = ""
//                    experience = "0"
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
//private fun extractInfoFromFileName(fileName: String): String {
//    // Extract info from filename like "John_5y_Kotlin_Java"
//    return fileName.replace("_", " ").replace(".pdf", "")
//}
//
//private fun extractCandidateName(fileName: String): String {
//    val name = fileName.split("_").firstOrNull() ?: fileName
//    return name.replace(".pdf", "")
//}
//
//private fun extractExperienceFromFileName(fileName: String): Int {
//    val parts = fileName.split("_")
//    for (part in parts) {
//        val years = part.replace("y", "").toIntOrNull()
//        if (years != null) return years
//    }
//    return 0
//}


// ============================================
// FILE 16: screens/ResumeUploadScreen.kt (SIMPLIFIED)
// ============================================
package com.example.feature_recruiter.screens

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
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

    var selectedFileName by remember { mutableStateOf("") }
    var candidateName by remember { mutableStateOf("") }
    var candidateEmail by remember { mutableStateOf("") }
    var experience by remember { mutableStateOf("0") }
    var extractedTechStack by remember { mutableStateOf<List<String>>(emptyList()) }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    var isProcessing by remember { mutableStateOf(false) }
    var uploadProgress by remember { mutableStateOf(0f) }
    var showSuccessDialog by remember { mutableStateOf(false) }

    val snackbarHostState = remember { SnackbarHostState() }

    // Single File Picker
    val singleFilePicker = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri ->
        if (uri != null) {
            selectedFileName = FileUtils.getFileNameFromUri(context, uri)
            errorMessage = null
            isProcessing = true
            uploadProgress = 0f

            scope.launch(Dispatchers.Default) {
                try {
                    // Attempt to extract text from PDF
                    val pdfText = PDFParser.extractTextFromPDF(context, uri.toString())

                    // Extract tech stack from PDF content
                    extractedTechStack = if (pdfText.isNotEmpty()) {
                        TechStackExtractor.extractTechStack(pdfText)
                    } else {
                        emptyList()
                    }

                    // Extract candidate info from filename
                    candidateName = extractCandidateName(selectedFileName)
                    experience = extractExperienceFromFileName(selectedFileName).toString()

                    uploadProgress = 1f
                } catch (e: Exception) {
                    e.printStackTrace()
                    errorMessage = "Error processing file: ${e.message}"
                }
                isProcessing = false
            }
        }
    }

    // Batch Upload - Multiple Files
    val batchUploadPicker = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.OpenMultipleDocuments()
    ) { uris ->
        if (uris.isNotEmpty()) {
            isProcessing = true
            uploadProgress = 0f

            scope.launch(Dispatchers.Default) {
                try {
                    val resumes = mutableListOf<RecruiterResumeEntity>()
                    val totalFiles = uris.size

                    uris.forEachIndexed { index, uri ->
                        try {
                            val fileName = FileUtils.getFileNameFromUri(context, uri)

                            // Extract text from PDF
                            val pdfText = if (FileUtils.isPDFFile(fileName)) {
                                PDFParser.extractTextFromPDF(context, uri.toString())
                            } else {
                                ""
                            }

                            // Extract tech stack
                            val techs = if (pdfText.isNotEmpty()) {
                                TechStackExtractor.extractTechStack(pdfText)
                            } else {
                                emptyList()
                            }

                            val resume = RecruiterResumeEntity(
                                resumeId = UUID.randomUUID().toString(),
                                fileName = fileName,
                                candidateName = extractCandidateName(fileName),
                                candidateEmail = extractEmailFromFileName(fileName),
                                experience = extractExperienceFromFileName(fileName),
                                techStack = techs.joinToString(", "),
                                rawText = pdfText,
                                uploadedDate = System.currentTimeMillis(),
                                recruiterEmail = recruiterEmail
                            )
                            resumes.add(resume)

                            uploadProgress = (index + 1).toFloat() / totalFiles
                        } catch (e: Exception) {
                            e.printStackTrace()
                        }
                    }

                    if (resumes.isNotEmpty()) {
                        viewModel.insertMultipleResumes(resumes)
                        showSuccessDialog = true
                    } else {
                        errorMessage = "No valid resumes found"
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                    errorMessage = "Upload failed: ${e.message}"
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

            // Upload Mode Selection
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
                    Column(modifier = Modifier.padding(20.dp)) {
                        Text(
                            "Upload Mode",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = if (isDark) Color.White else Color.Black
                        )
                        Spacer(modifier = Modifier.height(12.dp))

                        // Single Upload Button
                        Button(
                            onClick = { singleFilePicker.launch("application/pdf") },
                            enabled = !isProcessing,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(48.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color(0xFF4A90E2)
                            ),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Icon(Icons.Default.Upload, contentDescription = null)
                            Spacer(Modifier.width(8.dp))
                            Text("Upload Single Resume")
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        // Batch Upload Button
                        Button(
                            onClick = { batchUploadPicker.launch(arrayOf("application/pdf")) },
                            enabled = !isProcessing,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(48.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color(0xFFFFA726)
                            ),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Icon(Icons.Default.FolderOpen, contentDescription = null)
                            Spacer(Modifier.width(8.dp))
                            Text("Upload Multiple Resumes")
                        }
                    }
                }
            }

            // Selected File Info
            if (selectedFileName.isNotEmpty()) {
                item {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .shadow(6.dp, RoundedCornerShape(12.dp)),
                        shape = RoundedCornerShape(12.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = if (isDark) Color(0xFF1E1E2F) else Color.White
                        )
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Row(
                                modifier = Modifier.weight(1f),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(12.dp)
                            ) {
                                Icon(
                                    Icons.Default.FilePresent,
                                    contentDescription = null,
                                    tint = Color(0xFF4A90E2),
                                    modifier = Modifier.size(32.dp)
                                )
                                Column {
                                    Text(
                                        selectedFileName,
                                        fontSize = 14.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = if (isDark) Color.White else Color.Black,
                                        maxLines = 1
                                    )
                                    Text(
                                        "PDF Document",
                                        fontSize = 12.sp,
                                        color = Color.Gray
                                    )
                                }
                            }
                            IconButton(onClick = {
                                selectedFileName = ""
                                extractedTechStack = emptyList()
                                candidateName = ""
                                candidateEmail = ""
                                experience = "0"
                            }) {
                                Icon(Icons.Default.Close, contentDescription = "Remove")
                            }
                        }
                    }
                }
            }

            // Processing State
            if (isProcessing) {
                item {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text(
                                "Processing: ${(uploadProgress * 100).toInt()}%",
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            LinearProgressIndicator(
                                progress = uploadProgress,
                                modifier = Modifier.fillMaxWidth()
                            )
                        }
                    }
                }
            }

            // Extracted Tech Stack
            if (extractedTechStack.isNotEmpty()) {
                item {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = if (isDark) Color(0xFF1E1E2F) else Color.White
                        )
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text(
                                "Detected Technologies",
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold,
                                color = if (isDark) Color.White else Color.Black
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            FlowRow(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                extractedTechStack.forEach { tech ->
                                    Surface(
                                        shape = RoundedCornerShape(8.dp),
                                        color = Color(0xFF4A90E2).copy(alpha = 0.2f)
                                    ) {
                                        Text(
                                            tech,
                                            modifier = Modifier.padding(8.dp, 4.dp),
                                            fontSize = 12.sp,
                                            color = Color(0xFF4A90E2)
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }

            // Manual Info Entry
            if (selectedFileName.isNotEmpty() && !isProcessing) {
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

                            // Upload Button
                            Button(
                                onClick = {
                                    if (candidateName.isBlank() || candidateEmail.isBlank()) {
                                        errorMessage = "Please fill all fields"
                                        return@Button
                                    }

                                    val resume = RecruiterResumeEntity(
                                        resumeId = UUID.randomUUID().toString(),
                                        fileName = selectedFileName,
                                        candidateName = candidateName,
                                        candidateEmail = candidateEmail,
                                        experience = experience.toIntOrNull() ?: 0,
                                        techStack = extractedTechStack.joinToString(", "),
                                        rawText = "",
                                        uploadedDate = System.currentTimeMillis(),
                                        recruiterEmail = recruiterEmail
                                    )

                                    viewModel.insertResume(resume)
                                    showSuccessDialog = true
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
                                Text("Upload Resume", fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                }
            }

            // Error Message
            if (errorMessage != null) {
                item {
                    Surface(
                        modifier = Modifier.fillMaxWidth(),
                        color = Color(0xFFFFEBEE),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(12.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                Icons.Default.Error,
                                contentDescription = null,
                                tint = Color(0xFFC62828),
                                modifier = Modifier.size(20.dp)
                            )
                            Spacer(Modifier.width(8.dp))
                            Text(
                                errorMessage ?: "",
                                color = Color(0xFFC62828),
                                fontSize = 12.sp
                            )
                        }
                    }
                }
            }
        }
    }

    // Success Dialog
    if (showSuccessDialog) {
        AlertDialog(
            onDismissRequest = { showSuccessDialog = false },
            title = { Text("Success!") },
            text = { Text("Resume uploaded successfully") },
            confirmButton = {
                Button(onClick = {
                    showSuccessDialog = false
                    selectedFileName = ""
                    candidateName = ""
                    candidateEmail = ""
                    experience = "0"
                    extractedTechStack = emptyList()
                    onUploadSuccess()
                }) {
                    Text("OK")
                }
            }
        )
    }
}

// Helper functions
private fun extractCandidateName(fileName: String): String {
    val name = fileName.split("_").firstOrNull() ?: fileName
    return name.replace(".pdf", "").replace("-", " ")
}

private fun extractEmailFromFileName(fileName: String): String {
    // Try to extract email from filename like "john_doe_john@email.com.pdf"
    val parts = fileName.split("_", "-")
    for (part in parts) {
        if (part.contains("@")) {
            return part.replace(".pdf", "")
        }
    }
    return "candidate@example.com"
}

private fun extractExperienceFromFileName(fileName: String): Int {
    // Try to extract years like "5y" from filename
    val parts = fileName.split("_", "-", ".")
    for (part in parts) {
        val years = part.replace("y", "").replace("Y", "").toIntOrNull()
        if (years != null && years > 0) return years
    }
    return 0
}