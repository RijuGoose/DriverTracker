package com.riju.localdatasourceimpl.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.ZonedDateTime

@Entity(tableName = "debug_logs")
data class DebugLogEntity(
    @PrimaryKey(autoGenerate = true) val logId: Int? = null,
    val timestamp: ZonedDateTime,
    val message: String
)
