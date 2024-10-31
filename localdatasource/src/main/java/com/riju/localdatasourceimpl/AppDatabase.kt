package com.riju.localdatasourceimpl

import androidx.room.Database
import androidx.room.RoomDatabase
import com.riju.localdatasourceimpl.model.RoutePointEntity
import com.riju.localdatasourceimpl.model.TripEntity

@Database(
    entities = [
        TripEntity::class,
        RoutePointEntity::class
    ],
    version = 1
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun tripDao(): TripDao
}
