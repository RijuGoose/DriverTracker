package com.riju.repositoryimpl

import com.riju.localdatasourceimpl.SettingsDataSource
import com.riju.repository.SettingsRepository
import com.riju.repository.model.Settings
import com.riju.repositoryimpl.mapper.toSettings
import com.riju.repositoryimpl.mapper.toSettingsDataStoreModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class SettingsRepositoryImpl @Inject constructor(
    private val settingsDataSource: SettingsDataSource
) : SettingsRepository {
    override val settings: Flow<Settings>
        get() = settingsDataSource.settings.map {
            it.toSettings()
        }

    override suspend fun setShouldMergeTrips(value: Boolean) {
        settingsDataSource.updateSettings { it.copy(shouldMergeTrips = value) }
    }

    override suspend fun setMergeTripSeconds(value: Int) {
        settingsDataSource.updateSettings { it.copy(mergeTripSeconds = value) }
    }

    override suspend fun updateSettings(settings: Settings) {
        settingsDataSource.updateSettings {
            settings.toSettingsDataStoreModel()
        }
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
