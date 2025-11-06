//package com.example.feature_recruiter.dialog
//
//import androidx.compose.foundation.background
//import androidx.compose.foundation.layout.*
//import androidx.compose.foundation.shape.RoundedCornerShape
//import androidx.compose.material.icons.Icons
//import androidx.compose.material.icons.filled.Business
//import androidx.compose.material3.*
//import androidx.compose.runtime.*
//import androidx.compose.ui.Alignment
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.graphics.Color
//import androidx.compose.ui.text.font.FontWeight
//import androidx.compose.ui.unit.dp
//import androidx.compose.ui.unit.sp
//import androidx.compose.ui.window.Dialog
//import androidx.compose.ui.window.DialogProperties
//
//@OptIn(ExperimentalMaterial3Api::class)
//@Composable
//fun CompanySetupDialog(
//    recruiterEmail: String,
//    onSetupComplete: (companyName: String, recruiterName: String) -> Unit
//) {
//    var companyName by remember { mutableStateOf("") }
//    var recruiterName by remember { mutableStateOf("") }
//    var termsAccepted by remember { mutableStateOf(false) }
//    var showError by remember { mutableStateOf(false) }
//
//    Dialog(
//        onDismissRequest = {},
//        properties = DialogProperties(dismissOnBackPress = false, dismissOnClickOutside = false)
//    ) {
//        Card(
//            modifier = Modifier
//                .fillMaxWidth(0.9f)
//                .background(Color.White, RoundedCornerShape(20.dp)),
//            shape = RoundedCornerShape(20.dp),
//            colors = CardDefaults.cardColors(containerColor = Color.White)
//        ) {
//            Column(
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .padding(24.dp),
//                horizontalAlignment = Alignment.CenterHorizontally,
//                verticalArrangement = Arrangement.spacedBy(16.dp)
//            ) {
//                // Header
//                Icon(
//                    Icons.Default.Business,
//                    contentDescription = null,
//                    modifier = Modifier.size(56.dp),
//                    tint = Color(0xFF4A90E2)
//                )
//
//                Text(
//                    "Welcome to Talent Hub",
//                    fontSize = 24.sp,
//                    fontWeight = FontWeight.Bold,
//                    color = Color.Black
//                )
//
//                Text(
//                    "Complete your profile to get started",
//                    fontSize = 14.sp,
//                    color = Color.Gray
//                )
//
//                Spacer(modifier = Modifier.height(8.dp))
//
//                // Company Name Field
//                OutlinedTextField(
//                    value = companyName,
//                    onValueChange = { companyName = it },
//                    label = { Text("Company Name") },
//                    modifier = Modifier.fillMaxWidth(),
//                    shape = RoundedCornerShape(12.dp),
//                    colors = OutlinedTextFieldDefaults.colors(
//                        focusedBorderColor = Color(0xFF4A90E2),
//                        unfocusedBorderColor = Color.Gray.copy(0.3f)
//                    ),
//                    singleLine = true
//                )
//
//                // Recruiter Name Field
//                OutlinedTextField(
//                    value = recruiterName,
//                    onValueChange = { recruiterName = it },
//                    label = { Text("Your Name") },
//                    modifier = Modifier.fillMaxWidth(),
//                    shape = RoundedCornerShape(12.dp),
//                    colors = OutlinedTextFieldDefaults.colors(
//                        focusedBorderColor = Color(0xFF4A90E2),
//                        unfocusedBorderColor = Color.Gray.copy(0.3f)
//                    ),
//                    singleLine = true
//                )
//
//                // Email Display (Read-only)
//                OutlinedTextField(
//                    value = recruiterEmail,
//                    onValueChange = {},
//                    label = { Text("Email") },
//                    modifier = Modifier.fillMaxWidth(),
//                    shape = RoundedCornerShape(12.dp),
//                    enabled = false,
//                    colors = OutlinedTextFieldDefaults.colors(
//                        disabledBorderColor = Color.Gray.copy(0.3f),
//                        disabledTextColor = Color.Gray
//                    ),
//                    singleLine = true
//                )
//
//                // Terms Checkbox
//                Row(
//                    modifier = Modifier
//                        .fillMaxWidth()
//                        .padding(8.dp),
//                    verticalAlignment = Alignment.CenterVertically,
//                    horizontalArrangement = Arrangement.spacedBy(8.dp)
//                ) {
//                    Checkbox(
//                        checked = termsAccepted,
//                        onCheckedChange = { termsAccepted = it }
//                    )
//                    Text(
//                        "I agree to the terms & conditions",
//                        fontSize = 12.sp,
//                        color = Color.Gray
//                    )
//                }
//
//                // Error Message
//                if (showError) {
//                    Surface(
//                        modifier = Modifier.fillMaxWidth(),
//                        color = Color(0xFFFFEBEE),
//                        shape = RoundedCornerShape(8.dp)
//                    ) {
//                        Text(
//                            "Please fill all fields and accept terms",
//                            modifier = Modifier.padding(12.dp),
//                            color = Color(0xFFC62828),
//                            fontSize = 12.sp
//                        )
//                    }
//                }
//
//                Spacer(modifier = Modifier.height(8.dp))
//
//                // Button
//                Button(
//                    onClick = {
//                        if (companyName.isBlank() || recruiterName.isBlank() || !termsAccepted) {
//                            showError = true
//                        } else {
//                            onSetupComplete(companyName, recruiterName)
//                        }
//                    },
//                    modifier = Modifier
//                        .fillMaxWidth()
//                        .height(54.dp),
//                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4A90E2)),
//                    shape = RoundedCornerShape(12.dp)
//                ) {
//                    Text("Continue", fontWeight = FontWeight.Bold, fontSize = 16.sp)
//                }
//            }
//        }
//    }
//}


// ============================================
// FILE: dialog/CompanySetupDialog.kt (ENHANCED)
// ============================================
package com.example.feature_recruiter.dialog

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Business
import androidx.compose.material.icons.filled.Check
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
    onSetupComplete: (companyName: String, recruiterName: String) -> Unit
) {
    var companyName by remember { mutableStateOf("") }
    var recruiterName by remember { mutableStateOf("") }
    var showError by remember { mutableStateOf(false) }
    var errorText by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }

    Dialog(
        onDismissRequest = {},
        properties = DialogProperties(
            dismissOnBackPress = false,
            dismissOnClickOutside = false,
            usePlatformDefaultWidth = false
        )
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black.copy(alpha = 0.5f)),
            contentAlignment = Alignment.Center
        ) {
            Card(
                modifier = Modifier
                    .fillMaxWidth(0.85f)
                    .clip(RoundedCornerShape(24.dp))
                    .animateContentSize(),
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 16.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(0.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    // Header with gradient background
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
                            // Icon with background
                            Box(
                                modifier = Modifier
                                    .size(80.dp)
                                    .background(
                                        Color.White.copy(alpha = 0.2f),
                                        RoundedCornerShape(20.dp)
                                    ),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    Icons.Default.Business,
                                    contentDescription = null,
                                    modifier = Modifier.size(48.dp),
                                    tint = Color.White
                                )
                            }

                            Text(
                                "Welcome to Talent Hub",
                                fontSize = 28.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White,
                                textAlign = androidx.compose.ui.text.style.TextAlign.Center
                            )

                            Text(
                                "Set up your recruiter profile to get started",
                                fontSize = 14.sp,
                                color = Color.White.copy(alpha = 0.9f),
                                textAlign = androidx.compose.ui.text.style.TextAlign.Center
                            )
                        }
                    }

                    // Content
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
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
                                placeholder = { Text("Enter your company name") },
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(12.dp),
                                singleLine = true,
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedBorderColor = Color(0xFF4A90E2),
                                    unfocusedBorderColor = Color.Gray.copy(0.3f),
                                    focusedTextColor = Color.Black,
                                    unfocusedTextColor = Color.Black
                                ),
                                textStyle = androidx.compose.ui.text.TextStyle(fontSize = 14.sp),
                                isError = showError && companyName.isBlank()
                            )
                        }

                        // Your Name Field
                        Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                            Text(
                                "Your Name",
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
                                placeholder = { Text("Enter your full name") },
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(12.dp),
                                singleLine = true,
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedBorderColor = Color(0xFF4A90E2),
                                    unfocusedBorderColor = Color.Gray.copy(0.3f),
                                    focusedTextColor = Color.Black,
                                    unfocusedTextColor = Color.Black
                                ),
                                textStyle = androidx.compose.ui.text.TextStyle(fontSize = 14.sp),
                                isError = showError && recruiterName.isBlank()
                            )
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
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(8.dp)),
                                color = Color(0xFFFFEBEE)
                            ) {
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(12.dp),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                                ) {
                                    Icon(
                                        Icons.Default.Business,
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
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(8.dp)),
                            color = Color(0xFFE3F2FD)
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
                                    "You can update your company name and theme in settings anytime.",
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
                                        errorText = "Please enter company name"
                                    }
                                    recruiterName.isBlank() -> {
                                        showError = true
                                        errorText = "Please enter your name"
                                    }
                                    else -> {
                                        isLoading = true
                                        onSetupComplete(companyName, recruiterName)
                                    }
                                }
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(56.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color(0xFF4A90E2)
                            ),
                            shape = RoundedCornerShape(12.dp),
                            enabled = !isLoading
                        ) {
                            if (isLoading) {
                                CircularProgressIndicator(
                                    modifier = Modifier.size(20.dp),
                                    color = Color.White,
                                    strokeWidth = 2.dp
                                )
                            } else {
                                Text(
                                    "Get Started",
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 16.sp
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}