package com.riju.localdatasourceimpl

import com.riju.localdatasourceimpl.model.DebugLogEntity
import java.time.ZonedDateTime
import javax.inject.Inject

class LocalDebugLogDataSourceImpl @Inject constructor(
    private val logDao: DebugLogDao
) : LocalDebugLogDataSource {
    override fun addLog(message: String) {
        logDao.insertLog(
            DebugLogEntity(
                timestamp = ZonedDateTime.now(),
                message = message
            )
        )
    }

    override suspend fun getAllLogs(): List<DebugLogEntity> {
        return logDao.getAllLogs()
    }
}
