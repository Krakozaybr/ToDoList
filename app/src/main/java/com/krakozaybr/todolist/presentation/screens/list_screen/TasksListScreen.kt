package com.krakozaybr.todolist.presentation.screens.list_screen

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.BottomSheetScaffold
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.krakozaybr.todolist.domain.task.Task
import com.krakozaybr.todolist.presentation.components.ContentWithBottomSheet
import com.krakozaybr.todolist.presentation.components.ContentWithBottomSheetState
import com.krakozaybr.todolist.presentation.components.EmptyTasksList
import com.krakozaybr.todolist.presentation.components.FullDatePicker
import com.krakozaybr.todolist.presentation.components.Loading
import com.krakozaybr.todolist.presentation.components.TasksCalendar
import com.krakozaybr.todolist.presentation.components.TasksList
import com.krakozaybr.todolist.presentation.components.rememberContentWithBottomSheetState
import com.krakozaybr.todolist.presentation.components.rememberTasksCalendarState
import com.krakozaybr.todolist.presentation.components.rememberTasksDatePickerState
import com.krakozaybr.todolist.presentation.theme.AppTheme
import java.time.LocalDate

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TasksListScreen(
    tasksListVM: TasksListViewModel,
    showTaskInfo: (Task) -> Unit,
    showTaskCreate: (LocalDate) -> Unit,
) {
    val state by tasksListVM.state.collectAsState()

    val contentWithBottomSheetState = rememberContentWithBottomSheetState()

    ContentWithBottomSheet(
//        snackbarHost = {
//
//        },
        content = {
            TasksListScreenCalendar(
                state = state,
                updateDate = tasksListVM::updateDate
            )
        },
        sheetContent = {
            TasksListScreenBottomSheet(
                contentWithBottomSheetState = contentWithBottomSheetState,
                state = state,
                showItemDescription = {
                    showTaskInfo(it)
                },
                changeDoneState = tasksListVM::changeDoneState,
                deleteItem = tasksListVM::deleteTask,
                addTask = {
                    showTaskCreate(state.date)
                }
            )
        },
        modifier = Modifier,
        state = contentWithBottomSheetState,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun TasksListDatePicker(
    state: State,
    updateDate: (LocalDate) -> Unit,
) {
    val datePickerState = rememberTasksDatePickerState(
        initialDate = state.date
    )
    val selectedDate = datePickerState.selectedDate

    if (selectedDate != state.date) {
        selectedDate?.let { updateDate(it) }
    }

    FullDatePicker(
        modifier = Modifier
            .padding(top = 12.dp)
            .animateContentSize(),
        state = datePickerState
    )

}

@Composable
private fun TasksListScreenCalendar(
    state: State,
    updateDate: (LocalDate) -> Unit,
) {
    val calendarState = rememberTasksCalendarState(
        initialDate = state.date
    )

    if (calendarState.selectedDay != state.date) {
        updateDate(calendarState.selectedDay)
    }

    val padding = PaddingValues(
        top = 8.dp,
        bottom = 16.dp,
        start = 8.dp,
        end = 8.dp
    )

    Column {
        Box(
            modifier = Modifier
                .background(MaterialTheme.colorScheme.secondaryContainer)
                .padding(
                    paddingValues = padding
                )
                .animateContentSize()
        ) {
            TasksCalendar(state = calendarState)
        }

    }
}

@Composable
private fun TasksListScreenBottomSheet(
    contentWithBottomSheetState: ContentWithBottomSheetState,
    state: State,
    showItemDescription: (Task) -> Unit,
    changeDoneState: (Task) -> Unit,
    deleteItem: (Task) -> Unit,
    addTask: () -> Unit,
) {
    val sheetPeekHeight = contentWithBottomSheetState.sheetPeekHeight

    if (sheetPeekHeight == 0.dp) {
        return
    }

    AnimatedContent(
        targetState = state,
        transitionSpec = {
            when (targetState) {
                is State.Loading -> {
                    fadeIn() togetherWith
                            slideOutHorizontally { width -> -width }
                }

                is State.TaskList -> {
                    slideInVertically { height -> height } + fadeIn() togetherWith
                            fadeOut()
                }

                is State.EmptyTaskList -> {
                    fadeIn() togetherWith fadeOut()
                }
            }
        },
        contentKey = {
            it.date.toString() + it.javaClass
        },
        label = ""
    ) {
        when (it) {
            is State.Loading -> Loading(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(sheetPeekHeight)
            )

            is State.TaskList -> {
                TasksList(
                    tasks = it.tasks,
                    showItemDescription = showItemDescription,
                    changeDoneState = changeDoneState,
                    deleteItem = deleteItem,
                    addTask = addTask,
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(12.dp)
                )
            }

            is State.EmptyTaskList -> {
                EmptyTasksList(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(sheetPeekHeight)
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Composable
fun NestedScrollingSample() {
    AppTheme {
        Surface {
            BottomSheetScaffold(
                modifier = Modifier
                    .fillMaxSize(),
                content = {
                    Text(text = "Lolkek", style = MaterialTheme.typography.headlineMedium)
                },
                sheetSwipeEnabled = false,
                sheetDragHandle = null,
                sheetShape = RectangleShape,
                sheetPeekHeight = 400.dp,
                sheetContent = {
                    LazyColumn {
                        items(20) {
                            Card(
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Text(
                                    text = "Card $it",
                                    modifier = Modifier.padding(12.dp)
                                )
                            }
                        }
                    }
                }
            )
        }
    }
}
