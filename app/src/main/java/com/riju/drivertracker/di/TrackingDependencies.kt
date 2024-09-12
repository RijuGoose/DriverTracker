package com.riju.drivertracker.di

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.riju.drivertracker.repository.TrackingHistoryRepository
import com.riju.drivertracker.repository.TrackingHistoryRepositoryImpl
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
            database: DatabaseReference,
            firebaseAuth: FirebaseAuth
        ): TrackingRepository {
            return TrackingRepositoryImpl(database, firebaseAuth)
        }

        @Provides
        fun provideTrackingHistoryRepositoryImpl(database: DatabaseReference): TrackingHistoryRepository {
            return TrackingHistoryRepositoryImpl(database)
        }
    }
}