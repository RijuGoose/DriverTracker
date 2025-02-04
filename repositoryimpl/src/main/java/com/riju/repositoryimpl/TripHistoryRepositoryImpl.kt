package com.riju.repositoryimpl

import com.riju.domain.model.common.DatabaseConstants
import com.riju.localdatasourceimpl.LocalTrackingDataSource
import com.riju.repository.TripHistoryRepository
import com.riju.repository.model.TrackingPoint
import com.riju.repository.model.TripDetails
import com.riju.repositoryimpl.extensions.distanceBetweenInMeters
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDate
import javax.inject.Inject

@OptIn(ExperimentalCoroutinesApi::class)
class TripHistoryRepositoryImpl @Inject constructor(
    private val localTrackingDataSource: LocalTrackingDataSource,
) : TripHistoryRepository {
    private val _tripHistoryList = MutableStateFlow<Map<LocalDate, List<TripDetails>>>(emptyMap())
    override val tripHistoryList = _tripHistoryList.asStateFlow()

    private val collectScope = CoroutineScope(SupervisorJob() + Dispatchers.IO)
    private val orderBy = DatabaseConstants.Field.StartTime

    private val _isListAscending = MutableStateFlow(false)
    override val isListAscending = _isListAscending.asStateFlow()

    init {
        collectScope.launch {
            _isListAscending.flatMapLatest { isAscending ->
                localTrackingDataSource.getAllTripHistoryFlow(orderBy.fieldName, isAscending)
            }.collect { tripEntities ->
                _tripHistoryList.update {
                    tripEntities.map { tripEntity ->
                        TripDetails(
                            tripId = tripEntity.tripId,
                            startTime = tripEntity.startTime,
                            endTime = tripEntity.endTime,
                            startLocation = tripEntity.startLocation,
                            endLocation = tripEntity.endLocation,
                        )
                    }.groupBy { it.startTime.toLocalDate() }
                }
            }
        }
    }

    override fun toggleSortOrder() {
        _isListAscending.value = !_isListAscending.value
    }

    override suspend fun getTripHistoryRouteById(tripId: String): List<TrackingPoint> {
        return localTrackingDataSource.getTripPoints(tripId).map { routePointEntity ->
            TrackingPoint(
                lat = routePointEntity.latitude,
                lon = routePointEntity.longitude,
                speed = routePointEntity.speed
            )
        }
    }

    override suspend fun getTripDetails(tripId: String): TripDetails? {
        return localTrackingDataSource.getTripDetails(tripId)?.let { tripEntity ->
            TripDetails(
                tripId = tripId,
                startTime = tripEntity.startTime,
                endTime = tripEntity.endTime,
                startLocation = tripEntity.startLocation,
                endLocation = tripEntity.endLocation,
            )
        }
    }

    override suspend fun getLastTripDetails(): TripDetails? {
        return localTrackingDataSource.getLastTripDetails()?.let { tripEntity ->
            TripDetails(
                tripId = tripEntity.tripId,
                startTime = tripEntity.startTime,
                endTime = tripEntity.endTime,
                startLocation = tripEntity.startLocation,
                endLocation = tripEntity.endLocation,
            )
        }
    }

    @Suppress("MagicNumber")
    override suspend fun getDistanceTravelled(tripId: String): Double {
        return getTripHistoryRouteById(tripId).zipWithNext { point1, point2 ->
            point1.distanceBetweenInMeters(point2)
        }.sum() / 1000
    }

    override suspend fun modifyStartLocation(tripId: String, startLocation: String) {
        localTrackingDataSource.modifyStartLocation(tripId, startLocation)
    }

    override suspend fun modifyEndLocation(tripId: String, endLocation: String) {
        localTrackingDataSource.modifyEndLocation(tripId, endLocation)
    }
}
