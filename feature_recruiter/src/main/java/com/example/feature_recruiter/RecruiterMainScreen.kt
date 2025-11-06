//package com.example.feature_recruiter
//
//import androidx.compose.foundation.background
//import androidx.compose.foundation.isSystemInDarkTheme
//import androidx.compose.foundation.layout.*
//import androidx.compose.foundation.shape.CircleShape
//import androidx.compose.foundation.shape.RoundedCornerShape
//import androidx.compose.material.icons.Icons
//import androidx.compose.material.icons.filled.*
//import androidx.compose.material3.*
//import androidx.compose.runtime.*
//import androidx.compose.ui.Alignment
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.draw.clip
//import androidx.compose.ui.graphics.Color
//import androidx.compose.ui.platform.LocalContext
//import androidx.compose.ui.text.font.FontWeight
//import androidx.compose.ui.unit.dp
//import androidx.compose.ui.unit.sp
//import androidx.navigation.NavController
//import com.example.resumeanalyzer.core.navigation.datastore.UserPreference
//import com.example.resumeanalyzer.core.navigation.datastore.UserCache
//import com.example.feature_recruiter.database.RecruiterDatabase
//import com.example.feature_recruiter.dialog.CompanySetupDialog
//import com.example.feature_recruiter.repository.RecruiterProfileRepository
//import com.example.feature_recruiter.repository.RecruiterResumeRepository
//import com.example.feature_recruiter.screens.*
//import com.example.feature_recruiter.util.DatabaseHelper
//import com.example.feature_recruiter.viewmodel.RecruiterProfileViewModel
//import com.example.feature_recruiter.viewmodel.RecruiterResumeViewModel
//import kotlinx.coroutines.launch
//
//@OptIn(ExperimentalMaterial3Api::class)
//@Composable
//fun RecruiterMainScreen(
//    navController: NavController,
//    onNavigate: (String) -> Unit
//) {
//    val context = LocalContext.current
//    val user by UserPreference.getUser(context).collectAsState(initial = UserCache())
//    val userEmail = user.email ?: "Recruiter"
//    val scope = rememberCoroutineScope()
//
//    // Initialize Database
//    val db = remember { DatabaseHelper.getDatabase(context) }
//    val resumeRepo = remember { RecruiterResumeRepository(db.recruiterResumeDao()) }
//    val profileRepo = remember { RecruiterProfileRepository(db.recruiterProfileDao()) }
//    val resumeViewModel = remember { RecruiterResumeViewModel(resumeRepo, userEmail) }
//    val profileViewModel = remember { RecruiterProfileViewModel(profileRepo, userEmail) }
//
//    // UI State
//    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
//    var selectedScreen by remember { mutableStateOf("home") }
//    var showSetupDialog by remember { mutableStateOf(false) }
//
//    val isDark = when (user.theme) {
//        "light" -> false
//        "dark" -> true
//        else -> isSystemInDarkTheme()
//    }
//
//    // Check if setup is completed
//    val profile by profileViewModel.profile.collectAsState()
//    LaunchedEffect(profile) {
//        if (profile == null) {
//            showSetupDialog = true
//        }
//    }
//
//    // Show setup dialog on first login
//    if (showSetupDialog) {
//        CompanySetupDialog(
//            recruiterEmail = userEmail,
//            onSetupComplete = { companyName, recruiterName ->
//                scope.launch {
//                    profileViewModel.createInitialProfile(companyName, recruiterName)
//                    showSetupDialog = false
//                }
//            }
//        )
//    }
//
//    ModalNavigationDrawer(
//        drawerState = drawerState,
//        drawerContent = {
//            ModalDrawerSheet(
//                modifier = Modifier
//                    .fillMaxHeight()
//                    .width(320.dp),
//                drawerContainerColor = if (isDark) Color(0xFF121212) else Color(0xFFF5F6FA)
//            ) {
//                Column(
//                    modifier = Modifier
//                        .fillMaxWidth()
//                        .padding(16.dp),
//                    horizontalAlignment = Alignment.CenterHorizontally
//                ) {
//                    Box(
//                        modifier = Modifier
//                            .size(70.dp)
//                            .clip(CircleShape)
//                            .background(Color(0xFF4A90E2)),
//                        contentAlignment = Alignment.Center
//                    ) {
//                        Text(
//                            userEmail.firstOrNull()?.uppercase() ?: "R",
//                            color = Color.White,
//                            fontSize = 28.sp,
//                            fontWeight = FontWeight.Bold
//                        )
//                    }
//                    Spacer(modifier = Modifier.height(8.dp))
//                    Text(
//                        userEmail,
//                        fontSize = 14.sp,
//                        color = if (isDark) Color.LightGray else Color.DarkGray,
//                        fontWeight = FontWeight.Medium,
//                        maxLines = 1
//                    )
//                    if (profile != null) {
//                        Text(
//                            profile!!.companyName,
//                            fontSize = 12.sp,
//                            color = if (isDark) Color.LightGray else Color.DarkGray
//                        )
//                    }
//                }
//
//                Spacer(Modifier.height(16.dp))
//
//                DrawerItemRecruiter(
//                    icon = Icons.Default.Home,
//                    text = "Dashboard",
//                    isDark = isDark,
//                    isSelected = selectedScreen == "dashboard"
//                ) {
//                    scope.launch { drawerState.close() }
//                    selectedScreen = "dashboard"
//                }
//
//                DrawerItemRecruiter(
//                    icon = Icons.Default.CloudUpload,
//                    text = "Upload Resumes",
//                    isDark = isDark,
//                    isSelected = selectedScreen == "upload"
//                ) {
//                    scope.launch { drawerState.close() }
//                    selectedScreen = "upload"
//                }
//
//                DrawerItemRecruiter(
//                    icon = Icons.Default.FilterList,
//                    text = "Filter Candidates",
//                    isDark = isDark,
//                    isSelected = selectedScreen == "filter"
//                ) {
//                    scope.launch { drawerState.close() }
//                    selectedScreen = "filter"
//                }
//
//                DrawerItemRecruiter(
//                    icon = Icons.Default.History,
//                    text = "History",
//                    isDark = isDark,
//                    isSelected = selectedScreen == "history"
//                ) {
//                    scope.launch { drawerState.close() }
//                    selectedScreen = "history"
//                }
//
//                Spacer(modifier = Modifier.weight(1f))
//
//                Divider(color = if (isDark) Color.Gray else Color.LightGray)
//
//                DrawerItemRecruiter(
//                    icon = Icons.Default.Settings,
//                    text = "Settings",
//                    isDark = isDark,
//                    isSelected = false
//                ) {
//                    scope.launch { drawerState.close() }
//                    navController.navigate("recruiter_settings")
//                }
//
//                DrawerItemRecruiter(
//                    icon = Icons.Default.Logout,
//                    text = "Logout",
//                    isDark = isDark,
//                    isSelected = false
//                ) {
//                    scope.launch {
//                        UserPreference.clearUser(context)
//                        drawerState.close()
//                    }
//                    navController.navigate("login") {
//                        popUpTo("recruiter") { inclusive = true }
//                    }
//                }
//
//                Spacer(Modifier.height(8.dp))
//            }
//        }
//    ) {
//        Scaffold(
//            topBar = {
//                TopAppBar(
//                    title = {
//                        Text(
//                            text = getRecruiterScreenTitle(selectedScreen),
//                            fontWeight = FontWeight.Bold
//                        )
//                    },
//                    navigationIcon = {
//                        IconButton(onClick = {
//                            scope.launch { drawerState.open() }
//                        }) {
//                            Icon(Icons.Default.Menu, contentDescription = "Menu")
//                        }
//                    },
//                    colors = TopAppBarDefaults.topAppBarColors(
//                        containerColor = if (isDark) Color(0xFF1a1a2e) else Color(0xFF4A90E2),
//                        titleContentColor = Color.White,
//                        navigationIconContentColor = Color.White
//                    )
//                )
//            }
//        ) { innerPadding ->
//            when (selectedScreen) {
//                "dashboard" -> RecruiterDashboardScreen(
//                    userEmail = userEmail,
//                    isDark = isDark,
//                    viewModel = resumeViewModel,
//                    onNavigateToFilter = { selectedScreen = "filter" },
//                    modifier = Modifier.padding(innerPadding)
//                )
//
//                "upload" -> ResumeUploadScreen(
//                    recruiterEmail = userEmail,
//                    isDark = isDark,
//                    viewModel = resumeViewModel,
//                    onUploadSuccess = { selectedScreen = "dashboard" },
//                    modifier = Modifier.padding(innerPadding)
//                )
//
//                "filter" -> ResumeFilterScreen(
//                    isDark = isDark,
//                    viewModel = resumeViewModel,
//                    modifier = Modifier.padding(innerPadding)
//                )
//
//                "history" -> RecruiterHistoryScreen(
//                    isDark = isDark,
//                    viewModel = resumeViewModel,
//                    modifier = Modifier.padding(innerPadding)
//                )
//            }
//        }
//    }
//}
//
//@Composable
//fun DrawerItemRecruiter(
//    icon: androidx.compose.ui.graphics.vector.ImageVector,
//    text: String,
//    isDark: Boolean,
//    isSelected: Boolean = false,
//    onClick: () -> Unit
//) {
//    NavigationDrawerItem(
//        label = {
//            Text(
//                text,
//                fontSize = 16.sp,
//                color = if (isDark) Color.White else Color.Black,
//                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
//            )
//        },
//        selected = isSelected,
//        onClick = onClick,
//        icon = {
//            Icon(
//                icon,
//                contentDescription = text,
//                tint = if (isSelected) {
//                    Color(0xFF4A90E2)
//                } else {
//                    if (isDark) Color.White else Color.Black
//                }
//            )
//        },
//        colors = NavigationDrawerItemDefaults.colors(
//            unselectedContainerColor = Color.Transparent,
//            selectedContainerColor = if (isDark) {
//                Color(0xFF4A90E2).copy(alpha = 0.3f)
//            } else {
//                Color(0xFF4A90E2).copy(alpha = 0.2f)
//            }
//        ),
//        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
//    )
//}
//
//fun getRecruiterScreenTitle(screen: String): String {
//    return when (screen) {
//        "dashboard" -> "Dashboard"
//        "upload" -> "Upload Resumes"
//        "filter" -> "Filter Candidates"
//        "history" -> "History"
//        else -> "Talent Hub"
//    }
//}


// ============================================
// FILE: RecruiterMainScreen.kt (FINAL BEAUTIFUL VERSION)
// ============================================
package com.example.feature_recruiter

import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.resumeanalyzer.core.navigation.datastore.UserPreference
import com.example.resumeanalyzer.core.navigation.datastore.UserCache
import com.example.feature_recruiter.database.RecruiterDatabase
import com.example.feature_recruiter.dialog.CompanySetupDialog
import com.example.feature_recruiter.repository.RecruiterProfileRepository
import com.example.feature_recruiter.repository.RecruiterResumeRepository
import com.example.feature_recruiter.screens.*
import com.example.feature_recruiter.util.DatabaseHelper
import com.example.feature_recruiter.viewmodel.RecruiterProfileViewModel
import com.example.feature_recruiter.viewmodel.RecruiterResumeViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RecruiterMainScreen(
    navController: NavController,
    onNavigate: (String) -> Unit
) {
    val context = LocalContext.current
    val user by UserPreference.getUser(context).collectAsState(initial = UserCache())
    val userEmail = user.email ?: "Recruiter"
    val scope = rememberCoroutineScope()

    // Initialize Database & ViewModels
    val db = remember { DatabaseHelper.getDatabase(context) }
    val resumeRepo = remember { RecruiterResumeRepository(db.recruiterResumeDao()) }
    val profileRepo = remember { RecruiterProfileRepository(db.recruiterProfileDao()) }
    val resumeViewModel = remember { RecruiterResumeViewModel(resumeRepo, userEmail) }
    val profileViewModel = remember { RecruiterProfileViewModel(profileRepo, userEmail) }

    // UI State
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    var selectedScreen by remember { mutableStateOf("dashboard") }
    var showSetupDialog by remember { mutableStateOf(false) }

    val isDark = when (user.theme) {
        "light" -> false
        "dark" -> true
        else -> isSystemInDarkTheme()
    }

    // Check profile setup
    val profile by profileViewModel.profile.collectAsState()
    LaunchedEffect(profile) {
        if (profile == null) {
            showSetupDialog = true
        }
    }

    // Show company setup dialog on first login
    if (showSetupDialog) {
        CompanySetupDialog(
            recruiterEmail = userEmail,
            onSetupComplete = { companyName, recruiterName ->
                scope.launch {
                    profileViewModel.createInitialProfile(companyName, recruiterName)
                    showSetupDialog = false
                }
            }
        )
    }

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet(
                modifier = Modifier
                    .fillMaxHeight()
                    .width(300.dp),
                drawerContainerColor = if (isDark) Color(0xFF121212) else Color.White
            ) {
                // Drawer Header
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            if (isDark)
                                Color(0xFF1E1E2E)
                            else
                                Color(0xFF4A90E2)
                        )
                        .padding(16.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(60.dp)
                                .clip(CircleShape)
                                .background(
                                    if (isDark)
                                        Color(0xFF4A90E2)
                                    else
                                        Color.White.copy(alpha = 0.3f)
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                userEmail.firstOrNull()?.uppercase() ?: "R",
                                color = if (isDark) Color.White else Color.White,
                                fontSize = 24.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                        Text(
                            userEmail,
                            fontSize = 12.sp,
                            color = if (isDark) Color.White else Color.White,
                            fontWeight = FontWeight.Medium
                        )
                        if (profile != null) {
                            Text(
                                profile!!.companyName,
                                fontSize = 11.sp,
                                color = if (isDark) Color.White.copy(0.7f) else Color.White.copy(0.8f)
                            )
                        }
                    }
                }

                Spacer(Modifier.height(16.dp))

                // Menu Items
                DrawerItemRecruiter(
                    icon = Icons.Default.Home,
                    text = "Dashboard",
                    isDark = isDark,
                    isSelected = selectedScreen == "dashboard"
                ) {
                    scope.launch { drawerState.close() }
                    selectedScreen = "dashboard"
                }

                DrawerItemRecruiter(
                    icon = Icons.Default.CloudUpload,
                    text = "Upload Resumes",
                    isDark = isDark,
                    isSelected = selectedScreen == "upload"
                ) {
                    scope.launch { drawerState.close() }
                    selectedScreen = "upload"
                }

                DrawerItemRecruiter(
                    icon = Icons.Default.FilterList,
                    text = "Filter Candidates",
                    isDark = isDark,
                    isSelected = selectedScreen == "filter"
                ) {
                    scope.launch { drawerState.close() }
                    selectedScreen = "filter"
                }

                DrawerItemRecruiter(
                    icon = Icons.Default.History,
                    text = "History",
                    isDark = isDark,
                    isSelected = selectedScreen == "history"
                ) {
                    scope.launch { drawerState.close() }
                    selectedScreen = "history"
                }

                Spacer(modifier = Modifier.weight(1f))

                Divider(color = if (isDark) Color.Gray else Color.LightGray)

                DrawerItemRecruiter(
                    icon = Icons.Default.Settings,
                    text = "Settings",
                    isDark = isDark,
                    isSelected = false
                ) {
                    scope.launch { drawerState.close() }
                    navController.navigate("recruiter_settings")
                }

                DrawerItemRecruiter(
                    icon = Icons.Default.Logout,
                    text = "Logout",
                    isDark = isDark,
                    isSelected = false
                ) {
                    scope.launch {
                        UserPreference.clearUser(context)
                        drawerState.close()
                    }
                    navController.navigate("login") {
                        popUpTo("recruiter") { inclusive = true }
                    }
                }

                Spacer(Modifier.height(8.dp))
            }
        }
    ) {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = {
                        Text(
                            text = getRecruiterScreenTitle(selectedScreen),
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    },
                    navigationIcon = {
                        IconButton(onClick = {
                            scope.launch { drawerState.open() }
                        }) {
                            Icon(
                                Icons.Default.Menu,
                                contentDescription = "Menu",
                                tint = Color.White
                            )
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = Color(0xFF4A90E2),
                        titleContentColor = Color.White,
                        navigationIconContentColor = Color.White
                    ),
                    // ✅ FIX: Use modifier.shadow() instead
                    modifier = Modifier.shadow(elevation = 8.dp)
                )
            }
        ) { innerPadding ->
            when (selectedScreen) {
                "dashboard" -> RecruiterDashboardScreen(
                    userEmail = userEmail,
                    isDark = isDark,
                    viewModel = resumeViewModel,
                    onNavigateToFilter = { selectedScreen = "filter" },
                    modifier = Modifier.padding(innerPadding)
                )

                "upload" -> ResumeUploadScreen(
                    recruiterEmail = userEmail,
                    isDark = isDark,
                    viewModel = resumeViewModel,
                    onUploadSuccess = { selectedScreen = "dashboard" },
                    modifier = Modifier.padding(innerPadding)
                )

                "filter" -> ResumeFilterScreen(
                    isDark = isDark,
                    viewModel = resumeViewModel,
                    modifier = Modifier.padding(innerPadding)
                )

                "history" -> RecruiterHistoryScreen(
                    isDark = isDark,
                    viewModel = resumeViewModel,
                    modifier = Modifier.padding(innerPadding)
                )
            }
        }
    }
}

@Composable
fun DrawerItemRecruiter(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    text: String,
    isDark: Boolean,
    isSelected: Boolean = false,
    onClick: () -> Unit
) {
    NavigationDrawerItem(
        label = {
            Text(
                text,
                fontSize = 14.sp,
                color = if (isDark) Color.White else Color.Black,
                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
            )
        },
        selected = isSelected,
        onClick = onClick,
        icon = {
            Icon(
                icon,
                contentDescription = text,
                tint = if (isSelected) {
                    Color(0xFF4A90E2)
                } else {
                    if (isDark) Color.White else Color.Black
                }
            )
        },
        colors = NavigationDrawerItemDefaults.colors(
            unselectedContainerColor = Color.Transparent,
            selectedContainerColor = if (isDark) {
                Color(0xFF4A90E2).copy(alpha = 0.3f)
            } else {
                Color(0xFF4A90E2).copy(alpha = 0.15f)
            }
        ),
        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
    )
}

fun getRecruiterScreenTitle(screen: String): String {
    return when (screen) {
        "dashboard" -> "Dashboard"
        "upload" -> "Upload Resumes"
        "filter" -> "Filter Candidates"
        "history" -> "History"
        else -> "Talent Hub"
    }
}