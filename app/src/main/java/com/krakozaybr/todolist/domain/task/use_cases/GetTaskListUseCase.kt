package com.krakozaybr.todolist.domain.task.use_cases

import androidx.lifecycle.LiveData
import com.krakozaybr.todolist.domain.task.Task
import com.krakozaybr.todolist.domain.task.TaskRepository
import javax.inject.Inject

class GetTaskListUseCase @Inject constructor(
    private val repository: TaskRepository
) {

    operator fun invoke(): LiveData<List<Task>> {
        return repository.getTasks()
    }

}