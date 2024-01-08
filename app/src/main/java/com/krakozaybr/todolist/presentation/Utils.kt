package com.krakozaybr.todolist.presentation

import android.net.Uri
import com.kizitonwose.calendar.core.CalendarMonth
import com.kizitonwose.calendar.core.atStartOfMonth
import com.krakozaybr.todolist.domain.task.Task
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Locale
import java.util.SortedMap


const val MONTH_STRING_PATTERN = "LLLL yyyy"
fun CalendarMonth.toMonthString(): String {
    val dtf = DateTimeFormatter.ofPattern(MONTH_STRING_PATTERN)
    return dtf.format(this.yearMonth.atStartOfMonth())
        .replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.ROOT) else it.toString() }
}

typealias Hour = Int

fun List<Task>.groupByHour(): SortedMap<Hour, List<Task>> {
    val map: SortedMap<Hour, MutableList<Task>> = sortedMapOf()
    forEach {
        val hour: Hour = it.dateStart.hour
        val list = map.getOrDefault(hour, mutableListOf())
        list.add(it)
        map[hour] = list
    }
    return sortedMapOf<Hour, List<Task>>().apply {
        map.forEach { (hour, queue) ->
            put(hour, queue.sortedWith { t1, t2 ->
                val timeCheck = t1.dateStart.compareTo(t2.dateStart)
                if (timeCheck == 0) {
                    t1.id.compareTo(t2.id)
                } else {
                    timeCheck
                }
            })
        }
    }
}

fun Hour.toHourFormatString(): String {
    return String.format("%02d:00", this)
}

fun Task.timePeriodString(): String {
    val formatter = DateTimeFormatter.ofPattern("HH:mm")
    val start = dateStart.format(formatter)
    val finish = dateFinish.format(formatter)
    return String.format(
        "%s — %s",
        start,
        finish
    )
}

fun LocalDate.toUtcEpochMillis(): Long {
    val zoneId = ZoneId.of("UTC")
    return atStartOfDay(zoneId).toEpochSecond() * 1000L
}

fun LocalDate.toEpochMillis(): Long {
    val zoneId = ZoneId.systemDefault()
    return atStartOfDay(zoneId).toEpochSecond() * 1000L
}

fun Long.toLocalDate(): LocalDate {
    return Instant
        .ofEpochMilli(this)
        .atZone(ZoneId.systemDefault())
        .toLocalDate()
}

fun String.encode(): String {
    return Uri.encode(this)
}
