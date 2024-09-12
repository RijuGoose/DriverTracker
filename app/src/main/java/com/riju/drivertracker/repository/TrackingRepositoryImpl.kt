package com.riju.drivertracker.repository

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.riju.drivertracker.service.model.TrackingPoint
import java.time.LocalDateTime
import java.util.UUID

class TrackingRepositoryImpl(
    private val database: DatabaseReference,
    private val firebaseAuth: FirebaseAuth
) : TrackingRepository {
    private var currentTripId: String? = null
    private var currentTripCounter: Int = 0

    override fun startTracking() {
        currentTripId = UUID.randomUUID().toString()
        currentTripCounter = 0
        firebaseAuth.currentUser?.let { currentUser ->
            database
                .child("users")
                .child(currentUser.uid)
                .child("trips")
                .child(currentTripId.toString())
                .child("startTime")
                .setValue(LocalDateTime.now().toString())

        }
    }

    override fun addTrackingPoint(trackingPoint: TrackingPoint) {
        firebaseAuth.currentUser?.let { currentUser ->
            database
                .child("users")
                .child(currentUser.uid)
                .child("tripLocations")
                .child(currentTripId.toString())
                .child(currentTripCounter.toString())
                .setValue(trackingPoint)
                .also {
                    currentTripCounter++
                }
        }
    }

    override fun stopTracking() {
        currentTripId = null
        currentTripCounter = 0
    }

    override fun isTracking(): Boolean {
        TODO("Not yet implemented")
    }
}