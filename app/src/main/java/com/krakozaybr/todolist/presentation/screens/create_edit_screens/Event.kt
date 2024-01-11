package com.krakozaybr.todolist.presentation.screens.create_edit_screens

import java.time.LocalDate
import java.time.LocalTime

sealed class Event {

    data class NameChanged(val name: String) : Event()

    data class DescriptionChanged(val description: String) : Event()

    data class StartTimeChanged(val startTime: LocalTime) : Event()

    data class FinishTimeChanged(val finishTime: LocalTime) : Event()

    data class DateChanged(val date: LocalDate) : Event()

    data class DoneChanged(val done: Boolean) : Event()

    data object SavingStateSnackbarDismissed : Event()

    data object SaveEvent : Event()

    data object DeleteEvent : Event()

}