package com.riju.drivertracker.di

import com.google.firebase.database.DatabaseReference
import com.riju.drivertracker.datasource.TrackingDataSource
import com.riju.drivertracker.datasource.TrackingDataSourceImpl
import com.riju.drivertracker.datasource.UserDataSource
import com.riju.drivertracker.repository.TripHistoryRepository
import com.riju.drivertracker.repository.TripHistoryRepositoryImpl
import com.riju.drivertracker.repository.TrackingRepository
import com.riju.drivertracker.repository.TrackingRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class TrackingDependencies {
    companion object {
        @Provides
        fun provideTrackingRepositoryImpl(
            trackingDataSource: TrackingDataSource,
            userDataSource: UserDataSource
        ): TrackingRepository {
            return TrackingRepositoryImpl(trackingDataSource, userDataSource)
        }

        @Provides
        fun provideTrackingHistoryRepositoryImpl(
            trackingDataSource: TrackingDataSource,
            userDataSource: UserDataSource
        ): TripHistoryRepository {
            return TripHistoryRepositoryImpl(trackingDataSource, userDataSource)
        }

        @Provides
        fun provideTrackingDataSourceImpl(database: DatabaseReference): TrackingDataSource {
            return TrackingDataSourceImpl(database)
        }
    }
}