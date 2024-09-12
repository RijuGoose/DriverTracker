package com.riju.drivertracker.repository

import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class UserRepositoryImpl(
    private val firebaseAuth: FirebaseAuth
) : UserRepository {
    private val _currentUser = MutableStateFlow(firebaseAuth.currentUser)
    override val currentUser = _currentUser.asStateFlow()

    init {
        firebaseAuth.addAuthStateListener {
            _currentUser.value = it.currentUser
        }
    }

    override fun login(email: String, password: String, onCompleted: (task: Task<AuthResult>) -> Unit) {
        firebaseAuth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(onCompleted)
    }

    override fun register(
        email: String, password: String,
        onCompleted: (task: Task<AuthResult>) -> Unit
    ) {
        firebaseAuth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(onCompleted)
    }

    override fun logout() {
        firebaseAuth.signOut()
    }
}