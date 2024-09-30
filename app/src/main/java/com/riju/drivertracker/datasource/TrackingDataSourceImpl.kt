package com.riju.drivertracker.datasource

import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.getValue
import com.riju.drivertracker.datasource.model.routepoint.RoutePointRequest
import com.riju.drivertracker.datasource.model.routepoint.RoutePointResponse
import com.riju.drivertracker.datasource.model.tripdetails.TripDetailsRequest
import com.riju.drivertracker.datasource.model.tripdetails.TripDetailsResponse
import com.riju.drivertracker.repository.model.DatabaseConstants
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
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

    override fun modifyEndTime(user: FirebaseUser, tripId: String, endTime: String) {
        database
            .child(DatabaseConstants.LIST_USERS)
            .child(user.uid)
            .child(DatabaseConstants.LIST_TRIPS)
            .child(tripId)
            .child(DatabaseConstants.FIELD_END_TIME)
            .setValue(endTime)
    }

    override fun addRoutePoint(user: FirebaseUser, tripId: String, pointCount: Int, routePoint: RoutePointRequest) {
        database
            .child(DatabaseConstants.LIST_USERS)
            .child(user.uid)
            .child(DatabaseConstants.LIST_TRIP_LOCATIONS)
            .child(tripId)
            .child(pointCount.toString())
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

    override fun getCurrentTripFlow(user: FirebaseUser, tripId: String): Flow<List<RoutePointResponse>?> {
        return callbackFlow {
            val tripListener = object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    trySend(snapshot.getValue<List<RoutePointResponse>>())
                }

                override fun onCancelled(error: DatabaseError) {
                    /* no-op */
                }
            }

            database
                .child(DatabaseConstants.LIST_USERS)
                .child(user.uid)
                .child(DatabaseConstants.LIST_TRIP_LOCATIONS)
                .child(tripId)
                .addValueEventListener(tripListener)

            awaitClose {
                database.removeEventListener(tripListener)
            }
        }
    }
}
