package com.krakozaybr.todolist.presentation.screens.create_edit_screens

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.krakozaybr.todolist.domain.task.use_cases.AddEditTaskUseCase
import com.krakozaybr.todolist.presentation.wrappers.task_info.TaskInfo
import com.krakozaybr.todolist.presentation.wrappers.task_info.TaskInfoMapper
import com.krakozaybr.todolist.presentation.wrappers.task_info.TaskInfoValidator
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

open class CreateEditTaskViewModel(
    private val addEditTaskUseCase: AddEditTaskUseCase,
    private val taskInfoMapper: TaskInfoMapper,
    private val taskInfoValidator: TaskInfoValidator,
) : ViewModel() {

    protected val _state = MutableStateFlow<State>(State.Loading)
    val state = _state.asStateFlow()



    fun trySaveTask(taskInfo: TaskInfo) {
        val issues = taskInfoValidator.validate(taskInfo)

        if (issues.ok()) {
            saveTask(taskInfo)
        } else {
            _state.value = State.InputError(
                task = taskInfo,
                taskIssues = issues
            )
        }

    }
    private fun saveTask(taskInfo: TaskInfo) {
        _state.value = State.Saving(taskInfo)
        viewModelScope.launch(Dispatchers.IO) {
            addEditTaskUseCase(taskInfoMapper.taskInfoToTask(taskInfo))
            _state.value = State.SavedSuccessfully(taskInfo)
        }
    }

}