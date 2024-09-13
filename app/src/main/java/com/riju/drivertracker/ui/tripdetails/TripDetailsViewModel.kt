package com.riju.drivertracker.ui.tripdetails

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.google.android.gms.maps.model.LatLng
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

    init {
        viewModelScope.launch {
            try {
                _tripPoints.value = tripHistoryRepository.getTripHistoryRouteById(tripId)?.map {
                    LatLng(it.lat, it.lon)
                } ?: emptyList()
                _screenStatus.value = ScreenStatus.Success
                Log.d("libalog", _tripPoints.value.toString())
            } catch (e: Exception) {
                Log.e("libalog", e.message ?: "Unknown error")
                _screenStatus.value = ScreenStatus.Failure(e.message ?: "Unknown error")
            }
        }
    }
}