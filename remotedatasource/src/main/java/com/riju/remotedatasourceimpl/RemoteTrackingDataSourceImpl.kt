package com.riju.remotedatasourceimpl

import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.getValue
import com.riju.domain.model.common.DatabaseConstants
import com.riju.remotedatasourceimpl.model.routepoint.RoutePointRequest
import com.riju.remotedatasourceimpl.model.routepoint.RoutePointResponse
import com.riju.remotedatasourceimpl.model.tripdetails.TripDetailsRequest
import com.riju.remotedatasourceimpl.model.tripdetails.TripDetailsResponse
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class RemoteTrackingDataSourceImpl @Inject constructor(
    private val database: DatabaseReference
) : RemoteTrackingDataSource {
    override fun addTripDetails(user: FirebaseUser, tripId: String, tripDetails: TripDetailsRequest) {
        database
            .child(DatabaseConstants.List.Users.listName)
            .child(user.uid)
            .child(DatabaseConstants.List.Trips.listName)
            .child(tripId)
            .setValue(tripDetails)
    }

    override fun modifyEndTime(user: FirebaseUser, tripId: String, endTime: String) {
        database
            .child(DatabaseConstants.List.Users.listName)
            .child(user.uid)
            .child(DatabaseConstants.List.Trips.listName)
            .child(tripId)
            .child(DatabaseConstants.Field.EndTime.fieldName)
            .setValue(endTime)
    }

    override fun addRoutePoint(user: FirebaseUser, tripId: String, pointCount: Int, routePoint: RoutePointRequest) {
        database
            .child(DatabaseConstants.List.Users.listName)
            .child(user.uid)
            .child(DatabaseConstants.List.TripLocations.listName)
            .child(tripId)
            .child(pointCount.toString())
            .setValue(routePoint)
    }

    override suspend fun getAllTripHistory(user: FirebaseUser, orderBy: String): List<TripDetailsResponse> {
        return database
            .child(DatabaseConstants.List.Users.listName)
            .child(user.uid)
            .child(DatabaseConstants.List.Trips.listName)
            .orderByChild(orderBy)
            .get()
            .await().children.map { dataSnapshot ->
                dataSnapshot.getValue<TripDetailsResponse>()?.copy(
                    tripId = dataSnapshot.key.toString()
                ) ?: return emptyList()
            }
    }

    override suspend fun getTripHistoryRouteById(
        user: FirebaseUser,
        tripId: String
    ): List<RoutePointResponse>? {
        return database
            .child(DatabaseConstants.List.Users.listName)
            .child(user.uid)
            .child(DatabaseConstants.List.TripLocations.listName)
            .child(tripId)
            .get().await().getValue<List<RoutePointResponse>>()
    }

    override suspend fun getTripDetails(user: FirebaseUser, tripId: String): TripDetailsResponse? {
        return database
            .child(DatabaseConstants.List.Users.listName)
            .child(user.uid)
            .child(DatabaseConstants.List.Trips.listName)
            .child(tripId)
            .get().await().getValue<TripDetailsResponse>()
    }

    override fun getTripFlow(user: FirebaseUser, tripId: String): Flow<List<RoutePointResponse>?> {
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
                .child(DatabaseConstants.List.Users.listName)
                .child(user.uid)
                .child(DatabaseConstants.List.TripLocations.listName)
                .child(tripId)
                .addValueEventListener(tripListener)

            awaitClose {
                database.removeEventListener(tripListener)
            }
        }
    }
}
