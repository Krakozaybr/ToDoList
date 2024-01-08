package com.krakozaybr.todolist.presentation.wrappers.task_info

import java.time.LocalDate
import java.time.LocalTime
import javax.inject.Inject

class TaskInfoValidator @Inject constructor() {

    fun validate(taskInfo: TaskInfo) = TaskIssues(
        timeError = !nameCorrect(taskInfo.name),
        dateError = !isTimeCorrect(
            taskInfo.timeStart,
            taskInfo.timeFinish
        ),
        nameError = !isDateCorrect(taskInfo.date),
    )

    fun isDateCorrect(date: LocalDate?) = date != null

    fun nameCorrect(name: String) = name.isNotBlank()

    fun isTimeCorrect(start: LocalTime?, end: LocalTime?) =
        start != null && end != null && start.isAfter(end)

}

class TaskIssues(
    val timeError: Boolean,
    val dateError: Boolean,
    val nameError: Boolean
) {
    fun ok() = !(timeError || dateError || nameError)
}
