package com.krakozaybr.todolist.data.db.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

@Entity(tableName = "tasks")
data class TaskDbModel(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val dateStart: Date,
    val dateFinish: Date,
    val name: String,
    val description: String,
)