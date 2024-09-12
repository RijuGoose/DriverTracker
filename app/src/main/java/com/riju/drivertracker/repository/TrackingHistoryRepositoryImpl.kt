package com.riju.drivertracker.repository

import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.getValue
import com.riju.drivertracker.repository.model.Trip

class TrackingHistoryRepositoryImpl(
    private val database: DatabaseReference
): TrackingHistoryRepository {
    override fun getAllTrackingHistory(): List<Trip>? {
        return database.child("trips").get().result.getValue<List<Trip>>()
    }

    override fun getTrackingHistoryById(id: Int) {
        TODO("Not yet implemented")
    }
}