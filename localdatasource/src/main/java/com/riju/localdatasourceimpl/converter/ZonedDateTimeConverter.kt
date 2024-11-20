package com.riju.localdatasourceimpl.converter

import androidx.room.TypeConverter
import java.time.ZonedDateTime

class ZonedDateTimeConverter {

    @TypeConverter
    fun toDate(dateString: String?): ZonedDateTime? {
        return if (dateString == null) {
            null
        } else {
            ZonedDateTime.parse(dateString)
        }
    }

    @TypeConverter
    fun toDateString(date: ZonedDateTime?): String? {
        return date?.toString()
    }
}
