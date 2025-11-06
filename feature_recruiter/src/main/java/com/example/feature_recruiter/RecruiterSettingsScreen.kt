//
//package com.example.feature_recruiter
//
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
//import androidx.navigation.NavController
//import com.example.resumeanalyzer.core.navigation.datastore.UserPreference
//import com.example.resumeanalyzer.core.navigation.datastore.UserCache
//import kotlinx.coroutines.launch
//
//@OptIn(ExperimentalMaterial3Api::class)
//@Composable
//fun RecruiterSettingsScreen(
//    userEmail: String,
//    onBack: () -> Unit,
//    onThemeChange: (String) -> Unit,
//    navController: NavController
//) {
//    val context = LocalContext.current
//    val scope = rememberCoroutineScope()
//
//    val userState by UserPreference.getUser(context).collectAsState(initial = UserCache())
//
//    var companyName by remember { mutableStateOf("") }
//    var email by remember { mutableStateOf("") }
//    var selectedTheme by remember { mutableStateOf("system") }
//    var notificationEnabled by remember { mutableStateOf(true) }
//
//    var emailError by remember { mutableStateOf<String?>(null) }
//
//    LaunchedEffect(userState) {
//        userState?.let { user ->
//            email = user.email ?: userEmail
//            companyName = user.name ?: ""
//            selectedTheme = user.theme
//            notificationEnabled = user.notificationsEnabled
//        }
//    }
//
//    val snackbarHostState = remember { SnackbarHostState() }
//
//    Scaffold(
//        topBar = {
//            TopAppBar(
//                title = {
//                    Text(
//                        "Settings",
//                        style = MaterialTheme.typography.headlineMedium.copy(
//                            fontWeight = FontWeight.Bold
//                        )
//                    )
//                },
//                navigationIcon = {
//                    IconButton(onClick = onBack) {
//                        Icon(
//                            Icons.Default.ArrowBack,
//                            contentDescription = "Back",
//                            tint = MaterialTheme.colorScheme.onSurface
//                        )
//                    }
//                },
//                colors = TopAppBarDefaults.topAppBarColors(
//                    containerColor = Color(0xFF4A90E2)
//                )
//            )
//        },
//        snackbarHost = { SnackbarHost(snackbarHostState) }
//    ) { innerPadding ->
//        LazyColumn(
//            modifier = Modifier
//                .padding(innerPadding)
//                .fillMaxSize(),
//            contentPadding = PaddingValues(16.dp),
//            verticalArrangement = Arrangement.spacedBy(20.dp)
//        ) {
//            // Profile Section
//            item {
//                Card(
//                    modifier = Modifier
//                        .fillMaxWidth()
//                        .shadow(8.dp, RoundedCornerShape(20.dp)),
//                    shape = RoundedCornerShape(20.dp),
//                    colors = CardDefaults.cardColors(
//                        containerColor = Color(0xFF4A90E2).copy(alpha = 0.15f)
//                    )
//                ) {
//                    Column(
//                        modifier = Modifier.padding(24.dp)
//                    ) {
//                        Row(
//                            modifier = Modifier.fillMaxWidth(),
//                            verticalAlignment = Alignment.CenterVertically
//                        ) {
//                            Icon(
//                                Icons.Default.Business,
//                                contentDescription = "Profile",
//                                modifier = Modifier.size(64.dp),
//                                tint = Color(0xFF4A90E2)
//                            )
//                            Spacer(modifier = Modifier.width(16.dp))
//                            Column(modifier = Modifier.weight(1f)) {
//                                Text(
//                                    text = "Recruiter",
//                                    style = MaterialTheme.typography.headlineSmall.copy(
//                                        fontWeight = FontWeight.Bold
//                                    ),
//                                    color = Color(0xFF4A90E2)
//                                )
//                                Text(
//                                    text = "Account Settings",
//                                    style = MaterialTheme.typography.bodyLarge,
//                                    color = Color.Gray
//                                )
//                            }
//                        }
//
//                        Spacer(modifier = Modifier.height(20.dp))
//
//                        // Company Name Field
//                        OutlinedTextField(
//                            value = companyName,
//                            onValueChange = { companyName = it },
//                            label = { Text("Company Name") },
//                            modifier = Modifier.fillMaxWidth(),
//                            shape = RoundedCornerShape(12.dp),
//                            leadingIcon = {
//                                Icon(Icons.Default.Business, contentDescription = null)
//                            }
//                        )
//
//                        Spacer(modifier = Modifier.height(12.dp))
//
//                        // Email Field
//                        OutlinedTextField(
//                            value = email,
//                            onValueChange = { email = it },
//                            label = { Text("Email Address") },
//                            modifier = Modifier.fillMaxWidth(),
//                            shape = RoundedCornerShape(12.dp),
//                            leadingIcon = {
//                                Icon(Icons.Default.Email, contentDescription = null)
//                            },
//                            isError = emailError != null
//                        )
//
//                        if (emailError != null) {
//                            Text(
//                                text = emailError ?: "",
//                                color = MaterialTheme.colorScheme.error,
//                                style = MaterialTheme.typography.bodySmall,
//                                modifier = Modifier.padding(top = 4.dp)
//                            )
//                        }
//                    }
//                }
//            }
//
//            // Theme Card
//            item {
//                Card(
//                    modifier = Modifier
//                        .fillMaxWidth()
//                        .shadow(6.dp, RoundedCornerShape(16.dp)),
//                    shape = RoundedCornerShape(16.dp),
//                    colors = CardDefaults.cardColors(
//                        containerColor = MaterialTheme.colorScheme.surfaceVariant
//                    )
//                ) {
//                    RecruiterThemeItem(
//                        selectedTheme = selectedTheme,
//                        onThemeSelected = {
//                            selectedTheme = it
//                            scope.launch { UserPreference.saveTheme(context, it) }
//                            onThemeChange(it)
//                        }
//                    )
//                }
//            }
//
//            // Notifications Card
//            item {
//                Card(
//                    modifier = Modifier
//                        .fillMaxWidth()
//                        .shadow(6.dp, RoundedCornerShape(16.dp)),
//                    shape = RoundedCornerShape(16.dp),
//                    colors = CardDefaults.cardColors(
//                        containerColor = MaterialTheme.colorScheme.surfaceVariant
//                    )
//                ) {
//                    Row(
//                        modifier = Modifier
//                            .padding(20.dp)
//                            .fillMaxWidth(),
//                        verticalAlignment = Alignment.CenterVertically,
//                        horizontalArrangement = Arrangement.SpaceBetween
//                    ) {
//                        Row(verticalAlignment = Alignment.CenterVertically) {
//                            Icon(
//                                Icons.Default.Notifications,
//                                contentDescription = "Notifications",
//                                tint = Color(0xFF4A90E2),
//                                modifier = Modifier.size(28.dp)
//                            )
//                            Spacer(Modifier.width(16.dp))
//                            Column {
//                                Text(
//                                    text = "Notifications",
//                                    fontSize = 18.sp,
//                                    fontWeight = FontWeight.SemiBold
//                                )
//                                Text(
//                                    text = "Receive resume upload alerts",
//                                    style = MaterialTheme.typography.bodyMedium,
//                                    color = MaterialTheme.colorScheme.onSurfaceVariant
//                                )
//                            }
//                        }
//                        Switch(
//                            checked = notificationEnabled,
//                            onCheckedChange = {
//                                notificationEnabled = it
//                                scope.launch { UserPreference.saveNotification(context, it) }
//                            }
//                        )
//                    }
//                }
//            }
//
//            // Save Changes Button
//            item {
//                Button(
//                    onClick = {
//                        scope.launch {
//                            UserPreference.saveUser(context, email, "Recruiter", companyName)
//                            snackbarHostState.showSnackbar(
//                                message = "Settings saved successfully",
//                                duration = SnackbarDuration.Short
//                            )
//                            kotlinx.coroutines.delay(300)
//                            navController.popBackStack()
//                        }
//                    },
//                    modifier = Modifier
//                        .fillMaxWidth()
//                        .height(60.dp)
//                        .shadow(12.dp, RoundedCornerShape(30.dp)),
//                    shape = RoundedCornerShape(30.dp),
//                    colors = ButtonDefaults.buttonColors(
//                        containerColor = Color(0xFF4A90E2)
//                    )
//                ) {
//                    Icon(
//                        Icons.Default.Save,
//                        contentDescription = "Save",
//                        modifier = Modifier.size(24.dp)
//                    )
//                    Spacer(modifier = Modifier.width(12.dp))
//                    Text(
//                        "Save Settings",
//                        fontWeight = FontWeight.Bold,
//                        fontSize = 18.sp
//                    )
//                }
//            }
//
//            item {
//                Spacer(modifier = Modifier.height(16.dp))
//            }
//        }
//    }
//}
//
//@Composable
//fun RecruiterThemeItem(selectedTheme: String, onThemeSelected: (String) -> Unit) {
//    Column(modifier = Modifier.padding(20.dp)) {
//        Row(verticalAlignment = Alignment.CenterVertically) {
//            Icon(
//                Icons.Default.Palette,
//                contentDescription = "Theme",
//                tint = Color(0xFF4A90E2),
//                modifier = Modifier.size(28.dp)
//            )
//            Spacer(Modifier.width(16.dp))
//            Column {
//                Text(
//                    "Theme",
//                    fontSize = 18.sp,
//                    fontWeight = FontWeight.SemiBold
//                )
//                Text(
//                    "Customize app appearance",
//                    style = MaterialTheme.typography.bodyMedium,
//                    color = MaterialTheme.colorScheme.onSurfaceVariant
//                )
//            }
//        }
//
//        Spacer(Modifier.height(20.dp))
//
//        Row(
//            modifier = Modifier.fillMaxWidth(),
//            horizontalArrangement = Arrangement.SpaceEvenly
//        ) {
//            RecruiterThemeChip("Light", selectedTheme == "light") { onThemeSelected("light") }
//            RecruiterThemeChip("Dark", selectedTheme == "dark") { onThemeSelected("dark") }
//            RecruiterThemeChip("System", selectedTheme == "system") { onThemeSelected("system") }
//        }
//    }
//}
//
//@Composable
//fun RecruiterThemeChip(text: String, selected: Boolean, onClick: () -> Unit) {
//    FilterChip(
//        onClick = onClick,
//        label = {
//            Text(
//                text,
//                fontWeight = if (selected) FontWeight.SemiBold else FontWeight.Medium,
//                fontSize = 14.sp
//            )
//        },
//        selected = selected,
//        leadingIcon = if (selected) {
//            {
//                Icon(
//                    Icons.Default.Check,
//                    contentDescription = null,
//                    modifier = Modifier.size(18.dp)
//                )
//            }
//        } else null,
//        modifier = Modifier.shadow(if (selected) 6.dp else 3.dp, RoundedCornerShape(16.dp))
//    )
//}

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
import androidx.navigation.NavController
import com.example.resumeanalyzer.core.navigation.datastore.UserPreference
import com.example.resumeanalyzer.core.navigation.datastore.UserCache
import com.example.feature_recruiter.util.DatabaseHelper
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RecruiterSettingsScreen(
    userEmail: String,
    onBack: () -> Unit,
    onThemeChange: (String) -> Unit,
    navController: NavController
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    val userState by UserPreference.getUser(context).collectAsState(initial = UserCache())

    var companyName by remember { mutableStateOf("") }
    var recruiterName by remember { mutableStateOf("") }
    var selectedTheme by remember { mutableStateOf("system") }
    var notificationEnabled by remember { mutableStateOf(true) }

    // Edit states
    var isEditingCompany by remember { mutableStateOf(false) }
    var editedCompanyName by remember { mutableStateOf("") }
    var showSaveSuccess by remember { mutableStateOf(false) }

    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(userState) {
        userState?.let { user ->
            selectedTheme = user.theme
            notificationEnabled = user.notificationsEnabled

            // Load recruiter profile from database
            val db = DatabaseHelper.getDatabase(context)
            val profile = db.recruiterProfileDao().getProfileByEmail(userEmail)
            if (profile != null) {
                companyName = profile.companyName
                recruiterName = profile.recruiterName
                editedCompanyName = profile.companyName
            }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "Settings",
                        style = MaterialTheme.typography.headlineMedium.copy(
                            fontWeight = FontWeight.Bold
                        )
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
                )
            )
        },
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .background(Color(0xFFF5F6FA)),
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
                    colors = CardDefaults.cardColors(containerColor = Color.White)
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
                                        recruiterName.ifEmpty { "Recruiter" },
                                        fontSize = 18.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = Color.White
                                    )
                                    Text(
                                        companyName.ifEmpty { "Company" },
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
                    colors = CardDefaults.cardColors(containerColor = Color.White)
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
                                        color = Color.Black
                                    )
                                    Text(
                                        "Update your company details",
                                        fontSize = 12.sp,
                                        color = Color.Gray
                                    )
                                }
                            }
                            if (!isEditingCompany) {
                                IconButton(
                                    onClick = {
                                        isEditingCompany = true
                                        editedCompanyName = companyName
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
                        if (isEditingCompany) {
                            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                                OutlinedTextField(
                                    value = editedCompanyName,
                                    onValueChange = { editedCompanyName = it },
                                    label = { Text("Company Name") },
                                    modifier = Modifier.fillMaxWidth(),
                                    shape = RoundedCornerShape(12.dp),
                                    colors = OutlinedTextFieldDefaults.colors(
                                        focusedBorderColor = Color(0xFF4A90E2),
                                        unfocusedBorderColor = Color.Gray.copy(0.3f)
                                    ),
                                    singleLine = true
                                )

                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                                ) {
                                    Button(
                                        onClick = {
                                            if (editedCompanyName.isNotBlank()) {
                                                scope.launch {
                                                    val db = DatabaseHelper.getDatabase(context)
                                                    val profile = db.recruiterProfileDao()
                                                        .getProfileByEmail(userEmail)
                                                    if (profile != null) {
                                                        db.recruiterProfileDao().updateProfile(
                                                            profile.copy(
                                                                companyName = editedCompanyName
                                                            )
                                                        )
                                                        companyName = editedCompanyName
                                                        isEditingCompany = false
                                                        showSaveSuccess = true
                                                        snackbarHostState.showSnackbar(
                                                            message = "Company information updated",
                                                            duration = SnackbarDuration.Short
                                                        )
                                                    }
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
                                color = Color(0xFFF5F6FA)
                            ) {
                                Text(
                                    companyName.ifEmpty { "Not set" },
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(12.dp),
                                    fontSize = 14.sp,
                                    color = if (companyName.isEmpty()) Color.Gray else Color.Black
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
                    colors = CardDefaults.cardColors(containerColor = Color.White)
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
                                    color = Color.Black
                                )
                                Text(
                                    "Choose your preferred appearance",
                                    fontSize = 12.sp,
                                    color = Color.Gray
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
                                onClick = {
                                    selectedTheme = "light"
                                    scope.launch { UserPreference.saveTheme(context, "light") }
                                    onThemeChange("light")
                                },
                                modifier = Modifier.weight(1f)
                            )

                            ThemeOption(
                                label = "Dark",
                                isSelected = selectedTheme == "dark",
                                icon = Icons.Default.DarkMode,
                                onClick = {
                                    selectedTheme = "dark"
                                    scope.launch { UserPreference.saveTheme(context, "dark") }
                                    onThemeChange("dark")
                                },
                                modifier = Modifier.weight(1f)
                            )

                            ThemeOption(
                                label = "System",
                                isSelected = selectedTheme == "system",
                                icon = Icons.Default.SettingsBrightness,
                                onClick = {
                                    selectedTheme = "system"
                                    scope.launch { UserPreference.saveTheme(context, "system") }
                                    onThemeChange("system")
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
                    colors = CardDefaults.cardColors(containerColor = Color.White)
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
                                    color = Color.Black
                                )
                                Text(
                                    "Resume upload alerts",
                                    fontSize = 12.sp,
                                    color = Color.Gray
                                )
                            }
                        }
                        Switch(
                            checked = notificationEnabled,
                            onCheckedChange = {
                                notificationEnabled = it
                                scope.launch { UserPreference.saveNotification(context, it) }
                            },
                            modifier = Modifier.scale(1.2f),
                            colors = SwitchDefaults.colors(
                                checkedThumbColor = Color(0xFF4A90E2)
                            )
                        )
                    }
                }
            }

            // Account Info Section
            item {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .shadow(6.dp, RoundedCornerShape(16.dp)),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White)
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
                            color = Color.Black
                        )

                        InfoRow("Email", userEmail)
                        InfoRow("Name", recruiterName.ifEmpty { "Not set" })
                        InfoRow("Role", "Recruiter")
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
            containerColor = if (isSelected) Color(0xFF4A90E2) else Color(0xFFF5F6FA),
            contentColor = if (isSelected) Color.White else Color.Black
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
fun InfoRow(label: String, value: String) {
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
            color = Color.Gray
        )
        Text(
            value,
            fontSize = 13.sp,
            fontWeight = FontWeight.SemiBold,
            color = Color.Black
        )
    }
}