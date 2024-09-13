package com.riju.drivertracker.ui.tripdetails

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.navigation.toRoute
import com.riju.drivertracker.ui.Screen
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class TripDetailsViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle
) : ViewModel() {
    private val tripId = savedStateHandle.toRoute<Screen.TripDetails>().tripId

    init {
        Log.d("libalog", tripId)
    }
}