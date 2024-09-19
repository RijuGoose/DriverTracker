package com.riju.drivertracker.repository

import com.riju.drivertracker.datasource.TrackingDataSource
import com.riju.drivertracker.datasource.UserDataSource
import com.riju.drivertracker.datasource.model.routepoint.RoutePointRequest
import com.riju.drivertracker.datasource.model.tripdetails.TripDetailsRequest
import com.riju.drivertracker.service.model.TrackingPoint
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.UUID

class TrackingRepositoryImpl(
    private val trackingDataSource: TrackingDataSource,
    private val userDataSource: UserDataSource
) : TrackingRepository {
    private var currentTripId: String? = null
    private var currentTripCounter: Int = 0

    override fun startTracking() {
        currentTripId = UUID.randomUUID().toString()
        currentTripCounter = 0
        userDataSource.getUser()?.let { currentUser ->
            trackingDataSource.addTripDetails(
                user = currentUser,
                tripId = requireNotNull(currentTripId),
                tripDetails = TripDetailsRequest(
                    tripName = "TripName ChangeMe",
                    startTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS")),
                    startLocation = "Start Location",
                )
            )
        }
    }

    override fun addRoutePoint(trackingPoint: TrackingPoint) {
        userDataSource.getUser()?.let { currentUser ->
            trackingDataSource.addRoutePoint(
                user = currentUser,
                tripId = requireNotNull(currentTripId),
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
        currentTripId = null
        currentTripCounter = 0
    }

    override fun isTracking(): Boolean {
        TODO("Not yet implemented")
    }
}
