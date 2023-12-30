package com.krakozaybr.todolist.domain.task

import androidx.lifecycle.LiveData
import java.util.Date

interface TaskRepository {

    suspend fun addTask(task: Task)

    fun getTasks(): LiveData<List<Task>>

    suspend fun deleteTask(task: Task)

    suspend fun getTask(id: Int): Task
}