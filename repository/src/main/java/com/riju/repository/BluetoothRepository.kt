package com.riju.repository

import com.riju.repository.model.BTDevice
import com.riju.repository.model.UserPermissionState

interface BluetoothRepository {
    fun getPairedDevices(): UserPermissionState<List<BTDevice>>
}
