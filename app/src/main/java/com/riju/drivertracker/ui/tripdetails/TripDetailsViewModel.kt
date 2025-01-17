package com.riju.drivertracker.ui.tripdetails

import android.content.Context
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.lerp
import androidx.compose.ui.graphics.luminance
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.riju.drivertracker.R
import com.riju.drivertracker.extensions.roundToDecimalPlaces
import com.riju.drivertracker.ui.BaseViewModel
import com.riju.drivertracker.ui.ScreenStatus
import com.riju.drivertracker.ui.navigation.Screen
import com.riju.repository.LocationRepository
import com.riju.repository.TripHistoryRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TripDetailsViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val tripHistoryRepository: TripHistoryRepository,
    private val locationRepository: LocationRepository,
    @ApplicationContext private val context: Context
) : BaseViewModel<TripDetailsUiModel>(defaultScreenState = ScreenStatus.LoadingFullScreen) {
    private val tripId = savedStateHandle.toRoute<Screen.TripDetails>().tripId
    private val _tripPoints = MutableStateFlow<List<Pair<LatLng, Color>>>(emptyList())
    var tripPoints = _tripPoints.asStateFlow()

    private val _mapBounds = MutableStateFlow<LatLngBounds?>(null)
    val mapBounds = _mapBounds.asStateFlow()

    init {
        loadTripDetails()
    }

    private fun loadTripDetails() {
        viewModelScope.launch {
            _screenStatus.value = ScreenStatus.LoadingFullScreen
            try {
                val tripDetails = tripHistoryRepository.getTripDetails(tripId)
                val tripRoute = tripHistoryRepository.getTripHistoryRouteById(tripId)
                val tripDistance = tripHistoryRepository.getDistanceTravelled(tripId)
                tripRoute?.let { tripData ->
                    val maxSpeed = tripData.maxOf { it.speed }
                    _tripPoints.value = tripData.map { tripPoint ->
                        Pair(
                            LatLng(tripPoint.lat, tripPoint.lon),
                            getColorForSpeed(speed = tripPoint.speed, maxSpeed = maxSpeed)
                        )
                    }
                    val boundsBuilder = LatLngBounds.builder()
                    for (point in _tripPoints.value) {
                        boundsBuilder.include(point.first)
                    }
                    _mapBounds.value = boundsBuilder.build()
                    tripDetails?.let { tripDetails ->
                        _screenStatus.value = ScreenStatus.Success(
                            TripDetailsUiModel(
                                avgSpeed = tripData.map {
                                    it.speed
                                }.average().roundToDecimalPlaces(2),
                                maxSpeed = maxSpeed,
                                distance = tripDistance.roundToDecimalPlaces(2),
                                startTime = tripDetails.startTime,
                                endTime = tripDetails.endTime,
                                startLocation = tripDetails.startLocation,
                                endLocation = tripDetails.endLocation
                            )
                        )
                    }
                } ?: run {
                    _screenStatus.value = ScreenStatus.ErrorFullScreen(
                        error = context.getString(R.string.trip_details_error_trip_not_found)
                    )
                }
            } catch (e: Exception) {
                _screenStatus.value =
                    ScreenStatus.ErrorFullScreen(e.message ?: context.getString(R.string.common_unknown_error))
            }
        }
    }

    companion object {
        const val CAMERA_PADDING = 100
    }

    @Suppress("MagicNumber")
    private fun getColorForSpeed(speed: Double, minSpeed: Double = 0.0, maxSpeed: Double = 200.0): Color {
        val clampedSpeed = speed.coerceIn(minSpeed, maxSpeed)
        val ratio = (clampedSpeed - minSpeed) / (maxSpeed - minSpeed)

        return when {
            ratio < 0.5 -> {
                lerp(Color.Green, Color.Yellow, (ratio * 2).toFloat())
            }

            else -> {
                lerp(Color.Yellow, Color.Red, ((ratio - 0.5) * 2).toFloat())
            }
        }
    }

    @Suppress("MagicNumber")
    fun getSpeedForColor(color: Color, minSpeed: Double = 0.0, maxSpeed: Double = 200.0): Double {
        val yellow = Color.Yellow
        val green = Color.Green
        val red = Color.Red

        val ratio = when {
            color == green -> 0.0
            color == yellow -> 0.5
            color == red -> 1.0

            color.luminance() < yellow.luminance() -> {
                color.luminance() / yellow.luminance() * 0.5
            }

            else -> {
                0.5 + (color.luminance() - yellow.luminance()) / (red.luminance() - yellow.luminance()) * 0.5
            }
        }

        return minSpeed + (ratio * (maxSpeed - minSpeed))
    }

    fun getEndLocation() {
        viewModelScope.launch {
            try {
                val address = locationRepository.getLocationAddress(
                    lat = _tripPoints.value.last().first.latitude,
                    lon = _tripPoints.value.last().first.longitude
                )

                if (address != null) {
                    tripHistoryRepository.modifyEndLocation(
                        tripId = tripId,
                        endLocation = address
                    )
                }
                loadTripDetails()
            } catch (ex: Exception) {
                showSnackBar("Error while getting location")
            }
        }
    }

    fun getStartLocation() {
        viewModelScope.launch {
            try {
                val address = locationRepository.getLocationAddress(
                    lat = _tripPoints.value.first().first.latitude,
                    lon = _tripPoints.value.first().first.longitude
                )

                if (address != null) {
                    tripHistoryRepository.modifyStartLocation(
                        tripId = tripId,
                        startLocation = address
                    )
                }
                loadTripDetails()
            } catch (ex: Exception) {
                showSnackBar("Error while getting location")
            }
        }
    }
}
