package com.krakozaybr.todolist.domain.task.use_cases

import com.krakozaybr.todolist.domain.task.Task
import com.krakozaybr.todolist.domain.task.TaskRepository
import javax.inject.Inject

class GetTaskUseCase @Inject constructor(
    private val repository: TaskRepository
) {

    suspend operator fun invoke(id: Int): Task {
        return repository.getTask(id)
    }

}