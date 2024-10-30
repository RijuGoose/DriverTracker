package com.riju.remotedatasourceimpl

import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.flow.Flow

interface UserDataSource {
    fun getUserFlow(): Flow<FirebaseUser?>
    fun getUser(): FirebaseUser?
    suspend fun login(email: String, password: String): FirebaseUser?
    suspend fun register(email: String, password: String): FirebaseUser?
    fun logout()
}
