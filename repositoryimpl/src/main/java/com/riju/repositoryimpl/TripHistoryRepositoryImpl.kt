package com.riju.repositoryimpl

import com.riju.remotedatasourceimpl.TrackingDataSource
import com.riju.remotedatasourceimpl.UserDataSource
import com.riju.remotedatasourceimpl.model.DatabaseConstants
import com.riju.repository.TripHistoryRepository
import com.riju.repository.model.TrackingPoint
import com.riju.repository.model.TripDetails
import javax.inject.Inject

class TripHistoryRepositoryImpl @Inject constructor(
    private val trackingDataSource: TrackingDataSource,
    private val userDataSource: UserDataSource
) : TripHistoryRepository {
    override suspend fun getAllTripHistory(): List<TripDetails>? {
        return userDataSource.getUser()?.let { currentUser ->
            trackingDataSource.getAllTripHistory(currentUser, DatabaseConstants.FIELD_START_TIME).map { tripDetails ->
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
