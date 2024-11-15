package com.riju.drivertracker.ui.triphistory.model

import java.time.LocalDateTime

data class TripHistoryItemUIModel(
    val tripId: String,
    val startTime: LocalDateTime,
    val endTime: LocalDateTime? = null,
    val startLocation: String? = null,
    val endLocation: String? = null,
)
