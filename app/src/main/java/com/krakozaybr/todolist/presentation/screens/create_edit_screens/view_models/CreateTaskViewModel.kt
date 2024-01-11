package com.krakozaybr.todolist.presentation.screens.create_edit_screens.view_models

import com.krakozaybr.todolist.R
import com.krakozaybr.todolist.di.qualifiers.TaskInitialDate
import com.krakozaybr.todolist.domain.task.Task
import com.krakozaybr.todolist.domain.task.use_cases.CreateTaskUseCase
import com.krakozaybr.todolist.domain.task.use_cases.ValidateNameUseCase
import com.krakozaybr.todolist.domain.task.use_cases.ValidateTimeRangeUseCase
import com.krakozaybr.todolist.presentation.screens.create_edit_screens.SavingState
import com.krakozaybr.todolist.presentation.screens.create_edit_screens.ScreenState
import com.krakozaybr.todolist.presentation.validation.UiText
import com.krakozaybr.todolist.presentation.validation.ValidationMapper
import dagger.hilt.android.lifecycle.HiltViewModel
import java.time.LocalDate
import java.time.LocalTime
import javax.inject.Inject

@HiltViewModel
class CreateTaskViewModel @Inject constructor(
    validateNameUseCase: ValidateNameUseCase,
    validateTimeRangeUseCase: ValidateTimeRangeUseCase,
    validationMapper: ValidationMapper,
    private val createTaskUseCase: CreateTaskUseCase,
    @TaskInitialDate
    private val taskInitialDate: LocalDate
) : CreateEditTaskViewModel(
    validateNameUseCase = validateNameUseCase,
    validateTimeRangeUseCase = validateTimeRangeUseCase,
    validationMapper = validationMapper,
) {

    init {
        _screenState.value = ScreenState.TaskInfo(
            name = "f",
            description = "",
            done = false,
            startTime = LocalTime.now(),
            finishTime = LocalTime.now(),
            date = taskInitialDate
        )
    }

    override suspend fun deleteTask(task: Task) = throw NotImplementedError("Unstored task cannot be created")

    override suspend fun saveTask(task: Task) {
        try {
            val newTask = createTaskUseCase(task)
            updateSavingState(SavingState.SavedSuccessfully(newTask.id))
        } catch (e: Exception) {
            updateSavingState(SavingState.Error(UiText.StringResource(R.string.error_saving)))
        }
    }

}