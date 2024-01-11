package com.krakozaybr.todolist.presentation.screens.create_edit_screens

import com.krakozaybr.todolist.presentation.validation.UiText
import java.time.LocalDate
import java.time.LocalTime

sealed class ScreenState {

    data object Loading : ScreenState()

    data class TaskInfo(
        val name: String,
        val nameError: UiText? = null,
        val startTime: LocalTime,
        val finishTime: LocalTime,
        val timeError: UiText? = null,
        val date: LocalDate,
        val description: String,
        val done: Boolean,
        val savingState: SavingState = SavingState.Default
    ) : ScreenState()

}

sealed class SavingState {
    data object Default : SavingState()
    data object Saving : SavingState()

    data class SavedSuccessfully(val taskId: Int) : SavingState()

    data object Deleted : SavingState()

    data class Error(val description: UiText) : SavingState()

}
