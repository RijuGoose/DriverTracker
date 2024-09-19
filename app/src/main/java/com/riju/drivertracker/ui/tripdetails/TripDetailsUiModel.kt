package com.riju.drivertracker.ui.tripdetails

data class TripDetailsUiModel(
    val avgSpeed: Double,
    val maxSpeed: Double,
    val distance: Double,
    val startTime: String,
    val endTime: String? = null,
    val startLocation: String,
    val endLocation: String? = null
)
