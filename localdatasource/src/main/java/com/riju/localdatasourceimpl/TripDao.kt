package com.riju.localdatasourceimpl

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.riju.localdatasourceimpl.model.RoutePointEntity
import com.riju.localdatasourceimpl.model.TripEntity
import com.riju.localdatasourceimpl.model.TripRoutePoints
import kotlinx.coroutines.flow.Flow

@Dao
interface TripDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertTrip(trip: TripEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertRoutePoint(routePoint: RoutePointEntity)

    @Query("SELECT * FROM route_points WHERE tripId = :tripId")
    fun getRoutePointsFlow(tripId: String): Flow<List<RoutePointEntity>>

    @Transaction
    @Query("SELECT * FROM trips WHERE tripId = :tripId")
    suspend fun getTripRoute(tripId: String): TripRoutePoints

    @Query("SELECT * FROM trips")
    suspend fun getAllTripHistory(): List<TripEntity>

    @Query("SELECT * FROM trips WHERE tripId = :tripId")
    suspend fun getTripDetails(tripId: String): TripEntity?

    @Update
    fun updateTrip(trip: TripEntity)

    @Query("UPDATE trips SET endTime = :endTime WHERE tripId = :tripId")
    suspend fun updateTripEndTime(tripId: String, endTime: String)
}
