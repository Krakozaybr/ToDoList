package com.krakozaybr.todolist.utils

import android.net.Uri
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.kizitonwose.calendar.core.CalendarMonth
import com.kizitonwose.calendar.core.atStartOfMonth
import com.krakozaybr.todolist.R
import java.time.Instant
import java.time.LocalDate
import java.time.LocalTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Locale

private const val UTC = "UTC"

@Composable
fun CalendarMonth.toMonthString(): String {
    val format = stringResource(id = R.string.default_month_format)
    val dtf = DateTimeFormatter.ofPattern(format)
    return dtf.format(this.yearMonth.atStartOfMonth())
        .replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.ROOT) else it.toString() }
}

typealias Hour = Int

@Composable
fun Hour.toHourFormatString(): String {
    val format = stringResource(id = R.string.default_hour_format)
    return String.format(format, this)
}

fun LocalDate.toUtcEpochMillis(): Long {
    val zoneId = ZoneId.of(UTC)
    return atStartOfDay(zoneId).toEpochSecond() * 1000L
}

fun Long.utcToLocalDate(): LocalDate {
    return Instant.ofEpochMilli(this)
        .atZone(ZoneId.of(UTC))
        .toLocalDate()
}

fun LocalDate.toEpochMillis(): Long {
    val zoneId = ZoneId.systemDefault()
    return atStartOfDay(zoneId).toEpochSecond() * 1000L
}

fun Long.toLocalDate(): LocalDate {
    return Instant.ofEpochMilli(this)
        .atZone(ZoneId.systemDefault())
        .toLocalDate()
}

@Composable
fun LocalTime.defaultFormat(): String {
    val format = stringResource(id = R.string.default_time_format)
    val formatter = DateTimeFormatter.ofPattern(format)
    return formatter.format(this)
}

@Composable
fun LocalDate.defaultFormat(): String {
    val format = stringResource(id = R.string.default_date_format)
    val formatter = DateTimeFormatter.ofPattern(format)
    return formatter.format(this)
}

fun String.encode(): String {
    return Uri.encode(this)
}