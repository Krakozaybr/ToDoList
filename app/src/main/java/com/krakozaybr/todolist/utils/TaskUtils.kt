package com.krakozaybr.todolist.utils

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.krakozaybr.todolist.R
import com.krakozaybr.todolist.domain.task.Task
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.SortedMap

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

@Composable
fun Task.timePeriodString(): String {
    val dateTimeFormat = stringResource(id = R.string.default_time_format)
    val timePeriodFormat = stringResource(id = R.string.time_period_format)

    val formatter = DateTimeFormatter.ofPattern(dateTimeFormat)
    val start = dateStart.format(formatter)
    val finish = dateFinish.format(formatter)
    return String.format(
        timePeriodFormat,
        start,
        finish
    )
}

fun emptyTask(
    dateStart: LocalDateTime = LocalDateTime.now(),
    dateFinish: LocalDateTime = dateStart.plusHours(1),
    name: String = "",
    description: String = "",
    done: Boolean = false,
    id: Int = Task.UNDEFINED_ID
) = Task(
    dateStart = dateStart,
    dateFinish = dateFinish,
    name = name,
    description = description,
    done = done,
    id = id
)