package com.riju.drivertracker.di

import android.content.Context
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.riju.drivertracker.repository.LocationRepository
import com.riju.drivertracker.repository.LocationRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object LocationDependencies {
    @Provides
    fun provideFusedLocationProviderClient(@ApplicationContext context: Context): FusedLocationProviderClient {
        return LocationServices.getFusedLocationProviderClient(context)
    }

    @Provides
    fun provideLocationRepositoryImpl(
        @ApplicationContext context: Context,
        client: FusedLocationProviderClient
    ): LocationRepository {
        return LocationRepositoryImpl(context, client)
    }
}
