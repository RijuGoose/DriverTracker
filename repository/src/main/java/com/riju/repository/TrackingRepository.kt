package com.riju.repository

import com.riju.repository.model.TrackingPoint
import com.riju.repository.model.TripDetails
import kotlinx.coroutines.flow.Flow

interface TrackingRepository {
    val isTracking: Flow<Boolean>
    suspend fun startTrip()
    fun addRoutePoint(trackingPoint: TrackingPoint)
    suspend fun stopTrip()
    suspend fun setEndpoints(startLocation: String, endLocation: String)
    fun isTracking(): Boolean
    fun getCurrentTripFlow(): Flow<List<TrackingPoint>?>
    suspend fun resumeTrip(trip: TripDetails)
}
