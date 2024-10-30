package com.riju.localdatasourceimpl.model

import kotlinx.serialization.Serializable

@Serializable
data class SettingsDataStoreModel(
    val automaticTrip: Boolean = false,
    val calendarEvent: Boolean = false,
    val btDeviceName: String = "",
    val btDeviceMacAddress: String = ""
)
