package com.krakozaybr.todolist.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.hilt.getViewModel
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.krakozaybr.todolist.presentation.screens.list_screen.TasksListScreen
import com.krakozaybr.todolist.presentation.screens.list_screen.TasksListViewModel

object MainRoute : Screen {

    @Composable
    override fun Content() {

        val navigator = LocalNavigator.currentOrThrow

        val viewModel = getViewModel<TasksListViewModel>()

        TasksListScreen(
            showTaskInfo = {
                navigator push EditTaskRoute(it.id)
            },
            showTaskCreate = {
                navigator push CreateTaskRoute(it)
            },
            changeDoneState = viewModel::changeDoneState,
            updateDate = viewModel::updateDate,
            deleteItem = viewModel::deleteTask,
            state = viewModel.state.collectAsState().value
        )
    }
}