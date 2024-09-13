package com.riju.drivertracker.ui

import kotlinx.serialization.Serializable

object Screen {
    @Serializable
    object Login
    @Serializable
    object Register
    @Serializable
    object Map
    @Serializable
    object TripHistory
    @Serializable
    data class TripDetails(val tripId: String)
}