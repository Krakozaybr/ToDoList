package com.krakozaybr.todolist.presentation.screens.main

import androidx.compose.runtime.Composable
import com.krakozaybr.todolist.navigation.AppNavGraph
import com.krakozaybr.todolist.navigation.rememberNavigationState
import com.krakozaybr.todolist.presentation.screens.create_edit_screens.CreateTaskScreen
import com.krakozaybr.todolist.presentation.screens.create_edit_screens.EditTaskScreen
import com.krakozaybr.todolist.presentation.screens.list_screen.TasksListScreen

@Composable
fun MainScreen() {
    val navigationState = rememberNavigationState()

    AppNavGraph(
        navHostController = navigationState.navHostController,
        taskListScreenContent = {
            TasksListScreen(
                showTaskCreate = {
                    navigationState.navigateToNewTaskScreen(it)
                },
                showTaskInfo = {
                    navigationState.navigateToTaskInfo(it.id)
                }
            )
        },
        taskEditScreenContent = {
            EditTaskScreen {
                navigationState.navHostController.popBackStack()
            }
        },
        taskCreateScreenContent = {
            CreateTaskScreen(
                onBackPressed = {
                    navigationState.navHostController.popBackStack()
                },
                navigateToTask = {
                    navigationState.navigateToTaskInfo(it)
                }
            )
        }
    )
}
