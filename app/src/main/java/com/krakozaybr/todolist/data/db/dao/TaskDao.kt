package com.krakozaybr.todolist.data.db.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.krakozaybr.todolist.data.db.models.TaskDbModel

@Dao
interface TaskDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(task: TaskDbModel)

    @Query("delete from tasks where id = :id")
    suspend fun delete(id: Int)

    @Query("select * from tasks")
    fun getTasks(): LiveData<List<TaskDbModel>>

    @Query("select * from tasks where id = :id limit 1")
    suspend fun getTask(id: Int): TaskDbModel

}