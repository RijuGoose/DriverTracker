package com.riju.localdatasourceimpl

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.riju.localdatasourceimpl.converter.ZonedDateTimeConverter
import com.riju.localdatasourceimpl.model.RoutePointEntity
import com.riju.localdatasourceimpl.model.TripEntity

@Database(
    entities = [
        TripEntity::class,
        RoutePointEntity::class
    ],
    version = 1
)
@TypeConverters(ZonedDateTimeConverter::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun tripDao(): TripDao
}
