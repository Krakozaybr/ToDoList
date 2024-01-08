package com.krakozaybr.todolist.navigation

import com.krakozaybr.todolist.domain.task.Task

sealed class Screen(val route: String) {

    data object TaskListScreen : Screen(ROUTE_TASK_LIST)
    data object TaskInfoScreen : Screen(ROUTE_TASK_INFO) {

        private const val ROUTE_FOR_ARGS = "task_info"

        fun getRouteWithArgs(task: Task): String {
            return "$ROUTE_FOR_ARGS/${task.id}"
        }

    }

    data object NewTaskScreen : Screen(ROUTE_NEW_TASK)

    companion object {

        const val TASK_ID_KEY = "task_id"

        private const val ROUTE_TASK_LIST = "task_list"
        private const val ROUTE_TASK_INFO = "task_info/{$TASK_ID_KEY}"
        private const val ROUTE_NEW_TASK = "task_new"
    }

}
