package com.riju.repositoryimpl

import com.riju.localdatasourceimpl.LocalTrackingDataSource
import com.riju.localdatasourceimpl.model.RoutePointEntity
import com.riju.localdatasourceimpl.model.TripEntity
import com.riju.repository.TrackingRepository
import com.riju.repository.model.TrackingPoint
import com.riju.repository.model.TripDetails
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import java.time.ZonedDateTime
import java.util.UUID
import javax.inject.Inject

class TrackingRepositoryImpl @Inject constructor(
    private val localTrackingDataSource: LocalTrackingDataSource
) : TrackingRepository {
    private val _currentTripId: MutableStateFlow<String?> = MutableStateFlow(null)
    override val currentTripId: Flow<String?> = _currentTripId.asStateFlow()
    private var currentTripCounter: Int = 0

    override val isTracking = _currentTripId.map { it != null }

    override suspend fun startTrip() {
        currentTripCounter = 0
        _currentTripId.value = UUID.randomUUID().toString()

        localTrackingDataSource.addTrip(
            trip = TripEntity(
                tripId = requireNotNull(_currentTripId.value),
                startTime = ZonedDateTime.now()
            )
        )
    }

    override suspend fun resumeTrip(trip: TripDetails) {
        localTrackingDataSource.getTripPoints(trip.tripId).lastOrNull()?.let { lastPoint ->
            _currentTripId.value = trip.tripId
            currentTripCounter = lastPoint.pointCount + 1
        } ?: run {
            _currentTripId.value = trip.tripId
            currentTripCounter = 0
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    override fun getCurrentTripFlow(): Flow<List<TrackingPoint>?> {
        return _currentTripId.filterNotNull().flatMapLatest { tripId ->
            localTrackingDataSource.getTripPointsFlow(tripId).map { routePoints ->
                routePoints?.map { routePoint ->
                    TrackingPoint(
                        lat = routePoint.latitude,
                        lon = routePoint.longitude,
                        speed = routePoint.speed
                    )
                } ?: emptyList()
            }
        }
    }

    override fun addRoutePoint(trackingPoint: TrackingPoint) {
        localTrackingDataSource.addRoutePoint(
            routePoint = RoutePointEntity(
                tripId = requireNotNull(_currentTripId.value),
                pointCount = currentTripCounter++,
                latitude = trackingPoint.lat,
                longitude = trackingPoint.lon,
                speed = trackingPoint.speed
            )
        )
    }

    override suspend fun stopTrip() {
        _currentTripId.value?.let { tripId ->
            currentTripCounter = 0
            localTrackingDataSource.modifyEndTime(
                tripId = tripId,
                endTime = ZonedDateTime.now().toString()
            )
            _currentTripId.value = null
        }
    }

    override suspend fun setEndpoints(startLocation: String, endLocation: String) {
        _currentTripId.value?.let { tripId ->
            localTrackingDataSource.modifyStartLocation(tripId, startLocation)
            localTrackingDataSource.modifyEndLocation(tripId, endLocation)
        }
    }

    override fun isTracking(): Boolean {
        return _currentTripId.value != null
    }
}
