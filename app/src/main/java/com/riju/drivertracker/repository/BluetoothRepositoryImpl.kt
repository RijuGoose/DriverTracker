package com.riju.drivertracker.repository

import android.Manifest
import android.bluetooth.BluetoothAdapter
import android.content.Context
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import com.riju.drivertracker.repository.model.BTDevice

class BluetoothRepositoryImpl(
    private val bluetoothAdapter: BluetoothAdapter?,
    private val context: Context
) : BluetoothRepository {
    override fun getBondedDevices(): List<BTDevice> {
        if (ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.BLUETOOTH_CONNECT
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return emptyList()
            // TODO if permission is not granted, emit error
        }

        return bluetoothAdapter?.bondedDevices?.map { device ->
            BTDevice(device.name, device.address)
        } ?: emptyList()
    }
}
