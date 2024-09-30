package com.riju.drivertracker.extensions

import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

fun LocalDateTime.toTimeString(): String {
    return this.format(DateTimeFormatter.ofPattern("HH:mm:ss")).toString()
}

fun LocalDateTime.toLocalDateString(): String {
    return this.toLocalDate().toString()
}
