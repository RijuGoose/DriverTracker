package com.riju.drivertracker.repository

import com.riju.drivertracker.datasource.TrackingDataSource
import com.riju.drivertracker.datasource.UserDataSource
import com.riju.drivertracker.repository.model.TripDetails
import com.riju.drivertracker.service.model.TrackingPoint

class TripHistoryRepositoryImpl(
    private val trackingDataSource: TrackingDataSource,
    private val userDataSource: UserDataSource
) : TripHistoryRepository {
    override suspend fun getAllTripHistory(): List<TripDetails>? {
        return userDataSource.getUser()?.let { currentUser ->
            trackingDataSource.getAllTripHistory(currentUser)?.map { tripDetails ->
                TripDetails(
                    tripId = tripDetails.key,
                    tripName = tripDetails.value.tripName,
                    startTime = tripDetails.value.startTime,
                    endTime = tripDetails.value.endTime,
                    startLocation = tripDetails.value.startLocation,
                    endLocation = tripDetails.value.endLocation,
                )
            }
        }
    }

    override suspend fun getTripHistoryRouteById(tripId: String): List<TrackingPoint>? {
        return userDataSource.getUser()?.let { currentUser ->
            emptyList()
//            database
//                .child(DatabaseConstants.LIST_USERS)
//                .child(currentUser.uid)
//                .child(DatabaseConstants.LIST_TRIP_LOCATIONS)
//                .child(tripId).get().await().getValue<List<TrackingPoint>>()
        }
    }
}