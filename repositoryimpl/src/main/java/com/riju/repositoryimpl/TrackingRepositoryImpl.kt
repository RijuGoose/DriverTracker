package com.riju.repositoryimpl

import com.riju.localdatasourceimpl.LocalTrackingDataSource
import com.riju.localdatasourceimpl.model.RoutePointEntity
import com.riju.localdatasourceimpl.model.TripEntity
import com.riju.repository.SettingsRepository
import com.riju.repository.TrackingRepository
import com.riju.repository.model.TrackingPoint
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import java.time.Duration
import java.time.ZonedDateTime
import java.util.UUID
import javax.inject.Inject

class TrackingRepositoryImpl @Inject constructor(
    private val localTrackingDataSource: LocalTrackingDataSource,
    private val settingsRepository: SettingsRepository
) : TrackingRepository {
    private val currentTripId: MutableStateFlow<String?> = MutableStateFlow(null)
    private var currentTripCounter: Int = 0

    override val isTracking = currentTripId.map { it != null }

    override suspend fun startTracking() {
        currentTripId.value = UUID.randomUUID().toString()
        currentTripCounter = 0

        val lastTrip = localTrackingDataSource.getLastTripDetails()
        val shouldMergeTrips = settingsRepository.settings.first().shouldMergeTrips

        if (shouldMergeTrips) {
            resumeOrInitTrip(lastTrip)
        } else {
            initTrip()
        }
    }

    private suspend fun resumeOrInitTrip(lastTrip: TripEntity?) {
        lastTrip?.let { trip ->
            val duration = Duration.between(trip.endTime, ZonedDateTime.now())
            val tripMergeDuration = settingsRepository.settings.first().mergeTripSeconds.toLong()

            if (duration < Duration.ofSeconds(tripMergeDuration)) {
                localTrackingDataSource.getTripPoints(trip.tripId).lastOrNull()?.let { lastPoint ->
                    currentTripId.value = trip.tripId
                    currentTripCounter = lastPoint.pointCount + 1
                }
            } else {
                initTrip()
            }
        } ?: initTrip()
    }

    private fun initTrip() {
        localTrackingDataSource.addTrip(
            trip = TripEntity(
                tripId = requireNotNull(currentTripId.value),
                startTime = ZonedDateTime.now()
            )
        )
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

    override suspend fun stopTracking() {
        currentTripCounter = 0
        localTrackingDataSource.modifyEndTime(
            tripId = requireNotNull(currentTripId.value),
            endTime = ZonedDateTime.now().toString()
        )
        currentTripId.value = null
    }

    override fun isTracking(): Boolean {
        return currentTripId.value != null
    }
}
