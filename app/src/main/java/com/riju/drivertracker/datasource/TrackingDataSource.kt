package com.riju.drivertracker.datasource

import com.google.firebase.auth.FirebaseUser
import com.riju.drivertracker.datasource.model.RoutePointApiModel
import com.riju.drivertracker.datasource.model.TripDetailsApiModel

interface TrackingDataSource {
    fun addTripDetails(user: FirebaseUser, tripId: String, tripDetails: TripDetailsApiModel)
    fun addRoutePoint(user: FirebaseUser, tripId: String, routePoint: RoutePointApiModel)
    suspend fun getAllTripHistory(user: FirebaseUser): Map<String, TripDetailsApiModel>?
}