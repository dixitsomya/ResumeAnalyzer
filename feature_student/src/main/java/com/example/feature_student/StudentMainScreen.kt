//
//package com.example.feature_student
//
//import androidx.compose.foundation.background
//import androidx.compose.foundation.isSystemInDarkTheme
//import androidx.compose.foundation.layout.*
//import androidx.compose.foundation.lazy.LazyColumn
//import androidx.compose.foundation.shape.CircleShape
//import androidx.compose.foundation.shape.RoundedCornerShape
//import androidx.compose.material.icons.Icons
//import androidx.compose.material.icons.filled.*
//import androidx.compose.material3.*
//import androidx.compose.runtime.*
//import androidx.compose.ui.Alignment
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.draw.clip
//import androidx.compose.ui.graphics.Brush
//import androidx.compose.ui.graphics.Color
//import androidx.compose.ui.platform.LocalContext
//import androidx.compose.ui.text.font.FontWeight
//import androidx.compose.ui.unit.dp
//import androidx.compose.ui.unit.sp
//import kotlinx.coroutines.launch
//import androidx.navigation.NavController
//import com.example.resumeanalyzer.core.navigation.datastore.UserCache
//import com.example.resumeanalyzer.core.navigation.datastore.UserPreference
//
//@OptIn(ExperimentalMaterial3Api::class)
//@Composable
//fun StudentMainScreen(
//    navController: NavController,
//    onNavigate: (String) -> Unit
//) {
//    val context = LocalContext.current
//    val user by UserPreference.getUser(context).collectAsState(initial = UserCache())
//    val userEmail = user.email ?: "Guest" // fallback
//    val userName = user.name ?: "User"
//    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
//    val scope = rememberCoroutineScope()
//    var showLogoutMenu by remember { mutableStateOf(false) }
//    var selectedScreen by remember { mutableStateOf("home") }
//
//
//    // Get the current theme from user preferences
//    val isDark = when (user.theme) {
//        "light" -> false
//        "dark" -> true
//        else -> isSystemInDarkTheme()
//    }
//
//    ModalNavigationDrawer(
//        drawerState = drawerState,
//        drawerContent = {
//            ModalDrawerSheet(
//                modifier = Modifier
//                    .fillMaxHeight()
//                    .width(320.dp),
//                // Use the theme-aware isDark variable
//                drawerContainerColor = if (isDark) Color(0xFF121212) else Color(0xFFF5F6FA)
//            ) {
//                // 🔹 Top User Section
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
//                            .background(Color(0xFF6C63FF)),
//                        contentAlignment = Alignment.Center
//                    ) {
//                        Text(
//                            userEmail.firstOrNull()?.uppercase() ?: "?",
//                            color = Color.White,
//                            fontSize = 28.sp
//                        )
//                    }
//                    Spacer(modifier = Modifier.height(8.dp))
//                    Text(
//                        userEmail,
//                        fontSize = 14.sp,
//                        color = if (isDark) Color.LightGray else Color.DarkGray
//                    )
//                }
//
//                Spacer(Modifier.height(16.dp))
//
//                // 🔹 Drawer Items - Pass isDark to DrawerItem
//                DrawerItem(Icons.Default.Home, "Home", isDark,isSelected = selectedScreen == "home") {
//                    scope.launch { drawerState.close() }
//                    selectedScreen = "home"
//                }
//                DrawerItem(Icons.Default.UploadFile, "Upload Resume", isDark,isSelected = selectedScreen == "upload") {
//                    scope.launch { drawerState.close() }
//                    selectedScreen = "upload"
//                }
//                DrawerItem(Icons.Default.BarChart, "ATS Score", isDark,isSelected = selectedScreen == "ats") {
//                    scope.launch { drawerState.close() }
//                    selectedScreen = "ats"
//                }
//                DrawerItem(Icons.Default.TipsAndUpdates, "Suggestions", isDark,isSelected = selectedScreen == "suggestions") {
//                    scope.launch { drawerState.close() }
//                    selectedScreen = "suggestions"
//                }
//                DrawerItem(Icons.Default.History, "History", isDark,isSelected = selectedScreen == "history") {
//                    scope.launch { drawerState.close() }
//                    selectedScreen = "history"
//                }
//
//                Spacer(modifier = Modifier.weight(1f))
//
//                Divider(color = if (isDark) Color.Gray else Color.LightGray)
//
//                // 🔹 Bottom: Settings + Logout
//                DrawerItem(Icons.Default.Settings, "Settings", isDark,isSelected = false) {
//                    scope.launch { drawerState.close() }
//                    navController.navigate("settings")
//                }
//
//                DrawerItem(Icons.Default.Logout, "Logout", isDark,isSelected = false) {
//                    scope.launch {
//                        UserPreference.clearUser(context)
//                    }
//                    navController.navigate("login") {
//                        popUpTo("studentMain") { inclusive = true }
//                    }
//                }
//            }
//        }
//    ) {
//        Scaffold(
//            topBar = {
//                TopAppBar(
//                    //title = { Text("Resume Analyser") },
//                    title = {
//                        Text(getScreenTitle(selectedScreen))
//                    },
//                    navigationIcon = {
//                        IconButton(onClick = {
//                            scope.launch { drawerState.open() }
//                        }) {
//                            Icon(Icons.Default.Menu, contentDescription = "Menu")
//                        }
//                    }
//                )
//            }
//        ) { innerPadding ->
//            when (selectedScreen) {
//                "home" -> HomeDashboardContent(
//                    userEmail = userEmail,
//                    userName = userName,
//                    theme = user.theme,
//                    modifier = Modifier.padding(innerPadding),
//                    onNavigate = { screen -> selectedScreen = screen }
//                )
////                "upload" -> Text("Upload Resume Screen Coming Soon", modifier = Modifier.padding(innerPadding))
////                "ats" -> Text("ATS Score Screen Coming Soon", modifier = Modifier.padding(innerPadding))
////                "suggestions" -> Text("Suggestions Screen Coming Soon", modifier = Modifier.padding(innerPadding))
////                "history" -> Text("History Screen Coming Soon", modifier = Modifier.padding(innerPadding))
//                "upload" -> PlaceholderScreen(
//                    title = "Upload Resume",
//                    description = "Upload your resume for analysis",
//                    modifier = Modifier.padding(innerPadding),
//                    isDark = isDark
//                )
//                "ats" -> PlaceholderScreen(
//                    title = "ATS Score",
//                    description = "View your resume's ATS compatibility score",
//                    modifier = Modifier.padding(innerPadding),
//                    isDark = isDark
//                )
//                "suggestions" -> PlaceholderScreen(
//                    title = "Suggestions",
//                    description = "Get personalized suggestions to improve your resume",
//                    modifier = Modifier.padding(innerPadding),
//                    isDark = isDark
//                )
//                "history" -> PlaceholderScreen(
//                    title = "History",
//                    description = "View your past resume analysis history",
//                    modifier = Modifier.padding(innerPadding),
//                    isDark = isDark
//                )
//            }
//        }
//    }
//}
//
//@Composable
//fun DrawerItem(
//    icon: androidx.compose.ui.graphics.vector.ImageVector,
//    text: String,
//    isDark: Boolean, // Add isDark parameter
//    isSelected: Boolean = false,
//    onClick: () -> Unit
//) {
//    NavigationDrawerItem(
//        label = {
//            Text(
//                text,
//                fontSize = 16.sp,
//                color = if (isDark) Color.White else Color.Black
//            )
//        },
//        selected = isSelected,
//        onClick = onClick,
//        icon = {
//            Icon(
//                icon,
//                contentDescription = text,
//                tint = if (isDark) Color.White else Color.Black
//            )
//        },
//        colors = NavigationDrawerItemDefaults.colors(
//            unselectedContainerColor = Color.Transparent,
//            selectedContainerColor = if (isDark) Color(0xFF6C63FF).copy(alpha = 0.3f) else Color(0xFF6C63FF).copy(alpha = 0.2f)
//        ),
//        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
//    )
//}
//
//@Composable
//fun PlaceholderScreen(
//    title: String,
//    description: String,
//    modifier: Modifier = Modifier,
//    isDark: Boolean
//) {
//    val colorScheme = if (isDark) darkColorScheme() else lightColorScheme()
//
//    Column(
//        modifier = modifier
//            .fillMaxSize()
//            .padding(24.dp),
//        verticalArrangement = Arrangement.Center,
//        horizontalAlignment = Alignment.CenterHorizontally
//    ) {
//        Icon(
//            Icons.Default.Construction,
//            contentDescription = null,
//            modifier = Modifier.size(64.dp),
//            tint = colorScheme.primary
//        )
//        Spacer(Modifier.height(16.dp))
//        Text(
//            title,
//            style = MaterialTheme.typography.headlineMedium,
//            color = colorScheme.onBackground
//        )
//        Spacer(Modifier.height(8.dp))
//        Text(
//            description,
//            style = MaterialTheme.typography.bodyLarge,
//            color = colorScheme.onSurfaceVariant,
//            textAlign = androidx.compose.ui.text.style.TextAlign.Center
//        )
//        Spacer(Modifier.height(24.dp))
//        Text(
//            "Coming Soon...",
//            style = MaterialTheme.typography.bodyMedium,
//            color = colorScheme.primary
//        )
//    }
//}
//
//fun getScreenTitle(screen: String): String {
//    return when (screen) {
//        "home" -> "Home"
//        "upload" -> "Upload Resume"
//        "ats" -> "ATS Score"
//        "suggestions" -> "Suggestions"
//        "history" -> "History"
//        else -> "Resume Analyser"
//    }
//}
//
//
//@Composable
//fun HomeDashboardContent(
//    userEmail: String,
//    userName: String,
//    theme: String = "system",
//    modifier: Modifier = Modifier,
//    onNavigate: (String) -> Unit
//) {
//    val isDark = when (theme) {
//        "light" -> false
//        "dark" -> true
//        else -> isSystemInDarkTheme()
//    }
//
//    val backgroundBrush = if (!isDark) {
//        Brush.verticalGradient(
//            listOf(Color(0xFF667eea), Color(0xFF764ba2))
//        )
//    } else {
//        Brush.verticalGradient(
//            listOf(Color(0xFF2C1810), Color(0xFF5D4E75))
//        )
//    }
//
//    val colorScheme = if (isDark) darkColorScheme() else lightColorScheme()
//
//    LazyColumn(
//        modifier = modifier
//            .fillMaxSize()
//            .background(backgroundBrush)
//            .padding(20.dp),
//        verticalArrangement = Arrangement.spacedBy(24.dp)
//    ) {
//        item {
//            // Enhanced Welcome Section
//            Card(
//                modifier = Modifier.fillMaxWidth(),
//                shape = RoundedCornerShape(20.dp),
//                colors = CardDefaults.cardColors(
//                    containerColor = Color.White.copy(alpha = if (isDark) 0.1f else 0.95f)
//                ),
//                elevation = CardDefaults.cardElevation(8.dp)
//            ) {
//                Row(
//                    modifier = Modifier
//                        .fillMaxWidth()
//                        .padding(20.dp),
//                    verticalAlignment = Alignment.CenterVertically
//                ) {
//                    Box(
//                        modifier = Modifier
//                            .size(60.dp)
//                            .clip(CircleShape)
//                            .background(
//                                Brush.radialGradient(
//                                    listOf(Color(0xFFff7e5f), Color(0xFFfeb47b))
////                                    listOf(Color(0xFF667eea), Color(0xFF764ba2))
//                                )
//                            ),
//                        contentAlignment = Alignment.Center
//                    ) {
//                        Text(
//                            userEmail.firstOrNull()?.uppercase() ?: "?",
//                            color = Color.White,
//                            fontSize = 24.sp,
//                            fontWeight = FontWeight.Bold
//                        )
//                    }
//                    Spacer(Modifier.width(16.dp))
//                    Column {
//                        Text(
//                            text = "Welcome back!",
//                            fontSize = 16.sp,
//                            color = if (isDark) Color.White.copy(0.7f) else Color.Gray,
//                            fontWeight = FontWeight.Medium
//                        )
//                        Text(
//                            //text = userEmail.substringBefore('@'),
//                            text = userName.ifEmpty { userEmail.substringBefore('@') },
//                            fontSize = 24.sp,
//                            fontWeight = FontWeight.Bold,
//                            color = if (isDark) Color.White else Color.Black
//                        )
//                    }
//                }
//            }
//        }
//
//        item {
//            // Enhanced Quick Actions
//            Text(
//                "Quick Actions",
//                fontSize = 22.sp,
//                fontWeight = FontWeight.Bold,
//                color = Color.White,
//                modifier = Modifier.padding(horizontal = 8.dp)
//            )
//
//            Spacer(Modifier.height(12.dp))
//
//            Row(
//                modifier = Modifier.fillMaxWidth(),
//                horizontalArrangement = Arrangement.spacedBy(12.dp)
//            ) {
//                EnhancedQuickActionCard(
//                    title = "Upload\nResume",
//                    icon = Icons.Default.UploadFile,
//                    gradient = listOf(Color(0xFF667eea), Color(0xFF764ba2)),
//                    modifier = Modifier.weight(1f)
//                ) { onNavigate("upload") }
//
//                EnhancedQuickActionCard(
//                    title = "ATS\nScore",
//                    icon = Icons.Default.BarChart,
//                    gradient = listOf(Color(0xFFf093fb), Color(0xFFf5576c)),
//                    modifier = Modifier.weight(1f)
//                ) { onNavigate("ats") }
//
//                EnhancedQuickActionCard(
//                    title = "Get\nSuggestions",
//                    icon = Icons.Default.TipsAndUpdates,
//                    gradient = listOf(Color(0xFF4facfe), Color(0xFF00f2fe)),
//                    modifier = Modifier.weight(1f)
//                ) { onNavigate("suggestions") }
//            }
//        }
//
//        item {
//            // Enhanced Stats Section
//            Row(
//                modifier = Modifier.fillMaxWidth(),
//                horizontalArrangement = Arrangement.spacedBy(12.dp)
//            ) {
//                StatsCard(
//                    title = "Resumes Analyzed",
//                    value = "0",
//                    icon = Icons.Default.Description,
//                    color = Color(0xFF6C63FF),
//                    isDark = isDark,
//                    modifier = Modifier.weight(1f)
//                )
//
//                StatsCard(
//                    title = "Average Score",
//                    value = "N/A",
//                    icon = Icons.Default.Assessment,
//                    color = Color(0xFFE91E63),
//                    isDark = isDark,
//                    modifier = Modifier.weight(1f)
//                )
//            }
//        }
//
//        item {
//            // Enhanced Recent Activity
//            Card(
//                modifier = Modifier.fillMaxWidth(),
//                shape = RoundedCornerShape(20.dp),
//                colors = CardDefaults.cardColors(
//                    containerColor = Color.White.copy(alpha = if (isDark) 0.1f else 0.95f)
//                ),
//                elevation = CardDefaults.cardElevation(8.dp)
//            ) {
//                Column(
//                    modifier = Modifier
//                        .fillMaxWidth()
//                        .padding(20.dp)
//                ) {
//                    Row(
//                        verticalAlignment = Alignment.CenterVertically
//                    ) {
//                        Icon(
//                            Icons.Default.History,
//                            contentDescription = null,
//                            tint = Color(0xFF6C63FF),
//                            modifier = Modifier.size(24.dp)
//                        )
//                        Spacer(Modifier.width(8.dp))
//                        Text(
//                            "Recent Activity",
//                            fontSize = 20.sp,
//                            fontWeight = FontWeight.Bold,
//                            color = if (isDark) Color.White else Color.Black
//                        )
//                    }
//
//                    Spacer(Modifier.height(16.dp))
//
//                    Box(
//                        modifier = Modifier
//                            .fillMaxWidth()
//                            .height(120.dp),
//                        contentAlignment = Alignment.Center
//                    ) {
//                        Column(
//                            horizontalAlignment = Alignment.CenterHorizontally
//                        ) {
//                            Icon(
//                                Icons.Default.CloudOff,
//                                contentDescription = null,
//                                modifier = Modifier.size(48.dp),
//                                tint = if (isDark) Color.White.copy(0.4f) else Color.Gray
//                            )
//                            Spacer(Modifier.height(8.dp))
//                            Text(
//                                "No activity yet",
//                                color = if (isDark) Color.White.copy(0.6f) else Color.Gray,
//                                fontSize = 16.sp
//                            )
//                            Text(
//                                "Upload a resume to get started",
//                                color = if (isDark) Color.White.copy(0.4f) else Color.Gray.copy(0.7f),
//                                fontSize = 14.sp
//                            )
//                        }
//                    }
//                }
//            }
//        }
//    }
//}
//
//@Composable
//fun EnhancedQuickActionCard(
//    title: String,
//    icon: androidx.compose.ui.graphics.vector.ImageVector,
//    gradient: List<Color>,
//    modifier: Modifier = Modifier,
//    onClick: () -> Unit
//) {
//    Card(
//        modifier = modifier
//            .height(120.dp)
//            .clip(RoundedCornerShape(20.dp)),
//        onClick = onClick,
//        elevation = CardDefaults.cardElevation(12.dp)
//    ) {
//        Box(
//            modifier = Modifier
//                .fillMaxSize()
//                .background(Brush.linearGradient(gradient))
//                .padding(16.dp),
//            contentAlignment = Alignment.Center
//        ) {
//            Column(
//                horizontalAlignment = Alignment.CenterHorizontally
//            ) {
//                Icon(
//                    icon,
//                    contentDescription = title,
//                    tint = Color.White,
//                    modifier = Modifier.size(32.dp)
//                )
//                Spacer(Modifier.height(8.dp))
//                Text(
//                    title,
//                    color = Color.White,
//                    fontSize = 12.sp,
//                    fontWeight = FontWeight.Bold,
//                    textAlign = androidx.compose.ui.text.style.TextAlign.Center,
//                    lineHeight = 14.sp
//                )
//            }
//        }
//    }
//}
//
//@Composable
//fun StatsCard(
//    title: String,
//    value: String,
//    icon: androidx.compose.ui.graphics.vector.ImageVector,
//    color: Color,
//    isDark: Boolean,
//    modifier: Modifier = Modifier
//) {
//    Card(
//        modifier = modifier.height(100.dp),
//        shape = RoundedCornerShape(16.dp),
//        colors = CardDefaults.cardColors(
//            containerColor = Color.White.copy(alpha = if (isDark) 0.1f else 0.95f)
//        ),
//        elevation = CardDefaults.cardElevation(8.dp)
//    ) {
//        Column(
//            modifier = Modifier
//                .fillMaxSize()
//                .padding(16.dp),
//            verticalArrangement = Arrangement.SpaceBetween
//        ) {
//            Row(
//                modifier = Modifier.fillMaxWidth(),
//                horizontalArrangement = Arrangement.SpaceBetween,
//                verticalAlignment = Alignment.CenterVertically
//            ) {
//                Icon(
//                    icon,
//                    contentDescription = null,
//                    tint = color,
//                    modifier = Modifier.size(24.dp)
//                )
//            }
//
//            Column {
//                Text(
//                    value,
//                    fontSize = 24.sp,
//                    fontWeight = FontWeight.Bold,
//                    color = if (isDark) Color.White else Color.Black
//                )
//                Text(
//                    title,
//                    fontSize = 12.sp,
//                    color = if (isDark) Color.White.copy(0.7f) else Color.Gray,
//                    maxLines = 2
//                )
//            }
//        }
//    }
//}





//package com.example.feature_student
//
//import androidx.compose.foundation.background
//import androidx.compose.foundation.isSystemInDarkTheme
//import androidx.compose.foundation.layout.*
//import androidx.compose.foundation.lazy.LazyColumn
//import androidx.compose.foundation.shape.CircleShape
//import androidx.compose.foundation.shape.RoundedCornerShape
//import androidx.compose.material.icons.Icons
//import androidx.compose.material.icons.filled.*
//import androidx.compose.material3.*
//import androidx.compose.runtime.*
//import androidx.compose.ui.Alignment
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.draw.clip
//import androidx.compose.ui.graphics.Brush
//import androidx.compose.ui.graphics.Color
//import androidx.compose.ui.platform.LocalContext
//import androidx.compose.ui.text.font.FontWeight
//import androidx.compose.ui.unit.dp
//import androidx.compose.ui.unit.sp
//import kotlinx.coroutines.launch
//import androidx.navigation.NavController
//import com.example.resumeanalyzer.core.navigation.datastore.UserCache
//import com.example.resumeanalyzer.core.navigation.datastore.UserPreference
//import com.example.feature_student.upload.UploadResumeScreen
//
//@OptIn(ExperimentalMaterial3Api::class)
//@Composable
//fun StudentMainScreen(
//    navController: NavController,
//    onNavigate: (String) -> Unit
//) {
//    val context = LocalContext.current
//    val user by UserPreference.getUser(context).collectAsState(initial = UserCache())
//    val userEmail = user.email ?: "Guest"
//    val userName = user.name ?: "User"
//    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
//    val scope = rememberCoroutineScope()
//    var showLogoutMenu by remember { mutableStateOf(false) }
//    var selectedScreen by remember { mutableStateOf("home") }
//
//    val isDark = when (user.theme) {
//        "light" -> false
//        "dark" -> true
//        else -> isSystemInDarkTheme()
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
//                            .background(Color(0xFF6C63FF)),
//                        contentAlignment = Alignment.Center
//                    ) {
//                        Text(
//                            userEmail.firstOrNull()?.uppercase() ?: "?",
//                            color = Color.White,
//                            fontSize = 28.sp
//                        )
//                    }
//                    Spacer(modifier = Modifier.height(8.dp))
//                    Text(
//                        userEmail,
//                        fontSize = 14.sp,
//                        color = if (isDark) Color.LightGray else Color.DarkGray
//                    )
//                }
//
//                Spacer(Modifier.height(16.dp))
//
//                DrawerItem(Icons.Default.Home, "Home", isDark, isSelected = selectedScreen == "home") {
//                    scope.launch { drawerState.close() }
//                    selectedScreen = "home"
//                }
//                DrawerItem(Icons.Default.UploadFile, "Upload Resume", isDark, isSelected = selectedScreen == "upload") {
//                    scope.launch { drawerState.close() }
//                    selectedScreen = "upload"
//                }
//                DrawerItem(Icons.Default.BarChart, "ATS Score", isDark, isSelected = selectedScreen == "ats") {
//                    scope.launch { drawerState.close() }
//                    selectedScreen = "ats"
//                }
//                DrawerItem(Icons.Default.TipsAndUpdates, "Suggestions", isDark, isSelected = selectedScreen == "suggestions") {
//                    scope.launch { drawerState.close() }
//                    selectedScreen = "suggestions"
//                }
//                DrawerItem(Icons.Default.History, "History", isDark, isSelected = selectedScreen == "history") {
//                    scope.launch { drawerState.close() }
//                    selectedScreen = "history"
//                }
//
//                Spacer(modifier = Modifier.weight(1f))
//
//                Divider(color = if (isDark) Color.Gray else Color.LightGray)
//
//                DrawerItem(Icons.Default.Settings, "Settings", isDark, isSelected = false) {
//                    scope.launch { drawerState.close() }
//                    navController.navigate("settings")
//                }
//
//                DrawerItem(Icons.Default.Logout, "Logout", isDark, isSelected = false) {
//                    scope.launch {
//                        UserPreference.clearUser(context)
//                    }
//                    navController.navigate("login") {
//                        popUpTo("studentMain") { inclusive = true }
//                    }
//                }
//            }
//        }
//    ) {
//        Scaffold(
//            topBar = {
//                TopAppBar(
//                    title = {
//                        Text(getScreenTitle(selectedScreen))
//                    },
//                    navigationIcon = {
//                        IconButton(onClick = {
//                            scope.launch { drawerState.open() }
//                        }) {
//                            Icon(Icons.Default.Menu, contentDescription = "Menu")
//                        }
//                    }
//                )
//            }
//        ) { innerPadding ->
//            when (selectedScreen) {
//                "home" -> HomeDashboardContent(
//                    userEmail = userEmail,
//                    userName = userName,
//                    theme = user.theme,
//                    modifier = Modifier.padding(innerPadding),
//                    onNavigate = { screen -> selectedScreen = screen }
//                )
//                "upload" -> UploadResumeScreen(
//                    modifier = Modifier.padding(innerPadding),
//                    isDark = isDark
//                )
//                "ats" -> PlaceholderScreen(
//                    title = "ATS Score",
//                    description = "View your resume's ATS compatibility score",
//                    modifier = Modifier.padding(innerPadding),
//                    isDark = isDark
//                )
//                "suggestions" -> PlaceholderScreen(
//                    title = "Suggestions",
//                    description = "Get personalized suggestions to improve your resume",
//                    modifier = Modifier.padding(innerPadding),
//                    isDark = isDark
//                )
//                "history" -> PlaceholderScreen(
//                    title = "History",
//                    description = "View your past resume analysis history",
//                    modifier = Modifier.padding(innerPadding),
//                    isDark = isDark
//                )
//            }
//        }
//    }
//}
//
//@Composable
//fun DrawerItem(
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
//                color = if (isDark) Color.White else Color.Black
//            )
//        },
//        selected = isSelected,
//        onClick = onClick,
//        icon = {
//            Icon(
//                icon,
//                contentDescription = text,
//                tint = if (isDark) Color.White else Color.Black
//            )
//        },
//        colors = NavigationDrawerItemDefaults.colors(
//            unselectedContainerColor = Color.Transparent,
//            selectedContainerColor = if (isDark) Color(0xFF6C63FF).copy(alpha = 0.3f) else Color(0xFF6C63FF).copy(alpha = 0.2f)
//        ),
//        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
//    )
//}
//
//@Composable
//fun PlaceholderScreen(
//    title: String,
//    description: String,
//    modifier: Modifier = Modifier,
//    isDark: Boolean
//) {
//    val colorScheme = if (isDark) darkColorScheme() else lightColorScheme()
//
//    Column(
//        modifier = modifier
//            .fillMaxSize()
//            .padding(24.dp),
//        verticalArrangement = Arrangement.Center,
//        horizontalAlignment = Alignment.CenterHorizontally
//    ) {
//        Icon(
//            Icons.Default.Construction,
//            contentDescription = null,
//            modifier = Modifier.size(64.dp),
//            tint = colorScheme.primary
//        )
//        Spacer(Modifier.height(16.dp))
//        Text(
//            title,
//            style = MaterialTheme.typography.headlineMedium,
//            color = colorScheme.onBackground
//        )
//        Spacer(Modifier.height(8.dp))
//        Text(
//            description,
//            style = MaterialTheme.typography.bodyLarge,
//            color = colorScheme.onSurfaceVariant,
//            textAlign = androidx.compose.ui.text.style.TextAlign.Center
//        )
//        Spacer(Modifier.height(24.dp))
//        Text(
//            "Coming Soon...",
//            style = MaterialTheme.typography.bodyMedium,
//            color = colorScheme.primary
//        )
//    }
//}
//
//fun getScreenTitle(screen: String): String {
//    return when (screen) {
//        "home" -> "Home"
//        "upload" -> "Upload Resume"
//        "ats" -> "ATS Score"
//        "suggestions" -> "Suggestions"
//        "history" -> "History"
//        else -> "Resume Analyser"
//    }
//}
//
//@Composable
//fun HomeDashboardContent(
//    userEmail: String,
//    userName: String,
//    theme: String = "system",
//    modifier: Modifier = Modifier,
//    onNavigate: (String) -> Unit
//) {
//    val isDark = when (theme) {
//        "light" -> false
//        "dark" -> true
//        else -> isSystemInDarkTheme()
//    }
//
//    val backgroundBrush = if (!isDark) {
//        Brush.verticalGradient(
//            listOf(Color(0xFF667eea), Color(0xFF764ba2))
//        )
//    } else {
//        Brush.verticalGradient(
//            listOf(Color(0xFF2C1810), Color(0xFF5D4E75))
//        )
//    }
//
//    val colorScheme = if (isDark) darkColorScheme() else lightColorScheme()
//
//    LazyColumn(
//        modifier = modifier
//            .fillMaxSize()
//            .background(backgroundBrush)
//            .padding(20.dp),
//        verticalArrangement = Arrangement.spacedBy(24.dp)
//    ) {
//        item {
//            Card(
//                modifier = Modifier.fillMaxWidth(),
//                shape = RoundedCornerShape(20.dp),
//                colors = CardDefaults.cardColors(
//                    containerColor = Color.White.copy(alpha = if (isDark) 0.1f else 0.95f)
//                ),
//                elevation = CardDefaults.cardElevation(8.dp)
//            ) {
//                Row(
//                    modifier = Modifier
//                        .fillMaxWidth()
//                        .padding(20.dp),
//                    verticalAlignment = Alignment.CenterVertically
//                ) {
//                    Box(
//                        modifier = Modifier
//                            .size(60.dp)
//                            .clip(CircleShape)
//                            .background(
//                                Brush.radialGradient(
//                                    listOf(Color(0xFFff7e5f), Color(0xFFfeb47b))
//                                )
//                            ),
//                        contentAlignment = Alignment.Center
//                    ) {
//                        Text(
//                            userEmail.firstOrNull()?.uppercase() ?: "?",
//                            color = Color.White,
//                            fontSize = 24.sp,
//                            fontWeight = FontWeight.Bold
//                        )
//                    }
//                    Spacer(Modifier.width(16.dp))
//                    Column {
//                        Text(
//                            text = "Welcome back!",
//                            fontSize = 16.sp,
//                            color = if (isDark) Color.White.copy(0.7f) else Color.Gray,
//                            fontWeight = FontWeight.Medium
//                        )
//                        Text(
//                            text = userName.ifEmpty { userEmail.substringBefore('@') },
//                            fontSize = 24.sp,
//                            fontWeight = FontWeight.Bold,
//                            color = if (isDark) Color.White else Color.Black
//                        )
//                    }
//                }
//            }
//        }
//
//        item {
//            Text(
//                "Quick Actions",
//                fontSize = 22.sp,
//                fontWeight = FontWeight.Bold,
//                color = Color.White,
//                modifier = Modifier.padding(horizontal = 8.dp)
//            )
//
//            Spacer(Modifier.height(12.dp))
//
//            Row(
//                modifier = Modifier.fillMaxWidth(),
//                horizontalArrangement = Arrangement.spacedBy(12.dp)
//            ) {
//                EnhancedQuickActionCard(
//                    title = "Upload\nResume",
//                    icon = Icons.Default.UploadFile,
//                    gradient = listOf(Color(0xFF667eea), Color(0xFF764ba2)),
//                    modifier = Modifier.weight(1f)
//                ) { onNavigate("upload") }
//
//                EnhancedQuickActionCard(
//                    title = "ATS\nScore",
//                    icon = Icons.Default.BarChart,
//                    gradient = listOf(Color(0xFFf093fb), Color(0xFFf5576c)),
//                    modifier = Modifier.weight(1f)
//                ) { onNavigate("ats") }
//
//                EnhancedQuickActionCard(
//                    title = "Get\nSuggestions",
//                    icon = Icons.Default.TipsAndUpdates,
//                    gradient = listOf(Color(0xFF4facfe), Color(0xFF00f2fe)),
//                    modifier = Modifier.weight(1f)
//                ) { onNavigate("suggestions") }
//            }
//        }
//
//        item {
//            Row(
//                modifier = Modifier.fillMaxWidth(),
//                horizontalArrangement = Arrangement.spacedBy(12.dp)
//            ) {
//                StatsCard(
//                    title = "Resumes Analyzed",
//                    value = "0",
//                    icon = Icons.Default.Description,
//                    color = Color(0xFF6C63FF),
//                    isDark = isDark,
//                    modifier = Modifier.weight(1f)
//                )
//
//                StatsCard(
//                    title = "Average Score",
//                    value = "N/A",
//                    icon = Icons.Default.Assessment,
//                    color = Color(0xFFE91E63),
//                    isDark = isDark,
//                    modifier = Modifier.weight(1f)
//                )
//            }
//        }
//
//        item {
//            Card(
//                modifier = Modifier.fillMaxWidth(),
//                shape = RoundedCornerShape(20.dp),
//                colors = CardDefaults.cardColors(
//                    containerColor = Color.White.copy(alpha = if (isDark) 0.1f else 0.95f)
//                ),
//                elevation = CardDefaults.cardElevation(8.dp)
//            ) {
//                Column(
//                    modifier = Modifier
//                        .fillMaxWidth()
//                        .padding(20.dp)
//                ) {
//                    Row(
//                        verticalAlignment = Alignment.CenterVertically
//                    ) {
//                        Icon(
//                            Icons.Default.History,
//                            contentDescription = null,
//                            tint = Color(0xFF6C63FF),
//                            modifier = Modifier.size(24.dp)
//                        )
//                        Spacer(Modifier.width(8.dp))
//                        Text(
//                            "Recent Activity",
//                            fontSize = 20.sp,
//                            fontWeight = FontWeight.Bold,
//                            color = if (isDark) Color.White else Color.Black
//                        )
//                    }
//
//                    Spacer(Modifier.height(16.dp))
//
//                    Box(
//                        modifier = Modifier
//                            .fillMaxWidth()
//                            .height(120.dp),
//                        contentAlignment = Alignment.Center
//                    ) {
//                        Column(
//                            horizontalAlignment = Alignment.CenterHorizontally
//                        ) {
//                            Icon(
//                                Icons.Default.CloudOff,
//                                contentDescription = null,
//                                modifier = Modifier.size(48.dp),
//                                tint = if (isDark) Color.White.copy(0.4f) else Color.Gray
//                            )
//                            Spacer(Modifier.height(8.dp))
//                            Text(
//                                "No activity yet",
//                                color = if (isDark) Color.White.copy(0.6f) else Color.Gray,
//                                fontSize = 16.sp
//                            )
//                            Text(
//                                "Upload a resume to get started",
//                                color = if (isDark) Color.White.copy(0.4f) else Color.Gray.copy(0.7f),
//                                fontSize = 14.sp
//                            )
//                        }
//                    }
//                }
//            }
//        }
//    }
//}
//
//@Composable
//fun EnhancedQuickActionCard(
//    title: String,
//    icon: androidx.compose.ui.graphics.vector.ImageVector,
//    gradient: List<Color>,
//    modifier: Modifier = Modifier,
//    onClick: () -> Unit
//) {
//    Card(
//        modifier = modifier
//            .height(120.dp)
//            .clip(RoundedCornerShape(20.dp)),
//        onClick = onClick,
//        elevation = CardDefaults.cardElevation(12.dp)
//    ) {
//        Box(
//            modifier = Modifier
//                .fillMaxSize()
//                .background(Brush.linearGradient(gradient))
//                .padding(16.dp),
//            contentAlignment = Alignment.Center
//        ) {
//            Column(
//                horizontalAlignment = Alignment.CenterHorizontally
//            ) {
//                Icon(
//                    icon,
//                    contentDescription = title,
//                    tint = Color.White,
//                    modifier = Modifier.size(32.dp)
//                )
//                Spacer(Modifier.height(8.dp))
//                Text(
//                    title,
//                    color = Color.White,
//                    fontSize = 12.sp,
//                    fontWeight = FontWeight.Bold,
//                    textAlign = androidx.compose.ui.text.style.TextAlign.Center,
//                    lineHeight = 14.sp
//                )
//            }
//        }
//    }
//}
//
//@Composable
//fun StatsCard(
//    title: String,
//    value: String,
//    icon: androidx.compose.ui.graphics.vector.ImageVector,
//    color: Color,
//    isDark: Boolean,
//    modifier: Modifier = Modifier
//) {
//    Card(
//        modifier = modifier.height(100.dp),
//        shape = RoundedCornerShape(16.dp),
//        colors = CardDefaults.cardColors(
//            containerColor = Color.White.copy(alpha = if (isDark) 0.1f else 0.95f)
//        ),
//        elevation = CardDefaults.cardElevation(8.dp)
//    ) {
//        Column(
//            modifier = Modifier
//                .fillMaxSize()
//                .padding(16.dp),
//            verticalArrangement = Arrangement.SpaceBetween
//        ) {
//            Row(
//                modifier = Modifier.fillMaxWidth(),
//                horizontalArrangement = Arrangement.SpaceBetween,
//                verticalAlignment = Alignment.CenterVertically
//            ) {
//                Icon(
//                    icon,
//                    contentDescription = null,
//                    tint = color,
//                    modifier = Modifier.size(24.dp)
//                )
//            }
//
//            Column {
//                Text(
//                    value,
//                    fontSize = 24.sp,
//                    fontWeight = FontWeight.Bold,
//                    color = if (isDark) Color.White else Color.Black
//                )
//                Text(
//                    title,
//                    fontSize = 12.sp,
//                    color = if (isDark) Color.White.copy(0.7f) else Color.Gray,
//                    maxLines = 2
//                )
//            }
//        }
//    }
//}



package com.example.feature_student

import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.launch
import androidx.navigation.NavController
import com.example.resumeanalyzer.core.navigation.datastore.UserCache
import com.example.resumeanalyzer.core.navigation.datastore.UserPreference
import com.example.feature_student.upload.UploadResumeScreen
import com.example.feature_student.ats.ATSScoreScreen
import com.example.feature_student.suggestions.SuggestionsScreen
import com.example.feature_student.history.HistoryScreen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StudentMainScreen(
    navController: NavController,
    onNavigate: (String) -> Unit
) {
    val context = LocalContext.current
    val user by UserPreference.getUser(context).collectAsState(initial = UserCache())
    val userEmail = user.email ?: "Guest"
    val userName = user.name ?: "User"
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    var showLogoutMenu by remember { mutableStateOf(false) }
    var selectedScreen by remember { mutableStateOf("home") }

    // Key to force recomposition when data changes
    var refreshKey by remember { mutableStateOf(0) }

    // Theme detection
    val isDark = when (user.theme) {
        "light" -> false
        "dark" -> true
        else -> isSystemInDarkTheme()
    }

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet(
                modifier = Modifier
                    .fillMaxHeight()
                    .width(320.dp),
                drawerContainerColor = if (isDark) Color(0xFF121212) else Color(0xFFF5F6FA)
            ) {
                // Drawer Header - User Profile Section
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Box(
                        modifier = Modifier
                            .size(70.dp)
                            .clip(CircleShape)
                            .background(Color(0xFF6C63FF)),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            userEmail.firstOrNull()?.uppercase() ?: "?",
                            color = Color.White,
                            fontSize = 28.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        userEmail,
                        fontSize = 14.sp,
                        color = if (isDark) Color.LightGray else Color.DarkGray,
                        fontWeight = FontWeight.Medium
                    )
                }

                Spacer(Modifier.height(16.dp))

                // Navigation Items
                DrawerItem(
                    icon = Icons.Default.Home,
                    text = "Home",
                    isDark = isDark,
                    isSelected = selectedScreen == "home"
                ) {
                    scope.launch { drawerState.close() }
                    selectedScreen = "home"
                }

                DrawerItem(
                    icon = Icons.Default.UploadFile,
                    text = "Upload Resume",
                    isDark = isDark,
                    isSelected = selectedScreen == "upload"
                ) {
                    scope.launch { drawerState.close() }
                    selectedScreen = "upload"
                }

                DrawerItem(
                    icon = Icons.Default.BarChart,
                    text = "ATS Score",
                    isDark = isDark,
                    isSelected = selectedScreen == "ats"
                ) {
                    scope.launch { drawerState.close() }
                    selectedScreen = "ats"
                }

                DrawerItem(
                    icon = Icons.Default.TipsAndUpdates,
                    text = "Suggestions",
                    isDark = isDark,
                    isSelected = selectedScreen == "suggestions"
                ) {
                    scope.launch { drawerState.close() }
                    selectedScreen = "suggestions"
                }

                DrawerItem(
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

                // Bottom Settings & Logout
                DrawerItem(
                    icon = Icons.Default.Settings,
                    text = "Settings",
                    isDark = isDark,
                    isSelected = false
                ) {
                    scope.launch { drawerState.close() }
                    navController.navigate("settings")
                }

                DrawerItem(
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
                        popUpTo("studentMain") { inclusive = true }
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
                            text = getScreenTitle(selectedScreen),
                            fontWeight = FontWeight.Bold
                        )
                    },
                    navigationIcon = {
                        IconButton(onClick = {
                            scope.launch { drawerState.open() }
                        }) {
                            Icon(Icons.Default.Menu, contentDescription = "Menu")
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = if (isDark) Color(0xFF1a1a2e) else MaterialTheme.colorScheme.primaryContainer,
                        titleContentColor = if (isDark) Color.White else MaterialTheme.colorScheme.onPrimaryContainer,
                        navigationIconContentColor = if (isDark) Color.White else MaterialTheme.colorScheme.onPrimaryContainer
                    )
                )
            }
        ) { innerPadding ->
            // Screen Content Based on Selection
            when (selectedScreen) {
                "home" -> HomeDashboardContent(
                    userEmail = userEmail,
                    userName = userName,
                    theme = user.theme,
                    modifier = Modifier.padding(innerPadding),
                    onNavigate = { screen -> selectedScreen = screen }
                )

                "upload" -> UploadResumeScreen(
                    modifier = Modifier.padding(innerPadding),
                    isDark = isDark
                )

                "ats" -> ATSScoreScreen(
                    modifier = Modifier.padding(innerPadding),
                    isDark = isDark
                )

                "suggestions" -> SuggestionsScreen(
                    modifier = Modifier.padding(innerPadding),
                    isDark = isDark
                )

                "history" -> HistoryScreen(
                    modifier = Modifier.padding(innerPadding),
                    isDark = isDark
                )
            }
        }
    }
}

// Drawer Item Composable
@Composable
fun DrawerItem(
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
                fontSize = 16.sp,
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
                    Color(0xFF6C63FF)
                } else {
                    if (isDark) Color.White else Color.Black
                }
            )
        },
        colors = NavigationDrawerItemDefaults.colors(
            unselectedContainerColor = Color.Transparent,
            selectedContainerColor = if (isDark) {
                Color(0xFF6C63FF).copy(alpha = 0.3f)
            } else {
                Color(0xFF6C63FF).copy(alpha = 0.2f)
            }
        ),
        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
    )
}

// Get Screen Title Helper Function
fun getScreenTitle(screen: String): String {
    return when (screen) {
        "home" -> "Home"
        "upload" -> "Upload Resume"
        "ats" -> "ATS Score"
        "suggestions" -> "Suggestions"
        "history" -> "History"
        else -> "Resume Analyser"
    }
}

// Home Dashboard Content with Dynamic Stats
@Composable
fun HomeDashboardContent(
    userEmail: String,
    userName: String,
    theme: String = "system",
    modifier: Modifier = Modifier,
    onNavigate: (String) -> Unit
) {
    val isDark = when (theme) {
        "light" -> false
        "dark" -> true
        else -> isSystemInDarkTheme()
    }

    // Get live stats from database
    val totalAnalyzed = remember {
        com.example.feature_student.data.LocalResumeDatabase.getTotalAnalyzed()
    }
    val averageScore = remember {
        com.example.feature_student.data.LocalResumeDatabase.getAverageScore()
    }
    val recentResumes = remember {
        com.example.feature_student.data.LocalResumeDatabase.getAnalyzedResumes().take(3)
    }

    val backgroundBrush = if (!isDark) {
        Brush.verticalGradient(
            listOf(Color(0xFF667eea), Color(0xFF764ba2))
        )
    } else {
        Brush.verticalGradient(
            listOf(Color(0xFF2C1810), Color(0xFF5D4E75))
        )
    }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(backgroundBrush)
            .padding(20.dp),
        verticalArrangement = Arrangement.spacedBy(24.dp)
    ) {
        // Welcome Card
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(
                    containerColor = Color.White.copy(alpha = if (isDark) 0.1f else 0.95f)
                ),
                elevation = CardDefaults.cardElevation(8.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(60.dp)
                            .clip(CircleShape)
                            .background(
                                Brush.radialGradient(
                                    listOf(Color(0xFFff7e5f), Color(0xFFfeb47b))
                                )
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            userEmail.firstOrNull()?.uppercase() ?: "?",
                            color = Color.White,
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                    Spacer(Modifier.width(16.dp))
                    Column {
                        Text(
                            text = "Welcome back!",
                            fontSize = 16.sp,
                            color = if (isDark) Color.White.copy(0.7f) else Color.Gray,
                            fontWeight = FontWeight.Medium
                        )
                        Text(
                            text = userName.ifEmpty { userEmail.substringBefore('@') },
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Bold,
                            color = if (isDark) Color.White else Color.Black
                        )
                    }
                }
            }
        }

        // Quick Actions Section
        item {
            Text(
                "Quick Actions",
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White,
                modifier = Modifier.padding(horizontal = 8.dp)
            )

            Spacer(Modifier.height(12.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                EnhancedQuickActionCard(
                    title = "Upload\nResume",
                    icon = Icons.Default.UploadFile,
                    gradient = listOf(Color(0xFF667eea), Color(0xFF764ba2)),
                    modifier = Modifier.weight(1f)
                ) { onNavigate("upload") }

                EnhancedQuickActionCard(
                    title = "ATS\nScore",
                    icon = Icons.Default.BarChart,
                    gradient = listOf(Color(0xFFf093fb), Color(0xFFf5576c)),
                    modifier = Modifier.weight(1f)
                ) { onNavigate("ats") }

                EnhancedQuickActionCard(
                    title = "Get\nSuggestions",
                    icon = Icons.Default.TipsAndUpdates,
                    gradient = listOf(Color(0xFF4facfe), Color(0xFF00f2fe)),
                    modifier = Modifier.weight(1f)
                ) { onNavigate("suggestions") }
            }
        }

        // Statistics Cards
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                StatsCard(
                    title = "Resumes Analyzed",
                    value = totalAnalyzed.toString(),
                    icon = Icons.Default.Description,
                    color = Color(0xFF6C63FF),
                    isDark = isDark,
                    modifier = Modifier.weight(1f)
                )

                StatsCard(
                    title = "Average Score",
                    value = if (averageScore > 0) String.format("%.1f", averageScore) else "N/A",
                    icon = Icons.Default.Assessment,
                    color = Color(0xFFE91E63),
                    isDark = isDark,
                    modifier = Modifier.weight(1f)
                )
            }
        }

        // Recent Activity Section
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(
                    containerColor = Color.White.copy(alpha = if (isDark) 0.1f else 0.95f)
                ),
                elevation = CardDefaults.cardElevation(8.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            Icons.Default.History,
                            contentDescription = null,
                            tint = Color(0xFF6C63FF),
                            modifier = Modifier.size(24.dp)
                        )
                        Spacer(Modifier.width(8.dp))
                        Text(
                            "Recent Activity",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            color = if (isDark) Color.White else Color.Black
                        )
                    }

                    Spacer(Modifier.height(16.dp))

                    if (recentResumes.isEmpty()) {
                        // Empty State
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(120.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Icon(
                                    Icons.Default.CloudOff,
                                    contentDescription = null,
                                    modifier = Modifier.size(48.dp),
                                    tint = if (isDark) Color.White.copy(0.4f) else Color.Gray
                                )
                                Spacer(Modifier.height(8.dp))
                                Text(
                                    "No activity yet",
                                    color = if (isDark) Color.White.copy(0.6f) else Color.Gray,
                                    fontSize = 16.sp
                                )
                                Text(
                                    "Upload a resume to get started",
                                    color = if (isDark) Color.White.copy(0.4f) else Color.Gray.copy(0.7f),
                                    fontSize = 14.sp
                                )
                            }
                        }
                    } else {
                        // Recent Items List
                        Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                            recentResumes.forEach { resume ->
                                RecentActivityItem(resume, isDark)
                            }
                        }
                    }
                }
            }
        }
    }
}

// Recent Activity Item
@Composable
fun RecentActivityItem(
    resume: com.example.feature_student.model.Resume,
    isDark: Boolean
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(if (isDark) Color.White.copy(0.05f) else Color.Black.copy(0.03f))
            .padding(12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            Icons.Default.Description,
            contentDescription = null,
            tint = Color(0xFF6C63FF),
            modifier = Modifier.size(24.dp)
        )
        Spacer(Modifier.width(12.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(
                resume.fileName,
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium,
                color = if (isDark) Color.White else Color.Black,
                maxLines = 1
            )
            Text(
                formatDate(resume.uploadedDate),
                fontSize = 12.sp,
                color = if (isDark) Color.White.copy(0.5f) else Color.Gray
            )
        }
        Surface(
            shape = RoundedCornerShape(8.dp),
            color = getScoreColor(resume.atsScore ?: 0).copy(alpha = 0.2f)
        ) {
            Text(
                "${resume.atsScore}",
                modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                color = getScoreColor(resume.atsScore ?: 0)
            )
        }
    }
}

// Enhanced Quick Action Card
@Composable
fun EnhancedQuickActionCard(
    title: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    gradient: List<Color>,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Card(
        modifier = modifier
            .height(120.dp)
            .clip(RoundedCornerShape(20.dp)),
        onClick = onClick,
        elevation = CardDefaults.cardElevation(12.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Brush.linearGradient(gradient))
                .padding(16.dp),
            contentAlignment = Alignment.Center
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Icon(
                    icon,
                    contentDescription = title,
                    tint = Color.White,
                    modifier = Modifier.size(32.dp)
                )
                Spacer(Modifier.height(8.dp))
                Text(
                    title,
                    color = Color.White,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    textAlign = androidx.compose.ui.text.style.TextAlign.Center,
                    lineHeight = 14.sp
                )
            }
        }
    }
}

// Stats Card
@Composable
fun StatsCard(
    title: String,
    value: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    color: Color,
    isDark: Boolean,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.height(100.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White.copy(alpha = if (isDark) 0.1f else 0.95f)
        ),
        elevation = CardDefaults.cardElevation(8.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    icon,
                    contentDescription = null,
                    tint = color,
                    modifier = Modifier.size(24.dp)
                )
            }

            Column {
                Text(
                    value,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = if (isDark) Color.White else Color.Black
                )
                Text(
                    title,
                    fontSize = 12.sp,
                    color = if (isDark) Color.White.copy(0.7f) else Color.Gray,
                    maxLines = 2
                )
            }
        }
    }
}

// Helper Functions
fun formatDate(timestamp: Long): String {
    val sdf = java.text.SimpleDateFormat("MMM dd", java.util.Locale.getDefault())
    return sdf.format(java.util.Date(timestamp))
}

fun getScoreColor(score: Int): Color {
    return when {
        score >= 80 -> Color(0xFF4CAF50)
        score >= 60 -> Color(0xFFFF9800)
        else -> Color(0xFFF44336)
    }
}


