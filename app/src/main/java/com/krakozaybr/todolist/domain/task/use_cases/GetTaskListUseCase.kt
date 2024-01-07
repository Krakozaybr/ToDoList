package com.krakozaybr.todolist.domain.task.use_cases

import com.krakozaybr.todolist.domain.task.Task
import com.krakozaybr.todolist.domain.task.TaskRepository
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate
import javax.inject.Inject

class GetTaskListUseCase @Inject constructor(
    private val repository: TaskRepository
) {

    operator fun invoke(date: LocalDate): Flow<List<Task>> {
        return repository.getTasksForDate(date)
    }

}