package com.riju.drivertracker

import androidx.lifecycle.ViewModel
import com.riju.drivertracker.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    userRepository: UserRepository
) : ViewModel() {
    val currentUser = userRepository.getUser()
}
