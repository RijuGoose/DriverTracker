package com.riju.drivertracker

import androidx.lifecycle.ViewModel
import com.riju.drivertracker.service.LocationService
import com.riju.drivertracker.ui.navigation.CurrentTripAction
import com.riju.drivertracker.ui.navigation.Screen
import com.riju.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    userRepository: UserRepository
) : ViewModel() {
    val currentUser = userRepository.getUser()
    var startScreen: Screen = Screen.Login
    var mainStartScreen: Screen = Screen.CurrentTrip()

    fun setStartScreen(intentAction: String?) {
        startScreen =
//            if (currentUser != null) {
            when (intentAction) {
                LocationService.ACTION_TRIP_START -> {
                    mainStartScreen = Screen.CurrentTrip(action = CurrentTripAction.Start)
                    Screen.Main
                }

                LocationService.ACTION_TRIP_STOP -> {
                    mainStartScreen = Screen.CurrentTrip(action = CurrentTripAction.Stop)
                    Screen.Main
                }

                else -> {
                    Screen.Main
                }
            }
//            } else {
//                Screen.Auth // uncomment when user registration is enabled
//            }
    }
}
