package com.riju.drivertracker.datasource

import com.riju.drivertracker.datasource.model.SettingsDataStoreModel
import kotlinx.coroutines.flow.Flow

interface SettingsDataSource {
    val settings: Flow<SettingsDataStoreModel>
    suspend fun updateSettings(update: (SettingsDataStoreModel) -> SettingsDataStoreModel)
}
