package com.riju.drivertracker.repository

import com.riju.drivertracker.service.model.TrackingPoint
import kotlinx.coroutines.flow.Flow

interface TrackingRepository {
    val isTracking: Flow<Boolean>
    fun startTracking()
    fun addRoutePoint(trackingPoint: TrackingPoint)
    fun stopTracking()
    fun isTracking(): Boolean
    fun getCurrentTripFlow(): Flow<List<TrackingPoint>?>
}
