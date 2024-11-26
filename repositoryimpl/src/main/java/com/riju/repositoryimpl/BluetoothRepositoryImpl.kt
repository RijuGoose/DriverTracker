package com.riju.repositoryimpl

import android.Manifest
import android.bluetooth.BluetoothAdapter
import android.content.Context
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import com.riju.repository.BluetoothRepository
import com.riju.repository.model.BTDevice
import com.riju.repository.model.UserPermissionState
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class BluetoothRepositoryImpl @Inject constructor(
    private val bluetoothAdapter: BluetoothAdapter?,
    @ApplicationContext private val context: Context
) : BluetoothRepository {

    override fun getPairedDevices(): UserPermissionState<List<BTDevice>> {
        if (ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.BLUETOOTH_CONNECT
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            return UserPermissionState.Granted(
                bluetoothAdapter?.bondedDevices?.map { device ->
                    BTDevice(device.name, device.address)
                } ?: emptyList()
            )
        }
        return UserPermissionState.Denied
    }
}
