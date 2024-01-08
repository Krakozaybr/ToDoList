package com.krakozaybr.todolist.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.krakozaybr.todolist.domain.task.Task


@Composable
fun AppNavGraph(
    navHostController: NavHostController,
    taskListScreenContent: @Composable () -> Unit,
    taskInfoScreenContent: @Composable () -> Unit
) {
    NavHost(
        navController = navHostController,
        startDestination = Screen.TaskListScreen.route
    ) {
        composable(route = Screen.TaskListScreen.route) {
            taskListScreenContent()
        }
        composable(route = Screen.TaskInfoScreen.route, arguments = listOf(
            navArgument(Screen.TASK_ID_KEY) {
                type = NavType.IntType
            }
        )) {
            taskInfoScreenContent()
        }
        composable(route = Screen.NewTaskScreen.route) {
            it.arguments?.putInt(Screen.TASK_ID_KEY, Task.UNDEFINED_ID)
            taskInfoScreenContent()
        }
    }
}
