package com.riju.repository

import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.flow.Flow

interface UserRepository {
    suspend fun register(email: String, password: String): FirebaseUser?
    fun logout()
    suspend fun login(email: String, password: String): FirebaseUser?
    fun getUserFlow(): Flow<FirebaseUser?>
    fun getUser(): FirebaseUser?
}
