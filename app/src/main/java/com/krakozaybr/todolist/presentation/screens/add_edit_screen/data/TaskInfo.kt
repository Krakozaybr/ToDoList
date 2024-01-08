package com.krakozaybr.todolist.presentation.screens.add_edit_screen.data

import com.krakozaybr.todolist.domain.task.Task
import java.time.LocalDate
import java.time.LocalTime

data class TaskInfo(
    val date: LocalDate?,
    val timeStart: LocalTime?,
    val timeFinish: LocalTime?,
    val name: String,
    val description: String,
    val done: Boolean,
    var id: Int = Task.UNDEFINED_ID,
) {
    companion object {
        val EMPTY_TASK_INFO = TaskInfo(
            date = null,
            timeStart = null,
            timeFinish = null,
            name = "",
            description = "",
            done = false
        )
    }
}