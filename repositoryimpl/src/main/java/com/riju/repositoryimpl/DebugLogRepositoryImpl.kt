package com.riju.repositoryimpl

import com.riju.localdatasourceimpl.LocalDebugLogDataSource
import com.riju.repository.DebugLogRepository
import javax.inject.Inject

class DebugLogRepositoryImpl @Inject constructor(
    private val localDebugLogDataSource: LocalDebugLogDataSource,
) : DebugLogRepository {
    override fun addLog(message: String) {
        localDebugLogDataSource.addLog(message)
    }

    override suspend fun getAllLogs(): List<String> {
        return localDebugLogDataSource.getAllLogs().map {
            "${it.timestamp} & ${it.message}"
        }
    }
}
