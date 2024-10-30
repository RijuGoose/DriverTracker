package com.riju.localdatasourceimpl

import com.riju.localdatasourceimpl.model.SettingsDataStoreModel
import kotlinx.coroutines.flow.Flow

interface SettingsDataSource {
    val settings: Flow<SettingsDataStoreModel>
    suspend fun updateSettings(update: (SettingsDataStoreModel) -> SettingsDataStoreModel)
}
