package com.krakozaybr.todolist.data

import com.krakozaybr.todolist.data.db.dao.TaskDao
import com.krakozaybr.todolist.data.db.mappers.TaskMapper
import com.krakozaybr.todolist.domain.task.Task
import com.krakozaybr.todolist.domain.task.TaskRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.time.LocalDate
import javax.inject.Inject

class DatabaseTaskRepository @Inject constructor(
    private val taskDao: TaskDao,
    private val mapper: TaskMapper
) : TaskRepository {

    override suspend fun addTask(task: Task) {
        taskDao.insert(mapper.mapTaskToDbModel(task))
    }

    override suspend fun getTask(id: Int): Task {
        return mapper.mapDbModelToTask(taskDao.getTask(id))
    }

    override fun getTasksForDate(date: LocalDate): Flow<List<Task>> = taskDao.getTasksInPeriod(
        periodStart = date.atStartOfDay(),
        periodEnd = date.plusDays(1L).atStartOfDay()
    ).map {
        mapper.mapDbModelListToTaskList(it)
    }

    override suspend fun deleteTask(task: Task) {
        taskDao.delete(task.id)
    }

}