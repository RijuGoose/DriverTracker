package com.riju.localdatasourceimpl

import com.riju.localdatasourceimpl.model.RoutePointEntity
import com.riju.localdatasourceimpl.model.TripEntity
import kotlinx.coroutines.flow.Flow

interface LocalTrackingDataSource {
    fun addTrip(trip: TripEntity)
    fun addRoutePoint(routePoint: RoutePointEntity)
    fun getTripPointsFlow(tripId: String): Flow<List<RoutePointEntity>?>
    suspend fun modifyEndTime(tripId: String, endTime: String)
    suspend fun modifyStartLocation(tripId: String, startLocation: String)
    suspend fun modifyEndLocation(tripId: String, endLocation: String)
    suspend fun getAllTripHistoryFlow(orderBy: String, isAscending: Boolean): Flow<List<TripEntity>>
    suspend fun getLastTripDetails(): TripEntity?
    suspend fun getTripPoints(tripId: String): List<RoutePointEntity>
    suspend fun getTripDetails(tripId: String): TripEntity?
}
