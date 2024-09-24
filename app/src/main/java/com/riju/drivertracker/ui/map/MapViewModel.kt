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
) : BaseViewModel<Unit>(defaultScreenState = ScreenStatus.Success(Unit)) {
    private val _onLogoutSuccess = MutableSharedFlow<Unit>(extraBufferCapacity = 1)
    val onLogoutSuccess = _onLogoutSuccess.asSharedFlow()

    fun startLocationService() {
        showLoadingDialog()
        Intent(context, LocationService::class.java).apply {
            action = LocationService.ACTION_TRIP_START
            context.startService(this)
        }
        hideLoadingDialog()
    }

    fun stopLocationService() {
        showLoadingDialog()
        Intent(context, LocationService::class.java).apply {
            action = LocationService.ACTION_TRIP_STOP
            context.startService(this)
        }
        hideLoadingDialog()
    }

    fun logout() {
        showLoadingDialog()
        try {
            userRepository.logout()
            _onLogoutSuccess.tryEmit(Unit)
        } catch (e: Exception) {
            showError(e.message ?: "Unknown error")
        } finally {
            hideLoadingDialog()
        }
    }
}
