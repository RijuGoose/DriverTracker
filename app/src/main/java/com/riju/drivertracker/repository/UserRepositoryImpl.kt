package com.riju.drivertracker.repository

import com.google.firebase.auth.FirebaseUser
import com.riju.drivertracker.datasource.UserDataSource
import kotlinx.coroutines.flow.Flow

class UserRepositoryImpl(
    private val userDataSource: UserDataSource
) : UserRepository {
    override fun getUserFlow(): Flow<FirebaseUser?> {
        return userDataSource.getUserFlow()
    }

    override fun getUser(): FirebaseUser? {
        return userDataSource.getUser()
    }

    override suspend fun login(email: String, password: String): FirebaseUser? {
        return userDataSource.login(email, password)
    }

    override suspend fun register(
        email: String,
        password: String
    ): FirebaseUser? {
        return userDataSource.register(email, password)
    }

    override fun logout() {
        return userDataSource.logout()
    }
}
