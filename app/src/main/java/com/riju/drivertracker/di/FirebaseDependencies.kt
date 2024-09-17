package com.riju.drivertracker.di

import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.database
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object FirebaseDependencies {
    @Provides
    fun provideFirebaseAuth(): FirebaseAuth {
        return Firebase.auth
    }

    @Provides
    fun provideDatabaseReference(): DatabaseReference {
        return Firebase.database(
            "https://drivertracker-b34d5-default-rtdb.europe-west1.firebasedatabase.app/"
        ).reference
    }
}
