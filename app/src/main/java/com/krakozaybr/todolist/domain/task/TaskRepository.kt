package com.krakozaybr.todolist.domain.task

import androidx.lifecycle.LiveData
import java.util.Date

interface TaskRepository {

    fun addTask(task: Task)

    fun getTasksOfPeriod(
        start: Date,
        end: Date
    ): LiveData<List<Task>>

    fun deleteTask(task: Task)

}