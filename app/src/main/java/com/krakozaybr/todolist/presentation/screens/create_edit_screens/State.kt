package com.krakozaybr.todolist.presentation.screens.create_edit_screens

import com.krakozaybr.todolist.presentation.wrappers.task_info.TaskInfo
import com.krakozaybr.todolist.presentation.wrappers.task_info.TaskIssues

sealed class State {

    data object Loading : State()

    data class TaskLoaded(val task: TaskInfo) : State()

    data class InputError(
        val task: TaskInfo,
        val taskIssues: TaskIssues
    ) : State()

    data class SavedSuccessfully(val task: TaskInfo) : State()

    data class Saving(val task: TaskInfo) : State()

}