package com.riju.drivertracker.ui.login

import android.content.Context
import androidx.lifecycle.viewModelScope
import com.riju.drivertracker.R
import com.riju.drivertracker.ui.BaseViewModel
import com.riju.drivertracker.ui.ScreenStatus
import com.riju.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val userRepository: UserRepository,
    @ApplicationContext private val context: Context
) : BaseViewModel<Unit>(defaultScreenState = ScreenStatus.Success(Unit)) {
    private val _email = MutableStateFlow("")
    val email = _email.asStateFlow()

    private val _password = MutableStateFlow("")
    val password = _password.asStateFlow()

    private val _onSuccessLogin = MutableSharedFlow<Unit>()
    val onSuccessLogin = _onSuccessLogin.asSharedFlow()

    fun login(email: String, password: String) {
        viewModelScope.launch {
            showLoadingDialog()
            try {
                val user = userRepository.login(
                    email = email,
                    password = password
                )

                if (user != null) {
                    _onSuccessLogin.emit(Unit)
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

    fun setEmail(email: String) {
        _email.value = email
    }

    fun setPassword(password: String) {
        _password.value = password
    }
}
