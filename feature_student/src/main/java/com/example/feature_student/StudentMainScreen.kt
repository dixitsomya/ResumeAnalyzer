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

package com.example.feature_student

import androidx.compose.foundation.background
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.launch
import androidx.navigation.NavController

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StudentMainScreen(
    userEmail: String,
    navController: NavController,
    onNavigate: (String) -> Unit
) {
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    var showLogoutMenu by remember { mutableStateOf(false) }

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet(
                modifier = Modifier
                    .fillMaxHeight()
                    .width(320.dp),
                drawerContainerColor = Color(0xFFF5F6FA)
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
                    Text(userEmail, fontSize = 14.sp, color = Color.DarkGray)
                }

                Spacer(Modifier.height(16.dp))

                // 🔹 Drawer Items
                DrawerItem(Icons.Default.Home, "Home") { onNavigate("home") }
                DrawerItem(Icons.Default.UploadFile, "Upload Resume") { onNavigate("upload") }
                DrawerItem(Icons.Default.BarChart, "ATS Score") { onNavigate("ats") }
                DrawerItem(Icons.Default.TipsAndUpdates, "Suggestions") { onNavigate("suggestions") }
                DrawerItem(Icons.Default.History, "History") { onNavigate("history") }

                Spacer(modifier = Modifier.weight(1f))

                Divider()

                // 🔹 Bottom: Settings + Logout
                DrawerItem(Icons.Default.Settings, "Settings") { onNavigate("settings") }

                DrawerItem(Icons.Default.Logout, "Logout") {
                    navController.navigate("login") {
                        popUpTo("studentMain/$userEmail") { inclusive = true }
                    }
                }

            }
        }
    ) {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text("Resume Analyser") },
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
            HomeDashboardContent(
                userEmail = userEmail,
                modifier = Modifier.padding(innerPadding),
                onNavigate = onNavigate
            )
        }
    }
}

@Composable
fun DrawerItem(icon: androidx.compose.ui.graphics.vector.ImageVector, text: String, onClick: () -> Unit) {
    NavigationDrawerItem(
        label = { Text(text, fontSize = 16.sp) },
        selected = false,
        onClick = onClick,
        icon = { Icon(icon, contentDescription = text) },
        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
    )
}

@Composable
fun HomeDashboardContent(userEmail: String, modifier: Modifier = Modifier, onNavigate: (String) -> Unit) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    listOf(Color(0xFF6C63FF), Color(0xFFB388FF))
                )
            )
            .padding(16.dp)
    ) {
        // 🔹 Welcome
        Text(
            text = "Welcome back, ${userEmail.substringBefore('@')} 👋",
            color = Color.White,
            fontWeight = FontWeight.Bold,
            fontSize = 22.sp
        )
        Spacer(Modifier.height(16.dp))

        // 🔹 Quick Actions
        Text("Quick Actions", color = Color.White, fontSize = 18.sp)
        Spacer(Modifier.height(8.dp))
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            QuickActionCard("Upload Resume", Icons.Default.UploadFile) { onNavigate("upload") }
            QuickActionCard("ATS Score", Icons.Default.BarChart) { onNavigate("ats") }
            QuickActionCard("Suggestions", Icons.Default.TipsAndUpdates) { onNavigate("suggestions") }
        }

        Spacer(Modifier.height(24.dp))

        // 🔹 Stats Section
        Text("Your Stats", color = Color.White, fontSize = 18.sp)
        Spacer(Modifier.height(8.dp))
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White)
        ) {
            Column(Modifier.padding(16.dp)) {
                // Agar data nahi mila → placeholder text
                Text("No stats available yet", color = Color.Gray)
            }
        }

        Spacer(Modifier.height(24.dp))

        // 🔹 Recent History
        Text("Recent History", color = Color.White, fontSize = 18.sp)
        Spacer(Modifier.height(8.dp))
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White)
        ) {
            Column(Modifier.padding(16.dp)) {
                // Agar history nahi hai → placeholder
                Text("No resumes analyzed yet", color = Color.Gray)
            }
        }
    }
}

@Composable
fun QuickActionCard(title: String, icon: androidx.compose.ui.graphics.vector.ImageVector, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .size(100.dp)
            .padding(4.dp),
        shape = RoundedCornerShape(16.dp),
        onClick = onClick,
        colors = CardDefaults.cardColors(containerColor = Color.White.copy(alpha = 0.9f))
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(icon, contentDescription = title, tint = Color(0xFF6C63FF))
            Spacer(Modifier.height(4.dp))
            Text(title, fontSize = 12.sp)
        }
    }
}
