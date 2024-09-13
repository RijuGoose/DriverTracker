package com.riju.drivertracker.ui

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

open class BaseViewModel : ViewModel() {
    protected val _screenStatus = MutableStateFlow<ScreenStatus?>(ScreenStatus.Loading)
    val screenStatus = _screenStatus.asStateFlow()
}

sealed class ScreenStatus {
    data object Loading : ScreenStatus()
    data object Success : ScreenStatus()
    data class Failure(val error: String) : ScreenStatus()
}