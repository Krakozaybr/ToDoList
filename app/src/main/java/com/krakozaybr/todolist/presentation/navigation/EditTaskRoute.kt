package com.krakozaybr.todolist.presentation.navigation

import androidx.compose.runtime.Composable
import cafe.adriel.voyager.core.annotation.ExperimentalVoyagerApi
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.hilt.getViewModel
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.krakozaybr.todolist.presentation.screens.create_edit_screens.EditTaskScreen
import com.krakozaybr.todolist.presentation.screens.create_edit_screens.view_models.EditTaskViewModel

data class EditTaskRoute(val taskId: Int) : Screen {

    @OptIn(ExperimentalVoyagerApi::class)
    @Composable
    override fun Content() {

        val navigator = LocalNavigator.currentOrThrow

        val viewModel = getViewModel<EditTaskViewModel, EditTaskViewModel.Factory> { factory ->
            factory.create(taskId)
        }

        EditTaskScreen(
            viewModel = viewModel,
            onBackPressed = {
                navigator replace MainRoute
            }
        )

    }
}