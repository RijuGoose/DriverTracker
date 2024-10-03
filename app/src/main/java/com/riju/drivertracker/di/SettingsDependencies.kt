package com.riju.drivertracker.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.core.DataStoreFactory
import androidx.datastore.dataStoreFile
import com.riju.drivertracker.datasource.SettingsDataSource
import com.riju.drivertracker.datasource.SettingsDataSourceImpl
import com.riju.drivertracker.datasource.SettingsSerializer
import com.riju.drivertracker.datasource.model.SettingsDataStoreModel
import com.riju.drivertracker.repository.SettingsRepository
import com.riju.drivertracker.repository.SettingsRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object SettingsDependencies {
    @Singleton
    @Provides
    fun provideSettingsDataSourceImpl(
        settingsDataStorePreferences: DataStore<SettingsDataStoreModel>
    ): SettingsDataSource {
        return SettingsDataSourceImpl(settingsDataStorePreferences)
    }

    @Singleton
    @Provides
    fun provideSettingsRepository(settingsDataSource: SettingsDataSource): SettingsRepository {
        return SettingsRepositoryImpl(settingsDataSource = settingsDataSource)
    }

    @Provides
    @Singleton
    fun provideSettingsDataStorePreferences(
        @ApplicationContext context: Context
    ): DataStore<SettingsDataStoreModel> {
        return DataStoreFactory.create(
            serializer = SettingsSerializer,
            produceFile = {
                context.dataStoreFile(SETTINGS_PREFERENCES)
            }
        )
    }

    private const val SETTINGS_PREFERENCES = "settings"
}
