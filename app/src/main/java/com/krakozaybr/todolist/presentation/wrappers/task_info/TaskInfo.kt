package com.krakozaybr.todolist.presentation.wrappers.task_info

import com.krakozaybr.todolist.domain.task.Task
import java.time.LocalDate
import java.time.LocalTime

data class TaskInfo(
    val date: LocalDate? = null,
    val timeStart: LocalTime? = null,
    val timeFinish: LocalTime? = null,
    val name: String = "",
    val description: String = "",
    val done: Boolean = false,
    var id: Int = Task.UNDEFINED_ID,
)