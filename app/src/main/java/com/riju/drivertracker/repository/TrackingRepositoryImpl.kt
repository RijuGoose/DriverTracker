package com.riju.drivertracker.repository

import com.riju.drivertracker.datasource.TrackingDataSource
import com.riju.drivertracker.datasource.UserDataSource
import com.riju.drivertracker.datasource.model.routepoint.RoutePointRequest
import com.riju.drivertracker.datasource.model.tripdetails.TripDetailsRequest
import com.riju.drivertracker.service.model.TrackingPoint
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import java.time.LocalDateTime
import java.util.UUID

class TrackingRepositoryImpl(
    private val trackingDataSource: TrackingDataSource,
    private val userDataSource: UserDataSource
) : TrackingRepository {
    private val currentTripId: MutableStateFlow<String?> = MutableStateFlow(null)
    private var currentTripCounter: Int = 0

    override val isTracking = currentTripId.map { it != null }

    override fun startTracking() {
        currentTripId.value = UUID.randomUUID().toString()
        currentTripCounter = 0
        userDataSource.getUser()?.let { currentUser ->
            trackingDataSource.addTripDetails(
                user = currentUser,
                tripId = requireNotNull(currentTripId.value),
                tripDetails = TripDetailsRequest(
                    startTime = LocalDateTime.now().toString(),
                    startLocation = "Start Location", // TODO get address somehow
                )
            )
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    override fun getCurrentTripFlow(): Flow<List<TrackingPoint>?> {
        return userDataSource.getUserFlow().filterNotNull().flatMapLatest { currentUser ->
            currentTripId.filterNotNull().flatMapLatest { tripId ->
                trackingDataSource.getCurrentTripFlow(currentUser, tripId)
                    .map { routePoints ->
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
    }

    override fun addRoutePoint(trackingPoint: TrackingPoint) {
        userDataSource.getUser()?.let { currentUser ->
            trackingDataSource.addRoutePoint(
                user = currentUser,
                tripId = requireNotNull(currentTripId.value),
                pointCount = currentTripCounter++,
                routePoint = RoutePointRequest(
                    latitude = trackingPoint.lat,
                    longitude = trackingPoint.lon,
                    speed = trackingPoint.speed
                )
            )
        }
    }

    override fun stopTracking() {
        currentTripCounter = 0
        userDataSource.getUser()?.let { currentUser ->
            trackingDataSource.modifyEndTime(
                user = currentUser,
                tripId = requireNotNull(currentTripId.value),
                endTime = LocalDateTime.now().toString()
            )
        }
        currentTripId.value = null
    }

    override fun isTracking(): Boolean {
        return currentTripId.value != null
    }
}
