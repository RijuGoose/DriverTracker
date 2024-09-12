package com.riju.drivertracker.repository

import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.flow.StateFlow

interface UserRepository {
    val currentUser: StateFlow<FirebaseUser?>
    fun login(email: String, password: String, onCompleted: (task: Task<AuthResult>) -> Unit)
    fun register(email: String, password: String, onCompleted: (task: Task<AuthResult>) -> Unit)
    fun logout()
}