package com.riju.drivertracker.ui.map

import android.content.Context
import android.content.Intent
import com.riju.drivertracker.repository.UserRepository
import com.riju.drivertracker.service.LocationService
import com.riju.drivertracker.ui.BaseViewModel
import com.riju.drivertracker.ui.ScreenStatus
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import javax.inject.Inject

@HiltViewModel
class MapViewModel @Inject constructor(
    @ApplicationContext private val context: Context,
    private val userRepository: UserRepository,
) : BaseViewModel() {
    private val _onLogoutSuccess = MutableSharedFlow<Unit>(extraBufferCapacity = 1)
    val onLogoutSuccess = _onLogoutSuccess.asSharedFlow()

    fun startLocationService() {
        _screenStatus.value = ScreenStatus.Loading
        Intent(context, LocationService::class.java).apply {
            action = LocationService.ACTION_START
            context.startService(this)
        }
        _screenStatus.value = ScreenStatus.Success
    }

    fun stopLocationService() {
        _screenStatus.value = ScreenStatus.Loading
        Intent(context, LocationService::class.java).apply {
            action = LocationService.ACTION_STOP
            context.startService(this)
        }
        _screenStatus.value = ScreenStatus.Success
    }

    fun logout() {
        _screenStatus.value = ScreenStatus.Loading
        try {
            userRepository.logout()
            _screenStatus.value = ScreenStatus.Success
            _onLogoutSuccess.tryEmit(Unit)
        } catch (e: Exception) {
            _screenStatus.value = ScreenStatus.Error(e.message ?: "Unknown error")
        }
    }
}
