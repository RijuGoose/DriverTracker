package com.riju.repositoryimpl.di

import com.riju.repository.BluetoothRepository
import com.riju.repository.DebugLogRepository
import com.riju.repository.LocationRepository
import com.riju.repository.PermissionRepository
import com.riju.repository.SettingsRepository
import com.riju.repository.TrackingRepository
import com.riju.repository.TripHistoryRepository
import com.riju.repository.UserRepository
import com.riju.repositoryimpl.BluetoothRepositoryImpl
import com.riju.repositoryimpl.DebugLogRepositoryImpl
import com.riju.repositoryimpl.LocationRepositoryImpl
import com.riju.repositoryimpl.PermissionRepositoryImpl
import com.riju.repositoryimpl.SettingsRepositoryImpl
import com.riju.repositoryimpl.TrackingRepositoryImpl
import com.riju.repositoryimpl.TripHistoryRepositoryImpl
import com.riju.repositoryimpl.UserRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryDependencies {

    @Binds
    abstract fun provideBluetoothRepositoryImpl(impl: BluetoothRepositoryImpl): BluetoothRepository

    @Binds
    abstract fun provideLocationRepositoryImpl(impl: LocationRepositoryImpl): LocationRepository

    @Binds
    abstract fun providePermissionRepositoryImpl(impl: PermissionRepositoryImpl): PermissionRepository

    @Singleton
    @Binds
    abstract fun provideSettingsRepository(impl: SettingsRepositoryImpl): SettingsRepository

    @Singleton
    @Binds
    abstract fun provideTrackingRepositoryImpl(impl: TrackingRepositoryImpl): TrackingRepository

    @Binds
    abstract fun provideTrackingHistoryRepositoryImpl(impl: TripHistoryRepositoryImpl): TripHistoryRepository

    @Binds
    abstract fun provideUserRepositoryImpl(impl: UserRepositoryImpl): UserRepository

    @Singleton
    @Binds
    abstract fun provideDebugLogRepositoryImpl(impl: DebugLogRepositoryImpl): DebugLogRepository
}
