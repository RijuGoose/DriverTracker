package com.riju.drivertracker.ui.currenttrip

import android.content.Context
import android.content.Intent
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.google.android.gms.maps.model.LatLng
import com.riju.drivertracker.repository.LocationRepository
import com.riju.drivertracker.repository.TrackingRepository
import com.riju.drivertracker.repository.UserRepository
import com.riju.drivertracker.service.LocationService
import com.riju.drivertracker.ui.BaseViewModel
import com.riju.drivertracker.ui.ScreenStatus
import com.riju.drivertracker.ui.navigation.CurrentTripAction
import com.riju.drivertracker.ui.navigation.Screen
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class CurrentTripViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    @ApplicationContext private val context: Context,
    trackingRepository: TrackingRepository,
    locationRepository: LocationRepository,
    private val userRepository: UserRepository,
) : BaseViewModel<Unit>(ScreenStatus.Success(Unit)) {
    val currentTripRoute = trackingRepository.getCurrentTripFlow().map { trackingPoints ->
        trackingPoints?.map {
            LatLng(it.lat, it.lon)
        } ?: emptyList()
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(),
        initialValue = emptyList()
    )

    val currentLocation = locationRepository.getLocationUpdates(5000L)
        .map { location ->
            LatLng(location.latitude, location.longitude)
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(),
            initialValue = null
        )

    init {
        val action = savedStateHandle.toRoute<Screen.CurrentTrip>().action
        when (action) {
            CurrentTripAction.Start -> {
                if (!trackingRepository.isTracking()) {
                    Intent(context, LocationService::class.java).apply {
                        this.action = LocationService.ACTION_TRIP_START
                        context.startService(this)
                    }
                } else {
                    showSnackBar("Trip is already started")
                }
            }

            CurrentTripAction.Stop -> {
                if (trackingRepository.isTracking()) {
                    Intent(context, LocationService::class.java).apply {
                        this.action = LocationService.ACTION_TRIP_STOP
                        context.startService(this)
                    }
                } else {
                    _screenStatus.value = ScreenStatus.ErrorFullScreen("Trip is not started")
                }
            }

            else -> {
                /* no-op */
            }
        }
    }

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

    companion object {
        const val ZOOM_LEVEL: Float = 15f
    }
}
