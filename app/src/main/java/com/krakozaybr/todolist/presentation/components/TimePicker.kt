package com.krakozaybr.todolist.presentation.components

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.TimePicker
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TimePickerField(
    modifier: Modifier = Modifier
) {
    val state = rememberTimePickerState(
        is24Hour = true
    )
    TimePicker(
        state = state,

    )
}
