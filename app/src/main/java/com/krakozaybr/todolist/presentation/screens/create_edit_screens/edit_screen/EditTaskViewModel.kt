package com.krakozaybr.todolist.presentation.screens.create_edit_screens.edit_screen

import androidx.lifecycle.viewModelScope
import com.krakozaybr.todolist.di.qualifiers.TaskId
import com.krakozaybr.todolist.domain.task.use_cases.AddEditTaskUseCase
import com.krakozaybr.todolist.domain.task.use_cases.GetTaskUseCase
import com.krakozaybr.todolist.presentation.screens.create_edit_screens.CreateEditTaskViewModel
import com.krakozaybr.todolist.presentation.screens.create_edit_screens.State
import com.krakozaybr.todolist.presentation.wrappers.task_info.TaskInfoMapper
import com.krakozaybr.todolist.presentation.wrappers.task_info.TaskInfoValidator
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EditTaskViewModel @Inject constructor(
    addEditTaskUseCase: AddEditTaskUseCase,
    taskInfoMapper: TaskInfoMapper,
    taskInfoValidator: TaskInfoValidator,
    private val getTaskUseCase: GetTaskUseCase,
    @TaskId
    private val taskId: Int
) : CreateEditTaskViewModel(
    addEditTaskUseCase = addEditTaskUseCase,
    taskInfoMapper = taskInfoMapper,
    taskInfoValidator = taskInfoValidator,
)  {
    init {
        viewModelScope.launch(Dispatchers.IO) {
            val task = getTaskUseCase(taskId)
            _state.value = State.TaskLoaded(taskInfoMapper.taskToTaskInfo(task))
        }
    }
}