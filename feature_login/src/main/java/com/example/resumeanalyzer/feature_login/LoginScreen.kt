//package com.example.feature_login
//
//import androidx.compose.foundation.layout.*
//import androidx.compose.material3.*
//import androidx.compose.runtime.*
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.platform.LocalContext
//import androidx.compose.ui.text.input.PasswordVisualTransformation
//import androidx.compose.ui.unit.dp
//import com.example.resumeanalyzer.core.database.DatabaseModule
//import kotlinx.coroutines.Dispatchers
//import kotlinx.coroutines.launch
//import kotlinx.coroutines.withContext
//
//@OptIn(ExperimentalMaterial3Api::class)
//@Composable
//fun LoginScreen(
//    onLoginSuccess: () -> Unit,
//    onRegisterClick: () -> Unit
//) {
//    val context = LocalContext.current
//    var role by remember { mutableStateOf("Student") }
//    var email by remember { mutableStateOf("") }
//    var password by remember { mutableStateOf("") }
//    var errorMessage by remember { mutableStateOf<String?>(null) }
//
//    val scope = rememberCoroutineScope()
//    val roles = listOf("Student", "Recruiter")
//
//    Column(
//        modifier = Modifier
//            .fillMaxSize()
//            .padding(24.dp),
//        verticalArrangement = Arrangement.Center
//    ) {
//        Text("Login", style = MaterialTheme.typography.headlineMedium)
//        Spacer(modifier = Modifier.height(16.dp))
//
//        // Role Dropdown
//        var expanded by remember { mutableStateOf(false) }
//        ExposedDropdownMenuBox(expanded = expanded, onExpandedChange = { expanded = !expanded }) {
//            TextField(
//                value = role,
//                onValueChange = {},
//                readOnly = true,
//                label = { Text("Login as") },
//                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
//                modifier = Modifier.menuAnchor().fillMaxWidth()
//            )
//            ExposedDropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
//                roles.forEach {
//                    DropdownMenuItem(
//                        text = { Text(it) },
//                        onClick = {
//                            role = it
//                            expanded = false
//                        }
//                    )
//                }
//            }
//        }
//
//        Spacer(modifier = Modifier.height(8.dp))
//
//        TextField(value = email, onValueChange = { email = it }, label = { Text("Email") }, modifier = Modifier.fillMaxWidth())
//        Spacer(modifier = Modifier.height(8.dp))
//
//        TextField(
//            value = password,
//            onValueChange = { password = it },
//            label = { Text("Password") },
//            visualTransformation = PasswordVisualTransformation(),
//            modifier = Modifier.fillMaxWidth()
//        )
//        Spacer(modifier = Modifier.height(16.dp))
//
//        Button(
//            onClick = {
//                val db = DatabaseModule.provideDatabase(context)
//                val userDao = DatabaseModule.provideUserDao(db)
//                scope.launch(Dispatchers.IO) {
//                    val user = userDao.loginUser(email, password)
//                    withContext(Dispatchers.Main) {
//                        if (user != null && user.role == role) {
//                            onLoginSuccess()
//                        } else {
//                            errorMessage = "Invalid credentials or wrong role"
//                        }
//                    }
//                }
//            },
//            modifier = Modifier.fillMaxWidth()
//        ) {
//            Text("Login")
//        }
//
//        errorMessage?.let {
//            Spacer(modifier = Modifier.height(12.dp))
//            Text(it, color = MaterialTheme.colorScheme.error)
//        }
//
//        Spacer(modifier = Modifier.height(16.dp))
//
//        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
//            Text("Don’t have an account?")
//            Spacer(modifier = Modifier.width(6.dp))
//            TextButton(onClick = { onRegisterClick() }) {
//                Text("Register")
//            }
//        }
//    }
//}


//------------------------------------------------------------------------------------------

//package com.example.feature_login
//
//import androidx.compose.foundation.background
//import androidx.compose.foundation.layout.*
//import androidx.compose.foundation.shape.RoundedCornerShape
//import androidx.compose.material3.*
//import androidx.compose.runtime.*
//import androidx.compose.ui.Alignment
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.graphics.Brush
//import androidx.compose.ui.text.input.PasswordVisualTransformation
//import androidx.compose.ui.unit.dp
//import androidx.compose.ui.platform.LocalContext
//import com.example.resumeanalyzer.core.database.DatabaseModule
//import kotlinx.coroutines.Dispatchers
//import kotlinx.coroutines.launch
//import kotlinx.coroutines.withContext
//
//@OptIn(ExperimentalMaterial3Api::class)
//@Composable
//fun LoginScreen(
//    onLoginSuccess: () -> Unit,
//    onRegisterClick: () -> Unit
//) {
//    val context = LocalContext.current
//    var role by remember { mutableStateOf("Student") }
//    var email by remember { mutableStateOf("") }
//    var password by remember { mutableStateOf("") }
//    var errorMessage by remember { mutableStateOf<String?>(null) }
//
//    val scope = rememberCoroutineScope()
//    val roles = listOf("Student", "Recruiter")
//
//    //  Gradient background + Card wrapper
//    Box(
//        modifier = Modifier
//            .fillMaxSize()
//            .background(
//                Brush.verticalGradient(
//                    listOf(MaterialTheme.colorScheme.primary, MaterialTheme.colorScheme.secondary)
//                )
//            ),
//        contentAlignment = Alignment.Center
//    ) {
//        Card(
//            modifier = Modifier
//                .fillMaxWidth()
//                .padding(24.dp),
//            shape = RoundedCornerShape(16.dp),
//            elevation = CardDefaults.cardElevation(12.dp)
//        ) {
//            Column(
//                modifier = Modifier.padding(24.dp),
//                verticalArrangement = Arrangement.Center
//            ) {
//                Text("Login", style = MaterialTheme.typography.headlineMedium)
//                Spacer(modifier = Modifier.height(16.dp))
//
//                // Role Dropdown
//                var expanded by remember { mutableStateOf(false) }
//                ExposedDropdownMenuBox(expanded = expanded, onExpandedChange = { expanded = !expanded }) {
//                    TextField(
//                        value = role,
//                        onValueChange = {},
//                        readOnly = true,
//                        label = { Text("Login as") },
//                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
//                        modifier = Modifier
//                            .menuAnchor()
//                            .fillMaxWidth(),
//                        shape = RoundedCornerShape(12.dp)
//                    )
//                    ExposedDropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
//                        roles.forEach {
//                            DropdownMenuItem(
//                                text = { Text(it) },
//                                onClick = {
//                                    role = it
//                                    expanded = false
//                                }
//                            )
//                        }
//                    }
//                }
//
//                Spacer(modifier = Modifier.height(8.dp))
//
//                TextField(
//                    value = email,
//                    onValueChange = { email = it },
//                    label = { Text("Email") },
//                    modifier = Modifier.fillMaxWidth(),
//                    shape = RoundedCornerShape(12.dp)
//                )
//                Spacer(modifier = Modifier.height(8.dp))
//
//                TextField(
//                    value = password,
//                    onValueChange = { password = it },
//                    label = { Text("Password") },
//                    visualTransformation = PasswordVisualTransformation(),
//                    modifier = Modifier.fillMaxWidth(),
//                    shape = RoundedCornerShape(12.dp)
//                )
//                Spacer(modifier = Modifier.height(16.dp))
//
//                Button(
//                    onClick = {
//                        val db = DatabaseModule.provideDatabase(context)
//                        val userDao = DatabaseModule.provideUserDao(db)
//                        scope.launch(Dispatchers.IO) {
//                            val user = userDao.loginUser(email, password)
//                            withContext(Dispatchers.Main) {
//                                if (user != null && user.role == role) {
//                                    onLoginSuccess()
//                                } else {
//                                    errorMessage = "Invalid credentials or wrong role"
//                                }
//                            }
//                        }
//                    },
//                    modifier = Modifier.fillMaxWidth(),
//                    shape = RoundedCornerShape(12.dp)
//                ) {
//                    Text("Login")
//                }
//
//                errorMessage?.let {
//                    Spacer(modifier = Modifier.height(12.dp))
//                    Text(it, color = MaterialTheme.colorScheme.error)
//                }
//
//                Spacer(modifier = Modifier.height(16.dp))
//
//                Row(
//                    modifier = Modifier.fillMaxWidth(),
//                    horizontalArrangement = Arrangement.Center,
//                    verticalAlignment = Alignment.CenterVertically
//                ) {
//                    Text("Don’t have an account?")
//                    Spacer(modifier = Modifier.width(6.dp))
//                    TextButton(onClick = { onRegisterClick() }) {
//                        Text("Register")
//                    }
//                }
//            }
//        }
//    }
//}


package com.example.feature_login

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.platform.LocalContext
import com.example.resumeanalyzer.core.database.DatabaseModule
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(
    onStudentLogin: (String) -> Unit,
    onRecruiterLogin: () -> Unit,
    onRegisterClick: () -> Unit
) {
    val context = LocalContext.current
    var role by remember { mutableStateOf("Student") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    val scope = rememberCoroutineScope()
    val roles = listOf("Student", "Recruiter")

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
                Text("Login", style = MaterialTheme.typography.headlineMedium)
                Spacer(modifier = Modifier.height(16.dp))

                // Role Dropdown
                var expanded by remember { mutableStateOf(false) }
                ExposedDropdownMenuBox(expanded = expanded, onExpandedChange = { expanded = !expanded }) {
                    TextField(
                        value = role,
                        onValueChange = {},
                        readOnly = true,
                        label = { Text("Login as") },
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                        modifier = Modifier
                            .menuAnchor()
                            .fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp)
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

                Spacer(modifier = Modifier.height(8.dp))

                TextField(
                    value = email,
                    onValueChange = { email = it },
                    label = { Text("Email") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp)
                )
                Spacer(modifier = Modifier.height(8.dp))

                TextField(
                    value = password,
                    onValueChange = { password = it },
                    label = { Text("Password") },
                    visualTransformation = PasswordVisualTransformation(),
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp)
                )
                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    onClick = {
                        val db = DatabaseModule.provideDatabase(context)
                        val userDao = DatabaseModule.provideUserDao(db)
                        scope.launch(Dispatchers.IO) {
                            val user = userDao.loginUser(email, password)
                            withContext(Dispatchers.Main) {
//                                if (user != null && user.role == role) {
//                                    if (role == "Student") {
//                                        onStudentLogin()
//                                    } else {
//                                        onRecruiterLogin()
//                                    }
//                                } else {
//                                    errorMessage = "Invalid credentials or wrong role"
//                                }
                                if (user != null && user.role == role) {
                                    if (role == "Student") {
                                        onStudentLogin(user.email)
                                    } else {
                                        onRecruiterLogin()
                                    }
                                } else {
                                    errorMessage = "Invalid credentials or wrong role"
                                }
                            }
                        }
                    },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text("Login")
                }

                errorMessage?.let {
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(it, color = MaterialTheme.colorScheme.error)
                }

                Spacer(modifier = Modifier.height(16.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("Don’t have an account?")
                    Spacer(modifier = Modifier.width(6.dp))
                    TextButton(onClick = { onRegisterClick() }) {
                        Text("Register")
                    }
                }
            }
        }
    }
}

