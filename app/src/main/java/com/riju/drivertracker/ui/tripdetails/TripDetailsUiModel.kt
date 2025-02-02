package com.riju.drivertracker.ui.tripdetails

import java.time.ZonedDateTime

data class TripDetailsUiModel(
    val maxSpeed: Double,
    val distance: Double,
    val startTime: ZonedDateTime,
    val endTime: ZonedDateTime? = null,
    val startLocation: String? = null,
    val endLocation: String? = null
)
