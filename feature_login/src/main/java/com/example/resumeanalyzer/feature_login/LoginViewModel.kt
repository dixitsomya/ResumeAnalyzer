//package com.example.resumeanalyzer.feature_login
//
//import androidx.lifecycle.ViewModel
//import androidx.lifecycle.viewModelScope
//import com.example.resumeanalyzer.core.database.dao.UserDao
//import kotlinx.coroutines.flow.MutableStateFlow
//import kotlinx.coroutines.flow.StateFlow
//import kotlinx.coroutines.launch
//
//class LoginViewModel(private val userDao: UserDao) : ViewModel() {
//
//    private val _loginState = MutableStateFlow<LoginState>(LoginState.Idle)
//    val loginState: StateFlow<LoginState> = _loginState
//
//    fun login(email: String, password: String) {
//        viewModelScope.launch {
//            val user = userDao.loginUser(email, password)
//            if (user != null) {
//                _loginState.value = LoginState.Success
//            } else {
//                _loginState.value = LoginState.Error("Invalid credentials or please register first")
//            }
//        }
//    }
//}
//
//sealed class LoginState {
//    object Idle : LoginState()
//    object Success : LoginState()
//    data class Error(val message: String) : LoginState()
//}
