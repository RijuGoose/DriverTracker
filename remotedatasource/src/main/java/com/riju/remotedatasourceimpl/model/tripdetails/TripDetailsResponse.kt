package com.riju.remotedatasourceimpl.model.tripdetails

data class TripDetailsResponse(
    val tripId: String = "",
    override val startTime: String = "",
    override val endTime: String? = null,
    override val startLocation: String? = null,
    override val endLocation: String? = null
) : TripDetailsApiModel()
