package com.krakozaybr.todolist.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.Surface
import cafe.adriel.voyager.navigator.Navigator
import cafe.adriel.voyager.transitions.FadeTransition
import com.krakozaybr.todolist.presentation.navigation.MainRoute
import com.krakozaybr.todolist.presentation.theme.AppTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AppTheme {
                Surface {
                    Navigator(MainRoute) {
                        FadeTransition(it)
                    }
                }
            }
        }
    }
}
