package com.riju.repository.model

import java.time.ZonedDateTime

data class TripDetails(
    val tripId: String,
    val startTime: ZonedDateTime,
    val endTime: ZonedDateTime? = null,
    val startLocation: String? = null,
    val endLocation: String? = null,
)
