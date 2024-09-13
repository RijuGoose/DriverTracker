package com.riju.drivertracker.ui.map

import android.content.Context
import android.content.Intent
import com.riju.drivertracker.repository.UserRepository
import com.riju.drivertracker.service.LocationService
import com.riju.drivertracker.ui.BaseViewModel
import com.riju.drivertracker.ui.ScreenStatus
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

@HiltViewModel
class MapViewModel @Inject constructor(
    @ApplicationContext private val context: Context,
    private val userRepository: UserRepository,
) : BaseViewModel() {
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
            _screenStatus.value = ScreenStatus.Success
        } catch (e: Exception) {
            _screenStatus.value = ScreenStatus.Failure(e.message ?: "Unknown error")
        }
    }
}
