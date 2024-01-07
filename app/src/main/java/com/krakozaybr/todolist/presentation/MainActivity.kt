package com.krakozaybr.todolist.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.Surface
import com.krakozaybr.todolist.navigation.rememberNavigationState
import com.krakozaybr.todolist.presentation.components.TaskItemsPreview
import com.krakozaybr.todolist.presentation.screens.list_screen.TasksListScreen
import com.krakozaybr.todolist.presentation.theme.AppTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {

            val navigationState = rememberNavigationState()

            AppTheme {
                Surface {
                    TasksListScreen()
                }
            }
        }
    }
}
