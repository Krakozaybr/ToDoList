package com.krakozaybr.todolist.domain.task.use_cases

import com.krakozaybr.todolist.domain.task.Task
import com.krakozaybr.todolist.domain.task.TaskRepository

class DeleteTaskUseCase(private val repository: TaskRepository) {

    operator fun invoke(task: Task) {
        repository.deleteTask(task)
    }

}