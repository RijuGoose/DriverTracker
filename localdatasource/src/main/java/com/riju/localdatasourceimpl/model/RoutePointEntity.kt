package com.riju.localdatasourceimpl.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "route_points")
data class RoutePointEntity(
    @PrimaryKey(autoGenerate = true) val routePointId: Int? = null,
    val tripId: String,
    val pointCount: Int,
    val latitude: Double,
    val longitude: Double,
    val speed: Double
)
