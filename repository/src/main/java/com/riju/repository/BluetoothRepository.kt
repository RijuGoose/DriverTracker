package com.riju.repository

import com.riju.repository.model.BTDevice

interface BluetoothRepository {
    fun getBondedDevices(): List<BTDevice>
}
