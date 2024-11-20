package com.riju.drivertracker.extensions

import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter

fun ZonedDateTime.toTimeString(): String {
    return this.format(DateTimeFormatter.ofPattern("HH:mm:ss")).toString()
}

fun ZonedDateTime.toLocalDateString(): String {
    return this.toLocalDate().toString()
}
