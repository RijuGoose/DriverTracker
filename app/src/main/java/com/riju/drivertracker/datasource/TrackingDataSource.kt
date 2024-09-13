package com.riju.drivertracker.datasource

import com.google.firebase.auth.FirebaseUser
import com.riju.drivertracker.datasource.model.routepoint.RoutePointRequest
import com.riju.drivertracker.datasource.model.routepoint.RoutePointResponse
import com.riju.drivertracker.datasource.model.tripdetails.TripDetailsRequest
import com.riju.drivertracker.datasource.model.tripdetails.TripDetailsResponse

interface TrackingDataSource {
    fun addTripDetails(user: FirebaseUser, tripId: String, tripDetails: TripDetailsRequest)
    fun addRoutePoint(user: FirebaseUser, tripId: String, pointCount: Int, routePoint: RoutePointRequest)
    suspend fun getAllTripHistory(user: FirebaseUser): Map<String, TripDetailsResponse>?
    suspend fun getTripHistoryRouteById(user: FirebaseUser, tripId: String): List<RoutePointResponse>?
    suspend fun getTripDetails(user: FirebaseUser, tripId: String): TripDetailsResponse?
}