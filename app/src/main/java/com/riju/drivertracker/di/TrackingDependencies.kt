package com.riju.drivertracker.di

import com.google.firebase.database.DatabaseReference
import com.riju.drivertracker.datasource.TrackingDataSource
import com.riju.drivertracker.datasource.TrackingDataSourceImpl
import com.riju.drivertracker.datasource.UserDataSource
import com.riju.drivertracker.repository.TrackingRepository
import com.riju.drivertracker.repository.TrackingRepositoryImpl
import com.riju.drivertracker.repository.TripHistoryRepository
import com.riju.drivertracker.repository.TripHistoryRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object TrackingDependencies {
    @Singleton
    @Provides
    fun provideTrackingRepositoryImpl(
        trackingDataSource: TrackingDataSource,
        userDataSource: UserDataSource
    ): TrackingRepository {
        return TrackingRepositoryImpl(trackingDataSource, userDataSource)
    }

    @Singleton
    @Provides
    fun provideTrackingHistoryRepositoryImpl(
        trackingDataSource: TrackingDataSource,
        userDataSource: UserDataSource
    ): TripHistoryRepository {
        return TripHistoryRepositoryImpl(trackingDataSource, userDataSource)
    }

    @Singleton
    @Provides
    fun provideTrackingDataSourceImpl(database: DatabaseReference): TrackingDataSource {
        return TrackingDataSourceImpl(database)
    }
}
