package com.krakozaybr.todolist.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.krakozaybr.todolist.domain.task.Task
import com.krakozaybr.todolist.domain.task.TaskRepository
import java.time.LocalDateTime
import java.util.Date
import javax.inject.Inject
import kotlin.random.Random

class TestTaskRepository @Inject constructor() : TaskRepository {

    private val tasksLiveData = MutableLiveData<List<Task>>()

    private var id = 1

    private val tasks = MutableList(INITIAL_SIZE) {

        val dateStart = LocalDateTime.from(Date().toInstant()).plusHours(it.toLong())
        val dateEnd = dateStart.plusHours(Random.nextInt(1, 3).toLong())

        Task(
            dateStart = dateStart,
            dateFinish = dateEnd,
            name = "Task $it",
            description = "Description of task $it",
            id = id++,
        )
    }

    init {
        tasksLiveData.value = tasks
    }

    override suspend fun addTask(task: Task) {
        tasks.add(task.copy(id = id++))
        tasksLiveData.value = tasks
    }

    override fun getTasks(): LiveData<List<Task>> {
        return tasksLiveData
    }

    override suspend fun deleteTask(task: Task) {
        tasks.removeIf { it.id == task.id }
        tasksLiveData.value = tasks
    }

    override suspend fun getTask(id: Int): Task {
        return tasks.first { it.id == id }
    }

    companion object {

        const val INITIAL_SIZE = 100

    }
}