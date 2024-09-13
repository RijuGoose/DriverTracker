package com.riju.drivertracker.datasource

import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.getValue
import com.riju.drivertracker.datasource.model.RoutePointApiModel
import com.riju.drivertracker.datasource.model.TripDetailsApiModel
import com.riju.drivertracker.repository.model.DatabaseConstants
import kotlinx.coroutines.tasks.await

class TrackingDataSourceImpl(
    private val database: DatabaseReference
): TrackingDataSource {
    override fun addTripDetails(user: FirebaseUser, tripId: String, tripDetails: TripDetailsApiModel) {
        database
            .child(DatabaseConstants.LIST_USERS)
            .child(user.uid)
            .child(DatabaseConstants.LIST_TRIPS)
            .child(tripId)
            .setValue(tripDetails)
    }

    override fun addRoutePoint(user: FirebaseUser, tripId: String, routePoint: RoutePointApiModel) {
        database
            .child(DatabaseConstants.LIST_USERS)
            .child(user.uid)
            .child(DatabaseConstants.LIST_TRIP_LOCATIONS)
            .child(tripId)
            //.child(currentTripCounter.toString())
            .push()
            .setValue(routePoint)
    }

    override suspend fun getAllTripHistory(user: FirebaseUser): Map<String, TripDetailsApiModel>? {
        return database
            .child(DatabaseConstants.LIST_USERS)
            .child(user.uid)
            .child(DatabaseConstants.LIST_TRIPS)
            //.orderByChild(DatabaseConstants.FIELD_START_TIME)
            .get().await().getValue<Map<String, TripDetailsApiModel>>()
    }
}