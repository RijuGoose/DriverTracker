package com.riju.remotedatasourceimpl.di

import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.database
import com.riju.remotedatasourceimpl.RemoteTrackingDataSource
import com.riju.remotedatasourceimpl.RemoteTrackingDataSourceImpl
import com.riju.remotedatasourceimpl.UserDataSource
import com.riju.remotedatasourceimpl.UserDataSourceImpl
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class DataSourceDependencies {
    @Binds
    abstract fun provideRemoteTrackingDataSourceImpl(impl: RemoteTrackingDataSourceImpl): RemoteTrackingDataSource

    @Binds
    abstract fun provideUserDataSourceImpl(impl: UserDataSourceImpl): UserDataSource

    companion object {
        @Provides
        fun provideDatabaseReference(): DatabaseReference {
            return Firebase.database(
                "https://drivertracker-b34d5-default-rtdb.europe-west1.firebasedatabase.app/"
            ).reference
        }

        @Provides
        fun provideFirebaseAuth(): FirebaseAuth {
            return Firebase.auth
        }
    }
}
