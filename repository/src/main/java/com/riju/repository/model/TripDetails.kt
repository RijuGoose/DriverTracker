package com.riju.repository.model

data class TripDetails(
    val tripId: String,
    val startTime: String,
    val endTime: String? = null,
    val startLocation: String? = null,
    val endLocation: String? = null,
)
