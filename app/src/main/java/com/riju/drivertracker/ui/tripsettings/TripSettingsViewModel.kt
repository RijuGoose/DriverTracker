package com.riju.drivertracker.ui.tripsettings

import androidx.lifecycle.viewModelScope
import com.riju.drivertracker.datasource.model.SettingsDataStoreModel
import com.riju.drivertracker.repository.SettingsRepository
import com.riju.drivertracker.ui.BaseViewModel
import com.riju.drivertracker.ui.ScreenStatus
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TripSettingsViewModel @Inject constructor(
    private val settingsRepository: SettingsRepository
) : BaseViewModel<Unit>() {
    val settings =
        settingsRepository.settings.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(),
            initialValue = SettingsDataStoreModel()
        )

    private val _btDeviceName = MutableStateFlow("")
    val btDeviceName = _btDeviceName.asStateFlow()

    fun setAutomaticTrip(value: Boolean) {
        viewModelScope.launch {
            settingsRepository.setAutomaticTrip(value)
        }
    }

    fun setTripCalendarEvent(value: Boolean) {
        viewModelScope.launch {
            settingsRepository.setTripCalendarEvent(value)
        }
    }

    fun setBluetoothDeviceName(value: String) {
        _btDeviceName.value = value
        viewModelScope.launch {
            settingsRepository.setBluetoothDeviceName(value)
        }
    }

    init {
        viewModelScope.launch {
            _btDeviceName.value = settingsRepository.settings.first().btDeviceName
            _screenStatus.value = ScreenStatus.Success(Unit)
        }
    }
}
