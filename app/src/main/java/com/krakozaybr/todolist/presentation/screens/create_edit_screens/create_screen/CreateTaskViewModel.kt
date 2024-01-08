package com.krakozaybr.todolist.presentation.screens.create_edit_screens.create_screen

import com.krakozaybr.todolist.di.qualifiers.TaskInitialDate
import com.krakozaybr.todolist.domain.task.use_cases.AddEditTaskUseCase
import com.krakozaybr.todolist.presentation.screens.create_edit_screens.CreateEditTaskViewModel
import com.krakozaybr.todolist.presentation.screens.create_edit_screens.State
import com.krakozaybr.todolist.presentation.wrappers.task_info.TaskInfo
import com.krakozaybr.todolist.presentation.wrappers.task_info.TaskInfoMapper
import com.krakozaybr.todolist.presentation.wrappers.task_info.TaskInfoValidator
import dagger.hilt.android.lifecycle.HiltViewModel
import java.time.LocalDate
import javax.inject.Inject

@HiltViewModel
class CreateTaskViewModel @Inject constructor(
    addEditTaskUseCase: AddEditTaskUseCase,
    taskInfoMapper: TaskInfoMapper,
    taskInfoValidator: TaskInfoValidator,
    @TaskInitialDate
    private val taskInitialDate: LocalDate
) : CreateEditTaskViewModel(
    addEditTaskUseCase = addEditTaskUseCase,
    taskInfoMapper = taskInfoMapper,
    taskInfoValidator = taskInfoValidator,
) {

    init {
        _state.value = State.TaskLoaded(TaskInfo(date = taskInitialDate))
    }

}