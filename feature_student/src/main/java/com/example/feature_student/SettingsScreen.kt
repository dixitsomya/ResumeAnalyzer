//package com.example.feature_student
//
//import androidx.compose.foundation.clickable
//import androidx.compose.foundation.layout.*
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
//    // 🔹 Fetch user from DataStore
//    val userState by UserPreference.getUser(context).collectAsState(initial = null)
//
//    // 🔹 Initial values from DataStore
//    var name by remember { mutableStateOf(userState?.name ?: "") }
//    var email by remember { mutableStateOf(userState?.email ?: userEmail) }
//    var role by remember { mutableStateOf(userState?.role ?: "Student") }
//    var notificationEnabled by remember { mutableStateOf(userState?.notificationsEnabled ?: true) }
//    var selectedTheme by remember { mutableStateOf(userState?.theme ?: "system") }
//
//    // 🔹 Room DB fetch (updates values if present)
//    LaunchedEffect(userEmail) {
//        val db = DatabaseModule.provideDatabase(context)
//        val userDao = DatabaseModule.provideUserDao(db)
//        val dbUser = userDao.getUserByEmail(userEmail)
//        dbUser?.let {
//            name = it.name
//            email = it.email
//            role = it.role
//        }
//    }
//
//    // 🔹 UI states
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
//            verticalArrangement = Arrangement.spacedBy(20.dp)
//        ) {
//            // 🔹 Name
//            Row(verticalAlignment = Alignment.CenterVertically) {
//                if (isEditingName) {
//                    OutlinedTextField(
//                        value = name,
//                        onValueChange = { name = it },
//                        label = { Text("Name") },
//                        modifier = Modifier.weight(1f)
//                    )
//                    IconButton(onClick = { isEditingName = false }) {
//                        Icon(Icons.Default.Check, contentDescription = "Save Name")
//                    }
//                } else {
//                    Text(name, fontSize = 16.sp, fontWeight = FontWeight.Medium, modifier = Modifier.weight(1f))
//                    IconButton(onClick = { isEditingName = true }) {
//                        Icon(Icons.Default.Edit, contentDescription = "Edit Name")
//                    }
//                }
//            }
//
//            // 🔹 Email
//            Row(verticalAlignment = Alignment.CenterVertically) {
//                if (isEditingEmail) {
//                    OutlinedTextField(
//                        value = email,
//                        onValueChange = { email = it },
//                        label = { Text("Email") },
//                        modifier = Modifier.weight(1f)
//                    )
//                    IconButton(onClick = { isEditingEmail = false }) {
//                        Icon(Icons.Default.Check, contentDescription = "Save Email")
//                    }
//                } else {
//                    Text(email, fontSize = 16.sp, fontWeight = FontWeight.Medium, modifier = Modifier.weight(1f))
//                    IconButton(onClick = { isEditingEmail = true }) {
//                        Icon(Icons.Default.Edit, contentDescription = "Edit Email")
//                    }
//                }
//            }
//
//            // 🔹 Role DropDown
//            Row(verticalAlignment = Alignment.CenterVertically) {
//                Text("Role: ", fontWeight = FontWeight.Medium, modifier = Modifier.padding(end = 8.dp))
//                Box {
//                    Text(
//                        role,
//                        modifier = Modifier
//                            .clickable { expanded = true }
//                            .padding(8.dp)
//                    )
//                    DropdownMenu(
//                        expanded = expanded,
//                        onDismissRequest = { expanded = false }
//                    ) {
//                        DropdownMenuItem(
//                            text = { Text("Student") },
//                            onClick = { role = "Student"; expanded = false }
//                        )
//                        DropdownMenuItem(
//                            text = { Text("Recruiter") },
//                            onClick = { role = "Recruiter"; expanded = false }
//                        )
//                    }
//                }
//            }
//
//            // 🔹 Theme Selection
//            SettingsThemeItem(
//                selectedTheme = selectedTheme,
//                onThemeSelected = {
//                    selectedTheme = it
//                    scope.launch { UserPreference.saveTheme(context, it) }
//                    onThemeChange(it)
//                }
//            )
//
//            // 🔹 Notifications
//            Row(
//                modifier = Modifier.fillMaxWidth(),
//                verticalAlignment = Alignment.CenterVertically,
//                horizontalArrangement = Arrangement.SpaceBetween
//            ) {
//                Row(verticalAlignment = Alignment.CenterVertically) {
//                    Icon(Icons.Default.Notifications, contentDescription = "Notifications")
//                    Spacer(Modifier.width(16.dp))
//                    Text("Notifications", fontSize = 16.sp, fontWeight = FontWeight.Medium)
//                }
//                Switch(
//                    checked = notificationEnabled,
//                    onCheckedChange = {
//                        notificationEnabled = it
//                        scope.launch { UserPreference.saveNotification(context, it) }
//                    }
//                )
//            }
//
//            // 🔹 Save Profile Button
////            Button(
////                onClick = {
////                    scope.launch {
////                        // Update DataStore
////                        UserPreference.saveUser(context, email, role, name)
////
////                        // Update Room
////                        val db = DatabaseModule.provideDatabase(context)
////                        val userDao = DatabaseModule.provideUserDao(db)
////                        val user = userDao.getUserByEmail(userEmail)
////                        if (user != null) {
////                            userDao.updateUser(user.id, name, email, role)
////                        }
////
////                        // Navigate back
////                        navController.navigate("studentMain/$email") {
////                            popUpTo("settings") { inclusive = true }
////                        }
////                    }
////                },
////                modifier = Modifier.fillMaxWidth()
////            ) {
////                Text("Save Profile")
////            }
//
//            var originalRole = userState?.role ?: role // store original role
//
//            Button(
//                onClick = {
//                    scope.launch {
//                        // Update DataStore
//                        UserPreference.saveUser(context, email, role, name)
//
//                        // Update Room
//                        val db = DatabaseModule.provideDatabase(context)
//                        val userDao = DatabaseModule.provideUserDao(db)
//                        val user = userDao.getUserByEmail(userEmail)
//                        if (user != null) {
//                            userDao.updateUser(user.id, name, email, role)
//                        }
//
//                        // Navigate based on role change
//                        if (role != originalRole) {
//                            // Role changed → logout to login screen
//                            navController.navigate("login") {
//                                popUpTo("settings") { inclusive = true }
//                            }
//                        } else {
//                            // Role same → back to StudentMain
//                            navController.navigate("studentMain") {
//                                popUpTo("settings") { inclusive = true }
//                            }
//                        }
//                    }
//                },
//                modifier = Modifier.fillMaxWidth()
//            ) {
//                Text("Apply Changes")
//            }
//
//
//            // 🔹 Help
//            SettingsItem(
//                icon = Icons.Default.Info,
//                title = "Help & About",
//                description = "Version 1.0.0 • Contact: support@example.com"
//            )
//        }
//    }
//}
//
//
//@Composable
//fun SettingsItem(icon: androidx.compose.ui.graphics.vector.ImageVector, title: String, description: String) {
//    Row(
//        modifier = Modifier.fillMaxWidth(),
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
//    Column {
//        Row(verticalAlignment = Alignment.CenterVertically) {
//            Icon(Icons.Default.ColorLens, contentDescription = "Theme")
//            Spacer(Modifier.width(16.dp))
//            Text("Theme", fontSize = 16.sp, fontWeight = FontWeight.Medium)
//        }
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
//    }
//}
//
//@Composable
//fun ThemeChip(text: String, selected: Boolean, onClick: () -> Unit) {
//    AssistChip(
//        onClick = onClick,
//        label = { Text(text) },
//        leadingIcon = {
//            if (selected) Icon(Icons.Default.Check, contentDescription = null)
//        },
//        colors = AssistChipDefaults.assistChipColors(
//            containerColor = if (selected) MaterialTheme.colorScheme.primary.copy(alpha = 0.2f)
//            else MaterialTheme.colorScheme.surface
//        )
//    )
//}

//--------------------------------------------------------------------------------------------------
package com.example.feature_student

import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
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
import androidx.navigation.NavController
import com.example.resumeanalyzer.core.navigation.datastore.UserPreference
import com.example.resumeanalyzer.core.database.DatabaseModule
import com.example.resumeanalyzer.core.navigation.datastore.UserCache
import kotlinx.coroutines.launch

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
//    val userState by UserPreference.getUser(context).collectAsState(initial = null)
//
//    var name by remember { mutableStateOf(userState?.name ?: "") }
//    var email by remember { mutableStateOf(userState?.email ?: userEmail) }
//    var role by remember { mutableStateOf(userState?.role ?: "Student") }
//    var notificationEnabled by remember { mutableStateOf(userState?.notificationsEnabled ?: true) }
//    var selectedTheme by remember { mutableStateOf(userState?.theme ?: "system") }
//
//    LaunchedEffect(userEmail) {
//        val db = DatabaseModule.provideDatabase(context)
//        val userDao = DatabaseModule.provideUserDao(db)
//        val dbUser = userDao.getUserByEmail(userEmail)
//        dbUser?.let {
//            name = it.name
//            email = it.email
//            role = it.role
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
//                        Text(name, fontSize = 16.sp, fontWeight = FontWeight.Medium, modifier = Modifier.weight(1f))
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
//            var originalRole = userState?.role ?: role
//            Button(
//                onClick = {
//                    scope.launch {
//                        UserPreference.saveUser(context, email, role, name)
//                        val db = DatabaseModule.provideDatabase(context)
//                        val userDao = DatabaseModule.provideUserDao(db)
//                        val user = userDao.getUserByEmail(userEmail)
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
//
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
//    Column(modifier = Modifier.padding(12.dp)) {
//        Row(verticalAlignment = Alignment.CenterVertically) {
//            Icon(Icons.Default.ColorLens, contentDescription = "Theme")
//            Spacer(Modifier.width(16.dp))
//            Text("Theme", fontSize = 16.sp, fontWeight = FontWeight.Medium)
//        }
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

    var expanded by remember { mutableStateOf(false) }
    var isEditingName by remember { mutableStateOf(false) }
    var isEditingEmail by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Settings") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .padding(16.dp)
                .fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // 🔹 Name Card
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                elevation = CardDefaults.cardElevation(4.dp)
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    if (isEditingName) {
                        OutlinedTextField(
                            value = name,
                            onValueChange = { name = it },
                            label = { Text("Name") },
                            modifier = Modifier.weight(1f)
                        )
                        IconButton(onClick = { isEditingName = false }) {
                            Icon(Icons.Default.Check, contentDescription = "Save Name")
                        }
                    } else {
                        Text(
                            text = name.ifEmpty { "Add your name" },
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Medium,
                            modifier = Modifier.weight(1f)
                        )
                        IconButton(onClick = { isEditingName = true }) {
                            Icon(Icons.Default.Edit, contentDescription = "Edit Name")
                        }
                    }
                }
            }

            // 🔹 Email Card
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                elevation = CardDefaults.cardElevation(4.dp)
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    if (isEditingEmail) {
                        OutlinedTextField(
                            value = email,
                            onValueChange = { email = it },
                            label = { Text("Email") },
                            modifier = Modifier.weight(1f)
                        )
                        IconButton(onClick = { isEditingEmail = false }) {
                            Icon(Icons.Default.Check, contentDescription = "Save Email")
                        }
                    } else {
                        Text(email, fontSize = 16.sp, fontWeight = FontWeight.Medium, modifier = Modifier.weight(1f))
                        IconButton(onClick = { isEditingEmail = true }) {
                            Icon(Icons.Default.Edit, contentDescription = "Edit Email")
                        }
                    }
                }
            }

            // 🔹 Role Card
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                elevation = CardDefaults.cardElevation(4.dp)
            ) {
                Row(
                    modifier = Modifier
                        .padding(horizontal = 12.dp, vertical = 8.dp)
                        .fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("Role: ", fontWeight = FontWeight.Medium)
                    Spacer(Modifier.width(8.dp))
                    Box {
                        Text(
                            role,
                            modifier = Modifier
                                .clickable { expanded = true }
                                .padding(8.dp)
                        )
                        DropdownMenu(
                            expanded = expanded,
                            onDismissRequest = { expanded = false }
                        ) {
                            DropdownMenuItem(
                                text = { Text("Student") },
                                onClick = { role = "Student"; expanded = false }
                            )
                            DropdownMenuItem(
                                text = { Text("Recruiter") },
                                onClick = { role = "Recruiter"; expanded = false }
                            )
                        }
                    }
                }
            }

            // 🔹 Theme Card
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                elevation = CardDefaults.cardElevation(4.dp)
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

            // 🔹 Notifications Card
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                elevation = CardDefaults.cardElevation(4.dp)
            ) {
                Row(
                    modifier = Modifier
                        .padding(horizontal = 12.dp, vertical = 8.dp)
                        .fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.Notifications, contentDescription = "Notifications")
                        Spacer(Modifier.width(16.dp))
                        Text("Notifications", fontSize = 16.sp, fontWeight = FontWeight.Medium)
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

            // 🔹 Save Button
            val originalRole = userState?.role ?: role
            Button(
                onClick = {
                    scope.launch {
                        UserPreference.saveUser(context, email, role, name)
                        val db = DatabaseModule.provideDatabase(context)
                        val userDao = DatabaseModule.provideUserDao(db)
                        val user = userDao.getUserByEmail(userState?.email ?: userEmail)
                        if (user != null) {
                            userDao.updateUser(user.id, name, email, role)
                        }
                        if (role != originalRole) {
                            navController.navigate("login") { popUpTo("settings") { inclusive = true } }
                        } else {
                            navController.navigate("studentMain") { popUpTo("settings") { inclusive = true } }
                        }
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                shape = RoundedCornerShape(25.dp)
            ) {
                Text("Apply Changes", fontWeight = FontWeight.Bold)
            }

            // 🔹 Help Card
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                elevation = CardDefaults.cardElevation(4.dp)
            ) {
                SettingsItem(
                    icon = Icons.Default.Info,
                    title = "Help & About",
                    description = "Version 1.0.0 • Contact: support@example.com"
                )
            }
        }
    }
}

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
//    // Also load from database
//    LaunchedEffect(userEmail) {
//        if (userEmail.isNotEmpty()) {
//            val db = DatabaseModule.provideDatabase(context)
//            val userDao = DatabaseModule.provideUserDao(db)
//            val dbUser = userDao.getUserByEmail(userEmail)
//            dbUser?.let {
//                name = it.name
//                email = it.email
//                role = it.role
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
//                        Text(name.ifEmpty { "Add your name" }, fontSize = 16.sp, fontWeight = FontWeight.Medium, modifier = Modifier.weight(1f))
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
//                        val user = userDao.getUserByEmail(userEmail)
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


@Composable
fun SettingsItem(icon: androidx.compose.ui.graphics.vector.ImageVector, title: String, description: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(icon, contentDescription = title)
        Spacer(Modifier.width(16.dp))
        Column {
            Text(title, fontSize = 16.sp, fontWeight = FontWeight.Medium)
            Text(description, fontSize = 13.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
    }
}

@Composable
fun SettingsThemeItem(selectedTheme: String, onThemeSelected: (String) -> Unit) {
    val isSystemDark = isSystemInDarkTheme()

    Column(modifier = Modifier.padding(12.dp)) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(Icons.Default.Settings, contentDescription = "Theme")
            Spacer(Modifier.width(16.dp))
            Text("Theme", fontSize = 16.sp, fontWeight = FontWeight.Medium)
        }

        // Show current system theme status when system is selected
        if (selectedTheme == "system") {
            Text(
                "Following system: ${if (isSystemDark) "Dark" else "Light"}",
                fontSize = 12.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(start = 40.dp, top = 4.dp)
            )
        }

        Spacer(Modifier.height(8.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            ThemeChip("Light", selectedTheme == "light") { onThemeSelected("light") }
            ThemeChip("Dark", selectedTheme == "dark") { onThemeSelected("dark") }
            ThemeChip("System", selectedTheme == "system") { onThemeSelected("system") }
        }

        // Show what system currently is as a small text below chips
        if (selectedTheme == "system") {
            Text(
                "Currently: ${if (isSystemDark) "Dark mode" else "Light mode"}",
                fontSize = 11.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f),
                modifier = Modifier.padding(top = 4.dp)
            )
        }
    }
}

@Composable
fun ThemeChip(text: String, selected: Boolean, onClick: () -> Unit) {
    AssistChip(
        onClick = onClick,
        label = {
            Text(
                text,
                color = if (selected) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurface
            )
        },
        leadingIcon = {
            if (selected) Icon(Icons.Default.Check, contentDescription = null, tint = MaterialTheme.colorScheme.onPrimary)
        },
        colors = AssistChipDefaults.assistChipColors(
            containerColor = if (selected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surface
        )
    )
}
