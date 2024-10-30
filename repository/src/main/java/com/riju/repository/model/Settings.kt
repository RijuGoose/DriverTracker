package com.riju.repository.model

data class Settings(
    val automaticTrip: Boolean,
    val calendarEvent: Boolean,
    val btDeviceName: String,
    val btDeviceMacAddress: String
)
