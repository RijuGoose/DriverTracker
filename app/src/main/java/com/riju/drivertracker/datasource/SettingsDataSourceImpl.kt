package com.riju.drivertracker.datasource

import androidx.datastore.core.DataStore
import com.riju.drivertracker.datasource.model.SettingsDataStoreModel
import kotlinx.coroutines.flow.Flow

class SettingsDataSourceImpl(
    private val settingsDataStore: DataStore<SettingsDataStoreModel>
) : SettingsDataSource {

    override val settings: Flow<SettingsDataStoreModel>
        get() = settingsDataStore.data

    override suspend fun updateSettings(update: (SettingsDataStoreModel) -> SettingsDataStoreModel) {
        settingsDataStore.updateData { settingsDataStoreModel ->
            update(settingsDataStoreModel)
        }
    }
}
