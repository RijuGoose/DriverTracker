package com.riju.localdatasourceimpl

import com.riju.localdatasourceimpl.model.DebugLogEntity

interface LocalDebugLogDataSource {
    fun addLog(message: String)
    suspend fun getAllLogs(): List<DebugLogEntity>
}
