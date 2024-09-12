package com.riju.drivertracker.repository

import com.riju.drivertracker.repository.model.Trip

interface TrackingHistoryRepository {
    fun getAllTrackingHistory(): List<Trip>?
    fun getTrackingHistoryById(id: Int)
}