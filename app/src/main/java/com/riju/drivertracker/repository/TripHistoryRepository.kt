package com.riju.drivertracker.repository

import com.riju.drivertracker.repository.model.DatabaseConstants
import com.riju.drivertracker.repository.model.TripDetails
import com.riju.drivertracker.service.model.TrackingPoint

interface TripHistoryRepository {
    suspend fun getAllTripHistory(orderBy: String = DatabaseConstants.FIELD_TRIP_NAME): List<TripDetails>?
    suspend fun getTripHistoryRouteById(tripId: String): List<TrackingPoint>?
    suspend fun getTripDetails(tripId: String): TripDetails?
}
