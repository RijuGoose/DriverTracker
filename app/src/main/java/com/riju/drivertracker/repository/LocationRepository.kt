package com.riju.drivertracker.repository

import android.location.Location
import kotlinx.coroutines.flow.Flow

interface LocationRepository {
    fun getLocationUpdates(interval: Long): Flow<Location>
}
