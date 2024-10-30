package com.riju.repositoryimpl

import com.riju.localdatasourceimpl.SettingsDataSource
import com.riju.repository.SettingsRepository
import com.riju.repository.model.Settings
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class SettingsRepositoryImpl @Inject constructor(
    private val settingsDataSource: SettingsDataSource
) : SettingsRepository {
    override val settings: Flow<Settings>
        get() = settingsDataSource.settings.map {
            Settings(
                automaticTrip = it.automaticTrip,
                calendarEvent = it.calendarEvent,
                btDeviceName = it.btDeviceName,
                btDeviceMacAddress = it.btDeviceMacAddress
            )
        }

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
