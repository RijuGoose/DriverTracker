package com.riju.drivertracker

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.riju.drivertracker.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    userRepository: UserRepository
): ViewModel() {
    val currentUser = userRepository.currentUser
    init {
        viewModelScope.launch {
            userRepository.currentUser.collectLatest {
                Log.d("libalog", "User: $it")
            }
        }
    }
}