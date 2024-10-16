package com.riju.drivertracker.repository

import com.riju.drivertracker.datasource.SettingsDataSource
import com.riju.drivertracker.datasource.model.SettingsDataStoreModel
import kotlinx.coroutines.flow.Flow

class SettingsRepositoryImpl(
    private val settingsDataSource: SettingsDataSource
) : SettingsRepository {
    override val settings: Flow<SettingsDataStoreModel>
        get() = settingsDataSource.settings

    override suspend fun setAutomaticTrip(value: Boolean) {
        settingsDataSource.updateSettings { it.copy(automaticTrip = value) }
    }

    override suspend fun setTripCalendarEvent(value: Boolean) {
        settingsDataSource.updateSettings { it.copy(calendarEvent = value) }
    }

    override suspend fun setBluetoothDeviceName(value: String) {
        settingsDataSource.updateSettings { it.copy(btDeviceName = value) }
    }

    override suspend fun setBluetoothDeviceMacAddress(value: String) {
        settingsDataSource.updateSettings { it.copy(btDeviceMacAddress = value) }
    }
}
