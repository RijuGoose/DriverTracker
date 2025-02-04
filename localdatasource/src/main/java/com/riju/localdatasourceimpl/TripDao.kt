package com.riju.localdatasourceimpl

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.RawQuery
import androidx.room.Transaction
import androidx.room.Update
import androidx.sqlite.db.SimpleSQLiteQuery
import androidx.sqlite.db.SupportSQLiteQuery
import com.riju.localdatasourceimpl.model.RoutePointEntity
import com.riju.localdatasourceimpl.model.TripEntity
import com.riju.localdatasourceimpl.model.TripRoutePoints
import kotlinx.coroutines.flow.Flow

@Dao
@Suppress("TooManyFunctions")
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

    @RawQuery(observedEntities = [TripEntity::class])
    fun getAllTripHistoryFlowRaw(query: SupportSQLiteQuery): Flow<List<TripEntity>>

    fun getAllTripHistoryFlow(orderBy: String, isAscending: Boolean): Flow<List<TripEntity>> {
        val sortOrder = if (isAscending) "ASC" else "DESC"
        val query = SimpleSQLiteQuery("SELECT * FROM trips ORDER BY $orderBy $sortOrder")
        return getAllTripHistoryFlowRaw(query)
    }

    @Query("SELECT * FROM trips WHERE tripId = :tripId")
    suspend fun getTripDetails(tripId: String): TripEntity?

    @Query("SELECT * FROM trips ORDER BY endTime DESC LIMIT 1")
    suspend fun getLastTripDetails(): TripEntity?

    @Update
    fun updateTrip(trip: TripEntity)

    @Query("DELETE FROM trips WHERE tripId = :tripId")
    suspend fun deleteTrip(tripId: String)

    @Query("UPDATE trips SET endTime = :endTime WHERE tripId = :tripId")
    suspend fun updateTripEndTime(tripId: String, endTime: String)

    @Query("UPDATE trips SET startLocation = :startLocation WHERE tripId = :tripId")
    suspend fun updateTripStartLocation(tripId: String, startLocation: String)

    @Query("UPDATE trips SET endLocation = :endLocation WHERE tripId = :tripId")
    suspend fun updateTripEndLocation(tripId: String, endLocation: String)
}
