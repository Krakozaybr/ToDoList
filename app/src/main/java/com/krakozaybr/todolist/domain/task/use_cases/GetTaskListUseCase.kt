package com.krakozaybr.todolist.domain.task.use_cases

import androidx.lifecycle.LiveData
import com.krakozaybr.todolist.domain.task.Task
import com.krakozaybr.todolist.domain.task.TaskRepository
import java.util.Date

class GetTaskListUseCase(
    private val repository: TaskRepository
) {

    operator fun invoke(start: Date, end: Date): LiveData<List<Task>> {
        return repository.getTasksOfPeriod(start, end)
    }

}