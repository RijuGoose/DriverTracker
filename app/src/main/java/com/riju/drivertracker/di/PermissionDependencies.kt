package com.riju.drivertracker.di

import android.content.Context
import com.riju.drivertracker.repository.PermissionRepository
import com.riju.drivertracker.repository.PermissionRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object PermissionDependencies {

    @Singleton
    @Provides
    fun providePermissionRepositoryImpl(
        @ApplicationContext context: Context
    ): PermissionRepository {
        return PermissionRepositoryImpl(context)
    }
}
