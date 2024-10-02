package com.riju.drivertracker.ui.currenttrip

import android.content.Context
import android.content.Intent
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.google.android.gms.maps.model.LatLng
import com.riju.drivertracker.R
import com.riju.drivertracker.repository.LocationRepository
import com.riju.drivertracker.repository.TrackingRepository
import com.riju.drivertracker.service.LocationService
import com.riju.drivertracker.ui.BaseViewModel
import com.riju.drivertracker.ui.ScreenStatus
import com.riju.drivertracker.ui.navigation.CurrentTripAction
import com.riju.drivertracker.ui.navigation.Screen
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class CurrentTripViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    @ApplicationContext private val context: Context,
    trackingRepository: TrackingRepository,
    private val locationRepository: LocationRepository
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
                    showSnackBar(context.getString(R.string.current_trip_trip_already_running))
                }
            }

            CurrentTripAction.Stop -> {
                if (trackingRepository.isTracking()) {
                    Intent(context, LocationService::class.java).apply {
                        this.action = LocationService.ACTION_TRIP_STOP
                        context.startService(this)
                    }
                } else {
                    _screenStatus.value = ScreenStatus.ErrorFullScreen(
                        error = context.getString(R.string.current_trip_error_no_running_trip)
                    )
                }
            }

            else -> {
                /* no-op */
            }
        }
    }

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

    suspend fun getCurrentLocation(): LatLng? {
        return locationRepository.getCurrentLocation()?.let {
            LatLng(it.latitude, it.longitude)
        }
    }

    companion object {
        const val ZOOM_LEVEL: Float = 15f
    }
}
