package com.krakozaybr.todolist.presentation.screens.list_screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.krakozaybr.todolist.domain.task.Task
import com.krakozaybr.todolist.domain.task.use_cases.EditTaskUseCase
import com.krakozaybr.todolist.domain.task.use_cases.DeleteTaskUseCase
import com.krakozaybr.todolist.domain.task.use_cases.GetTaskListUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import java.time.LocalDate
import javax.inject.Inject

@HiltViewModel
class TasksListViewModel @Inject constructor(
    private val editTaskUseCase: EditTaskUseCase,
    private val deleteTaskUseCase: DeleteTaskUseCase,
    private val getTaskListUseCase: GetTaskListUseCase
) : ViewModel() {


    private val _state = MutableStateFlow<State>(State.Loading(LocalDate.now()))
    val state = _state.asStateFlow()

    private var taskListObserverJob: Job? = null

    init {
        updateDate(LocalDate.now())
    }

    fun updateDate(date: LocalDate) {
        taskListObserverJob?.cancel()
        _state.value = State.Loading(date)

        taskListObserverJob = viewModelScope.launch(Dispatchers.IO) {
            getTaskListUseCase(date)
                .onEach {
                    _state.value = if (it.isNotEmpty()) {
                        State.TaskList(it, date)
                    } else {
                        State.EmptyTaskList(date)
                    }
                }.collect()
        }
    }

    fun deleteTask(task: Task) {
        viewModelScope.launch(Dispatchers.IO) {
            deleteTaskUseCase(task)
        }
    }

    fun changeDoneState(task: Task) {
        viewModelScope.launch(Dispatchers.IO) {
            editTaskUseCase(task.copy(done = !task.done))
        }
    }

    companion object {
        private const val TAG = "TasksListViewModel"
    }
}
