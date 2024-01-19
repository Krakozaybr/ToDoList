package com.krakozaybr.todolist.presentation.components

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.krakozaybr.todolist.R
import com.krakozaybr.todolist.utils.defaultFormat
import com.krakozaybr.todolist.presentation.theme.AppTheme
import com.maxkeppeker.sheets.core.models.base.rememberUseCaseState
import com.maxkeppeler.sheets.calendar.CalendarDialog
import com.maxkeppeler.sheets.calendar.models.CalendarConfig
import com.maxkeppeler.sheets.calendar.models.CalendarSelection
import com.maxkeppeler.sheets.clock.ClockDialog
import com.maxkeppeler.sheets.clock.models.ClockConfig
import com.maxkeppeler.sheets.clock.models.ClockSelection
import java.time.LocalDate
import java.time.LocalTime

@Composable
fun NameInput(
    text: String,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    errorMessage: String? = null,
    onTextChanged: (String) -> Unit
) {

    var supportingText: @Composable (() -> Unit)? = null
    val error = errorMessage != null

    if (error) {
        supportingText = { Text(text = errorMessage.toString()) }
    }

    OutlinedTextField(
        modifier = modifier.animateContentSize(),
        value = text,
        enabled = enabled,
        singleLine = true,
        onValueChange = {
            onTextChanged(it)
        },
        supportingText = supportingText,
        isError = error,
        label = {
            Text(
                text = stringResource(id = R.string.name)
            )
        },
        textStyle = MaterialTheme.typography.bodyMedium
    )
}

@Preview
@Composable
fun NameInputPreview() {
    AppTheme {
        Surface {

            var text by remember { mutableStateOf("") }

            Column(modifier = Modifier.padding(12.dp)) {
                NameInput(
                    text = text,
                    errorMessage = "Error message",
                    enabled = true
                ) {
                    text = it
                }
            }
        }
    }
}

@Composable
fun DescriptionInput(
    text: String,
    modifier: Modifier = Modifier,
    onTextChanged: (String) -> Unit
) {
    OutlinedTextField(
        modifier = modifier,
        value = text,
        minLines = 6,
        onValueChange = onTextChanged,
        label = {
            Text(
                text = stringResource(id = R.string.description)
            )
        },
        textStyle = MaterialTheme.typography.bodyMedium
    )
}

@Preview
@Composable
fun DescriptionInputPreview() {
    AppTheme {
        Surface {

            var text by remember { mutableStateOf("") }

            Column(modifier = Modifier.padding(12.dp)) {
                DescriptionInput(text = text) {
                    text = it
                }
            }
        }
    }
}

@Composable
private fun DialogChangeTextField(
    text: String,
    label: String,
    enabled: Boolean,
    errorMessage: String?,
    modifier: Modifier = Modifier,
    onEditClick: () -> Unit
) {
    val supportingText: @Composable (() -> Unit) = { Text(text = errorMessage.toString()) }
    val error = errorMessage != null

    OutlinedTextField(
        modifier = modifier
            .animateContentSize(),
        value = text,
        onValueChange = {},
        readOnly = true,
        enabled = enabled,
        isError = error,
        label = { Text(text = label) },
        supportingText = supportingText.takeIf { error },
        textStyle = MaterialTheme.typography.bodyLarge,
        trailingIcon = {
            Icon(
                modifier = Modifier.clickable {
                    if (enabled) {
                        onEditClick()
                    }
                },
                imageVector = Icons.Default.Edit,
                contentDescription = label
            )
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TaskTimeInput(
    label: String,
    selectedTime: LocalTime,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    errorMessage: String? = null,
    onTimeChanged: (LocalTime) -> Unit
) {

    val useCaseState = rememberUseCaseState()

    DialogChangeTextField(
        text = selectedTime.defaultFormat(),
        label = label,
        enabled = enabled,
        errorMessage = errorMessage,
        modifier = modifier,
        onEditClick = {
            useCaseState.show()
        },
    )

    ClockDialog(
        state = useCaseState,
        selection = ClockSelection.HoursMinutes { hours, minutes ->
            onTimeChanged(LocalTime.of(hours, minutes))
        },
        config = ClockConfig(
            boundary = LocalTime.of(0, 0, 0)..LocalTime.of(23, 59, 0),
            defaultTime = selectedTime,
            is24HourFormat = true
        ),
    )
}

@Preview
@Composable
fun TaskTimeInputPreview() {
    AppTheme {
        Surface {
            Box(modifier = Modifier.fillMaxSize()) {

                var time by remember { mutableStateOf(LocalTime.now()) }
                val error = time.isAfter(LocalTime.now())
                val enabled = !error

                Column(Modifier.align(Alignment.Center)) {
                    Text(text = enabled.toString())
                    TaskTimeInput(
                        label = "Start time",
                        selectedTime = time,
                        enabled = enabled,
                        errorMessage = null,
                    ) {
                        time = it
                    }
                }

            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TaskDateInput(
    label: String,
    selectedDate: LocalDate,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    errorMessage: String? = null,
    onDateChanged: (LocalDate) -> Unit
) {
    val useCaseState = rememberUseCaseState()

    DialogChangeTextField(
        modifier = modifier,
        text = selectedDate.defaultFormat(),
        label = label,
        enabled = enabled,
        errorMessage = errorMessage,
        onEditClick = {
            useCaseState.show()
        },
    )

    CalendarDialog(
        state = useCaseState,
        config = CalendarConfig(
            yearSelection = true,
            monthSelection = true
        ),
        selection = CalendarSelection.Date(
            selectedDate = selectedDate,
            onSelectDate = onDateChanged
        )
    )
}


@Preview
@Composable
fun TaskDateInputPreview() {
    AppTheme {
        Surface {
            Box(modifier = Modifier.fillMaxSize()) {

                var date by remember { mutableStateOf(LocalDate.now()) }
                val error = date.isAfter(LocalDate.now())
                val enabled = !error

                Column(Modifier.align(Alignment.Center)) {
                    TaskDateInput(
                        label = "Date",
                        selectedDate = date,
                        enabled = enabled,
                        errorMessage = null,
                    ) {
                        date = it
                    }
                }

            }
        }
    }
}
