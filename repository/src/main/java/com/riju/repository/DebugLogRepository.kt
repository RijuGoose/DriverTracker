package com.riju.repository

interface DebugLogRepository {
    fun addLog(message: String)
    suspend fun getAllLogs(): List<String>
}
