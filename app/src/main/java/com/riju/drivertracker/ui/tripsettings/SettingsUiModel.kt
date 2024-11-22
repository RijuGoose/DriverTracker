package com.riju.drivertracker.ui.tripsettings

data class SettingsUiModel(
    var automaticTrip: Boolean = false,
    var btDeviceName: String = "",
    var calendarEvent: Boolean = false,
    var shouldMergeTrips: Boolean = false,
    var mergeTripSeconds: Int? = 60
)
