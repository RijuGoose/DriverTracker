package com.riju.drivertracker.ui.tripdetails

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.riju.drivertracker.repository.TripHistoryRepository
import com.riju.drivertracker.ui.BaseViewModel
import com.riju.drivertracker.ui.Screen
import com.riju.drivertracker.ui.ScreenStatus
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TripDetailsViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    tripHistoryRepository: TripHistoryRepository
) : BaseViewModel() {
    private val tripId = savedStateHandle.toRoute<Screen.TripDetails>().tripId
    private val _tripPoints = MutableStateFlow<List<LatLng>>(emptyList())
    var tripPoints = _tripPoints.asStateFlow()

    private val _mapBounds =
        MutableStateFlow(
            LatLngBounds(
                LatLng(47.473889, 19.040833),
                LatLng(47.508611, 19.081944)
            ) // Budapest city centre
        )
    val mapBounds = _mapBounds.asStateFlow()

    init {
        viewModelScope.launch {
            try {
                _tripPoints.value = tripHistoryRepository.getTripHistoryRouteById(tripId)?.map {
                    LatLng(it.lat, it.lon)
                } ?: emptyList()
                val boundsBuilder = LatLngBounds.builder()
                for (point in _tripPoints.value) {
                    boundsBuilder.include(point)
                }
                _mapBounds.value = boundsBuilder.build()
                _screenStatus.value = ScreenStatus.Success
            } catch (e: Exception) {
                _screenStatus.value = ScreenStatus.ErrorFullScreen(e.message ?: "Unknown error")
            }
        }
    }

    companion object {
        const val CAMERA_PADDING = 200
    }
}
