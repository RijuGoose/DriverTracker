package com.riju.repository

import com.riju.domain.model.common.DatabaseConstants
import com.riju.repository.model.TrackingPoint
import com.riju.repository.model.TripDetails

interface TripHistoryRepository {
    suspend fun getAllTripHistory(orderBy: DatabaseConstants.Field, isAscending: Boolean = false): List<TripDetails>
    suspend fun getTripHistoryRouteById(tripId: String): List<TrackingPoint>?
    suspend fun getTripDetails(tripId: String): TripDetails?
}
