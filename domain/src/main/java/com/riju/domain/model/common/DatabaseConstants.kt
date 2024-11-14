package com.riju.domain.model.common

sealed class DatabaseConstants {
    enum class Field(val fieldName: String) {
        StartTime("startTime"),
        EndTime("endTime"),
        StartLocation("startLocation"),
        EndLocation("endLocation")
    }

    enum class List(val listName: String) {
        Users("users"),
        TripLocations("tripLocations"),
        Trips("trips")
    }
}
