package com.riju.drivertracker.ui.map

import android.content.Context
import android.content.Intent
import androidx.lifecycle.ViewModel
import com.riju.drivertracker.repository.TripHistoryRepository
import com.riju.drivertracker.repository.TrackingRepository
import com.riju.drivertracker.repository.UserRepository
import com.riju.drivertracker.service.LocationService
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class MapViewModel @Inject constructor(
    @ApplicationContext private val context: Context,
    private val userRepository: UserRepository,
) : ViewModel() {
    private val _logoutStatus: MutableStateFlow<LogoutState?> = MutableStateFlow(null)
    val logoutStatus = _logoutStatus.asStateFlow()

    fun startLocationService() {
        Intent(context, LocationService::class.java).apply {
            action = LocationService.ACTION_START
            context.startService(this)
        }
    }

    fun stopLocationService() {
        Intent(context, LocationService::class.java).apply {
            action = LocationService.ACTION_STOP
            context.startService(this)
        }
    }

    fun logout() {
        try {
            userRepository.logout()
            _logoutStatus.value = LogoutState.Success
        } catch (e: Exception) {
            _logoutStatus.value = LogoutState.Failure(e.message ?: "Unknown error")
        }
    }
}

sealed class LogoutState {
    data object Success : LogoutState()
    data class Failure(val error: String) : LogoutState()
}