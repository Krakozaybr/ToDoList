package com.krakozaybr.todolist.domain.task

import java.util.Date

data class Task(
    val dateStart: Date,
    val dateFinish: Date,
    val name: String,
    val description: String,
    val id: Int = UNDEFINED_ID,
) {
    companion object {
        const val UNDEFINED_ID = 0
    }
}