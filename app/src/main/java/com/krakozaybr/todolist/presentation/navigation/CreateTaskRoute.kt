package com.krakozaybr.todolist.presentation.navigation

import androidx.compose.runtime.Composable
import cafe.adriel.voyager.core.annotation.ExperimentalVoyagerApi
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.hilt.getViewModel
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.krakozaybr.todolist.presentation.screens.create_edit_screens.CreateTaskScreen
import com.krakozaybr.todolist.presentation.screens.create_edit_screens.view_models.CreateTaskViewModel
import java.time.LocalDate

data class CreateTaskRoute(val date: LocalDate) : Screen {

    @OptIn(ExperimentalVoyagerApi::class)
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow

        val viewModel = getViewModel<CreateTaskViewModel, CreateTaskViewModel.Factory> { factory ->
            factory.create(date)
        }

        CreateTaskScreen(
            viewModel = viewModel,
            onBackPressed = {
                navigator replace MainRoute
            },
            navigateToTask = {
                navigator replace EditTaskRoute(it)
            },
        )
    }
}