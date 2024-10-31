package com.riju.localdatasourceimpl

import com.riju.localdatasourceimpl.model.RoutePointEntity
import com.riju.localdatasourceimpl.model.TripEntity
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class LocalTrackingDataSourceImpl @Inject constructor(
    private val tripDao: TripDao
) : LocalTrackingDataSource {
    override fun addTrip(trip: TripEntity) {
        tripDao.insertTrip(trip)
    }

    override fun addRoutePoint(routePoint: RoutePointEntity) {
        tripDao.insertRoutePoint(routePoint)
    }

    override suspend fun getTripDetails(tripId: String): TripEntity? {
        return tripDao.getTripDetails(tripId)
    }

    override fun getTripPointsFlow(tripId: String): Flow<List<RoutePointEntity>?> {
        return tripDao.getRoutePointsFlow(tripId)
    }

    override suspend fun modifyEndTime(tripId: String, endTime: String) {
        tripDao.updateTripEndTime(tripId, endTime)
    }

    override suspend fun getAllTripHistory(): List<TripEntity> {
        return tripDao.getAllTripHistory()
    }

    override suspend fun getTripPoints(tripId: String): List<RoutePointEntity> {
        return tripDao.getTripRoute(tripId).routePoints
    }
}
