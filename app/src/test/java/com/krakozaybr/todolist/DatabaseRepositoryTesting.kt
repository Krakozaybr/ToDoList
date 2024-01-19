package com.krakozaybr.todolist

import com.krakozaybr.todolist.data.DatabaseTaskRepository
import com.krakozaybr.todolist.data.db.dao.TaskDao
import com.krakozaybr.todolist.data.db.mappers.TaskMapper
import com.krakozaybr.todolist.utils.emptyTask
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Test
import org.mockito.ArgumentMatchers.anyInt
import org.mockito.kotlin.anyOrNull
import org.mockito.kotlin.doReturn
import org.mockito.kotlin.mock

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */

const val TASK_ID = 12
const val ROW_ID = 2L

class DatabaseRepositoryTesting {

    private val mapper = TaskMapper()

    @Test
    fun insertTaskTest() {
        val task = emptyTask()
        val taskDao = mock<TaskDao> {
            onBlocking { delete(anyInt()) } doReturn Unit
            onBlocking { insert(anyOrNull()) } doReturn ROW_ID
            onBlocking { getTaskByRowid(ROW_ID) } doReturn mapper.mapTaskToDbModel(task.copy(id = TASK_ID))
            onBlocking { getTask(anyInt()) } doReturn mapper.mapTaskToDbModel(task.copy(id = TASK_ID))
        }
        val repo = DatabaseTaskRepository(taskDao, mapper)

        runBlocking {
            val inserted = repo.insertTask(task)
            val id = inserted.id
            assertEquals(inserted, task.copy(id = id))
        }
    }


}