package com.krakozaybr.todolist.presentation.screens.list_screen

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
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
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.krakozaybr.todolist.presentation.components.Loading
import com.krakozaybr.todolist.presentation.components.TasksCalendar
import com.krakozaybr.todolist.presentation.components.TasksList
import com.krakozaybr.todolist.presentation.components.rememberTasksCalendarState
import com.krakozaybr.todolist.presentation.theme.AppTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TasksListScreen(
    tasksListVM: TasksListViewModel = viewModel()
) {
    val state by tasksListVM.state.collectAsState()

    val localDensity = LocalDensity.current
    val localConfiguration = LocalConfiguration.current

    val contentHeight = remember { mutableStateOf(localConfiguration.screenHeightDp.dp) }
    val sheetHeight by remember {
        derivedStateOf { localConfiguration.screenHeightDp.dp - contentHeight.value }
    }
    val sheetPeekHeight by animateDpAsState(
        targetValue = sheetHeight.value.coerceAtLeast(200f).dp,
        label = "",
        animationSpec = tween(3000)
    )

    BottomSheetScaffold(
        modifier = Modifier
            .onGloballyPositioned {
                contentHeight.value = with(localDensity) { it.size.height.toDp() }
            },
        content = {
            val calendarState = rememberTasksCalendarState(
                initialDate = state.date
            )

            if (calendarState.selectedDay != state.date) {
                tasksListVM.updateDate(calendarState.selectedDay)
            }
            Box(
                modifier = Modifier
                    .background(MaterialTheme.colorScheme.secondaryContainer)
                    .padding(PaddingValues(top = 8.dp, bottom = 16.dp, start = 8.dp, end = 8.dp))
            ) {
                TasksCalendar(state = calendarState)
            }
        },
        sheetShape = RectangleShape,
        sheetDragHandle = null,
        sheetPeekHeight = sheetPeekHeight,
        sheetContent = {
            when (val currentState = state) {
                is State.Loading -> Loading(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(300.dp)
                )

                is State.TaskList -> TasksList(
                    tasks = currentState.tasks,
                    showItemDescription = {

                    },
                    changeDoneState = {
                        tasksListVM.changeDoneState(it)
                    },
                    deleteItem = {
                        tasksListVM.deleteTask(it)
                    },
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(12.dp)
                )
            }
        }
    )
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
