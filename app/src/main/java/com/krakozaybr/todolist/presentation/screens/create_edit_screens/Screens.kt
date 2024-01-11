package com.krakozaybr.todolist.presentation.screens.create_edit_screens

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.krakozaybr.todolist.R
import com.krakozaybr.todolist.presentation.components.DeleteButton
import com.krakozaybr.todolist.presentation.components.DescriptionInput
import com.krakozaybr.todolist.presentation.components.Loading
import com.krakozaybr.todolist.presentation.components.NameInput
import com.krakozaybr.todolist.presentation.components.SaveButton
import com.krakozaybr.todolist.presentation.components.TaskDateInput
import com.krakozaybr.todolist.presentation.components.TaskSwitch
import com.krakozaybr.todolist.presentation.components.TaskTimeInput
import com.krakozaybr.todolist.presentation.screens.create_edit_screens.view_models.CreateTaskViewModel
import com.krakozaybr.todolist.presentation.screens.create_edit_screens.view_models.EditTaskViewModel
import com.krakozaybr.todolist.presentation.theme.AppTheme
import java.time.LocalDate
import java.time.LocalTime

@Composable
private fun CreateEditTaskFields(
    screenState: ScreenState,
    modifier: Modifier = Modifier,
    onEvent: (Event) -> Unit
) {
    Box(
        modifier = modifier
    ) {
        AnimatedContent(
            targetState = screenState,
            contentKey = { it.javaClass },
            label = "Loading animation"
        ) {
            when (it) {
                is ScreenState.Loading -> {
                    Loading(modifier = Modifier.fillMaxSize())
                }

                is ScreenState.TaskInfo -> {
                    Fields(screenState = it, onEvent = onEvent)
                }
            }
        }
    }
}

@Composable
private fun Fields(
    screenState: ScreenState.TaskInfo,
    modifier: Modifier = Modifier,
    onEvent: (Event) -> Unit
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {

        val enabled by remember { derivedStateOf { screenState.savingState !is SavingState.Saving } }

        TaskDateInput(
            modifier = Modifier.fillMaxWidth(),
            label = stringResource(id = R.string.task_date),
            selectedDate = screenState.date,
            enabled = enabled,
            onDateChanged = { onEvent(Event.DateChanged(it)) }
        )

        NameInput(
            modifier = Modifier.fillMaxWidth(),
            text = screenState.name,
            enabled = enabled,
            errorMessage = screenState.nameError?.asString(),
            onTextChanged = {
                onEvent(Event.NameChanged(it))
            }
        )

        TaskTimeInput(
            modifier = Modifier.fillMaxWidth(),
            label = stringResource(id = R.string.task_start_time),
            selectedTime = screenState.startTime,
            enabled = enabled,
            errorMessage = screenState.timeError?.asString(),
            onTimeChanged = { onEvent(Event.StartTimeChanged(it)) }
        )

        TaskTimeInput(
            modifier = Modifier.fillMaxWidth(),
            label = stringResource(id = R.string.task_finish_time),
            selectedTime = screenState.finishTime,
            enabled = enabled,
            errorMessage = screenState.timeError?.asString(),
            onTimeChanged = { onEvent(Event.FinishTimeChanged(it)) }
        )

        DescriptionInput(
            modifier = Modifier.fillMaxWidth(),
            text = screenState.description,
            onTextChanged = { onEvent(Event.DescriptionChanged(it)) }
        )

        Box(modifier = Modifier.fillMaxWidth()) {
            Text(
                text = stringResource(id = R.string.task_done),
                modifier = Modifier.align(Alignment.CenterStart)
            )
            TaskSwitch(
                modifier = Modifier
                    .size(57.dp, 30.dp)
                    .align(Alignment.CenterEnd),
                checked = screenState.done
            ) {
                onEvent(Event.DoneChanged(it))
            }
        }

    }
}

@Preview
@Composable
fun FieldsPreview() {
    AppTheme {
        Surface(modifier = Modifier.fillMaxSize()) {

            val screenState = ScreenState.TaskInfo(
                name = "",
                nameError = null,
                startTime = LocalTime.now(),
                finishTime = LocalTime.now(),
                timeError = null,
                date = LocalDate.now(),
                description = "Some description",
                done = true,
                savingState = SavingState.Default,
            )

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(12.dp)
            ) {
                Fields(
                    modifier = Modifier.align(Alignment.Center),
                    screenState = screenState,
                    onEvent = {}
                )
            }

        }
    }
}

@Composable
fun SavingStateSnackbar(
    state: SavingState,
    onDismiss: () -> Unit
) {
    val snackbarHostState = remember { SnackbarHostState() }
    val message: String? = when (state) {
        is SavingState.Saving -> stringResource(id = R.string.saving)
        is SavingState.SavedSuccessfully -> stringResource(id = R.string.saved_successfully)
        is SavingState.Error -> state.description.asString()
        else -> null
    }

    LaunchedEffect(state) {
        message?.let {
            val result = snackbarHostState.showSnackbar(
                message = message,
                duration = SnackbarDuration.Indefinite,
                withDismissAction = true
            )

            if (result == SnackbarResult.Dismissed) {
                onDismiss()
            }

        } ?: snackbarHostState.currentSnackbarData?.dismiss()
    }

    SnackbarHost(snackbarHostState)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ScreenContent(
    state: ScreenState,
    topAppBarText: String,
    onBackPressed: () -> Unit,
    onEvent: (Event) -> Unit,
    bottomBar: @Composable () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(text = topAppBarText)
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant
                ),
                navigationIcon = {
                    IconButton(onClick = onBackPressed) {
                        Icon(
                            imageVector = Icons.Filled.ArrowBack,
                            contentDescription = stringResource(
                                id = R.string.go_back_button
                            )
                        )
                    }
                }
            )
        },
        snackbarHost = {
            if (state is ScreenState.TaskInfo) {
                SavingStateSnackbar(
                    state = state.savingState,
                    onDismiss = {
                        onEvent(Event.SavingStateSnackbarDismissed)
                    }
                )
            }
        },
        content = {
            CreateEditTaskFields(
                screenState = state,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(it)
                    .padding(12.dp),
                onEvent = onEvent
            )
        },
        bottomBar = bottomBar
    )
}

@Composable
fun CreateTaskScreen(
    viewModel: CreateTaskViewModel = hiltViewModel(),
    navigateToTask: (Int) -> Unit,
    onBackPressed: () -> Unit
) {

    val state by viewModel.state.collectAsState()

    ScreenContent(
        state = state,
        topAppBarText = stringResource(id = R.string.create_task),
        onBackPressed = onBackPressed,
        onEvent = viewModel::onEvent,
        bottomBar = {
            Row(
                modifier = Modifier.padding(12.dp)
            ) {
                SaveButton {
                    viewModel.onEvent(Event.SaveEvent)
                }
            }
        }
    )

    with(state) {
        if (this is ScreenState.TaskInfo && this.savingState is SavingState.SavedSuccessfully) {
            navigateToTask(this.savingState.taskId)
        }
    }
}

@Composable
fun EditTaskScreen(
    viewModel: EditTaskViewModel = hiltViewModel(),
    onBackPressed: () -> Unit
) {

    val state by viewModel.state.collectAsState()

    ScreenContent(
        state = state,
        topAppBarText = stringResource(id = R.string.edit_task),
        onBackPressed = onBackPressed,
        onEvent = viewModel::onEvent,
        bottomBar = {
            Row(
                modifier = Modifier.padding(12.dp),
                horizontalArrangement = Arrangement.SpaceAround
            ) {
                SaveButton(modifier = Modifier
                    .weight(1f)
                    .padding(end = 12.dp)) {
                    viewModel.onEvent(Event.SaveEvent)
                }
                DeleteButton(modifier = Modifier
                    .weight(1f)
                    .padding(start = 12.dp)) {
                    viewModel.onEvent(Event.DeleteEvent)
                }
            }
        }
    )

    with(state) {
        if (this is ScreenState.TaskInfo && this.savingState is SavingState.Deleted) {
            onBackPressed()
        }
    }
}
