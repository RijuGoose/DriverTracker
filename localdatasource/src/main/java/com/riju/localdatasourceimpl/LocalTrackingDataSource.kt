package com.riju.localdatasourceimpl

import com.riju.localdatasourceimpl.model.RoutePointEntity
import com.riju.localdatasourceimpl.model.TripEntity
import kotlinx.coroutines.flow.Flow

interface LocalTrackingDataSource {
    fun addTrip(trip: TripEntity)
    fun addRoutePoint(routePoint: RoutePointEntity)
    fun getTripPointsFlow(tripId: String): Flow<List<RoutePointEntity>?>
    suspend fun modifyEndTime(tripId: String, endTime: String)
    suspend fun getAllTripHistory(): List<TripEntity>
    suspend fun getTripPoints(tripId: String): List<RoutePointEntity>
    suspend fun getTripDetails(tripId: String): TripEntity?
}
