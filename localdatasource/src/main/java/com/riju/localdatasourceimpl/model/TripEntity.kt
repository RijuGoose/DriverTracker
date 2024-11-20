package com.riju.localdatasourceimpl.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.ZonedDateTime

@Entity(tableName = "trips")
data class TripEntity(
    @PrimaryKey val tripId: String,
    val startTime: ZonedDateTime,
    val startLocation: String? = null,
    val endTime: ZonedDateTime? = null,
    val endLocation: String? = null,
)
