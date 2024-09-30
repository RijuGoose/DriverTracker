package com.riju.drivertracker.repository.model

data class TripDetails(
    val tripId: String,
    val startTime: String,
    val endTime: String? = null,
    val startLocation: String,
    val endLocation: String? = null,
)
