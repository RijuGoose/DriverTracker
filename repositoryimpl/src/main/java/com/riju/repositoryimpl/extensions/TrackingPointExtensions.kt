package com.riju.repositoryimpl.extensions

import android.location.Location
import com.riju.repository.model.TrackingPoint

fun TrackingPoint.distanceBetweenInMeters(trackingPoint: TrackingPoint): Double {
    return Location("point1").apply {
        latitude = lat
        longitude = lon
    }.distanceTo(
        Location("point2").apply {
            latitude = trackingPoint.lat
            longitude = trackingPoint.lon
        }
    ).toDouble()
}
