package com.krakozaybr.todolist.presentation.wrappers.task_info

import com.krakozaybr.todolist.domain.task.Task
import javax.inject.Inject

class TaskInfoMapper @Inject constructor() {

    fun taskToTaskInfo(task: Task) = TaskInfo(
        date = task.dateStart.toLocalDate(),
        timeStart = task.dateStart.toLocalTime(),
        timeFinish = task.dateFinish.toLocalTime(),
        name = task.name,
        description = task.description,
        done = task.done,
        id = task.id
    )

    fun taskInfoToTask(taskInfo: TaskInfo): Task {
        with(taskInfo) {
            if (date == null || timeStart == null || timeFinish == null) {
                throw IllegalStateException()
            }
            return Task(
                dateStart = date.atTime(timeStart),
                dateFinish = date.atTime(timeFinish),
                name = name,
                description = description,
                done = done,
                id = id,
            )
        }
    }

}