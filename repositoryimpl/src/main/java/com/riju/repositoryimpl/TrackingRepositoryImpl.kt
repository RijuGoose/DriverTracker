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
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import java.time.ZonedDateTime
import java.util.UUID
import javax.inject.Inject

class TrackingRepositoryImpl @Inject constructor(
    private val localTrackingDataSource: LocalTrackingDataSource
) : TrackingRepository {
    private val currentTripId: MutableStateFlow<String?> = MutableStateFlow(null)
    private var currentTripCounter: Int = 0

    override val isTracking = currentTripId.map { it != null }

    override suspend fun startTrip() {
        currentTripCounter = 0
        currentTripId.value = UUID.randomUUID().toString()

        localTrackingDataSource.addTrip(
            trip = TripEntity(
                tripId = requireNotNull(currentTripId.value),
                startTime = ZonedDateTime.now()
            )
        )
    }

    override suspend fun resumeTrip(trip: TripDetails) {
        localTrackingDataSource.getTripPoints(trip.tripId).lastOrNull()?.let { lastPoint ->
            currentTripId.value = trip.tripId
            currentTripCounter = lastPoint.pointCount + 1
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    override fun getCurrentTripFlow(): Flow<List<TrackingPoint>?> {
        return currentTripId.filterNotNull().flatMapLatest { tripId ->
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
                tripId = requireNotNull(currentTripId.value),
                pointCount = currentTripCounter++,
                latitude = trackingPoint.lat,
                longitude = trackingPoint.lon,
                speed = trackingPoint.speed
            )
        )
    }

    override suspend fun stopTrip() {
        currentTripId.value?.let { tripId ->
            currentTripCounter = 0
            localTrackingDataSource.modifyEndTime(
                tripId = tripId,
                endTime = ZonedDateTime.now().toString()
            )
            currentTripId.value = null
        }
    }

    override suspend fun setEndpoints(startLocation: String, endLocation: String) {
        currentTripId.value?.let { tripId ->
            localTrackingDataSource.modifyStartLocation(tripId, startLocation)
            localTrackingDataSource.modifyEndLocation(tripId, endLocation)
        }
    }

    override fun isTracking(): Boolean {
        return currentTripId.value != null
    }
}
