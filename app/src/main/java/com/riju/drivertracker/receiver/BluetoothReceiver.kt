package com.riju.drivertracker.receiver

import android.bluetooth.BluetoothDevice
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.riju.drivertracker.extensions.goAsync
import com.riju.drivertracker.service.LocationService
import com.riju.repository.PermissionRepository
import com.riju.repository.SettingsRepository
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.first
import javax.inject.Inject

@AndroidEntryPoint
class BluetoothReceiver : BroadcastReceiver() {
    @Inject
    lateinit var settingsRepository: SettingsRepository

    @Inject
    lateinit var permissionRepository: PermissionRepository

    override fun onReceive(context: Context?, intent: Intent) = goAsync {
        if (!settingsRepository.settings.first().automaticTrip) {
            return@goAsync
        }

        if (!permissionRepository.isBackgroundLocationPermissionGranted() ||
            !permissionRepository.isBluetoothPermissionGranted()
        ) {
            return@goAsync
        }

        val action: String? = intent.action
        val bluetoothDevice: BluetoothDevice? = intent.getParcelableExtra(
            BluetoothDevice.EXTRA_DEVICE,
            BluetoothDevice::class.java
        )
        val deviceName = bluetoothDevice?.name
        val deviceMacAddress = bluetoothDevice?.address ?: ""

        when (action) {
            BluetoothDevice.ACTION_ACL_CONNECTED -> {
                if (settingsRepository.settings.first().btDeviceName == deviceName) {
                    settingsRepository.setBluetoothDeviceMacAddress(deviceMacAddress)
                    Intent(context, LocationService::class.java).apply {
                        this.action = LocationService.ACTION_TRIP_START
                        context?.startForegroundService(this)
                    }
                } // TODO handle short-time disconnects
            }

            BluetoothDevice.ACTION_ACL_DISCONNECTED -> {
                // check by mac address because device name can be null if user turns off bluetooth
                if (settingsRepository.settings.first().btDeviceMacAddress == deviceMacAddress) {
                    Intent(context, LocationService::class.java).apply {
                        this.action = LocationService.ACTION_TRIP_STOP
                        context?.startForegroundService(this)
                    }
                }
            }
        }
    }
}
