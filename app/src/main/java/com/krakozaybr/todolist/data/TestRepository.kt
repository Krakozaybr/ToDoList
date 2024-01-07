package com.krakozaybr.todolist.data

import com.krakozaybr.todolist.domain.task.Task
import com.krakozaybr.todolist.domain.task.TaskRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.flow
import java.time.LocalDate
import java.time.LocalDateTime
import javax.inject.Inject
import kotlin.random.Random

class TestTaskRepository @Inject constructor() : TaskRepository {

    private val tasksFlow = MutableSharedFlow<List<Task>>(replay = 1)

    private var _id = 1

    private val tasks = MutableList(INITIAL_SIZE) {

        val dateStart = LocalDateTime.now().minusDays(1).plusHours(it.toLong() / 3 - 1L)
        val dateEnd = dateStart.minusDays(1).plusHours(it.toLong() / 3)

        Task(
            dateStart = dateStart,
            dateFinish = dateEnd,
            name = "Task $it",
            description = "Description of task $it",
            id = _id++,
            done = Random.nextBoolean()
        )
    }

    init {
        tasksFlow.tryEmit(tasks)
    }

    override suspend fun addTask(task: Task) {
        tasks.removeIf { it.id == task.id }
        if (task.id == Task.UNDEFINED_ID){
            tasks.add(task.copy(id = _id++))
        } else {
            tasks.add(task)
        }
        tasksFlow.emit(tasks)
    }

    override fun getTasksForDate(date: LocalDate): Flow<List<Task>> {
        return flow {
            emit(tasks.forDate(date))

            tasksFlow.collect {
                emit(it.forDate(date))
            }
        }
    }

    private fun List<Task>.forDate(date: LocalDate): List<Task> {
        return filter { task -> task.dateStart.toLocalDate() == date }.sortedBy { it.dateStart }
    }

    override suspend fun deleteTask(task: Task) {
        tasks.removeIf { it.id == task.id }
        tasksFlow.emit(tasks)
    }

    override suspend fun getTask(id: Int): Task {
        return tasks.first { it.id == id }
    }

    companion object {

        const val INITIAL_SIZE = 100

    }
}