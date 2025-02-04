package com.riju.repository

import com.riju.repository.model.TrackingPoint
import com.riju.repository.model.TripDetails
import kotlinx.coroutines.flow.StateFlow
import java.time.LocalDate

interface TripHistoryRepository {
    val tripHistoryList: StateFlow<Map<LocalDate, List<TripDetails>>>
    val isListAscending: StateFlow<Boolean>
    suspend fun getTripRouteById(tripId: String): List<TrackingPoint>?
    suspend fun getTripDetails(tripId: String): TripDetails?
    suspend fun getLastTripDetails(): TripDetails?
    suspend fun getDistanceTravelled(tripId: String): Double
    suspend fun modifyStartLocation(tripId: String, startLocation: String)
    suspend fun modifyEndLocation(tripId: String, endLocation: String)
    suspend fun deleteTrip(tripId: String)
    fun toggleSortOrder()
}
