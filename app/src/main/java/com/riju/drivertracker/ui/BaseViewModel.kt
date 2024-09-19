package com.riju.drivertracker.ui

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow

open class BaseViewModel<T : Any>(
    defaultScreenState: ScreenStatus<T> = ScreenStatus.LoadingFullScreen
) : ViewModel() {
    @Suppress("VariableNaming")
    protected val _screenStatus = MutableStateFlow<ScreenStatus<T>>(defaultScreenState)
    val screenStatus = _screenStatus.asStateFlow()

    @Suppress("VariableNaming")
    protected val _showLoadingDialog = MutableStateFlow(false)
    val showLoadingDialog = _showLoadingDialog.asStateFlow()

    @Suppress("VariableNaming")
    protected val _errorState = MutableSharedFlow<String>()
    val errorState = _errorState.asSharedFlow()

    protected fun showLoadingDialog() {
        _showLoadingDialog.value = true
    }

    protected fun hideLoadingDialog() {
        _showLoadingDialog.value = false
    }

    protected fun showError(error: String) {
        _errorState.tryEmit(error) // TODO: lehet nem lesz jó így
    }
}

sealed class ScreenStatus<out T : Any> {
    data object LoadingFullScreen : ScreenStatus<Nothing>()
    data class Success<out T : Any>(val value: T) : ScreenStatus<T>()
    data class ErrorFullScreen(val error: String) : ScreenStatus<Nothing>()
}
