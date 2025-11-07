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
//import androidx.compose.ui.draw.shadow
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
//import com.example.feature_recruiter.repository.SearchHistoryRepository
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
//    val db = remember(context) {
//        DatabaseHelper.getDatabase(context)
//    }
//
//    val resumeRepo = remember(db) {
//        RecruiterResumeRepository(db.recruiterResumeDao())
//    }
//    val profileRepo = remember(db) {
//        RecruiterProfileRepository(db.recruiterProfileDao())
//    }
//    val searchHistoryRepo = remember(db) {
//        SearchHistoryRepository(db.searchHistoryDao())
//    }
//
//    val resumeViewModel = remember(resumeRepo, searchHistoryRepo, userEmail) {
//        RecruiterResumeViewModel(resumeRepo, userEmail, searchHistoryRepo)
//    }
//    val profileViewModel = remember(profileRepo, userEmail) {
//        RecruiterProfileViewModel(profileRepo, userEmail)
//    }
//
//    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
//    var selectedScreen by remember { mutableStateOf("dashboard") }
//    var currentTheme by remember { mutableStateOf(user.theme) }
//
//    val isDark = when (currentTheme) {
//        "light" -> false
//        "dark" -> true
//        else -> isSystemInDarkTheme()
//    }
//
//    val profile by profileViewModel.profile.collectAsState()
//    val showSetupDialog by profileViewModel.showSetupDialog.collectAsState()
//    val isLoading by profileViewModel.isLoading.collectAsState()
//
//    // ✅ SHOW SETUP DIALOG IF NEEDED
//    if (showSetupDialog) {
//        CompanySetupDialog(
//            recruiterEmail = userEmail,
//            onSetupComplete = { companyName, recruiterName ->
//                scope.launch {
//                    profileViewModel.createInitialProfile(companyName, recruiterName)
//                }
//            },
//            isLoading = isLoading
//        )
//    }
//
//    ModalNavigationDrawer(
//        drawerState = drawerState,
//        drawerContent = {
//            ModalDrawerSheet(
//                modifier = Modifier
//                    .fillMaxHeight()
//                    .width(300.dp),
//                drawerContainerColor = if (isDark) Color(0xFF121212) else Color.White
//            ) {
//                Box(
//                    modifier = Modifier
//                        .fillMaxWidth()
//                        .background(
//                            if (isDark) Color(0xFF1E1E2E) else Color(0xFF4A90E2)
//                        )
//                        .padding(16.dp),
//                    contentAlignment = Alignment.Center
//                ) {
//                    Column(
//                        horizontalAlignment = Alignment.CenterHorizontally,
//                        verticalArrangement = Arrangement.spacedBy(8.dp)
//                    ) {
//                        Box(
//                            modifier = Modifier
//                                .size(60.dp)
//                                .clip(CircleShape)
//                                .background(
//                                    if (isDark) Color(0xFF4A90E2)
//                                    else Color.White.copy(alpha = 0.3f)
//                                ),
//                            contentAlignment = Alignment.Center
//                        ) {
//                            Text(
//                                userEmail.firstOrNull()?.uppercase() ?: "R",
//                                color = Color.White,
//                                fontSize = 24.sp,
//                                fontWeight = FontWeight.Bold
//                            )
//                        }
//                        Text(
//                            userEmail,
//                            fontSize = 12.sp,
//                            color = Color.White,
//                            fontWeight = FontWeight.Medium
//                        )
//                        if (profile != null) {
//                            Text(
//                                profile!!.companyName,
//                                fontSize = 11.sp,
//                                color = Color.White.copy(0.7f)
//                            )
//                        }
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
//                    isSelected = selectedScreen == "settings"
//                ) {
//                    scope.launch { drawerState.close() }
//                    selectedScreen = "settings"
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
//                            fontWeight = FontWeight.Bold,
//                            color = Color.White
//                        )
//                    },
//                    navigationIcon = {
//                        IconButton(onClick = {
//                            scope.launch { drawerState.open() }
//                        }) {
//                            Icon(
//                                Icons.Default.Menu,
//                                contentDescription = "Menu",
//                                tint = Color.White
//                            )
//                        }
//                    },
//                    colors = TopAppBarDefaults.topAppBarColors(
//                        containerColor = Color(0xFF4A90E2),
//                        titleContentColor = Color.White,
//                        navigationIconContentColor = Color.White
//                    ),
//                    modifier = Modifier.shadow(elevation = 8.dp)
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
//                "filter" -> ResumeFilterScreenWithUpload(
//                    isDark = isDark,
//                    viewModel = resumeViewModel,
//                    recruiterEmail = userEmail,
//                    modifier = Modifier.padding(innerPadding)
//                )
//
//                "history" -> RecruiterHistoryScreen(
//                    isDark = isDark,
//                    viewModel = resumeViewModel,
//                    modifier = Modifier.padding(innerPadding)
//                )
//
//                "settings" -> RecruiterSettingsScreen(
//                    userEmail = userEmail,
//                    isDark = isDark,
//                    profileViewModel = profileViewModel,
//                    onThemeChange = { newTheme ->
//                        currentTheme = newTheme
//                        scope.launch {
//                            UserPreference.saveTheme(context, newTheme)
//                        }
//                    },
//                    onBack = { selectedScreen = "dashboard" }
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
//                fontSize = 14.sp,
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
//                tint = if (isSelected) Color(0xFF4A90E2)
//                else if (isDark) Color.White else Color.Black
//            )
//        },
//        colors = NavigationDrawerItemDefaults.colors(
//            unselectedContainerColor = Color.Transparent,
//            selectedContainerColor = if (isDark)
//                Color(0xFF4A90E2).copy(alpha = 0.3f)
//            else Color(0xFF4A90E2).copy(alpha = 0.15f)
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
//        "settings" -> "Settings"
//        else -> "Talent Hub"
//    }
//}


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
import com.example.feature_recruiter.repository.SearchHistoryRepository
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

    val db = remember(context) {
        DatabaseHelper.getDatabase(context)
    }

    val resumeRepo = remember(db) {
        RecruiterResumeRepository(db.recruiterResumeDao())
    }
    val profileRepo = remember(db) {
        RecruiterProfileRepository(db.recruiterProfileDao())
    }
    val searchHistoryRepo = remember(db) {
        SearchHistoryRepository(db.searchHistoryDao())
    }

    val resumeViewModel = remember(resumeRepo, searchHistoryRepo, userEmail) {
        RecruiterResumeViewModel(resumeRepo, userEmail, searchHistoryRepo)
    }
    val profileViewModel = remember(profileRepo, userEmail) {
        RecruiterProfileViewModel(profileRepo, userEmail)
    }

    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    var selectedScreen by remember { mutableStateOf("dashboard") }
    var currentTheme by remember { mutableStateOf(user.theme) }

    val isDark = when (currentTheme) {
        "light" -> false
        "dark" -> true
        else -> isSystemInDarkTheme()
    }

    val profile by profileViewModel.profile.collectAsState()
    val showSetupDialog by profileViewModel.showSetupDialog.collectAsState()
    val isLoading by profileViewModel.isLoading.collectAsState()

    // ✅ SHOW SETUP DIALOG IF NEEDED
    if (showSetupDialog) {
        CompanySetupDialog(
            recruiterEmail = userEmail,
            onSetupComplete = { companyName, recruiterName ->
                scope.launch {
                    profileViewModel.createInitialProfile(companyName, recruiterName)
                }
            },
            isLoading = isLoading
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
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            if (isDark) Color(0xFF1E1E2E) else Color(0xFF4A90E2)
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
                                    if (isDark) Color(0xFF4A90E2)
                                    else Color.White.copy(alpha = 0.3f)
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                userEmail.firstOrNull()?.uppercase() ?: "R",
                                color = Color.White,
                                fontSize = 24.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                        Text(
                            userEmail,
                            fontSize = 12.sp,
                            color = Color.White,
                            fontWeight = FontWeight.Medium
                        )
                        if (profile != null) {
                            Text(
                                profile!!.companyName,
                                fontSize = 11.sp,
                                color = Color.White.copy(0.7f)
                            )
                        }
                    }
                }

                Spacer(Modifier.height(16.dp))

                DrawerItemRecruiter(
                    icon = Icons.Default.Home,
                    text = "Dashboard",
                    isDark = isDark,
                    isSelected = selectedScreen == "dashboard"
                ) {
                    scope.launch { drawerState.close() }
                    selectedScreen = "dashboard"
                }

                // ✅ RENAMED: "Upload Resumes" → "Filter & Upload"
                DrawerItemRecruiter(
                    icon = Icons.Default.FilterList,
                    text = "Filter & Upload",
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
                    isSelected = selectedScreen == "settings"
                ) {
                    scope.launch { drawerState.close() }
                    selectedScreen = "settings"
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

                // ❌ REMOVED: "upload" screen - now handled in "filter"

                "filter" -> ResumeFilterScreenWithUpload(
                    isDark = isDark,
                    viewModel = resumeViewModel,
                    recruiterEmail = userEmail,
                    modifier = Modifier.padding(innerPadding)
                )

                "history" -> RecruiterHistoryScreen(
                    isDark = isDark,
                    viewModel = resumeViewModel,
                    modifier = Modifier.padding(innerPadding)
                )

                "settings" -> RecruiterSettingsScreen(
                    userEmail = userEmail,
                    isDark = isDark,
                    profileViewModel = profileViewModel,
                    onThemeChange = { newTheme ->
                        currentTheme = newTheme
                        scope.launch {
                            UserPreference.saveTheme(context, newTheme)
                        }
                    },
                    onBack = { selectedScreen = "dashboard" }
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
                tint = if (isSelected) Color(0xFF4A90E2)
                else if (isDark) Color.White else Color.Black
            )
        },
        colors = NavigationDrawerItemDefaults.colors(
            unselectedContainerColor = Color.Transparent,
            selectedContainerColor = if (isDark)
                Color(0xFF4A90E2).copy(alpha = 0.3f)
            else Color(0xFF4A90E2).copy(alpha = 0.15f)
        ),
        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
    )
}

fun getRecruiterScreenTitle(screen: String): String {
    return when (screen) {
        "dashboard" -> "Dashboard"
        "filter" -> "Filter & Upload"  // ✅ Updated
        "history" -> "History"
        "settings" -> "Settings"
        else -> "Talent Hub"
    }
}