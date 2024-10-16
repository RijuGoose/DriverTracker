package com.riju.drivertracker.ui.tripsettings

import android.annotation.SuppressLint
import androidx.lifecycle.viewModelScope
import com.riju.drivertracker.datasource.model.SettingsDataStoreModel
import com.riju.drivertracker.repository.BluetoothRepository
import com.riju.drivertracker.repository.SettingsRepository
import com.riju.drivertracker.repository.model.BTDevice
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

@SuppressLint("MissingPermission")
@HiltViewModel
class TripSettingsViewModel @Inject constructor(
    private val settingsRepository: SettingsRepository,
    private val bluetoothRepository: BluetoothRepository
) : BaseViewModel<Unit>() {
    private val _bluetoothPermissionDialog = MutableStateFlow(false)
    val bluetoothPermissionDialog = _bluetoothPermissionDialog.asStateFlow()
    private val _backgroundLocationPermissionDialog = MutableStateFlow(false)
    val backgroundLocationPermissionDialog = _backgroundLocationPermissionDialog.asStateFlow()

    val settings =
        settingsRepository.settings.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(),
            initialValue = SettingsDataStoreModel()
        )

    var btBondedDevices: List<BTDevice> = emptyList()
        private set

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

    fun getBondedBTDevices() {
        btBondedDevices = bluetoothRepository.getBondedDevices()
    }

    fun showBackgroundLocationPermissionDialog() {
        _backgroundLocationPermissionDialog.value = true
    }

    fun hideBackgroundLocationPermissionDialog() {
        _backgroundLocationPermissionDialog.value = false
    }

    fun showBluetoothPermissionDialog() {
        _bluetoothPermissionDialog.value = true
    }

    fun hideBluetoothPermissionDialog() {
        _bluetoothPermissionDialog.value = false
    }

    init {
        viewModelScope.launch {
            _btDeviceName.value = settingsRepository.settings.first().btDeviceName
            _screenStatus.value = ScreenStatus.Success(Unit)
        }
    }
}
