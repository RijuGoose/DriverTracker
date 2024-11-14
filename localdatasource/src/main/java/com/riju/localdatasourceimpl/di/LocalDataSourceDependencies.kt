package com.riju.localdatasourceimpl.di

import android.content.Context
import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.core.DataStoreFactory
import androidx.datastore.dataStoreFile
import androidx.room.Room
import com.riju.localdatasourceimpl.AppDatabase
import com.riju.localdatasourceimpl.LocalTrackingDataSource
import com.riju.localdatasourceimpl.LocalTrackingDataSourceImpl
import com.riju.localdatasourceimpl.SettingsDataSource
import com.riju.localdatasourceimpl.SettingsDataSourceImpl
import com.riju.localdatasourceimpl.model.SettingsDataStoreModel
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import java.util.concurrent.Executors
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class LocalDataSourceDependencies {
    @Binds
    abstract fun provideSettingsDataSourceImpl(impl: SettingsDataSourceImpl): SettingsDataSource

    @Binds
    abstract fun provideLocalTrackingDataSourceImpl(impl: LocalTrackingDataSourceImpl): LocalTrackingDataSource

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

        @Provides
        @Singleton
        fun provideDatabase(
            @ApplicationContext context: Context
        ) =
            Room.databaseBuilder(
                context = context,
                klass = AppDatabase::class.java,
                name = "tracker_db"
            ).setQueryCallback({ sql, _ -> Log.d("Room query", sql) }, Executors.newSingleThreadExecutor())
                .build()

        @Provides
        @Singleton
        fun provideTripDao(database: AppDatabase) = database.tripDao()

        private const val SETTINGS_PREFERENCES = "settings"
    }
}
