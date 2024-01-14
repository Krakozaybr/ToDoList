package com.krakozaybr.todolist.presentation.screens.create_edit_screens.view_models

import androidx.lifecycle.viewModelScope
import com.krakozaybr.todolist.R
import com.krakozaybr.todolist.domain.task.Task
import com.krakozaybr.todolist.domain.task.use_cases.DeleteTaskUseCase
import com.krakozaybr.todolist.domain.task.use_cases.EditTaskUseCase
import com.krakozaybr.todolist.domain.task.use_cases.GetTaskUseCase
import com.krakozaybr.todolist.domain.task.use_cases.ValidateNameUseCase
import com.krakozaybr.todolist.domain.task.use_cases.ValidateTimeRangeUseCase
import com.krakozaybr.todolist.presentation.screens.create_edit_screens.SavingState
import com.krakozaybr.todolist.presentation.screens.create_edit_screens.ScreenState
import com.krakozaybr.todolist.presentation.validation.UiText
import com.krakozaybr.todolist.presentation.validation.ValidationMapper
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@HiltViewModel(assistedFactory = EditTaskViewModel.Factory::class)
class EditTaskViewModel @AssistedInject constructor(
    validateNameUseCase: ValidateNameUseCase,
    validateTimeRangeUseCase: ValidateTimeRangeUseCase,
    validationMapper: ValidationMapper,
    private val editTaskUseCase: EditTaskUseCase,
    private val getTaskUseCase: GetTaskUseCase,
    private val deleteTaskUseCase: DeleteTaskUseCase,
    @Assisted
    private val taskId: Int
) : CreateEditTaskViewModel(
    validateNameUseCase = validateNameUseCase,
    validateTimeRangeUseCase = validateTimeRangeUseCase,
    validationMapper = validationMapper,
) {
    init {
        viewModelScope.launch(Dispatchers.IO) {
            with(getTaskUseCase(taskId)) {
                _screenState.value = ScreenState.TaskInfo(
                    name = name,
                    startTime = dateStart.toLocalTime(),
                    finishTime = dateFinish.toLocalTime(),
                    date = dateStart.toLocalDate(),
                    description = description,
                    done = done,
                )
            }
        }
    }

    override suspend fun deleteTask(task: Task) {
        deleteTaskUseCase(task.copy(id = taskId))
    }

    override suspend fun saveTask(task: Task) {
        try {
            editTaskUseCase(task)
            updateSavingState(SavingState.SavedSuccessfully(task.id))
        } catch (e: Exception) {
            updateSavingState(SavingState.Error(UiText.StringResource(R.string.error_saving)))
        }
    }

    @AssistedFactory
    interface Factory {
        fun create(taskId: Int): EditTaskViewModel
    }
}