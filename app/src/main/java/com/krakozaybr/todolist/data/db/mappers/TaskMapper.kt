package com.krakozaybr.todolist.data.db.mappers

import com.krakozaybr.todolist.data.db.models.TaskDbModel
import com.krakozaybr.todolist.domain.task.Task
import javax.inject.Inject

class TaskMapper @Inject constructor() {

    fun mapTaskToDbModel(task: Task) = TaskDbModel(
        id = task.id,
        dateStart = task.dateStart,
        dateFinish = task.dateFinish,
        name = task.name,
        description = task.description,
        done = task.done
    )

    fun mapDbModelToTask(model: TaskDbModel) = Task(
        id = model.id,
        dateStart = model.dateStart,
        dateFinish = model.dateFinish,
        name = model.name,
        description = model.description,
        done = model.done
    )

    fun mapDbModelListToTaskList(models: List<TaskDbModel>) = models.map {
        mapDbModelToTask(it)
    }

}