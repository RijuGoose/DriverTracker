package com.riju.drivertracker.repository

import com.riju.drivertracker.repository.model.TripDetails
import com.riju.drivertracker.service.model.TrackingPoint

interface TripHistoryRepository {
    suspend fun getAllTripHistory(): List<TripDetails>?
    suspend fun getTripHistoryRouteById(tripId: String): List<TrackingPoint>?
}