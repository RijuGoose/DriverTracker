package com.riju.localdatasourceimpl

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.riju.localdatasourceimpl.model.DebugLogEntity

@Dao
interface DebugLogDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertLog(log: DebugLogEntity)

    @Query("SELECT * FROM debug_logs")
    suspend fun getAllLogs(): List<DebugLogEntity>
}
