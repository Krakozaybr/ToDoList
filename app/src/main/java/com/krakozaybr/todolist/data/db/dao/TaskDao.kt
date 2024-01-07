package com.krakozaybr.todolist.data.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.krakozaybr.todolist.data.db.models.TaskDbModel
import kotlinx.coroutines.flow.Flow
import java.time.LocalDateTime

@Dao
interface TaskDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(task: TaskDbModel)

    @Query("delete from tasks where id = :id")
    suspend fun delete(id: Int)

    @Query("select * from tasks where dateStart < :periodEnd and dateFinish > :periodStart")
    fun getTasksInPeriod(periodStart: LocalDateTime, periodEnd: LocalDateTime): Flow<List<TaskDbModel>>

    @Query("select * from tasks where id = :id limit 1")
    suspend fun getTask(id: Int): TaskDbModel

}