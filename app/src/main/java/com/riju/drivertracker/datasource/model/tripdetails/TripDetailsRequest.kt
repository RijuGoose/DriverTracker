package com.riju.drivertracker.datasource.model.tripdetails

data class TripDetailsRequest(
    override val tripName: String,
    override val startTime: String,
    override val startLocation: String,
    override val endLocation: String? = null,
    override val endTime: String? = null,
) : TripDetailsApiModel()
