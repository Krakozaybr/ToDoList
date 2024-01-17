package com.krakozaybr.todolist.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import cafe.adriel.voyager.core.annotation.ExperimentalVoyagerApi
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.hilt.getViewModel
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.krakozaybr.todolist.presentation.screens.create_edit_screens.EditTaskScreen
import com.krakozaybr.todolist.presentation.screens.create_edit_screens.SavingState
import com.krakozaybr.todolist.presentation.screens.create_edit_screens.ScreenState
import com.krakozaybr.todolist.presentation.screens.create_edit_screens.view_models.EditTaskViewModel

data class EditTaskRoute(val taskId: Int) : Screen {

    @OptIn(ExperimentalVoyagerApi::class)
    @Composable
    override fun Content() {

        val navigator = LocalNavigator.currentOrThrow

        val viewModel = getViewModel<EditTaskViewModel, EditTaskViewModel.Factory> { factory ->
            factory.create(taskId)
        }
        val state by viewModel.state.collectAsState()

        with(state) {
            if (this is ScreenState.TaskInfo && this.savingState is SavingState.Deleted) {
                navigator.popUntilRoot()
            }
        }

        EditTaskScreen(
            state = state,
            onBackPressed = {
                navigator.popUntilRoot()
            },
            onEvent = viewModel::onEvent
        )

    }
}