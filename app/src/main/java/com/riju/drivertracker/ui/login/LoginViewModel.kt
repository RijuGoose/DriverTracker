package com.riju.drivertracker.ui.login

import androidx.lifecycle.viewModelScope
import com.riju.drivertracker.repository.UserRepository
import com.riju.drivertracker.ui.BaseViewModel
import com.riju.drivertracker.ui.ScreenStatus
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val userRepository: UserRepository
) : BaseViewModel() {
    private val _userName = MutableStateFlow("")
    val userName = _userName.asStateFlow()

    private val _password = MutableStateFlow("")
    val password = _password.asStateFlow()

    private val _onSuccessLogin = MutableSharedFlow<Unit>()
    val onSuccessLogin = _onSuccessLogin.asSharedFlow()

    fun login(email: String, password: String) {
        viewModelScope.launch {
            _screenStatus.value = ScreenStatus.Loading
            try {
                val user = userRepository.login(
                    email = email,
                    password = password
                )
                if (user != null) {
                    _screenStatus.value = ScreenStatus.Success
                    _onSuccessLogin.emit(Unit)
                } else {
                    _screenStatus.value = ScreenStatus.Error(
                        error = "Unknown error"
                    )
                }
            } catch (e: Exception) {
                _screenStatus.value = ScreenStatus.Error(
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
