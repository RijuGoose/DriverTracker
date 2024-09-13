package com.riju.drivertracker.datasource.model.tripdetails

import kotlinx.serialization.Serializable

@Serializable
abstract class TripDetailsApiModel{
    abstract val tripName: String
    abstract val startTime: String
    abstract val endTime: String?
    abstract val startLocation: String
    abstract val endLocation: String?
}

