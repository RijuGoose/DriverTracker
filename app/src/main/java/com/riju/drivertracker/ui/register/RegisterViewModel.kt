package com.riju.drivertracker.ui.register

import android.content.Context
import androidx.lifecycle.viewModelScope
import com.riju.drivertracker.R
import com.riju.drivertracker.repository.UserRepository
import com.riju.drivertracker.ui.BaseViewModel
import com.riju.drivertracker.ui.ScreenStatus
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel @Inject constructor(
    private val userRepository: UserRepository,
    @ApplicationContext private val context: Context
) : BaseViewModel<Unit>(defaultScreenState = ScreenStatus.Success(Unit)) {
    private val _userName = MutableStateFlow("")
    val userName = _userName.asStateFlow()

    private val _password = MutableStateFlow("")
    val password = _password.asStateFlow()

    private val _onRegistrationSuccess = MutableSharedFlow<Unit>(extraBufferCapacity = 1)
    val onRegistrationSuccess = _onRegistrationSuccess.asSharedFlow()

    fun register(email: String, password: String) {
        viewModelScope.launch {
            showLoadingDialog()
            try {
                val user = userRepository.register(email, password)
                if (user != null) {
                    _onRegistrationSuccess.tryEmit(Unit)
                } else {
                    showError(context.getString(R.string.common_unknown_error))
                }
            } catch (e: Exception) {
                showError(e.message ?: context.getString(R.string.common_unknown_error))
            } finally {
                hideLoadingDialog()
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
