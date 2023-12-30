package com.krakozaybr.todolist.data.db.converters

import androidx.room.ProvidedTypeConverter
import androidx.room.TypeConverter
import java.time.LocalDateTime
import java.time.ZoneOffset

@ProvidedTypeConverter
class LocalDateTimeConverter {
    @TypeConverter
    fun localDateTimeToLong(dateTime: LocalDateTime) = dateTime.toEpochSecond(ZoneOffset.MIN)

    @TypeConverter
    fun longToLocalDateTime(value: Long) = LocalDateTime.ofEpochSecond(value, 0, OFFSET)

    companion object {
        private val OFFSET = ZoneOffset.MIN
    }
}