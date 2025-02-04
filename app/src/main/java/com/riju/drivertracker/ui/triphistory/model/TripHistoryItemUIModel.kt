package com.riju.drivertracker.ui.triphistory.model

import java.time.ZonedDateTime

data class TripHistoryItemUIModel(
    val tripId: String,
    val inProgress: Boolean,
    val startTime: ZonedDateTime,
    val endTime: ZonedDateTime? = null,
    val startLocation: String? = null,
    val endLocation: String? = null,
)
