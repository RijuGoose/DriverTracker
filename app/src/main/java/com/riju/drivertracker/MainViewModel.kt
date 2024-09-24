package com.riju.drivertracker

import androidx.lifecycle.ViewModel
import com.riju.drivertracker.repository.UserRepository
import com.riju.drivertracker.service.LocationService
import com.riju.drivertracker.ui.CurrentTripAction
import com.riju.drivertracker.ui.Screen
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    userRepository: UserRepository
) : ViewModel() {
    val currentUser = userRepository.getUser()
    var startScreen: Screen = Screen.Login

    fun setStartScreen(intentAction: String?) {
        startScreen = if (currentUser != null) {
            when (intentAction) {
                LocationService.ACTION_TRIP_START -> {
                    Screen.CurrentTrip(action = CurrentTripAction.Start)
                }

                LocationService.ACTION_TRIP_STOP -> {
                    Screen.CurrentTrip(action = CurrentTripAction.Stop)
                }

                else -> {
                    Screen.Map
                }
            }
        } else {
            Screen.Login
        }
    }
}
