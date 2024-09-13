package com.riju.drivertracker.ui.register

import androidx.lifecycle.viewModelScope
import com.riju.drivertracker.repository.UserRepository
import com.riju.drivertracker.ui.BaseViewModel
import com.riju.drivertracker.ui.ScreenStatus
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel @Inject constructor(
    private val userRepository: UserRepository
) : BaseViewModel() {
    private val _userName = MutableStateFlow("")
    val userName = _userName.asStateFlow()

    private val _password = MutableStateFlow("")
    val password = _password.asStateFlow()

    fun register(email: String, password: String) {
        viewModelScope.launch {
            try {
                val user = userRepository.register(email, password)
                if (user != null) {
                    _screenStatus.tryEmit(ScreenStatus.Success)
                } else {
                    _screenStatus.tryEmit(
                        ScreenStatus.Failure(
                            error = "Unknown error"
                        )
                    )
                }

            } catch (e: Exception) {
                _screenStatus.tryEmit(
                    ScreenStatus.Failure(
                        error = e.message ?: "Unknown error"
                    )
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
