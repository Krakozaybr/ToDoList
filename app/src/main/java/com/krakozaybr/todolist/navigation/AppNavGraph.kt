package com.krakozaybr.todolist.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument


typealias TaskId = Int

@Composable
fun AppNavGraph(
    navHostController: NavHostController,
    taskListScreenContent: @Composable () -> Unit,
    taskInfoScreenContent: @Composable (TaskId) -> Unit
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
            val taskId: Int = it.arguments?.getInt(Screen.TASK_ID_KEY)
                ?: throw RuntimeException("Arguments are null")
            taskInfoScreenContent(taskId)
        }
    }
}
