//package com.example.feature_student
//
//import androidx.compose.foundation.clickable
//import androidx.compose.foundation.isSystemInDarkTheme
//import androidx.compose.foundation.layout.*
//import androidx.compose.foundation.shape.RoundedCornerShape
//import androidx.compose.material.icons.Icons
//import androidx.compose.material.icons.filled.*
//import androidx.compose.material3.*
//import androidx.compose.runtime.*
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.unit.dp
//import androidx.compose.ui.Alignment
//import androidx.compose.ui.text.font.FontWeight
//import androidx.compose.ui.unit.sp
//import androidx.compose.ui.platform.LocalContext
//import androidx.navigation.NavController
//import com.example.resumeanalyzer.core.navigation.datastore.UserPreference
//import com.example.resumeanalyzer.core.database.DatabaseModule
//import com.example.resumeanalyzer.core.navigation.datastore.UserCache
//import kotlinx.coroutines.launch
//
//@OptIn(ExperimentalMaterial3Api::class)
//@Composable
//fun SettingsScreen(
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
//    var name by remember { mutableStateOf("") }
//    var email by remember { mutableStateOf("") }
//    var role by remember { mutableStateOf("Student") }
//    var notificationEnabled by remember { mutableStateOf(true) }
//    var selectedTheme by remember { mutableStateOf("system") }
//
//    // Update state when userState changes
//    LaunchedEffect(userState) {
//        userState?.let { user ->
//            name = user.name ?: ""
//            email = user.email ?: userEmail
//            role = user.role ?: "Student"
//            notificationEnabled = user.notificationsEnabled
//            selectedTheme = user.theme
//        }
//    }
//
//    // Load from database with better condition
//    LaunchedEffect(userState?.email) {
//        val emailToUse = userState?.email ?: userEmail
//        if (emailToUse.isNotEmpty()) {
//            val db = DatabaseModule.provideDatabase(context)
//            val userDao = DatabaseModule.provideUserDao(db)
//            val dbUser = userDao.getUserByEmail(emailToUse)
//            dbUser?.let {
//                name = it.name
//                email = it.email
//                role = it.role
//
//                // Also update UserPreference with database name if it's different
//                if (it.name != userState?.name) {
//                    scope.launch {
//                        UserPreference.saveUser(context, it.email, it.role, it.name)
//                    }
//                }
//            }
//        }
//    }
//
//    var expanded by remember { mutableStateOf(false) }
//    var isEditingName by remember { mutableStateOf(false) }
//    var isEditingEmail by remember { mutableStateOf(false) }
//
//    Scaffold(
//        topBar = {
//            TopAppBar(
//                title = { Text("Settings") },
//                navigationIcon = {
//                    IconButton(onClick = onBack) {
//                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
//                    }
//                }
//            )
//        }
//    ) { innerPadding ->
//        Column(
//            modifier = Modifier
//                .padding(innerPadding)
//                .padding(16.dp)
//                .fillMaxSize(),
//            verticalArrangement = Arrangement.spacedBy(16.dp)
//        ) {
//            // 🔹 Name Card
//            Card(
//                modifier = Modifier.fillMaxWidth(),
//                shape = RoundedCornerShape(12.dp),
//                elevation = CardDefaults.cardElevation(4.dp)
//            ) {
//                Row(
//                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
//                    verticalAlignment = Alignment.CenterVertically
//                ) {
//                    if (isEditingName) {
//                        OutlinedTextField(
//                            value = name,
//                            onValueChange = { name = it },
//                            label = { Text("Name") },
//                            modifier = Modifier.weight(1f)
//                        )
//                        IconButton(onClick = { isEditingName = false }) {
//                            Icon(Icons.Default.Check, contentDescription = "Save Name")
//                        }
//                    } else {
//                        Text(
//                            text = name.ifEmpty { "Add your name" },
//                            fontSize = 16.sp,
//                            fontWeight = FontWeight.Medium,
//                            modifier = Modifier.weight(1f)
//                        )
//                        IconButton(onClick = { isEditingName = true }) {
//                            Icon(Icons.Default.Edit, contentDescription = "Edit Name")
//                        }
//                    }
//                }
//            }
//
//            // 🔹 Email Card
//            Card(
//                modifier = Modifier.fillMaxWidth(),
//                shape = RoundedCornerShape(12.dp),
//                elevation = CardDefaults.cardElevation(4.dp)
//            ) {
//                Row(
//                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
//                    verticalAlignment = Alignment.CenterVertically
//                ) {
//                    if (isEditingEmail) {
//                        OutlinedTextField(
//                            value = email,
//                            onValueChange = { email = it },
//                            label = { Text("Email") },
//                            modifier = Modifier.weight(1f)
//                        )
//                        IconButton(onClick = { isEditingEmail = false }) {
//                            Icon(Icons.Default.Check, contentDescription = "Save Email")
//                        }
//                    } else {
//                        Text(email, fontSize = 16.sp, fontWeight = FontWeight.Medium, modifier = Modifier.weight(1f))
//                        IconButton(onClick = { isEditingEmail = true }) {
//                            Icon(Icons.Default.Edit, contentDescription = "Edit Email")
//                        }
//                    }
//                }
//            }
//
//            // 🔹 Role Card
//            Card(
//                modifier = Modifier.fillMaxWidth(),
//                shape = RoundedCornerShape(12.dp),
//                elevation = CardDefaults.cardElevation(4.dp)
//            ) {
//                Row(
//                    modifier = Modifier
//                        .padding(horizontal = 12.dp, vertical = 8.dp)
//                        .fillMaxWidth(),
//                    verticalAlignment = Alignment.CenterVertically
//                ) {
//                    Text("Role: ", fontWeight = FontWeight.Medium)
//                    Spacer(Modifier.width(8.dp))
//                    Box {
//                        Text(
//                            role,
//                            modifier = Modifier
//                                .clickable { expanded = true }
//                                .padding(8.dp)
//                        )
//                        DropdownMenu(
//                            expanded = expanded,
//                            onDismissRequest = { expanded = false }
//                        ) {
//                            DropdownMenuItem(
//                                text = { Text("Student") },
//                                onClick = { role = "Student"; expanded = false }
//                            )
//                            DropdownMenuItem(
//                                text = { Text("Recruiter") },
//                                onClick = { role = "Recruiter"; expanded = false }
//                            )
//                        }
//                    }
//                }
//            }
//
//            // 🔹 Theme Card
//            Card(
//                modifier = Modifier.fillMaxWidth(),
//                shape = RoundedCornerShape(12.dp),
//                elevation = CardDefaults.cardElevation(4.dp)
//            ) {
//                SettingsThemeItem(
//                    selectedTheme = selectedTheme,
//                    onThemeSelected = {
//                        selectedTheme = it
//                        scope.launch { UserPreference.saveTheme(context, it) }
//                        onThemeChange(it)
//                    }
//                )
//            }
//
//            // 🔹 Notifications Card
//            Card(
//                modifier = Modifier.fillMaxWidth(),
//                shape = RoundedCornerShape(12.dp),
//                elevation = CardDefaults.cardElevation(4.dp)
//            ) {
//                Row(
//                    modifier = Modifier
//                        .padding(horizontal = 12.dp, vertical = 8.dp)
//                        .fillMaxWidth(),
//                    verticalAlignment = Alignment.CenterVertically,
//                    horizontalArrangement = Arrangement.SpaceBetween
//                ) {
//                    Row(verticalAlignment = Alignment.CenterVertically) {
//                        Icon(Icons.Default.Notifications, contentDescription = "Notifications")
//                        Spacer(Modifier.width(16.dp))
//                        Text("Notifications", fontSize = 16.sp, fontWeight = FontWeight.Medium)
//                    }
//                    Switch(
//                        checked = notificationEnabled,
//                        onCheckedChange = {
//                            notificationEnabled = it
//                            scope.launch { UserPreference.saveNotification(context, it) }
//                        }
//                    )
//                }
//            }
//
//            // 🔹 Save Button
//            val originalRole = userState?.role ?: role
//            Button(
//                onClick = {
//                    scope.launch {
//                        UserPreference.saveUser(context, email, role, name)
//                        val db = DatabaseModule.provideDatabase(context)
//                        val userDao = DatabaseModule.provideUserDao(db)
//                        val user = userDao.getUserByEmail(userState?.email ?: userEmail)
//                        if (user != null) {
//                            userDao.updateUser(user.id, name, email, role)
//                        }
//                        if (role != originalRole) {
//                            navController.navigate("login") { popUpTo("settings") { inclusive = true } }
//                        } else {
//                            navController.navigate("studentMain") { popUpTo("settings") { inclusive = true } }
//                        }
//                    }
//                },
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .height(50.dp),
//                shape = RoundedCornerShape(25.dp)
//            ) {
//                Text("Apply Changes", fontWeight = FontWeight.Bold)
//            }
//
//            // 🔹 Help Card
//            Card(
//                modifier = Modifier.fillMaxWidth(),
//                shape = RoundedCornerShape(12.dp),
//                elevation = CardDefaults.cardElevation(4.dp)
//            ) {
//                SettingsItem(
//                    icon = Icons.Default.Info,
//                    title = "Help & About",
//                    description = "Version 1.0.0 • Contact: support@example.com"
//                )
//            }
//        }
//    }
//}
//@Composable
//fun SettingsItem(icon: androidx.compose.ui.graphics.vector.ImageVector, title: String, description: String) {
//    Row(
//        modifier = Modifier
//            .fillMaxWidth()
//            .padding(horizontal = 12.dp, vertical = 8.dp),
//        verticalAlignment = Alignment.CenterVertically
//    ) {
//        Icon(icon, contentDescription = title)
//        Spacer(Modifier.width(16.dp))
//        Column {
//            Text(title, fontSize = 16.sp, fontWeight = FontWeight.Medium)
//            Text(description, fontSize = 13.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
//        }
//    }
//}
//
//@Composable
//fun SettingsThemeItem(selectedTheme: String, onThemeSelected: (String) -> Unit) {
//    val isSystemDark = isSystemInDarkTheme()
//
//    Column(modifier = Modifier.padding(12.dp)) {
//        Row(verticalAlignment = Alignment.CenterVertically) {
//            Icon(Icons.Default.Settings, contentDescription = "Theme")
//            Spacer(Modifier.width(16.dp))
//            Text("Theme", fontSize = 16.sp, fontWeight = FontWeight.Medium)
//        }
//
//        // Show current system theme status when system is selected
//        if (selectedTheme == "system") {
//            Text(
//                "Following system: ${if (isSystemDark) "Dark" else "Light"}",
//                fontSize = 12.sp,
//                color = MaterialTheme.colorScheme.onSurfaceVariant,
//                modifier = Modifier.padding(start = 40.dp, top = 4.dp)
//            )
//        }
//
//        Spacer(Modifier.height(8.dp))
//
//        Row(
//            modifier = Modifier.fillMaxWidth(),
//            horizontalArrangement = Arrangement.SpaceEvenly
//        ) {
//            ThemeChip("Light", selectedTheme == "light") { onThemeSelected("light") }
//            ThemeChip("Dark", selectedTheme == "dark") { onThemeSelected("dark") }
//            ThemeChip("System", selectedTheme == "system") { onThemeSelected("system") }
//        }
//
//        // Show what system currently is as a small text below chips
//        if (selectedTheme == "system") {
//            Text(
//                "Currently: ${if (isSystemDark) "Dark mode" else "Light mode"}",
//                fontSize = 11.sp,
//                color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f),
//                modifier = Modifier.padding(top = 4.dp)
//            )
//        }
//    }
//}
//
//@Composable
//fun ThemeChip(text: String, selected: Boolean, onClick: () -> Unit) {
//    AssistChip(
//        onClick = onClick,
//        label = {
//            Text(
//                text,
//                color = if (selected) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurface
//            )
//        },
//        leadingIcon = {
//            if (selected) Icon(Icons.Default.Check, contentDescription = null, tint = MaterialTheme.colorScheme.onPrimary)
//        },
//        colors = AssistChipDefaults.assistChipColors(
//            containerColor = if (selected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surface
//        )
//    )
//}

package com.example.feature_student

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.Alignment
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.draw.shadow
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.text.selection.TextSelectionColors
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavController
import com.example.resumeanalyzer.core.navigation.datastore.UserPreference
import com.example.resumeanalyzer.core.database.DatabaseModule
import com.example.resumeanalyzer.core.navigation.datastore.UserCache
import kotlinx.coroutines.launch

// Validation regex constants
private val EMAIL_REGEX = Regex("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.(com|org|net|in)$")
private val NAME_REGEX = Regex("^[A-Za-z ]{2,30}$")

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    userEmail: String,
    onBack: () -> Unit,
    onThemeChange: (String) -> Unit,
    navController: NavController
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    val userState by UserPreference.getUser(context).collectAsState(initial = UserCache())

    var name by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var role by remember { mutableStateOf("Student") }
    var notificationEnabled by remember { mutableStateOf(true) }
    var selectedTheme by remember { mutableStateOf("system") }

    // Error states for validation
    var nameError by remember { mutableStateOf<String?>(null) }
    var emailError by remember { mutableStateOf<String?>(null) }

    // Update state when userState changes
    LaunchedEffect(userState) {
        userState?.let { user ->
            name = user.name ?: ""
            email = user.email ?: userEmail
            role = user.role ?: "Student"
            notificationEnabled = user.notificationsEnabled
            selectedTheme = user.theme
        }
    }

    // Load from database with better condition
    LaunchedEffect(userState?.email) {
        val emailToUse = userState?.email ?: userEmail
        if (emailToUse.isNotEmpty()) {
            val db = DatabaseModule.provideDatabase(context)
            val userDao = DatabaseModule.provideUserDao(db)
            val dbUser = userDao.getUserByEmail(emailToUse)
            dbUser?.let {
                name = it.name
                email = it.email
                role = it.role

                // Also update UserPreference with database name if it's different
                if (it.name != userState?.name) {
                    scope.launch {
                        UserPreference.saveUser(context, it.email, it.role, it.name)
                    }
                }
            }
        }
    }

    var isEditingName by remember { mutableStateOf(false) }
    var isEditingEmail by remember { mutableStateOf(false) }

    // Validation functions
    fun validateName(inputName: String): String? {
        return when {
            inputName.isBlank() -> "Name is required"
            !NAME_REGEX.matches(inputName) -> "Enter a valid name (only letters, 2-30 chars)"
            else -> null
        }
    }

    fun validateEmail(inputEmail: String): String? {
        return when {
            inputEmail.isBlank() -> "Email is required"
            !EMAIL_REGEX.matches(inputEmail) -> "Invalid email address"
            else -> null
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
                            tint = MaterialTheme.colorScheme.onSurface
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface
                )
            )
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize(),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            // Profile Section
            item {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .shadow(8.dp, RoundedCornerShape(20.dp)),
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.primaryContainer
                    )
                ) {
                    Column(
                        modifier = Modifier.padding(24.dp)
                    ) {
                        // Profile Header
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                Icons.Default.AccountCircle,
                                contentDescription = "Profile",
                                modifier = Modifier.size(64.dp),
                                tint = MaterialTheme.colorScheme.onPrimaryContainer
                            )
                            Spacer(modifier = Modifier.width(16.dp))
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = "Profile",
                                    style = MaterialTheme.typography.headlineSmall.copy(
                                        fontWeight = FontWeight.Bold
                                    ),
                                    color = MaterialTheme.colorScheme.onPrimaryContainer
                                )
                                Text(
                                    text = role,
                                    style = MaterialTheme.typography.bodyLarge,
                                    color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.8f)
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(20.dp))

                        // Name Field
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                Icons.Default.Person,
                                contentDescription = "Name",
                                tint = MaterialTheme.colorScheme.onPrimaryContainer,
                                modifier = Modifier.size(24.dp)
                            )
                            Spacer(modifier = Modifier.width(12.dp))

                            if (isEditingName) {
                                Column(modifier = Modifier.weight(1f)) {
                                    OutlinedTextField(
                                        value = name,
                                        onValueChange = {
                                            name = it
                                            nameError = validateName(it)
                                        },
                                        label = { Text("Full Name") },
                                        isError = nameError != null,
                                        modifier = Modifier.fillMaxWidth(),
                                        shape = RoundedCornerShape(12.dp),
                                        colors = OutlinedTextFieldDefaults.colors(
                                            focusedBorderColor = MaterialTheme.colorScheme.onPrimaryContainer,
                                            unfocusedBorderColor = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.5f),
                                            cursorColor = MaterialTheme.colorScheme.onPrimaryContainer,           // Add this
                                            focusedTextColor = MaterialTheme.colorScheme.onPrimaryContainer,      // Add this
                                            unfocusedTextColor = MaterialTheme.colorScheme.onPrimaryContainer,
                                            selectionColors = TextSelectionColors(
                                                handleColor = MaterialTheme.colorScheme.onPrimaryContainer,
                                                backgroundColor = MaterialTheme.colorScheme.onPrimaryContainer.copy(
                                                    alpha = 0.3f
                                                )
                                            )
                                        )
                                    )
                                    nameError?.let { error ->
                                        Text(
                                            text = error,
                                            color = MaterialTheme.colorScheme.error,
                                            style = MaterialTheme.typography.bodySmall,
                                            modifier = Modifier.padding(top = 4.dp)
                                        )
                                    }
                                }
                                IconButton(
                                    onClick = {
                                        val error = validateName(name)
                                        if (error == null) {
                                            isEditingName = false
                                            nameError = null
                                        } else {
                                            nameError = error
                                        }
                                    }
                                ) {
                                    Icon(
                                        Icons.Default.Check,
                                        contentDescription = "Save Name",
                                        tint = MaterialTheme.colorScheme.onPrimaryContainer
                                    )
                                }
                            } else {
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(
                                        text = "Full Name",
                                        style = MaterialTheme.typography.bodySmall,
                                        color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.7f)
                                    )
                                    Text(
                                        text = name.ifEmpty { "Add your name" },
                                        fontSize = 16.sp,
                                        fontWeight = FontWeight.Medium,
                                        color = if (name.isEmpty()) MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.7f)
                                        else MaterialTheme.colorScheme.onPrimaryContainer
                                    )
                                }
                                IconButton(onClick = { isEditingName = true }) {
                                    Icon(
                                        Icons.Default.Edit,
                                        contentDescription = "Edit Name",
                                        tint = MaterialTheme.colorScheme.onPrimaryContainer
                                    )
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        // Email Field
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                Icons.Default.Email,
                                contentDescription = "Email",
                                tint = MaterialTheme.colorScheme.onPrimaryContainer,
                                modifier = Modifier.size(24.dp)
                            )
                            Spacer(modifier = Modifier.width(12.dp))

                            if (isEditingEmail) {
                                Column(modifier = Modifier.weight(1f)) {
                                    OutlinedTextField(
                                        value = email,
                                        onValueChange = {
                                            email = it
                                            emailError = validateEmail(it)
                                        },
                                        label = { Text("Email Address") },
                                        isError = emailError != null,
                                        modifier = Modifier.fillMaxWidth(),
                                        shape = RoundedCornerShape(12.dp),
                                        colors = OutlinedTextFieldDefaults.colors(
                                            focusedBorderColor = MaterialTheme.colorScheme.onPrimaryContainer,
                                            unfocusedBorderColor = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.5f),
                                            cursorColor = MaterialTheme.colorScheme.onPrimaryContainer,           // Add this
                                            focusedTextColor = MaterialTheme.colorScheme.onPrimaryContainer,      // Add this
                                            unfocusedTextColor = MaterialTheme.colorScheme.onPrimaryContainer ,
                                            selectionColors = TextSelectionColors(
                                                handleColor = MaterialTheme.colorScheme.onPrimaryContainer,
                                                backgroundColor = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.3f)
                                            )
                                        )
                                    )
                                    emailError?.let { error ->
                                        Text(
                                            text = error,
                                            color = MaterialTheme.colorScheme.error,
                                            style = MaterialTheme.typography.bodySmall,
                                            modifier = Modifier.padding(top = 4.dp)
                                        )
                                    }
                                }
                                IconButton(
                                    onClick = {
                                        val error = validateEmail(email)
                                        if (error == null) {
                                            isEditingEmail = false
                                            emailError = null
                                        } else {
                                            emailError = error
                                        }
                                    }
                                ) {
                                    Icon(
                                        Icons.Default.Check,
                                        contentDescription = "Save Email",
                                        tint = MaterialTheme.colorScheme.onPrimaryContainer
                                    )
                                }
                            } else {
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(
                                        text = "Email Address",
                                        style = MaterialTheme.typography.bodySmall,
                                        color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.7f)
                                    )
                                    Text(
                                        text = email,
                                        fontSize = 16.sp,
                                        fontWeight = FontWeight.Medium,
                                        color = MaterialTheme.colorScheme.onPrimaryContainer
                                    )
                                }
                                IconButton(onClick = { isEditingEmail = true }) {
                                    Icon(
                                        Icons.Default.Edit,
                                        contentDescription = "Edit Email",
                                        tint = MaterialTheme.colorScheme.onPrimaryContainer
                                    )
                                }
                            }
                        }
                    }
                }
            }

            // Theme Card
            item {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .shadow(6.dp, RoundedCornerShape(16.dp)),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surfaceVariant
                    )
                ) {
                    SettingsThemeItem(
                        selectedTheme = selectedTheme,
                        onThemeSelected = {
                            selectedTheme = it
                            scope.launch { UserPreference.saveTheme(context, it) }
                            onThemeChange(it)
                        }
                    )
                }
            }

            // Notifications Card
            item {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .shadow(6.dp, RoundedCornerShape(16.dp)),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surfaceVariant
                    )
                ) {
                    Row(
                        modifier = Modifier
                            .padding(20.dp)
                            .fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                Icons.Default.Notifications,
                                contentDescription = "Notifications",
                                tint = MaterialTheme.colorScheme.primary,
                                modifier = Modifier.size(28.dp)
                            )
                            Spacer(Modifier.width(16.dp))
                            Column {
                                Text(
                                    text = "Notifications",
                                    fontSize = 18.sp,
                                    fontWeight = FontWeight.SemiBold
                                )
                                Text(
                                    text = "Receive app notifications",
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }
                        Switch(
                            checked = notificationEnabled,
                            onCheckedChange = {
                                notificationEnabled = it
                                scope.launch { UserPreference.saveNotification(context, it) }
                            }
                        )
                    }
                }
            }

            // Apply Changes Button
            item {
                Button(
                    onClick = {
                        // Validate before saving
                        val nameValidation = validateName(name)
                        val emailValidation = validateEmail(email)

                        if (nameValidation != null) {
                            nameError = nameValidation
                            return@Button
                        }
                        if (emailValidation != null) {
                            emailError = emailValidation
                            return@Button
                        }

                        scope.launch {
                            UserPreference.saveUser(context, email, role, name)
                            val db = DatabaseModule.provideDatabase(context)
                            val userDao = DatabaseModule.provideUserDao(db)
                            val user = userDao.getUserByEmail(userState?.email ?: userEmail)
                            if (user != null) {
                                userDao.updateUser(user.id, name, email, role)
                            }
                            navController.navigate("studentMain") {
                                popUpTo("settings") { inclusive = true }
                            }
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(60.dp)
                        .shadow(12.dp, RoundedCornerShape(30.dp)),
                    shape = RoundedCornerShape(30.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primary
                    ),
                    elevation = ButtonDefaults.buttonElevation(
                        defaultElevation = 8.dp,
                        pressedElevation = 12.dp
                    )
                ) {
                    Icon(
                        Icons.Default.Save,
                        contentDescription = "Save",
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Text(
                        "Apply Changes",
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp
                    )
                }
            }

            // Help & About Card
            item {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .shadow(6.dp, RoundedCornerShape(16.dp)),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surfaceVariant
                    )
                ) {
                    SettingsItem(
                        icon = Icons.Default.Info,
                        title = "Help & About",
                        description = "Version 1.0.0 • Contact: support@example.com"
                    )
                }
            }

            // Bottom spacing
            item {
                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    }
}

@Composable
fun SettingsItem(icon: androidx.compose.ui.graphics.vector.ImageVector, title: String, description: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(20.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            icon,
            contentDescription = title,
            tint = MaterialTheme.colorScheme.primary,
            modifier = Modifier.size(28.dp)
        )
        Spacer(Modifier.width(16.dp))
        Column {
            Text(
                title,
                fontSize = 18.sp,
                fontWeight = FontWeight.SemiBold
            )
            Text(
                description,
                fontSize = 14.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
fun SettingsThemeItem(selectedTheme: String, onThemeSelected: (String) -> Unit) {
    val isSystemDark = isSystemInDarkTheme()

    Column(modifier = Modifier.padding(20.dp)) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                Icons.Default.Palette,
                contentDescription = "Theme",
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(28.dp)
            )
            Spacer(Modifier.width(16.dp))
            Column {
                Text(
                    "Theme",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.SemiBold
                )
                Text(
                    "Customize app appearance",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }

        Spacer(Modifier.height(20.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            ThemeChip("Light", selectedTheme == "light") { onThemeSelected("light") }
            ThemeChip("Dark", selectedTheme == "dark") { onThemeSelected("dark") }
            ThemeChip("System", selectedTheme == "system") { onThemeSelected("system") }
        }

        // Show current system theme status
        if (selectedTheme == "system") {
            Text(
                "Currently following system: ${if (isSystemDark) "Dark mode" else "Light mode"}",
                fontSize = 12.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f),
                modifier = Modifier.padding(top = 12.dp)
            )
        }
    }
}

@Composable
fun ThemeChip(text: String, selected: Boolean, onClick: () -> Unit) {
    FilterChip(
        onClick = onClick,
        label = {
            Text(
                text,
                fontWeight = if (selected) FontWeight.SemiBold else FontWeight.Medium,
                fontSize = 14.sp
            )
        },
        selected = selected,
        leadingIcon = if (selected) {
            {
                Icon(
                    Icons.Default.Check,
                    contentDescription = null,
                    modifier = Modifier.size(18.dp)
                )
            }
        } else null,
        modifier = Modifier.shadow(if (selected) 6.dp else 3.dp, RoundedCornerShape(16.dp))
    )
}