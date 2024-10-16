package com.riju.drivertracker.extensions

import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.PermissionState
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.shouldShowRationale

@OptIn(ExperimentalPermissionsApi::class)
fun PermissionState.shouldShowDialog(): Boolean {
    return this.status.shouldShowRationale && this.status.isGranted.not()
}
