package com.example.feature_login

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.unit.dp
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.PasswordVisualTransformation
import com.example.resumeanalyzer.core.database.DatabaseModule
import com.example.resumeanalyzer.core.database.entity.UserEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

//  Regex constants
private val EMAIL_REGEX = Regex("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.(com|org|net|in)$")
private val NAME_REGEX = Regex("^[A-Za-z ]{2,30}$")

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegisterScreen(
    onRegisterSuccess: () -> Unit,
    onLoginClick: () -> Unit
) {
    val context = LocalContext.current
    var name by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var role by remember { mutableStateOf("Student") }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    var isLoading by remember { mutableStateOf(false) }

    val scope = rememberCoroutineScope()
    val roles = listOf("Student", "Recruiter")

    //  Validation logic
    fun validateInput(): String? {
        if (name.isBlank() || email.isBlank() || password.isBlank()) {
            return "All fields are required"
        }
        if (!NAME_REGEX.matches(name)) {
            return "Enter a valid name (only letters, min 2 chars)"
        }
        if (!EMAIL_REGEX.matches(email)) {
            return "Invalid email address"
        }
        if (password.length < 6 || !password.any { it.isDigit() } || !password.any { it.isUpperCase() }) {
            return "Password must be 6+ chars, include 1 digit and 1 uppercase letter"
        }
        return null
    }

    //  Gradient background + Card UI
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    listOf(MaterialTheme.colorScheme.primary, MaterialTheme.colorScheme.secondary)
                )
            ),
        contentAlignment = Alignment.Center
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp),
            shape = RoundedCornerShape(16.dp),
            elevation = CardDefaults.cardElevation(12.dp)
        ) {
            Column(
                modifier = Modifier.padding(24.dp),
                verticalArrangement = Arrangement.Center
            ) {
                Text("Register", style = MaterialTheme.typography.headlineMedium)
                Spacer(modifier = Modifier.height(16.dp))

                TextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("Name") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    enabled = !isLoading
                )
                Spacer(modifier = Modifier.height(8.dp))

                TextField(
                    value = email,
                    onValueChange = { email = it.trim() },
                    label = { Text("Email") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    enabled = !isLoading
                )
                Spacer(modifier = Modifier.height(8.dp))

                TextField(
                    value = password,
                    onValueChange = { password = it },
                    label = { Text("Password") },
                    visualTransformation = PasswordVisualTransformation(),
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    enabled = !isLoading
                )
                Spacer(modifier = Modifier.height(8.dp))

                //  Role Dropdown
                var expanded by remember { mutableStateOf(false) }
                ExposedDropdownMenuBox(
                    expanded = expanded,
                    onExpandedChange = { if (!isLoading) expanded = !expanded }
                ) {
                    TextField(
                        value = role,
                        onValueChange = {},
                        readOnly = true,
                        label = { Text("Register as") },
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                        modifier = Modifier.menuAnchor().fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        enabled = !isLoading
                    )
                    ExposedDropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
                        roles.forEach {
                            DropdownMenuItem(
                                text = { Text(it) },
                                onClick = {
                                    role = it
                                    expanded = false
                                }
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    onClick = {
                        val validationError = validateInput()
                        if (validationError != null) {
                            errorMessage = validationError
                            return@Button
                        }

                        isLoading = true
                        errorMessage = null

                        scope.launch {
                            try {
                                val db = DatabaseModule.provideDatabase(context)
                                val userDao = DatabaseModule.provideUserDao(db)

                                withContext(Dispatchers.IO) {
                                    val existing = userDao.getUserByEmail(email.trim())

                                    withContext(Dispatchers.Main) {
                                        if (existing != null) {
                                            errorMessage = "User already exists"
                                            isLoading = false
                                        } else {
                                            // Register in IO thread
                                            withContext(Dispatchers.IO) {
                                                userDao.registerUser(
                                                    UserEntity(
                                                        name = name.trim(),
                                                        email = email.trim(),
                                                        password = password,
                                                        role = role
                                                    )
                                                )
                                            }

                                            withContext(Dispatchers.Main) {
                                                isLoading = false
                                                onLoginClick() // Navigate to login screen
                                            }
                                        }
                                    }
                                }
                            } catch (e: Exception) {
                                withContext(Dispatchers.Main) {
                                    errorMessage = "Registration failed: ${e.message}"
                                    isLoading = false
                                }
                            }
                        }
                    },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    enabled = !isLoading
                ) {
                    if (isLoading) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(20.dp),
                            color = MaterialTheme.colorScheme.onPrimary
                        )
                    } else {
                        Text("Register")
                    }
                }

                errorMessage?.let {
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(text = it, color = MaterialTheme.colorScheme.error)
                }

                Spacer(modifier = Modifier.height(16.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("Already have an account?")
                    Spacer(modifier = Modifier.width(6.dp))
                    TextButton(
                        onClick = { onLoginClick() },
                        enabled = !isLoading
                    ) {
                        Text("Login")
                    }
                }
            }
        }
    }
}