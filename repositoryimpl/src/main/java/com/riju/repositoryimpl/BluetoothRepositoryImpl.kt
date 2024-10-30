package com.riju.repositoryimpl

import android.Manifest
import android.bluetooth.BluetoothAdapter
import android.content.Context
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import com.riju.repository.model.BTDevice
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class BluetoothRepositoryImpl @Inject constructor(
    private val bluetoothAdapter: BluetoothAdapter?,
    @ApplicationContext private val context: Context
) : com.riju.repository.BluetoothRepository {
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
