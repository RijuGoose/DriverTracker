package com.riju.remotedatasourceimpl

import com.google.firebase.auth.FirebaseUser
import com.riju.remotedatasourceimpl.model.routepoint.RoutePointRequest
import com.riju.remotedatasourceimpl.model.routepoint.RoutePointResponse
import com.riju.remotedatasourceimpl.model.tripdetails.TripDetailsRequest
import com.riju.remotedatasourceimpl.model.tripdetails.TripDetailsResponse
import kotlinx.coroutines.flow.Flow

interface RemoteTrackingDataSource {
    fun addTripDetails(user: FirebaseUser, tripId: String, tripDetails: TripDetailsRequest)
    fun modifyEndTime(user: FirebaseUser, tripId: String, endTime: String)
    suspend fun modifyStartLocation(user: FirebaseUser, tripId: String, startLocation: String)
    suspend fun modifyEndLocation(user: FirebaseUser, tripId: String, endLocation: String)
    fun addRoutePoint(user: FirebaseUser, tripId: String, pointCount: Int, routePoint: RoutePointRequest)
    suspend fun getAllTripHistory(user: FirebaseUser, orderBy: String): List<TripDetailsResponse>
    suspend fun getTripHistoryRouteById(user: FirebaseUser, tripId: String): List<RoutePointResponse>?
    suspend fun getTripDetails(user: FirebaseUser, tripId: String): TripDetailsResponse?
    fun getTripFlow(user: FirebaseUser, tripId: String): Flow<List<RoutePointResponse>?>
}
