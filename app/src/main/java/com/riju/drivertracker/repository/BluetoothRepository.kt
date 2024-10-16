package com.riju.drivertracker.repository

import com.riju.drivertracker.repository.model.BTDevice

interface BluetoothRepository {
    fun getBondedDevices(): List<BTDevice>
}
