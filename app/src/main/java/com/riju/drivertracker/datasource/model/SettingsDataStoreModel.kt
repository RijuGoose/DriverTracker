package com.riju.drivertracker.datasource.model

import kotlinx.serialization.Serializable

@Serializable
data class SettingsDataStoreModel(
    val automaticTrip: Boolean = false,
    val calendarEvent: Boolean = false,
    val btDeviceName: String = "",
)
