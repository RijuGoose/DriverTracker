package com.riju.repository

import com.riju.repository.model.Settings
import kotlinx.coroutines.flow.Flow

interface SettingsRepository {
    val settings: Flow<Settings>
    suspend fun setAutomaticTrip(value: Boolean)
    suspend fun setBluetoothDeviceName(value: String)
    suspend fun setMergeTripSeconds(value: Int)
    suspend fun setShouldMergeTrips(value: Boolean)
    suspend fun setBluetoothDeviceMacAddress(value: String)
    suspend fun updateSettings(settings: Settings)
}
