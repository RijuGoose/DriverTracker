package com.riju.drivertracker.ui.tripsettings

import android.annotation.SuppressLint
import androidx.lifecycle.viewModelScope
import com.riju.drivertracker.ui.BaseViewModel
import com.riju.drivertracker.ui.ScreenStatus
import com.riju.repository.BluetoothRepository
import com.riju.repository.SettingsRepository
import com.riju.repository.model.BTDevice
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@SuppressLint("MissingPermission")
@Suppress("TooManyFunctions")
@HiltViewModel
class TripSettingsViewModel @Inject constructor(
    private val settingsRepository: SettingsRepository,
    private val bluetoothRepository: BluetoothRepository
) : BaseViewModel<Unit>() {
    private val _bluetoothPermissionDialog = MutableStateFlow(false)
    val bluetoothPermissionDialog = _bluetoothPermissionDialog.asStateFlow()
    private val _backgroundLocationPermissionDialog = MutableStateFlow(false)
    val backgroundLocationPermissionDialog = _backgroundLocationPermissionDialog.asStateFlow()

    val tripSecondsConditionValid: () -> Boolean = {
        !_settingsUiModel.value.shouldMergeTrips ||
            _settingsUiModel.value.mergeTripSeconds in 1..300
    }

    var btBondedDevices: List<BTDevice> = emptyList()
        private set

    private val _settingsUiModel = MutableStateFlow(SettingsUiModel())
    val settingsUiModel = _settingsUiModel.asStateFlow()

    fun setAutomaticTrip(value: Boolean) {
        _settingsUiModel.update {
            it.copy(automaticTrip = value)
        }
    }

    fun setTripCalendarEvent(value: Boolean) {
        _settingsUiModel.update {
            it.copy(calendarEvent = value)
        }
    }

    fun setShouldMergeTrips(value: Boolean) {
        _settingsUiModel.update {
            it.copy(shouldMergeTrips = value)
        }
    }

    fun setBluetoothDeviceName(value: String) {
        _settingsUiModel.update {
            it.copy(btDeviceName = value)
        }
    }

    fun setMergeTripSeconds(value: String) {
        _settingsUiModel.update {
            it.copy(mergeTripSeconds = value.toIntOrNull())
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

    fun saveChanges() {
        viewModelScope.launch {
            try {
                if (isFormValid()) {
                    val settings = with(_settingsUiModel.value) {
                        val currentSettings = settingsRepository.settings.first()

                        currentSettings.copy(
                            automaticTrip = automaticTrip,
                            btDeviceName = btDeviceName,
                            calendarEvent = calendarEvent,
                            shouldMergeTrips = shouldMergeTrips,
                            mergeTripSeconds = if (shouldMergeTrips) {
                                requireNotNull(mergeTripSeconds)
                            } else {
                                currentSettings.mergeTripSeconds
                            }
                        )
                    }
                    settingsRepository.updateSettings(settings)
                    showSnackBar("Settings saved")
                } else {
                    showSnackBar("Something went wrong")
                }
            } catch (ex: Exception) {
                showSnackBar("Error while saving settings")
            }
        }
    }

    private fun isFormValid(): Boolean {
        return tripSecondsConditionValid()
    }

    init {
        viewModelScope.launch {
            val settings = settingsRepository.settings.first()
            _settingsUiModel.value = SettingsUiModel(
                automaticTrip = settings.automaticTrip,
                btDeviceName = settings.btDeviceName,
                calendarEvent = settings.calendarEvent,
                shouldMergeTrips = settings.shouldMergeTrips,
                mergeTripSeconds = settings.mergeTripSeconds
            )
            _screenStatus.value = ScreenStatus.Success(Unit)
        }
    }
}
