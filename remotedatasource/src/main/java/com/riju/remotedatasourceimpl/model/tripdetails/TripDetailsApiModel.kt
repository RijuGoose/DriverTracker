package com.riju.remotedatasourceimpl.model.tripdetails

import kotlinx.serialization.Serializable

@Serializable
abstract class TripDetailsApiModel {
    abstract val startTime: String
    abstract val endTime: String?
    abstract val startLocation: String?
    abstract val endLocation: String?
}
