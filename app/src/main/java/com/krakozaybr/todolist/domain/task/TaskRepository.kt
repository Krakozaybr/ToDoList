package com.krakozaybr.todolist.domain.task

import kotlinx.coroutines.flow.Flow
import java.time.LocalDate

interface TaskRepository {

    suspend fun addTask(task: Task)

    fun getTasksForDate(date: LocalDate): Flow<List<Task>>

    suspend fun deleteTask(task: Task)

    suspend fun getTask(id: Int): Task
}