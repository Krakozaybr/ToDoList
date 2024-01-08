package com.krakozaybr.todolist.presentation.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDefaults
import androidx.compose.material3.DatePickerState
import androidx.compose.material3.DisplayMode
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
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
fun TasksDatePicker(
    modifier: Modifier = Modifier,
    state: TasksDatePickerState = rememberTasksDatePickerState()
) {
    Column {
        DatePicker(
            modifier = modifier,
            state = state.datePickerState,
            title = null,
            colors = DatePickerDefaults.colors(
                containerColor = MaterialTheme.colorScheme.primary,
                //titleContentColor =,
                //headlineContentColor =,
                //weekdayContentColor =,
                //subheadContentColor = ,
                //yearContentColor =,
                //currentYearContentColor =,
                //selectedYearContentColor =,
                //selectedYearContainerColor =,
                //dayContentColor = MaterialTheme.colorScheme,
                //disabledDayContentColor =,
                //selectedDayContentColor =,
                //disabledSelectedDayContentColor =,
                //selectedDayContainerColor =,
                //disabledSelectedDayContainerColor =,
                //todayContentColor =,
                //todayDateBorderColor =,
                //dayInSelectionRangeContentColor =,
                //dayInSelectionRangeContainerColor =,
            )
        )
    }
}

@Preview
@Composable
fun TasksDatePickerPreview() {
    AppTheme {
        Surface {
            Box(modifier = Modifier.fillMaxSize()) {
                TasksDatePicker(modifier = Modifier.align(Alignment.Center))
            }
        }
    }
}