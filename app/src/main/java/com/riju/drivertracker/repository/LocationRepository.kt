package com.riju.drivertracker.repository

import android.location.Location
import com.riju.drivertracker.repository.model.UserPermissionState
import kotlinx.coroutines.flow.Flow

interface LocationRepository {
    fun getLocationUpdates(interval: Long): Flow<UserPermissionState<Location>>
    suspend fun getCurrentLocation(): UserPermissionState<Location>
}
