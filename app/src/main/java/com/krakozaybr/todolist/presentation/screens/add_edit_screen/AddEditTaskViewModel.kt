package com.krakozaybr.todolist.presentation.screens.add_edit_screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.krakozaybr.todolist.di.qualifiers.TaskId
import com.krakozaybr.todolist.domain.task.use_cases.AddEditTaskUseCase
import com.krakozaybr.todolist.domain.task.use_cases.GetTaskUseCase
import com.krakozaybr.todolist.presentation.screens.add_edit_screen.data.TaskInfo
import com.krakozaybr.todolist.presentation.screens.add_edit_screen.data.TaskInfoMapper
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.LocalTime
import javax.inject.Inject

@HiltViewModel
class AddEditTaskViewModel @Inject constructor(
    private val addEditTaskUseCase: AddEditTaskUseCase,
    private val getTaskUseCase: GetTaskUseCase,
    private val taskInfoMapper: TaskInfoMapper,
    @TaskId
    private val taskId: Int
) : ViewModel() {

    private val _state = MutableStateFlow<State>(State.Loading)
    val state = _state.asStateFlow()

    init {
        viewModelScope.launch(Dispatchers.IO) {
            val task = getTaskUseCase(taskId)
            _state.value = State.TaskLoaded(taskInfoMapper.taskToTaskInfo(task))
        }
    }

    fun trySaveTask(taskInfo: TaskInfo) {
        val nameError = !nameCorrect(taskInfo.name)
        val timeError = !isTimeCorrect(
            taskInfo.timeStart,
            taskInfo.timeFinish
        )
        val dateError = !isDateCorrect(taskInfo.date)

        if (nameError || timeError || dateError) {
            _state.value = State.InputError(
                task = taskInfo,
                timeError = timeError,
                nameError = nameError,
                dateError = dateError
            )
        } else {
            saveTask(taskInfo)
        }

    }

    private fun isDateCorrect(date: LocalDate?) = date != null

    private fun nameCorrect(name: String) = name.isNotBlank()

    private fun isTimeCorrect(start: LocalTime?, end: LocalTime?) =
        start != null && end != null && start.isAfter(end)

    private fun saveTask(taskInfo: TaskInfo) {
        _state.value = State.Saving(taskInfo)
        viewModelScope.launch(Dispatchers.IO) {
            addEditTaskUseCase(taskInfoMapper.taskInfoToTask(taskInfo))
            _state.value = State.SavedSuccessfully(taskInfo)
        }
    }

}