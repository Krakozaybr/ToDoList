package com.krakozaybr.todolist.domain.task.use_cases

import com.krakozaybr.todolist.domain.task.Task
import com.krakozaybr.todolist.domain.task.TaskRepository

class AddEditTaskUseCase(private val repository: TaskRepository) {

    operator fun invoke(task: Task) {
        repository.addTask(task)
    }

}