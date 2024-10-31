package com.riju.repository

import com.riju.repository.model.TrackingPoint
import kotlinx.coroutines.flow.Flow

interface TrackingRepository {
    val isTracking: Flow<Boolean>
    fun startTracking()
    fun addRoutePoint(trackingPoint: TrackingPoint)
    suspend fun stopTracking()
    fun isTracking(): Boolean
    fun getCurrentTripFlow(): Flow<List<TrackingPoint>?>
}
