package com.riju.drivertracker.repository

import com.riju.drivertracker.service.model.TrackingPoint

interface TrackingRepository {
    fun startTracking()
    fun addRoutePoint(trackingPoint: TrackingPoint)
    fun stopTracking()
    fun isTracking(): Boolean
}