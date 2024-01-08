package com.krakozaybr.todolist.presentation.screens.add_edit_screen

import com.krakozaybr.todolist.presentation.screens.add_edit_screen.data.TaskInfo

sealed class State {

    data object Loading : State()

    data class TaskLoaded(val task: TaskInfo) : State()

    data class InputError(
        val task: TaskInfo,
        val timeError: Boolean,
        val dateError: Boolean,
        val nameError: Boolean
    ) : State()

    data class SavedSuccessfully(val task: TaskInfo) : State()

    data class Saving(val task: TaskInfo) : State()

}