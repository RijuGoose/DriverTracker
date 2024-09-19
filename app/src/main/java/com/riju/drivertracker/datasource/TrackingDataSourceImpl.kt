package com.riju.drivertracker.datasource

import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.getValue
import com.riju.drivertracker.datasource.model.routepoint.RoutePointRequest
import com.riju.drivertracker.datasource.model.routepoint.RoutePointResponse
import com.riju.drivertracker.datasource.model.tripdetails.TripDetailsRequest
import com.riju.drivertracker.datasource.model.tripdetails.TripDetailsResponse
import com.riju.drivertracker.repository.model.DatabaseConstants
import kotlinx.coroutines.tasks.await

class TrackingDataSourceImpl(
    private val database: DatabaseReference
) : TrackingDataSource {
    override fun addTripDetails(user: FirebaseUser, tripId: String, tripDetails: TripDetailsRequest) {
        database
            .child(DatabaseConstants.LIST_USERS)
            .child(user.uid)
            .child(DatabaseConstants.LIST_TRIPS)
            .child(tripId)
            .setValue(tripDetails)
    }

    override fun addRoutePoint(user: FirebaseUser, tripId: String, pointCount: Int, routePoint: RoutePointRequest) {
        database
            .child(DatabaseConstants.LIST_USERS)
            .child(user.uid)
            .child(DatabaseConstants.LIST_TRIP_LOCATIONS)
            .child(tripId)
            .child(pointCount.toString())
            // .push()
            .setValue(routePoint)
    }

    override suspend fun getAllTripHistory(user: FirebaseUser, orderBy: String): Map<String, TripDetailsResponse>? {
        return database
            .child(DatabaseConstants.LIST_USERS)
            .child(user.uid)
            .child(DatabaseConstants.LIST_TRIPS)
            .orderByChild(orderBy)
            .get().await().getValue<Map<String, TripDetailsResponse>>()
    }

    override suspend fun getTripHistoryRouteById(
        user: FirebaseUser,
        tripId: String
    ): List<RoutePointResponse>? {
        return database
            .child(DatabaseConstants.LIST_USERS)
            .child(user.uid)
            .child(DatabaseConstants.LIST_TRIP_LOCATIONS)
            .child(tripId)
            .get().await().getValue<List<RoutePointResponse>>()
    }

    override suspend fun getTripDetails(user: FirebaseUser, tripId: String): TripDetailsResponse? {
        return database
            .child(DatabaseConstants.LIST_USERS)
            .child(user.uid)
            .child(DatabaseConstants.LIST_TRIPS)
            .child(tripId)
            .get().await().getValue<TripDetailsResponse>()
    }
}
