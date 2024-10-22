package com.riju.drivertracker.repository

import com.riju.drivertracker.datasource.TrackingDataSource
import com.riju.drivertracker.datasource.UserDataSource
import com.riju.drivertracker.repository.model.TripDetails
import com.riju.drivertracker.service.model.TrackingPoint

class TripHistoryRepositoryImpl(
    private val trackingDataSource: TrackingDataSource,
    private val userDataSource: UserDataSource
) : TripHistoryRepository {
    override suspend fun getAllTripHistory(orderBy: String): List<TripDetails>? {
        return userDataSource.getUser()?.let { currentUser ->
            trackingDataSource.getAllTripHistory(currentUser, orderBy).map { tripDetails ->
                TripDetails(
                    tripId = tripDetails.tripId,
                    startTime = tripDetails.startTime,
                    endTime = tripDetails.endTime,
                    startLocation = tripDetails.startLocation,
                    endLocation = tripDetails.endLocation,
                )
            }
        }
    }

    override suspend fun getTripHistoryRouteById(tripId: String): List<TrackingPoint>? {
        return userDataSource.getUser()?.let { currentUser ->
            trackingDataSource.getTripHistoryRouteById(currentUser, tripId)?.map { routePoint ->
                TrackingPoint(
                    lat = routePoint.latitude,
                    lon = routePoint.longitude,
                    speed = routePoint.speed
                )
            }
        }
    }

    override suspend fun getTripDetails(tripId: String): TripDetails? {
        return userDataSource.getUser()?.let { currentUser ->
            trackingDataSource.getTripDetails(currentUser, tripId).let { tripDetails ->
                TripDetails(
                    tripId = tripId,
                    startTime = requireNotNull(tripDetails?.startTime),
                    endTime = tripDetails?.endTime,
                    startLocation = requireNotNull(tripDetails?.startLocation),
                    endLocation = tripDetails?.endLocation,
                )
            }
        }
    }
}
