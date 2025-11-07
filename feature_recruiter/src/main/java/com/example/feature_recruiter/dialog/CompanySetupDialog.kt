package com.example.feature_recruiter.dialog

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Business
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
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
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CompanySetupDialog(
    recruiterEmail: String,
    onSetupComplete: (companyName: String, recruiterName: String) -> Unit,
    isLoading: Boolean = false
) {
    var companyName by remember { mutableStateOf("") }
    var recruiterName by remember { mutableStateOf("") }
    var showError by remember { mutableStateOf(false) }
    var errorText by remember { mutableStateOf("") }

    Dialog(
        onDismissRequest = {}, // Cannot dismiss
        properties = DialogProperties(
            dismissOnBackPress = false,
            dismissOnClickOutside = false,
            usePlatformDefaultWidth = false
        )
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black.copy(alpha = 0.6f)),
            contentAlignment = Alignment.Center
        ) {
            Card(
                modifier = Modifier
                    .fillMaxWidth(0.9f)
                    .clip(RoundedCornerShape(24.dp))
                    .animateContentSize(),
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 16.dp)
            ) {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    // Header
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(
                                Brush.verticalGradient(
                                    listOf(
                                        Color(0xFF4A90E2),
                                        Color(0xFF357ABD)
                                    )
                                )
                            )
                            .padding(24.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(70.dp)
                                    .background(
                                        Color.White.copy(alpha = 0.2f),
                                        RoundedCornerShape(18.dp)
                                    ),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    Icons.Default.Business,
                                    contentDescription = null,
                                    modifier = Modifier.size(40.dp),
                                    tint = Color.White
                                )
                            }

                            Text(
                                "Welcome to Talent Hub 🎉",
                                fontSize = 24.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White,
                                textAlign = androidx.compose.ui.text.style.TextAlign.Center
                            )

                            Text(
                                "Complete your profile to get started",
                                fontSize = 13.sp,
                                color = Color.White.copy(alpha = 0.9f),
                                textAlign = androidx.compose.ui.text.style.TextAlign.Center
                            )
                        }
                    }

                    // Content (Scrollable)
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .verticalScroll(rememberScrollState())
                            .padding(24.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        // Company Name Field
                        Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                            Text(
                                "Company Name",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = Color(0xFF4A90E2)
                            )
                            OutlinedTextField(
                                value = companyName,
                                onValueChange = {
                                    companyName = it
                                    showError = false
                                },
                                placeholder = { Text("e.g., Tech Solutions Inc") },
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(12.dp),
                                singleLine = true,
                                enabled = !isLoading,
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedBorderColor = Color(0xFF4A90E2),
                                    unfocusedBorderColor = Color.Gray.copy(0.3f),
                                    focusedTextColor = Color.Black,
                                    unfocusedTextColor = Color.Black
                                ),
                                textStyle = androidx.compose.ui.text.TextStyle(fontSize = 14.sp),
                                isError = showError && companyName.isBlank()
                            )
                            if (showError && companyName.isBlank()) {
                                Text(
                                    "Company name is required",
                                    fontSize = 11.sp,
                                    color = Color(0xFFC62828)
                                )
                            }
                        }

                        // Your Name Field
                        Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                            Text(
                                "Your Full Name",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = Color(0xFF4A90E2)
                            )
                            OutlinedTextField(
                                value = recruiterName,
                                onValueChange = {
                                    recruiterName = it
                                    showError = false
                                },
                                placeholder = { Text("e.g., John Doe") },
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(12.dp),
                                singleLine = true,
                                enabled = !isLoading,
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedBorderColor = Color(0xFF4A90E2),
                                    unfocusedBorderColor = Color.Gray.copy(0.3f),
                                    focusedTextColor = Color.Black,
                                    unfocusedTextColor = Color.Black
                                ),
                                textStyle = androidx.compose.ui.text.TextStyle(fontSize = 14.sp),
                                isError = showError && recruiterName.isBlank()
                            )
                            if (showError && recruiterName.isBlank()) {
                                Text(
                                    "Your name is required",
                                    fontSize = 11.sp,
                                    color = Color(0xFFC62828)
                                )
                            }
                        }

                        // Email Display (Read-only)
                        Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                            Text(
                                "Email Address",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = Color(0xFF4A90E2)
                            )
                            OutlinedTextField(
                                value = recruiterEmail,
                                onValueChange = {},
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(12.dp),
                                enabled = false,
                                colors = OutlinedTextFieldDefaults.colors(
                                    disabledBorderColor = Color.Gray.copy(0.3f),
                                    disabledTextColor = Color.Gray
                                ),
                                textStyle = androidx.compose.ui.text.TextStyle(fontSize = 14.sp),
                                singleLine = true
                            )
                        }

                        // Error Message
                        if (showError && errorText.isNotEmpty()) {
                            Surface(
                                modifier = Modifier.fillMaxWidth(),
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
                                        Icons.Default.Close,
                                        contentDescription = null,
                                        tint = Color(0xFFC62828),
                                        modifier = Modifier.size(18.dp)
                                    )
                                    Text(
                                        errorText,
                                        color = Color(0xFFC62828),
                                        fontSize = 12.sp
                                    )
                                }
                            }
                        }

                        // Info Box
                        Surface(
                            modifier = Modifier.fillMaxWidth(),
                            color = Color(0xFFE3F2FD),
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(12.dp),
                                verticalAlignment = Alignment.Top,
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                Icon(
                                    Icons.Default.Check,
                                    contentDescription = null,
                                    tint = Color(0xFF1976D2),
                                    modifier = Modifier.size(18.dp)
                                )
                                Text(
                                    "You can update your company name and profile in settings anytime.",
                                    fontSize = 11.sp,
                                    color = Color(0xFF1565C0)
                                )
                            }
                        }

                        // Continue Button
                        Button(
                            onClick = {
                                when {
                                    companyName.isBlank() -> {
                                        showError = true
                                        errorText = "Please enter your company name"
                                    }
                                    recruiterName.isBlank() -> {
                                        showError = true
                                        errorText = "Please enter your full name"
                                    }
                                    else -> {
                                        // ✅ All validation passed
                                        onSetupComplete(companyName, recruiterName)
                                    }
                                }
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(56.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color(0xFF4A90E2),
                                disabledContainerColor = Color(0xFF4A90E2).copy(alpha = 0.5f)
                            ),
                            shape = RoundedCornerShape(12.dp),
                            enabled = !isLoading
                        ) {
                            if (isLoading) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                                ) {
                                    CircularProgressIndicator(
                                        modifier = Modifier.size(20.dp),
                                        color = Color.White,
                                        strokeWidth = 2.dp
                                    )
                                    Text(
                                        "Setting up...",
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 14.sp
                                    )
                                }
                            } else {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                                ) {
                                    Icon(Icons.Default.Check, contentDescription = null, modifier = Modifier.size(20.dp))
                                    Text(
                                        "Get Started",
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 14.sp
                                    )
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(4.dp))
                    }
                }
            }
        }
    }
}