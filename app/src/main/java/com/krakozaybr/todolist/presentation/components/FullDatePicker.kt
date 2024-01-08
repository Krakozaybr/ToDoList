package com.krakozaybr.todolist.presentation.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDefaults
import androidx.compose.material3.DatePickerState
import androidx.compose.material3.DisplayMode
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Surface
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.kizitonwose.calendar.core.atStartOfMonth
import com.krakozaybr.todolist.presentation.theme.AppTheme
import com.krakozaybr.todolist.presentation.toLocalDate
import com.krakozaybr.todolist.presentation.toUtcEpochMillis
import java.time.LocalDate
import java.time.YearMonth

@OptIn(ExperimentalMaterial3Api::class)
class TasksDatePickerState(
    internal val datePickerState: DatePickerState
) {

    var displayMode: DisplayMode
        get() = datePickerState.displayMode
        set(value) {
            datePickerState.displayMode = value
        }
    var selectedDate: LocalDate?
        get() = datePickerState.selectedDateMillis?.toLocalDate()
        set(value) {
            value?.let {
                datePickerState.setSelection(it.toUtcEpochMillis())
            }
        }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun rememberTasksDatePickerState(
    initialDate: LocalDate = LocalDate.now(),
    initialSelectedMonth: YearMonth = YearMonth.now(),
    yearRange: IntRange = DatePickerDefaults.YearRange,
    initialDisplayMode: DisplayMode = DisplayMode.Picker
): TasksDatePickerState {
    return TasksDatePickerState(
        datePickerState = rememberDatePickerState(
            initialSelectedDateMillis = initialDate.toUtcEpochMillis(),
            initialDisplayedMonthMillis = initialSelectedMonth.atStartOfMonth().toUtcEpochMillis(),
            yearRange = yearRange,
            initialDisplayMode = initialDisplayMode,
        )
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FullDatePicker(
    modifier: Modifier = Modifier,
    state: TasksDatePickerState = rememberTasksDatePickerState()
) {
    Column {
        DatePicker(
            modifier = modifier,
            state = state.datePickerState,
            title = null,
        )
    }
}

@Preview
@Composable
fun TasksDatePickerPreview() {
    AppTheme {
        Surface {
            Box(modifier = Modifier.fillMaxSize()) {
                FullDatePicker(modifier = Modifier.align(Alignment.Center))
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun rememberTaskDatePickerState(
    initialDate: LocalDate = LocalDate.now(),
    initialSelectedMonth: YearMonth = YearMonth.now(),
    yearRange: IntRange = DatePickerDefaults.YearRange,
    initialDisplayMode: DisplayMode = DisplayMode.Input
): TasksDatePickerState {
    return TasksDatePickerState(
        datePickerState = rememberDatePickerState(
            initialSelectedDateMillis = initialDate.toUtcEpochMillis(),
            initialDisplayedMonthMillis = initialSelectedMonth.atStartOfMonth().toUtcEpochMillis(),
            yearRange = yearRange,
            initialDisplayMode = initialDisplayMode,
        )
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DatePickerField(
    modifier: Modifier = Modifier,
    state: TasksDatePickerState = rememberTaskDatePickerState()
) {
    Column {
        DatePicker(
            modifier = modifier,
            state = state.datePickerState,
            title = null,
            headline = null,
            showModeToggle = false,
        )
    }
}

@Preview
@Composable
fun TaskDatePickerPreview() {
    AppTheme {
        Surface {
            Column(modifier = Modifier.fillMaxSize()) {
                DatePickerField()
            }
        }
    }
}