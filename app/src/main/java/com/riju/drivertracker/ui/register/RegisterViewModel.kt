package com.riju.drivertracker.ui.register

import androidx.lifecycle.ViewModel
import com.riju.drivertracker.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel @Inject constructor(
    private val userRepository: UserRepository
) : ViewModel() {
    private val _userName = MutableStateFlow("")
    val userName = _userName.asStateFlow()

    private val _password = MutableStateFlow("")
    val password = _password.asStateFlow()

    private val _registrationStatus: MutableSharedFlow<RegistrationStatus?> = MutableSharedFlow(extraBufferCapacity = 1)
    val registrationStatus = _registrationStatus.asSharedFlow()

    fun register(email: String, password: String) {
        try {
            userRepository.register(email, password) {
                if (it.isSuccessful) {
                    _registrationStatus.tryEmit(RegistrationStatus.Success)
                } else {
                    _registrationStatus.tryEmit(
                        RegistrationStatus.Failure(
                            error = it.exception?.message ?: "Unknown error"
                        )
                    )
                }
            }
        }
        catch (e: Exception) {
            _registrationStatus.tryEmit(
                RegistrationStatus.Failure(
                    error = e.message ?: "Unknown error"
                )
            )
        }
    }

    fun setUserName(userName: String) {
        _userName.value = userName
    }

    fun setPassword(password: String) {
        _password.value = password
    }
}

sealed class RegistrationStatus {
    data object Success : RegistrationStatus()
    data class Failure(val error: String) : RegistrationStatus()
}