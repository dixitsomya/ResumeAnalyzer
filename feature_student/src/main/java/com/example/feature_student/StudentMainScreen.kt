//
//package com.example.feature_student
//
//import androidx.compose.foundation.Image
//import androidx.compose.foundation.layout.*
//import androidx.compose.material.icons.Icons
//import androidx.compose.material.icons.filled.*
//import androidx.compose.material3.*
//import androidx.compose.runtime.*
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.res.painterResource
//import androidx.compose.ui.unit.dp
//import androidx.compose.ui.Alignment
//import kotlinx.coroutines.launch
//import androidx.navigation.NavController
//
//
//@OptIn(ExperimentalMaterial3Api::class)
//@Composable
//fun StudentMainScreen(
//    userEmail: String,
//    navController: NavController
//) {
//    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
//    val scope = rememberCoroutineScope()
//    var showLogoutMenu by remember { mutableStateOf(false) }
//
//    ModalNavigationDrawer(
//        drawerState = drawerState,
//        drawerContent = {
//            ModalDrawerSheet {
//                // 🔹 Top Section with App Logo
//                Column(
//                    modifier = Modifier
//                        .fillMaxWidth()
//                        .padding(16.dp),
//                    horizontalAlignment = Alignment.Start
//                ) {
//                    Image(
//                        painter = painterResource(id = R.drawable.transparent_logo), // 🔹 Your app logo here
//                        contentDescription = "App Logo",
//                        modifier = Modifier.size(48.dp)
//                    )
//                    Spacer(modifier = Modifier.height(8.dp))
//                    Text("Resume Analyser", style = MaterialTheme.typography.titleMedium)
//                }
//
//                Divider()
//
//                // 🔹 Drawer Items
//                NavigationDrawerItem(
//                    icon = { Icon(Icons.Default.Home, contentDescription = null) },
//                    label = { Text("Home") },
//                    selected = false,
//                    onClick = {}
//                )
//                NavigationDrawerItem(
//                    icon = { Icon(Icons.Default.UploadFile, contentDescription = null) },
//                    label = { Text("Resume Upload") },
//                    selected = false,
//                    onClick = {}
//                )
//                NavigationDrawerItem(
//                    icon = { Icon(Icons.Default.BarChart, contentDescription = null) },
//                    label = { Text("ATS Score + Suggestions") },
//                    selected = false,
//                    onClick = {}
//                )
//                NavigationDrawerItem(
//                    icon = { Icon(Icons.Default.History, contentDescription = null) },
//                    label = { Text("History") },
//                    selected = false,
//                    onClick = {}
//                )
//
//                Spacer(modifier = Modifier.weight(1f))
//
//                Divider()
//
//                // 🔹 Bottom Section (Profile + Logout)
//                Box {
//                    NavigationDrawerItem(
//                        icon = { Icon(Icons.Default.Email, contentDescription = null) },
//                        label = { Text(userEmail) },
//                        selected = false,
//                        onClick = { showLogoutMenu = true }
//                    )
//
//                    DropdownMenu(
//                        expanded = showLogoutMenu,
//                        onDismissRequest = { showLogoutMenu = false }
//                    ) {
//                        DropdownMenuItem(
//                            text = { Text("Logout") },
//                            onClick = {
//                                showLogoutMenu = false
//                                navController.navigate("login") {
//                                    popUpTo("studentMain/$userEmail") { inclusive = true }
//                                }
//                            }
//                        )
//                    }
//                }
//
//                NavigationDrawerItem(
//                    icon = { Icon(Icons.Default.Settings, contentDescription = null) },
//                    label = { Text("Settings") },
//                    selected = false,
//                    onClick = {}
//                )
//            }
//        }
//    ) {
//        Scaffold(
//            topBar = {
//                TopAppBar(
//                    title = { Text("Resume Analyser") },
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
//            Box(
//                modifier = Modifier
//                    .fillMaxSize()
//                    .padding(innerPadding),
//                contentAlignment = Alignment.Center
//            ) {
//                Text("🎓 Student Dashboard Content Here")
//            }
//        }
//    }
//}
//



//package com.example.feature_student
//
//import androidx.compose.foundation.Image
//import androidx.compose.foundation.background
//import androidx.compose.foundation.layout.*
//import androidx.compose.foundation.shape.RoundedCornerShape
//import androidx.compose.material.icons.Icons
//import androidx.compose.material.icons.filled.*
//import androidx.compose.material3.*
//import androidx.compose.runtime.*
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.graphics.Brush
//import androidx.compose.ui.res.painterResource
//import androidx.compose.ui.unit.dp
//import androidx.compose.ui.Alignment
//import kotlinx.coroutines.launch
//import androidx.navigation.NavController
//import androidx.compose.material3.TopAppBarDefaults
//
//@OptIn(ExperimentalMaterial3Api::class)
//@Composable
//fun StudentMainScreen(
//    userEmail: String,
//    navController: NavController
//) {
//    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
//    val scope = rememberCoroutineScope()
//    var showLogoutMenu by remember { mutableStateOf(false) }
//
//    ModalNavigationDrawer(
//        drawerState = drawerState,
//        drawerContent = {
//            ModalDrawerSheet(
//                modifier = Modifier.background(MaterialTheme.colorScheme.surfaceVariant)
//            ) {
//                // 🔹 Top Section with App Logo
//                Column(
//                    modifier = Modifier
//                        .fillMaxWidth()
//                        .padding(16.dp),
//                    horizontalAlignment = Alignment.Start
//                ) {
//                    Image(
//                        painter = painterResource(id = R.drawable.transparent_logo),
//                        contentDescription = "App Logo",
//                        modifier = Modifier.size(48.dp)
//                    )
//                    Spacer(modifier = Modifier.height(8.dp))
//                    Text(
//                        "Resume Analyzer",
//                        style = MaterialTheme.typography.titleMedium
//                    )
//                }
//
//                Divider()
//
//                // 🔹 Drawer Items
//                val drawerItems = listOf(
//                    "Home" to Icons.Default.Home,
//                    "Resume Upload" to Icons.Default.UploadFile,
//                    "ATS Score + Suggestions" to Icons.Default.BarChart,
//                    "History" to Icons.Default.History,
//                    "Settings" to Icons.Default.Settings
//                )
//
//                drawerItems.forEach { (label, icon) ->
//                    NavigationDrawerItem(
//                        icon = { Icon(icon, contentDescription = null) },
//                        label = { Text(label) },
//                        selected = false,
//                        onClick = { /* Add navigation logic */ },
//                        modifier = Modifier.padding(vertical = 4.dp)
//                    )
//                }
//
//                Spacer(modifier = Modifier.weight(1f))
//
//                Divider()
//
//                // 🔹 Bottom Section (Profile + Logout)
//                Box {
//                    NavigationDrawerItem(
//                        icon = { Icon(Icons.Default.Email, contentDescription = null) },
//                        label = { Text(userEmail) },
//                        selected = false,
//                        onClick = { showLogoutMenu = true }
//                    )
//
//                    DropdownMenu(
//                        expanded = showLogoutMenu,
//                        onDismissRequest = { showLogoutMenu = false }
//                    ) {
//                        DropdownMenuItem(
//                            text = { Text("Logout") },
//                            onClick = {
//                                showLogoutMenu = false
//                                navController.navigate("login") {
//                                    popUpTo("studentMain/$userEmail") { inclusive = true }
//                                }
//                            }
//                        )
//                    }
//                }
//            }
//        }
//    ) {
//        // 🔹 Main content with gradient background and rounded card
//        Box(
//            modifier = Modifier
//                .fillMaxSize()
//                .background(
//                    Brush.verticalGradient(
//                        listOf(MaterialTheme.colorScheme.primary.copy(alpha = 0.1f),
//                            MaterialTheme.colorScheme.secondary.copy(alpha = 0.1f))
//                    )
//                ),
//            contentAlignment = Alignment.Center
//        ) {
//            Card(
//                modifier = Modifier
//                    .fillMaxWidth(0.9f)
//                    .padding(16.dp),
//                shape = RoundedCornerShape(16.dp),
//                elevation = CardDefaults.cardElevation(12.dp)
//            ) {
//                Column(
//                    modifier = Modifier.padding(24.dp),
//                    horizontalAlignment = Alignment.CenterHorizontally,
//                    verticalArrangement = Arrangement.Center
//                ) {
//                    Text("🎓 Student Dashboard", style = MaterialTheme.typography.headlineSmall)
//                    Spacer(modifier = Modifier.height(16.dp))
//                    Text("Welcome, $userEmail!", style = MaterialTheme.typography.bodyLarge)
//                    Spacer(modifier = Modifier.height(24.dp))
//                    Button(
//                        onClick = { /* Add navigation to main features */ },
//                        modifier = Modifier.fillMaxWidth(),
//                        shape = RoundedCornerShape(12.dp)
//                    ) {
//                        Text("Go to Dashboard Features")
//                    }
//                }
//            }
//        }
//
//        // 🔹 TopAppBar
//        TopAppBar(
//            title = { Text("Resume Analyzer") },
//            navigationIcon = {
//                IconButton(onClick = { scope.launch { drawerState.open() } }) {
//                    Icon(Icons.Default.Menu, contentDescription = "Menu")
//                }
//            },
//            colors = TopAppBarDefaults.topAppBarColors(
//                containerColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.9f),
//                titleContentColor = MaterialTheme.colorScheme.onPrimary
//            )
//        )
//
//
//    }
//}

//----------------------------------  code without settings    -------------------------------------------------------------------------

//package com.example.feature_student
//
//import androidx.compose.foundation.background
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
//import androidx.compose.ui.graphics.Brush
//import androidx.compose.ui.graphics.Color
//import androidx.compose.ui.platform.LocalContext
//import androidx.compose.ui.text.font.FontWeight
//import androidx.compose.ui.unit.dp
//import androidx.compose.ui.unit.sp
//import kotlinx.coroutines.launch
//import androidx.navigation.NavController
//import com.example.resumeanalyzer.core.navigation.datastore.UserPreference
//
//
//@OptIn(ExperimentalMaterial3Api::class)
//@Composable
//fun StudentMainScreen(
//    userEmail: String,
//    navController: NavController,
//    onNavigate: (String) -> Unit
//) {
//    val context = LocalContext.current
//    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
//    val scope = rememberCoroutineScope()
//    var showLogoutMenu by remember { mutableStateOf(false) }
//
//    ModalNavigationDrawer(
//        drawerState = drawerState,
//        drawerContent = {
//            ModalDrawerSheet(
//                modifier = Modifier
//                    .fillMaxHeight()
//                    .width(320.dp),
//                drawerContainerColor = Color(0xFFF5F6FA)
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
//                    Text(userEmail, fontSize = 14.sp, color = Color.DarkGray)
//                }
//
//                Spacer(Modifier.height(16.dp))
//
//                // 🔹 Drawer Items
//                DrawerItem(Icons.Default.Home, "Home") { onNavigate("home") }
//                DrawerItem(Icons.Default.UploadFile, "Upload Resume") { onNavigate("upload") }
//                DrawerItem(Icons.Default.BarChart, "ATS Score") { onNavigate("ats") }
//                DrawerItem(Icons.Default.TipsAndUpdates, "Suggestions") { onNavigate("suggestions") }
//                DrawerItem(Icons.Default.History, "History") { onNavigate("history") }
//
//                Spacer(modifier = Modifier.weight(1f))
//
//                Divider()
//
//                // 🔹 Bottom: Settings + Logout
//               DrawerItem(Icons.Default.Settings, "Settings") { onNavigate("settings") }
////                DrawerItem(Icons.Default.Settings, "Settings") {
////                    scope.launch { drawerState.close() }
////                    navController.navigate("settings")
////                }
//
//
//                DrawerItem(Icons.Default.Logout, "Logout") {
//                    scope.launch {
//                        UserPreference.clearUser(context)
//                    }
//                    navController.navigate("login") {
//                        popUpTo("studentMain/$userEmail") { inclusive = true }
//                    }
//                }
//
//            }
//        }
//    ) {
//        Scaffold(
//            topBar = {
//                TopAppBar(
//                    title = { Text("Resume Analyser") },
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
//            HomeDashboardContent(
//                userEmail = userEmail,
//                modifier = Modifier.padding(innerPadding),
//                onNavigate = onNavigate
//            )
//        }
//    }
//}
//
//@Composable
//fun DrawerItem(icon: androidx.compose.ui.graphics.vector.ImageVector, text: String, onClick: () -> Unit) {
//    NavigationDrawerItem(
//        label = { Text(text, fontSize = 16.sp) },
//        selected = false,
//        onClick = onClick,
//        icon = { Icon(icon, contentDescription = text) },
//        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
//    )
//}
//
//@Composable
//fun HomeDashboardContent(userEmail: String, modifier: Modifier = Modifier, onNavigate: (String) -> Unit) {
//    Column(
//        modifier = modifier
//            .fillMaxSize()
//            .background(
//                Brush.verticalGradient(
//                    listOf(Color(0xFF6C63FF), Color(0xFFB388FF))
//                )
//            )
//            .padding(16.dp)
//    ) {
//        // 🔹 Welcome
//        Text(
//            text = "Welcome back, ${userEmail.substringBefore('@')} 👋",
//            color = Color.White,
//            fontWeight = FontWeight.Bold,
//            fontSize = 22.sp
//        )
//        Spacer(Modifier.height(16.dp))
//
//        // 🔹 Quick Actions
//        Text("Quick Actions", color = Color.White, fontSize = 18.sp)
//        Spacer(Modifier.height(8.dp))
//        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
//            QuickActionCard("Upload Resume", Icons.Default.UploadFile) { onNavigate("upload") }
//            QuickActionCard("ATS Score", Icons.Default.BarChart) { onNavigate("ats") }
//            QuickActionCard("Suggestions", Icons.Default.TipsAndUpdates) { onNavigate("suggestions") }
//        }
//
//        Spacer(Modifier.height(24.dp))
//
//        // 🔹 Stats Section
//        Text("Your Stats", color = Color.White, fontSize = 18.sp)
//        Spacer(Modifier.height(8.dp))
//        Card(
//            modifier = Modifier.fillMaxWidth(),
//            shape = RoundedCornerShape(16.dp),
//            colors = CardDefaults.cardColors(containerColor = Color.White)
//        ) {
//            Column(Modifier.padding(16.dp)) {
//                // Agar data nahi mila → placeholder text
//                Text("No stats available yet", color = Color.Gray)
//            }
//        }
//
//        Spacer(Modifier.height(24.dp))
//
//        // 🔹 Recent History
//        Text("Recent History", color = Color.White, fontSize = 18.sp)
//        Spacer(Modifier.height(8.dp))
//        Card(
//            modifier = Modifier.fillMaxWidth(),
//            shape = RoundedCornerShape(16.dp),
//            colors = CardDefaults.cardColors(containerColor = Color.White)
//        ) {
//            Column(Modifier.padding(16.dp)) {
//                // Agar history nahi hai → placeholder
//                Text("No resumes analyzed yet", color = Color.Gray)
//            }
//        }
//    }
//}
//
//@Composable
//fun QuickActionCard(title: String, icon: androidx.compose.ui.graphics.vector.ImageVector, onClick: () -> Unit) {
//    Card(
//        modifier = Modifier
//            .size(100.dp)
//            .padding(4.dp),
//        shape = RoundedCornerShape(16.dp),
//        onClick = onClick,
//        colors = CardDefaults.cardColors(containerColor = Color.White.copy(alpha = 0.9f))
//    ) {
//        Column(
//            modifier = Modifier.fillMaxSize(),
//            verticalArrangement = Arrangement.Center,
//            horizontalAlignment = Alignment.CenterHorizontally
//        ) {
//            Icon(icon, contentDescription = title, tint = Color(0xFF6C63FF))
//            Spacer(Modifier.height(4.dp))
//            Text(title, fontSize = 12.sp)
//        }
//    }
//}







//@OptIn(ExperimentalMaterial3Api::class)
//@Composable
//fun StudentMainScreen(
//    navController: NavController,
//    onNavigate: (String) -> Unit
//) {
//    val context = LocalContext.current
//    val user by UserPreference.getUser(context).collectAsState(initial = UserCache())
//    val userEmail = user.email ?: "Guest" // fallback
//    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
//    val scope = rememberCoroutineScope()
//    var showLogoutMenu by remember { mutableStateOf(false) }
//    //val drawerColor = if (isSystemInDarkTheme()) Color(0xFF121212) else Color(0xFFF5F6FA)
//
//
//    ModalNavigationDrawer(
//        drawerState = drawerState,
//        drawerContent = {
//            ModalDrawerSheet(
//                modifier = Modifier
//                    .fillMaxHeight()
//                    .width(320.dp),
////                drawerContainerColor = Color(0xFFF5F6FA)
//                drawerContainerColor = if (isSystemInDarkTheme()) Color(0xFF121212) else Color(0xFFF5F6FA)
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
//                    Text(userEmail, fontSize = 14.sp, color = Color.DarkGray)
//                }
//
//                Spacer(Modifier.height(16.dp))
//
//                // 🔹 Drawer Items
//                DrawerItem(Icons.Default.Home, "Home") { onNavigate("home") }
//                DrawerItem(Icons.Default.UploadFile, "Upload Resume") { onNavigate("upload") }
//                DrawerItem(Icons.Default.BarChart, "ATS Score") { onNavigate("ats") }
//                DrawerItem(Icons.Default.TipsAndUpdates, "Suggestions") { onNavigate("suggestions") }
//                DrawerItem(Icons.Default.History, "History") { onNavigate("history") }
//
//                Spacer(modifier = Modifier.weight(1f))
//
//                Divider()
//
//                // 🔹 Bottom: Settings + Logout
////                DrawerItem(Icons.Default.Settings, "Settings") { onNavigate("settings") }
//                DrawerItem(Icons.Default.Settings, "Settings") {
//                    scope.launch { drawerState.close() }
//                    navController.navigate("settings")
//                }
//
//
//                DrawerItem(Icons.Default.Logout, "Logout") {
//                    scope.launch {
//                        UserPreference.clearUser(context)
//                    }
//                    navController.navigate("login") {
//                        popUpTo("studentMain/$userEmail") { inclusive = true }
//                    }
//                }
//
//            }
//        }
//    ) {
//        Scaffold(
//            topBar = {
//                TopAppBar(
//                    title = { Text("Resume Analyser") },
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
//            HomeDashboardContent(
//                userEmail = userEmail,
//                modifier = Modifier.padding(innerPadding),
//                onNavigate = onNavigate
//            )
//        }
//    }
//}
//
//@Composable
//fun DrawerItem(icon: androidx.compose.ui.graphics.vector.ImageVector, text: String, onClick: () -> Unit) {
//    NavigationDrawerItem(
////        label = { Text(text, fontSize = 16.sp) },
////        selected = false,
////        onClick = onClick,
////        icon = { Icon(icon, contentDescription = text) },
////        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
//        label = { Text(text, fontSize = 16.sp, color = if (isSystemInDarkTheme()) Color.White else Color.Black) },
//        selected = false,
//        onClick = onClick,
//        icon = { Icon(icon, contentDescription = text, tint = if (isSystemInDarkTheme()) Color.White else Color.Black) },
//        colors = NavigationDrawerItemDefaults.colors(
//            unselectedContainerColor = Color.Transparent, // ensures drawerColor is visible
//            selectedContainerColor = Color.Gray.copy(alpha = 0.2f)
//        ),
//        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
//    )
//}
//@Composable
//fun HomeDashboardContent(userEmail: String, modifier: Modifier = Modifier, onNavigate: (String) -> Unit) {
//
//    Column(
//        modifier = modifier
//            .fillMaxSize()
//            .background(
//                Brush.verticalGradient(
////                    listOf(Color(0xFF6C63FF), Color(0xFFB388FF))
////                    listOf(
////                        Color(0xFF6C63FF), // deep violet
////                        Color(0xFF8E54E9), // bright purple
////                        Color(0xFFB388FF)  // lavender
////                    )
//                     listOf(
//                        Color(0xFF89CFF0), // baby blue
//                        Color(0xFFB39DDB), // soft purple
//                        Color(0xFFE1BEE7)  // pale pinkish-lavender
//                    )
//
//                )
//            )
//            .padding(16.dp)
//    ) {
//        // 🔹 Welcome
//        Text(
//            text = "Welcome back, ${userEmail.substringBefore('@')} 👋",
//            color = Color.White,
//            fontWeight = FontWeight.Bold,
//            fontSize = 22.sp
//        )
//        Spacer(Modifier.height(16.dp))
//
//        // 🔹 Quick Actions
//        Text("Quick Actions", color = Color.White, fontSize = 18.sp)
//        Spacer(Modifier.height(8.dp))
//        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
//            QuickActionCard("Upload Resume", Icons.Default.UploadFile) { onNavigate("upload") }
//            QuickActionCard("ATS Score", Icons.Default.BarChart) { onNavigate("ats") }
//            QuickActionCard("Suggestions", Icons.Default.TipsAndUpdates) { onNavigate("suggestions") }
//        }
//
//        Spacer(Modifier.height(24.dp))
//
//        // 🔹 Stats Section
//        Text("Your Stats", color = Color.White, fontSize = 18.sp)
//        Spacer(Modifier.height(8.dp))
//        Card(
//            modifier = Modifier.fillMaxWidth(),
//            shape = RoundedCornerShape(16.dp),
//            colors = CardDefaults.cardColors(containerColor = Color.White)
//        ) {
//            Column(Modifier.padding(16.dp)) {
//                // Agar data nahi mila → placeholder text
//                Text("No stats available yet", color = Color.Gray)
//            }
//        }
//
//        Spacer(Modifier.height(24.dp))
//
//        // 🔹 Recent History
//        Text("Recent History", color = Color.White, fontSize = 18.sp)
//        Spacer(Modifier.height(8.dp))
//        Card(
//            modifier = Modifier.fillMaxWidth(),
//            shape = RoundedCornerShape(16.dp),
//            colors = CardDefaults.cardColors(containerColor = Color.White)
//        ) {
//            Column(Modifier.padding(16.dp)) {
//                // Agar history nahi hai → placeholder
//                Text("No resumes analyzed yet", color = Color.Gray)
//            }
//        }
//    }
//}
//
//@Composable
//fun QuickActionCard(title: String, icon: androidx.compose.ui.graphics.vector.ImageVector, onClick: () -> Unit) {
//    Card(
//        modifier = Modifier
//            .size(100.dp)
//            .padding(4.dp),
//        shape = RoundedCornerShape(16.dp),
//        onClick = onClick,
//        colors = CardDefaults.cardColors(containerColor = Color.White.copy(alpha = 0.9f))
//    ) {
//        Column(
//            modifier = Modifier.fillMaxSize(),
//            verticalArrangement = Arrangement.Center,
//            horizontalAlignment = Alignment.CenterHorizontally
//        ) {
//            Icon(icon, contentDescription = title, tint = Color(0xFF6C63FF))
//            Spacer(Modifier.height(4.dp))
//            Text(title, fontSize = 12.sp)
//        }
//    }
//}
//
//




package com.example.feature_student

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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StudentMainScreen(
    navController: NavController,
    onNavigate: (String) -> Unit
) {
    val context = LocalContext.current
    val user by UserPreference.getUser(context).collectAsState(initial = UserCache())
    val userEmail = user.email ?: "Guest" // fallback
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    var showLogoutMenu by remember { mutableStateOf(false) }
    var selectedScreen by remember { mutableStateOf("home") }


    // Get the current theme from user preferences
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
                // Use the theme-aware isDark variable
                drawerContainerColor = if (isDark) Color(0xFF121212) else Color(0xFFF5F6FA)
            ) {
                // 🔹 Top User Section
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
                            fontSize = 28.sp
                        )
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        userEmail,
                        fontSize = 14.sp,
                        color = if (isDark) Color.LightGray else Color.DarkGray
                    )
                }

                Spacer(Modifier.height(16.dp))

                // 🔹 Drawer Items - Pass isDark to DrawerItem
                DrawerItem(Icons.Default.Home, "Home", isDark,isSelected = selectedScreen == "home") {
                    scope.launch { drawerState.close() }
                    selectedScreen = "home"
                }
                DrawerItem(Icons.Default.UploadFile, "Upload Resume", isDark,isSelected = selectedScreen == "upload") {
                    scope.launch { drawerState.close() }
                    selectedScreen = "upload"
                }
                DrawerItem(Icons.Default.BarChart, "ATS Score", isDark,isSelected = selectedScreen == "ats") {
                    scope.launch { drawerState.close() }
                    selectedScreen = "ats"
                }
                DrawerItem(Icons.Default.TipsAndUpdates, "Suggestions", isDark,isSelected = selectedScreen == "suggestions") {
                    scope.launch { drawerState.close() }
                    selectedScreen = "suggestions"
                }
                DrawerItem(Icons.Default.History, "History", isDark,isSelected = selectedScreen == "history") {
                    scope.launch { drawerState.close() }
                    selectedScreen = "history"
                }

                Spacer(modifier = Modifier.weight(1f))

                Divider(color = if (isDark) Color.Gray else Color.LightGray)

                // 🔹 Bottom: Settings + Logout
                DrawerItem(Icons.Default.Settings, "Settings", isDark,isSelected = false) {
                    scope.launch { drawerState.close() }
                    navController.navigate("settings")
                }

                DrawerItem(Icons.Default.Logout, "Logout", isDark,isSelected = false) {
                    scope.launch {
                        UserPreference.clearUser(context)
                    }
                    navController.navigate("login") {
                        popUpTo("studentMain") { inclusive = true }
                    }
                }
            }
        }
    ) {
        Scaffold(
            topBar = {
                TopAppBar(
                    //title = { Text("Resume Analyser") },
                    title = {
                        Text(getScreenTitle(selectedScreen))
                    },
                    navigationIcon = {
                        IconButton(onClick = {
                            scope.launch { drawerState.open() }
                        }) {
                            Icon(Icons.Default.Menu, contentDescription = "Menu")
                        }
                    }
                )
            }
        ) { innerPadding ->
            when (selectedScreen) {
                "home" -> HomeDashboardContent(
                    userEmail = userEmail,
                    theme = user.theme,
                    modifier = Modifier.padding(innerPadding),
                    onNavigate = { screen -> selectedScreen = screen }
                )
//                "upload" -> Text("Upload Resume Screen Coming Soon", modifier = Modifier.padding(innerPadding))
//                "ats" -> Text("ATS Score Screen Coming Soon", modifier = Modifier.padding(innerPadding))
//                "suggestions" -> Text("Suggestions Screen Coming Soon", modifier = Modifier.padding(innerPadding))
//                "history" -> Text("History Screen Coming Soon", modifier = Modifier.padding(innerPadding))
                "upload" -> PlaceholderScreen(
                    title = "Upload Resume",
                    description = "Upload your resume for analysis",
                    modifier = Modifier.padding(innerPadding),
                    isDark = isDark
                )
                "ats" -> PlaceholderScreen(
                    title = "ATS Score",
                    description = "View your resume's ATS compatibility score",
                    modifier = Modifier.padding(innerPadding),
                    isDark = isDark
                )
                "suggestions" -> PlaceholderScreen(
                    title = "Suggestions",
                    description = "Get personalized suggestions to improve your resume",
                    modifier = Modifier.padding(innerPadding),
                    isDark = isDark
                )
                "history" -> PlaceholderScreen(
                    title = "History",
                    description = "View your past resume analysis history",
                    modifier = Modifier.padding(innerPadding),
                    isDark = isDark
                )
            }
        }
    }
}

@Composable
fun DrawerItem(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    text: String,
    isDark: Boolean, // Add isDark parameter
    isSelected: Boolean = false,
    onClick: () -> Unit
) {
    NavigationDrawerItem(
        label = {
            Text(
                text,
                fontSize = 16.sp,
                color = if (isDark) Color.White else Color.Black
            )
        },
        selected = isSelected,
        onClick = onClick,
        icon = {
            Icon(
                icon,
                contentDescription = text,
                tint = if (isDark) Color.White else Color.Black
            )
        },
        colors = NavigationDrawerItemDefaults.colors(
            unselectedContainerColor = Color.Transparent,
            selectedContainerColor = if (isDark) Color(0xFF6C63FF).copy(alpha = 0.3f) else Color(0xFF6C63FF).copy(alpha = 0.2f)
        ),
        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
    )
}

@Composable
fun PlaceholderScreen(
    title: String,
    description: String,
    modifier: Modifier = Modifier,
    isDark: Boolean
) {
    val colorScheme = if (isDark) darkColorScheme() else lightColorScheme()

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            Icons.Default.Construction,
            contentDescription = null,
            modifier = Modifier.size(64.dp),
            tint = colorScheme.primary
        )
        Spacer(Modifier.height(16.dp))
        Text(
            title,
            style = MaterialTheme.typography.headlineMedium,
            color = colorScheme.onBackground
        )
        Spacer(Modifier.height(8.dp))
        Text(
            description,
            style = MaterialTheme.typography.bodyLarge,
            color = colorScheme.onSurfaceVariant,
            textAlign = androidx.compose.ui.text.style.TextAlign.Center
        )
        Spacer(Modifier.height(24.dp))
        Text(
            "Coming Soon...",
            style = MaterialTheme.typography.bodyMedium,
            color = colorScheme.primary
        )
    }
}

fun getScreenTitle(screen: String): String {
    return when (screen) {
        "home" -> "Resume Analyser"
        "upload" -> "Upload Resume"
        "ats" -> "ATS Score"
        "suggestions" -> "Suggestions"
        "history" -> "History"
        else -> "Resume Analyser"
    }
}
@Composable
fun HomeDashboardContent(userEmail: String, theme: String = "system",modifier: Modifier = Modifier, onNavigate: (String) -> Unit) {
    val isDark = when (theme) {
        "light" -> false
        "dark" -> true
        else -> isSystemInDarkTheme()
    }

    val backgroundBrush = if (!isDark) {
        Brush.verticalGradient(
            listOf(Color(0xFF64B5F6), Color(0xFF9575CD), Color(0xFFD1C4E9))

        )
    } else {
        Brush.verticalGradient(
            listOf(Color(0xFF1E1E2F), Color(0xFF3E3E55), Color(0xFF5E5E7A))
        )
    }

    val colorScheme = if (isDark) darkColorScheme() else lightColorScheme()

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(backgroundBrush)
            .padding(16.dp)
    ) {
        Text(
            text = "Welcome back, ${userEmail.substringBefore('@')} 👋",
            fontSize = 28.sp,
            fontWeight = FontWeight.ExtraBold,
            color = if (!isDark) Color.White else colorScheme.onBackground
        )


        Spacer(Modifier.height(16.dp))
        Text("Quick Actions", fontSize = 20.sp, fontWeight = FontWeight.SemiBold,
            color = if (!isDark) Color.White else Color(0xFFF1F1F1)
        )

        Spacer(Modifier.height(8.dp))
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
//            QuickActionCard("Upload Resume", Icons.Default.UploadFile, isDark) { onNavigate("upload") }
//            QuickActionCard("ATS Score", Icons.Default.BarChart, isDark) { onNavigate("ats") }
//            QuickActionCard("Suggestions", Icons.Default.TipsAndUpdates, isDark) { onNavigate("suggestions") }

            QuickActionCard("Upload Resume", Icons.Default.UploadFile, isDark) { onNavigate("upload") }
            QuickActionCard("ATS Score", Icons.Default.BarChart, isDark) { onNavigate("ats") }
            QuickActionCard("Suggestions", Icons.Default.TipsAndUpdates, isDark) { onNavigate("suggestions") }

        }

        Spacer(Modifier.height(24.dp))
        Text("Your Stats", fontSize = 20.sp, fontWeight = FontWeight.SemiBold,
            color = if (!isDark) Color.White else Color(0xFFF1F1F1)
        )

        Spacer(Modifier.height(8.dp))
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = if (!isDark) Color.White else colorScheme.surfaceVariant)
        ) {
            Column(Modifier.padding(16.dp)) {
                Text("No stats available yet", color = if (!isDark) Color.Gray else colorScheme.onSurfaceVariant)
            }
        }

        Spacer(Modifier.height(24.dp))
        Text("Recent History", fontSize = 20.sp, fontWeight = FontWeight.SemiBold,
            color = if (!isDark) Color.White else Color(0xFFF1F1F1)
        )

        Spacer(Modifier.height(8.dp))
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = if (!isDark) Color.White else colorScheme.surfaceVariant)
        ) {
            Column(Modifier.padding(16.dp)) {
                Text("No resumes analyzed yet", color = if (!isDark) Color.Gray else colorScheme.onSurfaceVariant)
            }
        }
    }
}

@Composable
fun QuickActionCard(title: String, icon: androidx.compose.ui.graphics.vector.ImageVector, isDark: Boolean, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .size(100.dp)
            .padding(4.dp),
        shape = RoundedCornerShape(16.dp),
        onClick = onClick,
        colors = CardDefaults.cardColors(containerColor = if (!isDark) Color.White.copy(alpha = 0.9f) else MaterialTheme.colorScheme.surfaceVariant)
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(icon, contentDescription = title, tint = if (!isDark) Color(0xFF6C63FF) else MaterialTheme.colorScheme.primary)
            Spacer(Modifier.height(4.dp))
            Text(title, fontSize = 12.sp, color = if (!isDark) Color.Black else MaterialTheme.colorScheme.onSurface)
        }
    }
}
