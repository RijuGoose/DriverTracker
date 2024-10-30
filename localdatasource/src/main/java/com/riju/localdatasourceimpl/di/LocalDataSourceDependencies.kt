package com.riju.localdatasourceimpl.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.core.DataStoreFactory
import androidx.datastore.dataStoreFile
import com.riju.localdatasourceimpl.SettingsDataSource
import com.riju.localdatasourceimpl.SettingsDataSourceImpl
import com.riju.localdatasourceimpl.model.SettingsDataStoreModel
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class LocalDataSourceDependencies {
    @Binds
    abstract fun provideSettingsDataSourceImpl(impl: SettingsDataSourceImpl): SettingsDataSource

    companion object {
        @Provides
        @Singleton
        fun provideSettingsDataStorePreferences(
            @ApplicationContext context: Context
        ): DataStore<SettingsDataStoreModel> {
            return DataStoreFactory.create(
                serializer = com.riju.localdatasourceimpl.SettingsSerializer,
                produceFile = {
                    context.dataStoreFile(SETTINGS_PREFERENCES)
                }
            )
        }

        private const val SETTINGS_PREFERENCES = "settings"
    }
}
