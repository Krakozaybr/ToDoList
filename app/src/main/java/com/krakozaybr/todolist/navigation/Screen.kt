package com.krakozaybr.todolist.navigation

import com.krakozaybr.todolist.presentation.toUtcEpochMillis
import java.time.LocalDate

sealed class Screen(val route: String) {

    data object TaskListScreen : Screen(ROUTE_TASK_LIST)
    data object TaskInfoScreen : Screen(ROUTE_TASK_INFO) {

        private const val ROUTE_FOR_ARGS = "task_info"

        fun getRouteWithArgs(taskId: Int): String {
            return "$ROUTE_FOR_ARGS/$taskId"
        }

    }

    data object NewTaskScreen : Screen(ROUTE_NEW_TASK) {

        private const val ROUTE_FOR_ARGS = "task_new"

        fun getRouteWithArgs(date: LocalDate): String {
            return "$ROUTE_FOR_ARGS/${date.toUtcEpochMillis()}"
        }

    }

    companion object {

        const val TASK_ID_KEY = "task_id"
        const val TASK_INITIAL_DATE_KEY = "task_date"

        private const val ROUTE_TASK_LIST = "task_list"
        private const val ROUTE_TASK_INFO = "task_info/{$TASK_ID_KEY}"
        private const val ROUTE_NEW_TASK = "task_new/{$TASK_INITIAL_DATE_KEY}"
    }

}
