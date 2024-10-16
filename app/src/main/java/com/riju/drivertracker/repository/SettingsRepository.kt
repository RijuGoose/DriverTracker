package com.riju.drivertracker.repository

import com.riju.drivertracker.datasource.model.SettingsDataStoreModel
import kotlinx.coroutines.flow.Flow

interface SettingsRepository {
    val settings: Flow<SettingsDataStoreModel>
    suspend fun setAutomaticTrip(value: Boolean)
    suspend fun setBluetoothDeviceName(value: String)
    suspend fun setTripCalendarEvent(value: Boolean)
    suspend fun setBluetoothDeviceMacAddress(value: String)
}
