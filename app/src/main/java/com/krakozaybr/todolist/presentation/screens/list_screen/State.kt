package com.krakozaybr.todolist.presentation.screens.list_screen

import com.krakozaybr.todolist.domain.task.Task
import java.time.LocalDate

sealed class State(open val date: LocalDate) {

    data class Loading(override val date: LocalDate) : State(date)
    data class TaskList(val tasks: List<Task>, override val date: LocalDate) : State(date)

    data class EmptyTaskList(override val date: LocalDate) : State(date)

}