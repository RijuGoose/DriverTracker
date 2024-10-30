package com.riju.drivertracker.di

import android.bluetooth.BluetoothManager
import android.content.Context
import androidx.core.content.ContextCompat.getSystemService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object BluetoothDependencies {

    @Provides
    fun provideAndroidBluetoothAdapter(@ApplicationContext context: Context) =
        getSystemService(context, BluetoothManager::class.java)?.adapter
}
