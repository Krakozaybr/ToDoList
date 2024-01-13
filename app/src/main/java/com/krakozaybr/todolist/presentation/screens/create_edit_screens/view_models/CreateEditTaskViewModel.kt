package com.krakozaybr.todolist.presentation.screens.create_edit_screens.view_models

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.krakozaybr.todolist.domain.task.Task
import com.krakozaybr.todolist.domain.task.use_cases.ValidateNameUseCase
import com.krakozaybr.todolist.domain.task.use_cases.ValidateTimeRangeUseCase
import com.krakozaybr.todolist.presentation.screens.create_edit_screens.Event
import com.krakozaybr.todolist.presentation.screens.create_edit_screens.SavingState
import com.krakozaybr.todolist.presentation.screens.create_edit_screens.ScreenState
import com.krakozaybr.todolist.presentation.validation.ValidationMapper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.time.LocalTime

abstract class CreateEditTaskViewModel(
    private val validateNameUseCase: ValidateNameUseCase,
    private val validateTimeRangeUseCase: ValidateTimeRangeUseCase,
    private val validationMapper: ValidationMapper
) : ViewModel() {

    protected val _screenState = MutableStateFlow<ScreenState>(ScreenState.Loading)
    val state = _screenState.asStateFlow()

    fun onEvent(event: Event) {

        when (event) {
            is Event.DateChanged -> {
                _screenState.value = requireTaskLoaded().copy(date = event.date)
            }

            is Event.DescriptionChanged -> {
                _screenState.value = requireTaskLoaded().copy(description = event.description)
            }

            is Event.FinishTimeChanged -> {
                validateTime(finishTime = event.finishTime)
            }

            is Event.StartTimeChanged -> {
                validateTime(startTime = event.startTime)
            }

            is Event.NameChanged -> {
                validateName(name = event.name)
            }

            is Event.DoneChanged -> {
                _screenState.value = requireTaskLoaded().copy(done = event.done)
            }

            is Event.SaveEvent -> {
                if (validateName() && validateTime()) {
                    saveTask()
                }
            }

            is Event.DeleteEvent -> {
                updateSavingState(SavingState.Saving)
                deleteTask()
            }

            is Event.SavingStateSnackbarDismissed -> {
                updateSavingState(SavingState.Default)
                resetSavingStateJob?.cancel()
            }
        }

    }

    private fun deleteTask() {
        viewModelScope.launch (Dispatchers.IO) {
            deleteTask(getTask())
        }
    }

    abstract suspend fun deleteTask(task: Task)

    private fun validateName(
        curState: ScreenState.TaskInfo = requireTaskLoaded(),
        name: String = requireTaskLoaded().name
    ): Boolean {
        val nameError = validationMapper(validateNameUseCase(name))
        _screenState.value = curState.copy(
            name = name,
            nameError = nameError.errorMessage
        )
        return nameError.successful
    }

    private fun validateTime(
        curState: ScreenState.TaskInfo = requireTaskLoaded(),
        startTime: LocalTime = curState.startTime,
        finishTime: LocalTime = curState.finishTime
    ): Boolean {
        val timeError = validationMapper(
            validateTimeRangeUseCase(
                start = startTime,
                finish = finishTime
            )
        )
        _screenState.value = curState.copy(
            startTime = startTime,
            finishTime = finishTime,
            timeError = timeError.errorMessage
        )
        return timeError.successful
    }

    private fun getTask(): Task {
        with (requireTaskLoaded()) {
            return Task(
                name = name,
                dateStart = date.atTime(startTime),
                dateFinish = date.atTime(finishTime),
                description = description,
                done = done
            )
        }
    }

    private var savingJob: Job? = null

    private fun saveTask() {

        savingJob?.cancel()

        _screenState.value = requireTaskLoaded().copy(savingState = SavingState.Saving)
        savingJob = viewModelScope.launch(Dispatchers.IO) {
            saveTask(getTask())
            resetSavingState()
        }
    }

    protected abstract suspend fun saveTask(task: Task)

    private var resetSavingStateJob: Job? = null

    private fun resetSavingState() {

        resetSavingStateJob?.cancel()

        resetSavingStateJob = viewModelScope.launch(Dispatchers.Unconfined) {
            delay(SAVING_STATE_VISIBILITY_DURATION)
            updateSavingState(SavingState.Default)
        }
    }

    protected fun updateSavingState(savingState: SavingState) {
        _screenState.value = requireTaskLoaded().copy(savingState = savingState)
    }

    private fun requireTaskLoaded() = _screenState.value as ScreenState.TaskInfo

    companion object {
        const val SAVING_STATE_VISIBILITY_DURATION = 5000L
    }

}