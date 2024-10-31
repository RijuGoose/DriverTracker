package com.riju.repositoryimpl

import com.riju.localdatasourceimpl.LocalTrackingDataSource
import com.riju.remotedatasourceimpl.RemoteTrackingDataSource
import com.riju.remotedatasourceimpl.UserDataSource
import com.riju.remotedatasourceimpl.model.DatabaseConstants
import com.riju.repository.TripHistoryRepository
import com.riju.repository.model.TrackingPoint
import com.riju.repository.model.TripDetails
import javax.inject.Inject

class TripHistoryRepositoryImpl @Inject constructor(
    private val remoteTrackingDataSource: RemoteTrackingDataSource,
    private val localTrackingDataSource: LocalTrackingDataSource,
    private val userDataSource: UserDataSource
) : TripHistoryRepository {
    override suspend fun getAllTripHistory(): List<TripDetails> {
        return userDataSource.getUser()?.let { currentUser ->
            remoteTrackingDataSource.getAllTripHistory(currentUser, DatabaseConstants.FIELD_START_TIME)
                .map { tripDetails ->
                    TripDetails(
                        tripId = tripDetails.tripId,
                        startTime = tripDetails.startTime,
                        endTime = tripDetails.endTime,
                        startLocation = tripDetails.startLocation,
                        endLocation = tripDetails.endLocation,
                    )
                }
        } ?: localTrackingDataSource.getAllTripHistory().map { tripEntity ->
            TripDetails(
                tripId = tripEntity.tripId,
                startTime = tripEntity.startTime,
                endTime = tripEntity.endTime,
                startLocation = tripEntity.startLocation,
                endLocation = tripEntity.endLocation,
            )
        }
    }

    override suspend fun getTripHistoryRouteById(tripId: String): List<TrackingPoint>? {
        return userDataSource.getUser()?.let { currentUser ->
            remoteTrackingDataSource.getTripHistoryRouteById(currentUser, tripId)?.map { routePoint ->
                TrackingPoint(
                    lat = routePoint.latitude,
                    lon = routePoint.longitude,
                    speed = routePoint.speed
                )
            }
        } ?: localTrackingDataSource.getTripPoints(tripId).map { routePointEntity ->
            TrackingPoint(
                lat = routePointEntity.latitude,
                lon = routePointEntity.longitude,
                speed = routePointEntity.speed
            )
        }
    }

    override suspend fun getTripDetails(tripId: String): TripDetails? {
        return userDataSource.getUser()?.let { currentUser ->
            remoteTrackingDataSource.getTripDetails(currentUser, tripId).let { tripDetails ->
                TripDetails(
                    tripId = tripId,
                    startTime = requireNotNull(tripDetails?.startTime),
                    endTime = tripDetails?.endTime,
                    startLocation = requireNotNull(tripDetails?.startLocation),
                    endLocation = tripDetails?.endLocation,
                )
            }
        } ?: localTrackingDataSource.getTripDetails(tripId)?.let { tripEntity ->
            TripDetails(
                tripId = tripId,
                startTime = tripEntity.startTime,
                endTime = tripEntity.endTime,
                startLocation = tripEntity.startLocation,
                endLocation = tripEntity.endLocation,
            )
        }
    }
}
