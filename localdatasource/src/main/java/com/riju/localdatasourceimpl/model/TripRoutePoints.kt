package com.riju.localdatasourceimpl.model

import androidx.room.Embedded
import androidx.room.Relation

data class TripRoutePoints(
    @Embedded val trip: TripEntity,
    @Relation(
        parentColumn = "tripId",
        entityColumn = "tripId"
    )
    val routePoints: List<RoutePointEntity> = emptyList()
)
