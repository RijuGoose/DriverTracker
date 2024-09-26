package com.riju.drivertracker.di

import com.google.firebase.auth.FirebaseAuth
import com.riju.drivertracker.datasource.UserDataSource
import com.riju.drivertracker.datasource.UserDataSourceImpl
import com.riju.drivertracker.repository.UserRepository
import com.riju.drivertracker.repository.UserRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object UserDependencies {
    @Singleton
    @Provides
    fun provideUserRepositoryImpl(userDataSource: UserDataSource): UserRepository {
        return UserRepositoryImpl(userDataSource)
    }

    @Singleton
    @Provides
    fun provideUserDataSourceImpl(firebaseAuth: FirebaseAuth): UserDataSource {
        return UserDataSourceImpl(firebaseAuth)
    }
}
