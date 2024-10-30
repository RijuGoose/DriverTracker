package com.riju.repository

interface PermissionRepository {
    fun isBackgroundLocationPermissionGranted(): Boolean
    fun isBluetoothPermissionGranted(): Boolean
    fun isLocationPermissionGranted(): Boolean
}
