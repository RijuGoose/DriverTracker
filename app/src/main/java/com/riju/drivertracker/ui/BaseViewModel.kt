package com.riju.drivertracker.ui

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

open class BaseViewModel : ViewModel() {
    protected val _screenStatus = MutableStateFlow<ScreenStatus?>(ScreenStatus.Success)
    val screenStatus = _screenStatus.asStateFlow()
}

sealed class ScreenStatus {
    data object Loading : ScreenStatus()
    data object LoadingFullScreen : ScreenStatus()
    data object Success : ScreenStatus()
    data class Error(val error: String) : ScreenStatus()
    data class ErrorFullScreen(val error: String) : ScreenStatus()
}