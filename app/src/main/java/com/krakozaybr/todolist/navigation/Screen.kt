package com.krakozaybr.todolist.navigation

import android.net.Uri
import com.krakozaybr.todolist.domain.task.Task

sealed class Screen(val route: String) {

    object TaskListScreen : Screen(ROUTE_TASK_LIST)
    object TaskInfoScreen : Screen(ROUTE_TASK_INFO) {

        private const val ROUTE_FOR_ARGS = "task_info"

        fun getRouteWithArgs(task: Task): String {
            return "$ROUTE_FOR_ARGS/${task.id}"
        }

    }

    companion object {

        const val TASK_ID_KEY = "task_id"

        private const val ROUTE_TASK_LIST = "task_list"
        private const val ROUTE_TASK_INFO = "task_info/{$TASK_ID_KEY}"
    }

}

fun String.encode(): String {
    return Uri.encode(this)
}
