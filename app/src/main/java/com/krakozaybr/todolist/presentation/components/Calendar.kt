package com.krakozaybr.todolist.presentation.components

import androidx.compose.animation.animateColor
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.updateTransition
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.ScrollAxisRange
import androidx.compose.ui.semantics.horizontalScrollAxisRange
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.kizitonwose.calendar.compose.CalendarState
import com.kizitonwose.calendar.compose.HorizontalCalendar
import com.kizitonwose.calendar.compose.rememberCalendarState
import com.kizitonwose.calendar.core.CalendarDay
import com.kizitonwose.calendar.core.CalendarMonth
import com.kizitonwose.calendar.core.DayPosition
import com.kizitonwose.calendar.core.OutDateStyle
import com.kizitonwose.calendar.core.nextMonth
import com.kizitonwose.calendar.core.previousMonth
import com.kizitonwose.calendar.core.yearMonth
import com.krakozaybr.todolist.R
import com.krakozaybr.todolist.presentation.theme.AppTheme
import com.krakozaybr.todolist.presentation.toMonthString
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.YearMonth


class TasksCalendarState(
    initialDate: LocalDate = LocalDate.now(),
    val calendarState: CalendarState
) {
    var selectedDay by mutableStateOf(initialDate)
}

@Composable
fun rememberTasksCalendarState(
    initialDate: LocalDate = LocalDate.now(),
    initialMonth: YearMonth = initialDate.yearMonth,
    extraMonthBuffer: Long = 120L,
    calendarState: CalendarState = rememberCalendarState(
        startMonth = initialMonth.minusMonths(extraMonthBuffer),
        firstVisibleMonth = initialMonth,
        endMonth = initialMonth.plusMonths(extraMonthBuffer),
        outDateStyle = OutDateStyle.EndOfGrid
    )
): TasksCalendarState {
    return remember {
        TasksCalendarState(
            initialDate = initialDate,
            calendarState = calendarState
        )
    }
}

/*
    * The library that is used here for calendar doesn`t support specifying arrangement of days,
      so default padding values here are closer to "Magic constants". I see only one solution - to fork
      the library, but it doesn`t worth the trouble.

    * Also there are some performance issues
*/
@Composable
fun TasksCalendar(
    modifier: Modifier = Modifier,
    state: TasksCalendarState = rememberTasksCalendarState()
) {
    Column(
        modifier = modifier
    ) {
        HorizontalCalendar(
            modifier = Modifier.semantics {
                horizontalScrollAxisRange = ScrollAxisRange(value = { 0f }, maxValue = { 0f })
            },
            state = state.calendarState,
            userScrollEnabled = true,
            calendarScrollPaged = true,
            dayContent = { TasksCalendarDay(it, state) },
            monthHeader = { TasksCalendarHeader(it) },
        )

        val coroutineScope = rememberCoroutineScope()

        CalendarControlPanel(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 8.dp, start = 6.dp, end = 6.dp),
            showCurrentChecked = {
                coroutineScope.launch {
                    state.calendarState.animateScrollToMonth(
                        state.selectedDay.yearMonth
                    )
                }
            },
            showCurrentDate = {
                coroutineScope.launch {
                    state.calendarState.animateScrollToMonth(
                        YearMonth.now()
                    )
                }
            },
            showNext = {
                coroutineScope.launch {
                    state.calendarState.animateScrollToMonth(
                        state.calendarState.firstVisibleMonth.yearMonth.nextMonth
                    )
                }
            },
            showPrevious = {
                coroutineScope.launch {
                    state.calendarState.animateScrollToMonth(
                        state.calendarState.firstVisibleMonth.yearMonth.previousMonth
                    )
                }
            })
    }
}

@Composable
private fun BoxScope.TasksCalendarDay(
    day: CalendarDay,
    state: TasksCalendarState,
    elementPadding: PaddingValues = PaddingValues(4.dp),
    elementSize: Dp = 40.dp,
) {
    val selected = state.selectedDay == day.date
    val shape = CardDefaults.shape
    val outDateAlpha = 0.3f
    val borderWidth = 2.dp

    // Animation spec
    val dampingRatio = Spring.DampingRatioNoBouncy
    val stiffness = Spring.StiffnessLow

    val transition = updateTransition(targetState = selected, label = "Day animation transition")

    val borderColor by transition.animateColor(
        targetValueByState = {
            if (it) {
                MaterialTheme.colorScheme.primary
            } else {
                Color.Transparent
            }
        },
        transitionSpec = {
            spring(dampingRatio, stiffness)
        },
        label = "Border animation for Day in calendar"
    )

    val containerColor by transition.animateColor(
        targetValueByState = {
            if (it) {
                MaterialTheme.colorScheme.inversePrimary
            } else {
                MaterialTheme.colorScheme.secondary
            }
        },
        transitionSpec = {
            spring(dampingRatio, stiffness)
        },
        label = "Container color animation for Day in calendar"
    )

    val contentColor by transition.animateColor(
        targetValueByState = {
            if (it) {
                MaterialTheme.colorScheme.primary
            } else {
                MaterialTheme.colorScheme.onSecondary
            }
        },
        transitionSpec = {
            spring(dampingRatio, stiffness)
        },
        label = "Content color animation for Day in calendar"
    )

    Box(
        modifier = Modifier
            .align(Alignment.Center)
            .padding(elementPadding)
            .size(elementSize)
            .aspectRatio(1f) // make days square
            .background(color = containerColor, shape = shape)
            .alpha(if (day.position == DayPosition.MonthDate) 1f else outDateAlpha)
            .clickable(
                indication = null,
                interactionSource = remember { MutableInteractionSource() }
            ) {
                state.selectedDay = day.date
            }
            .border(
                width = borderWidth,
                color = borderColor,
                shape = shape
            ),
    ) {
        Text(
            modifier = Modifier
                .align(Alignment.Center),
            text = day.date.dayOfMonth.toString(),
            color = contentColor
        )
    }
}

@Composable
private fun ColumnScope.TasksCalendarHeader(
    month: CalendarMonth,
    padding: PaddingValues = PaddingValues(start = 6.dp, end = 6.dp, bottom = 12.dp)
) {
    Text(
        modifier = Modifier
            .padding(padding),
        text = month.toMonthString(),
        style = MaterialTheme.typography.headlineMedium
    )

}

@Composable
private fun CalendarControlPanel(
    showCurrentChecked: () -> Unit,
    showCurrentDate: () -> Unit,
    showNext: () -> Unit,
    showPrevious: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        modifier = modifier,
    ) {

        val lineHeight = 40.dp
        val iconCardModifier = Modifier
            .size(lineHeight)
            .aspectRatio(1f)
        val buttonModifier = Modifier
            .weight(1f)
            .height(lineHeight)
        val cardColors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.inversePrimary
        )

        Card(
            modifier = iconCardModifier
                .clickable {
                    showPrevious()
                },
            colors = cardColors
        ) {
            Icon(
                modifier = Modifier
                    .fillMaxSize(),
                imageVector = Icons.Default.KeyboardArrowLeft,
                contentDescription = stringResource(id = R.string.previous_month)
            )
        }

        Button(
            modifier = buttonModifier,
            onClick = showCurrentDate
        ) {
            Text(
                modifier = Modifier.fillMaxWidth(),
                text = stringResource(id = R.string.today),
                textAlign = TextAlign.Center
            )
        }

        Button(
            modifier = buttonModifier,
            onClick = showCurrentChecked
        ) {
            Text(
                modifier = Modifier.fillMaxWidth(),
                text = stringResource(id = R.string.checked),
                textAlign = TextAlign.Center
            )
        }

        Card(
            modifier = iconCardModifier
                .clickable {
                    showNext()
                },
            colors = cardColors
        ) {
            Icon(
                modifier = Modifier
                    .fillMaxSize(),
                imageVector = Icons.Default.KeyboardArrowRight,
                contentDescription = stringResource(id = R.string.next_month)
            )
        }
    }
}

@Preview
@Composable
fun TasksCalendarPreview() {
    AppTheme {
        Surface {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.secondaryContainer)
            ) {
                TasksCalendar(
                    modifier = Modifier.padding(12.dp)
                )
            }
        }
    }
}
