package com.riju.drivertracker.datasource.model.tripdetails

data class TripDetailsResponse(
    val tripId: String = "",
    override val startTime: String = "",
    override val endTime: String? = null,
    override val startLocation: String = "",
    override val endLocation: String? = null
) : TripDetailsApiModel()
