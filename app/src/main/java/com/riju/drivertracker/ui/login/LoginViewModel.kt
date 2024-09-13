package com.riju.drivertracker.ui.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.riju.drivertracker.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val userRepository: UserRepository
) : ViewModel() {
    private val _userName = MutableStateFlow("")
    val userName = _userName.asStateFlow()

    private val _password = MutableStateFlow("")
    val password = _password.asStateFlow()

    private val _loginStatus = MutableStateFlow<LoginStatus?>(null)
    val loginStatus = _loginStatus.asStateFlow()

    fun login(email: String, password: String) {
        viewModelScope.launch {
            try {
                val user = userRepository.login(
                    email = email,
                    password = password
                )
                if (user != null) {
                    _loginStatus.value = LoginStatus.Success
                } else {
                    _loginStatus.value = LoginStatus.Failure(
                        error = "Unknown error"
                    )
                }
            } catch (e: Exception) {
                _loginStatus.value = LoginStatus.Failure(
                    error = e.message ?: "Unknown error"
                )
            }
        }
    }

    fun setUserName(userName: String) {
        _userName.value = userName
    }

    fun setPassword(password: String) {
        _password.value = password
    }
}


sealed class LoginStatus {
    data object Success : LoginStatus()
    data class Failure(val error: String) : LoginStatus()
}