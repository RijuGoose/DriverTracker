package com.riju.drivertracker.di

import com.google.firebase.auth.FirebaseAuth
import com.riju.drivertracker.repository.UserRepository
import com.riju.drivertracker.repository.UserRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class UserDependencies {
    companion object {
        @Provides
        fun provideUserRepositoryImpl(firebaseAuth: FirebaseAuth): UserRepository {
            return UserRepositoryImpl(firebaseAuth)
        }
    }
}