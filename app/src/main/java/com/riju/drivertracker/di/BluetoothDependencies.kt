package com.riju.drivertracker.di

import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothManager
import android.content.Context
import androidx.core.content.ContextCompat.getSystemService
import com.riju.drivertracker.repository.BluetoothRepository
import com.riju.drivertracker.repository.BluetoothRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object BluetoothDependencies {

    @Singleton
    @Provides
    fun provideBluetoothRepositoryImpl(
        bluetoothAdapter: BluetoothAdapter?,
        @ApplicationContext context: Context,
    ): BluetoothRepository {
        return BluetoothRepositoryImpl(
            bluetoothAdapter = bluetoothAdapter,
            context = context
        )
    }

    @Provides
    fun provideAndroidBluetoothAdapter(@ApplicationContext context: Context) =
        getSystemService(context, BluetoothManager::class.java)?.adapter
}
