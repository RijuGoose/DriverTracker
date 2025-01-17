package com.riju.repository

import android.location.Location
import com.riju.repository.model.UserPermissionState
import kotlinx.coroutines.flow.Flow

interface LocationRepository {
    fun getLocationUpdates(interval: Long): Flow<UserPermissionState<Location>>
    suspend fun getCurrentLocation(): UserPermissionState<Location>
    suspend fun getLocationAddress(lat: Double, lon: Double): String?
}
