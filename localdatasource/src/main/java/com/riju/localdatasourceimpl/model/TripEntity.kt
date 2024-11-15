package com.riju.localdatasourceimpl.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "trips")
data class TripEntity(
    @PrimaryKey val tripId: String,
    val startTime: String,
    val startLocation: String? = null,
    val endTime: String? = null,
    val endLocation: String? = null,
)
