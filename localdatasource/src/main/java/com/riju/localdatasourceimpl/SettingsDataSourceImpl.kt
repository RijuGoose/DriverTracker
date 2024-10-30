package com.riju.localdatasourceimpl

import androidx.datastore.core.DataStore
import com.riju.localdatasourceimpl.model.SettingsDataStoreModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class SettingsDataSourceImpl @Inject constructor(
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
