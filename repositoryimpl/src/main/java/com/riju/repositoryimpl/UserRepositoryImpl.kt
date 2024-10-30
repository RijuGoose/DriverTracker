package com.riju.repositoryimpl

import com.google.firebase.auth.FirebaseUser
import com.riju.remotedatasourceimpl.UserDataSource
import com.riju.repository.UserRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(
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
