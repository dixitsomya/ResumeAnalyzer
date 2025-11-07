package com.example.feature_recruiter

import androidx.compose.animation.animateContentSize
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.resumeanalyzer.core.navigation.datastore.UserPreference
import com.example.feature_recruiter.util.DatabaseHelper
import com.example.feature_recruiter.viewmodel.RecruiterProfileViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RecruiterSettingsScreen(
    userEmail: String,
    isDark: Boolean,
    profileViewModel: RecruiterProfileViewModel,
    onThemeChange: (String) -> Unit,
    onBack: () -> Unit
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val profile by profileViewModel.profile.collectAsState()
    val userPreferences by UserPreference.getUser(context).collectAsState(initial = null)

    var selectedTheme by remember { mutableStateOf("system") }
    var notificationEnabled by remember { mutableStateOf(true) }

    // Edit states
    var isEditingCompany by remember { mutableStateOf(false) }
    var editedCompanyName by remember { mutableStateOf("") }

    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(userPreferences) {
        userPreferences?.let { user ->
            selectedTheme = user.theme
            notificationEnabled = user.notificationsEnabled
        }
    }

    LaunchedEffect(profile) {
        profile?.let {
            editedCompanyName = it.companyName
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "Settings",
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            Icons.Default.ArrowBack,
                            contentDescription = "Back",
                            tint = Color.White
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFF4A90E2)
                ),
                modifier = Modifier.shadow(elevation = 8.dp)
            )
        },
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .background(if (isDark) Color(0xFF121212) else Color(0xFFF5F6FA)),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            // Profile Header Section
            item {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .shadow(8.dp, RoundedCornerShape(20.dp)),
                    shape = RoundedCornerShape(20.dp),
                    elevation = CardDefaults.cardElevation(0.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = if (isDark) Color(0xFF1E1E2E) else Color.White
                    )
                ) {
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
                            .padding(24.dp)
                    ) {
                        Column(
                            verticalArrangement = Arrangement.spacedBy(16.dp)
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(16.dp)
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(70.dp)
                                        .background(
                                            Color.White.copy(alpha = 0.2f),
                                            RoundedCornerShape(16.dp)
                                        ),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        userEmail.firstOrNull()?.uppercase() ?: "R",
                                        color = Color.White,
                                        fontSize = 32.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                                Column(
                                    verticalArrangement = Arrangement.spacedBy(4.dp)
                                ) {
                                    Text(
                                        profile?.recruiterName?.ifEmpty { "Recruiter" } ?: "Recruiter",
                                        fontSize = 18.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = Color.White
                                    )
                                    Text(
                                        profile?.companyName?.ifEmpty { "Company" } ?: "Company",
                                        fontSize = 14.sp,
                                        color = Color.White.copy(alpha = 0.8f)
                                    )
                                    Text(
                                        userEmail,
                                        fontSize = 12.sp,
                                        color = Color.White.copy(alpha = 0.7f)
                                    )
                                }
                            }
                        }
                    }
                }
            }

            // Company Information Section (Editable)
            item {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .shadow(6.dp, RoundedCornerShape(16.dp)),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = if (isDark) Color(0xFF1E1E2E) else Color.White
                    )
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(20.dp)
                            .animateContentSize(),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(12.dp)
                            ) {
                                Icon(
                                    Icons.Default.Business,
                                    contentDescription = null,
                                    tint = Color(0xFF4A90E2),
                                    modifier = Modifier.size(28.dp)
                                )
                                Column {
                                    Text(
                                        "Company Information",
                                        fontSize = 16.sp,
                                        fontWeight = FontWeight.SemiBold,
                                        color = if (isDark) Color.White else Color.Black
                                    )
                                    Text(
                                        "Update your company details",
                                        fontSize = 12.sp,
                                        color = if (isDark) Color.White.copy(0.6f) else Color.Gray
                                    )
                                }
                            }
                            if (!isEditingCompany) {
                                IconButton(
                                    onClick = {
                                        isEditingCompany = true
                                    },
                                    modifier = Modifier.size(40.dp)
                                ) {
                                    Icon(
                                        Icons.Default.Edit,
                                        contentDescription = "Edit",
                                        tint = Color(0xFF4A90E2)
                                    )
                                }
                            }
                        }

                        // Company Name Display/Edit
                        if (isEditingCompany && profile != null) {
                            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                                OutlinedTextField(
                                    value = editedCompanyName,
                                    onValueChange = { editedCompanyName = it },
                                    label = { Text("Company Name") },
                                    modifier = Modifier.fillMaxWidth(),
                                    shape = RoundedCornerShape(12.dp),
                                    colors = OutlinedTextFieldDefaults.colors(
                                        focusedBorderColor = Color(0xFF4A90E2),
                                        unfocusedBorderColor = if (isDark) Color.White.copy(0.3f) else Color.Gray.copy(0.3f),
                                        focusedTextColor = if (isDark) Color.White else Color.Black,
                                        unfocusedTextColor = if (isDark) Color.White else Color.Black
                                    ),
                                    singleLine = true
                                )

                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                                ) {
                                    Button(
                                        onClick = {
                                            if (editedCompanyName.isNotBlank() && profile != null) {
                                                scope.launch {
                                                    profileViewModel.updateCompanyInfo(
                                                        editedCompanyName,
                                                        profile!!.recruiterName
                                                    )
                                                    isEditingCompany = false
                                                    snackbarHostState.showSnackbar(
                                                        message = "Company information updated",
                                                        duration = SnackbarDuration.Short
                                                    )
                                                }
                                            }
                                        },
                                        modifier = Modifier
                                            .weight(1f)
                                            .height(48.dp),
                                        colors = ButtonDefaults.buttonColors(
                                            containerColor = Color(0xFF4A90E2)
                                        ),
                                        shape = RoundedCornerShape(10.dp)
                                    ) {
                                        Icon(Icons.Default.Check, contentDescription = null, modifier = Modifier.size(18.dp))
                                        Spacer(Modifier.width(4.dp))
                                        Text("Save", fontWeight = FontWeight.Bold)
                                    }
                                    OutlinedButton(
                                        onClick = { isEditingCompany = false },
                                        modifier = Modifier
                                            .weight(1f)
                                            .height(48.dp),
                                        shape = RoundedCornerShape(10.dp)
                                    ) {
                                        Icon(Icons.Default.Close, contentDescription = null, modifier = Modifier.size(18.dp))
                                        Spacer(Modifier.width(4.dp))
                                        Text("Cancel")
                                    }
                                }
                            }
                        } else {
                            // Display mode
                            Surface(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(10.dp)),
                                color = if (isDark) Color(0xFF2A2A3E) else Color(0xFFF5F6FA)
                            ) {
                                Text(
                                    profile?.companyName?.ifEmpty { "Not set" } ?: "Not set",
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(12.dp),
                                    fontSize = 14.sp,
                                    color = if (isDark) Color.White else Color.Black
                                )
                            }
                        }
                    }
                }
            }

            // Theme Selection Section
            item {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .shadow(6.dp, RoundedCornerShape(16.dp)),
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
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            Icon(
                                Icons.Default.Palette,
                                contentDescription = null,
                                tint = Color(0xFF4A90E2),
                                modifier = Modifier.size(28.dp)
                            )
                            Column {
                                Text(
                                    "Theme",
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    color = if (isDark) Color.White else Color.Black
                                )
                                Text(
                                    "Choose your preferred appearance",
                                    fontSize = 12.sp,
                                    color = if (isDark) Color.White.copy(0.6f) else Color.Gray
                                )
                            }
                        }

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            ThemeOption(
                                label = "Light",
                                isSelected = selectedTheme == "light",
                                icon = Icons.Default.LightMode,
                                isDark = isDark,
                                onClick = {
                                    selectedTheme = "light"
                                    scope.launch {
                                        UserPreference.saveTheme(context, "light")
                                        onThemeChange("light")
                                    }
                                },
                                modifier = Modifier.weight(1f)
                            )

                            ThemeOption(
                                label = "Dark",
                                isSelected = selectedTheme == "dark",
                                icon = Icons.Default.DarkMode,
                                isDark = isDark,
                                onClick = {
                                    selectedTheme = "dark"
                                    scope.launch {
                                        UserPreference.saveTheme(context, "dark")
                                        onThemeChange("dark")
                                    }
                                },
                                modifier = Modifier.weight(1f)
                            )

                            ThemeOption(
                                label = "System",
                                isSelected = selectedTheme == "system",
                                icon = Icons.Default.SettingsBrightness,
                                isDark = isDark,
                                onClick = {
                                    selectedTheme = "system"
                                    scope.launch {
                                        UserPreference.saveTheme(context, "system")
                                        onThemeChange("system")
                                    }
                                },
                                modifier = Modifier.weight(1f)
                            )
                        }
                    }
                }
            }

            // Notifications Section
            item {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .shadow(6.dp, RoundedCornerShape(16.dp)),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = if (isDark) Color(0xFF1E1E2E) else Color.White
                    )
                ) {
                    Row(
                        modifier = Modifier
                            .padding(20.dp)
                            .fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            Icon(
                                Icons.Default.Notifications,
                                contentDescription = null,
                                tint = Color(0xFF4A90E2),
                                modifier = Modifier.size(28.dp)
                            )
                            Column {
                                Text(
                                    "Notifications",
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    color = if (isDark) Color.White else Color.Black
                                )
                                Text(
                                    "Resume upload alerts",
                                    fontSize = 12.sp,
                                    color = if (isDark) Color.White.copy(0.6f) else Color.Gray
                                )
                            }
                        }
                        Switch(
                            checked = notificationEnabled,
                            onCheckedChange = {
                                notificationEnabled = it
                                scope.launch {
                                    UserPreference.saveNotification(context, it)
                                }
                            },
                            modifier = Modifier.scale(1.2f),
                            colors = SwitchDefaults.colors(
                                checkedThumbColor = Color(0xFF4A90E2)
                            )
                        )
                    }
                }
            }

            // Account Info Section (Read-Only)
            item {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .shadow(6.dp, RoundedCornerShape(16.dp)),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = if (isDark) Color(0xFF1E1E2E) else Color.White
                    )
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(20.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Text(
                            "Account Information",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = if (isDark) Color.White else Color.Black
                        )

                        InfoRow(
                            "Email",
                            userEmail,
                            isDark
                        )
                        InfoRow(
                            "Name",
                            profile?.recruiterName?.ifEmpty { "Not set" } ?: "Not set",
                            isDark
                        )
                        InfoRow(
                            "Role",
                            "Recruiter",
                            isDark
                        )
                    }
                }
            }

            item {
                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    }
}

@Composable
fun ThemeOption(
    label: String,
    isSelected: Boolean,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    isDark: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Button(
        onClick = onClick,
        modifier = modifier
            .height(56.dp)
            .shadow(
                elevation = if (isSelected) 8.dp else 4.dp,
                shape = RoundedCornerShape(12.dp)
            ),
        colors = ButtonDefaults.buttonColors(
            containerColor = if (isSelected) Color(0xFF4A90E2) else (if (isDark) Color(0xFF2A2A3E) else Color(0xFFF5F6FA)),
            contentColor = if (isSelected) Color.White else (if (isDark) Color.White else Color.Black)
        ),
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Icon(icon, contentDescription = null, modifier = Modifier.size(20.dp))
            Text(label, fontSize = 11.sp, fontWeight = FontWeight.SemiBold)
        }
    }
}

@Composable
fun InfoRow(label: String, value: String, isDark: Boolean) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            label,
            fontSize = 13.sp,
            color = if (isDark) Color.White.copy(0.6f) else Color.Gray
        )
        Text(
            value,
            fontSize = 13.sp,
            fontWeight = FontWeight.SemiBold,
            color = if (isDark) Color.White else Color.Black
        )
    }
}