package com.riju.drivertracker.datasource.model

import kotlinx.serialization.Serializable

@Serializable
data class TripDetailsApiModel(
    val tripName: String = "",
    val startTime: String = "",
    val endTime: String? = null,
    val startLocation: String = "",
    val endLocation: String? = null,
)
