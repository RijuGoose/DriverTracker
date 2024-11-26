package com.riju.repositoryimpl

import android.location.Location
import com.riju.domain.model.common.DatabaseConstants
import com.riju.localdatasourceimpl.LocalTrackingDataSource
import com.riju.remotedatasourceimpl.RemoteTrackingDataSource
import com.riju.remotedatasourceimpl.UserDataSource
import com.riju.repository.TripHistoryRepository
import com.riju.repository.model.TrackingPoint
import com.riju.repository.model.TripDetails
import java.time.ZonedDateTime
import javax.inject.Inject

class TripHistoryRepositoryImpl @Inject constructor(
    private val remoteTrackingDataSource: RemoteTrackingDataSource,
    private val localTrackingDataSource: LocalTrackingDataSource,
    private val userDataSource: UserDataSource
) : TripHistoryRepository {
    override suspend fun getAllTripHistory(orderBy: DatabaseConstants.Field, isAscending: Boolean): List<TripDetails> {
        return userDataSource.getUser()?.let { currentUser ->
            remoteTrackingDataSource.getAllTripHistory(currentUser, orderBy.fieldName) // TODO isAscending
                .map { tripDetails ->
                    TripDetails(
                        tripId = tripDetails.tripId,
                        startTime = ZonedDateTime.parse(tripDetails.startTime),
                        endTime = ZonedDateTime.parse(tripDetails.endTime),
                        startLocation = tripDetails.startLocation,
                        endLocation = tripDetails.endLocation,
                    )
                }
        } ?: localTrackingDataSource.getAllTripHistory(orderBy.fieldName, isAscending).map { tripEntity ->
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
                    startTime = ZonedDateTime.parse(requireNotNull(tripDetails?.startTime)),
                    endTime = ZonedDateTime.parse(tripDetails?.endTime),
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

    override suspend fun getLastTripDetails(): TripDetails? {
        return userDataSource.getUser()?.let {
            // TODO implement
            null
        } ?: localTrackingDataSource.getAllTripHistory(
            orderBy = DatabaseConstants.Field.EndTime.fieldName,
            isAscending = false
        ).map { tripEntity ->
            TripDetails(
                tripId = tripEntity.tripId,
                startTime = tripEntity.startTime,
                endTime = tripEntity.endTime,
                startLocation = tripEntity.startLocation,
                endLocation = tripEntity.endLocation,
            )
        }.firstOrNull()
    }

    @Suppress("MagicNumber")
    override suspend fun getDistanceTravelled(tripId: String): Double {
        return localTrackingDataSource.getTripPoints(tripId).zipWithNext { point1, point2 ->
            Location("point1").apply {
                latitude = point1.latitude
                longitude = point1.longitude
            }.distanceTo(
                Location("point2").apply {
                    latitude = point2.latitude
                    longitude = point2.longitude
                }
            )
        }.sum().toDouble() / 1000
    }

    override suspend fun modifyStartLocation(tripId: String, startLocation: String) {
        userDataSource.getUser()?.let { currentUser ->
            remoteTrackingDataSource.modifyStartLocation(currentUser, tripId, startLocation)
        } ?: localTrackingDataSource.modifyStartLocation(tripId, startLocation)
    }

    override suspend fun modifyEndLocation(tripId: String, endLocation: String) {
        userDataSource.getUser()?.let { currentUser ->
            remoteTrackingDataSource.modifyEndLocation(currentUser, tripId, endLocation)
        } ?: localTrackingDataSource.modifyEndLocation(tripId, endLocation)
    }
}
