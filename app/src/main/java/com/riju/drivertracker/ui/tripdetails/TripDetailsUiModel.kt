package com.riju.drivertracker.ui.tripdetails

import java.time.LocalDateTime

data class TripDetailsUiModel(
    val avgSpeed: Double,
    val maxSpeed: Double,
    val distance: Double,
    val startTime: LocalDateTime,
    val endTime: LocalDateTime? = null,
    val startLocation: String,
    val endLocation: String? = null
)
