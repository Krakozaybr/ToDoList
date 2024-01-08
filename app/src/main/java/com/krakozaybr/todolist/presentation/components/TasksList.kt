package com.krakozaybr.todolist.presentation.components

import androidx.compose.animation.Crossfade
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.outlined.CheckCircle
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DismissDirection
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.SwipeToDismiss
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDismissState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.krakozaybr.todolist.R
import com.krakozaybr.todolist.domain.task.Task
import com.krakozaybr.todolist.presentation.Hour
import com.krakozaybr.todolist.presentation.groupByHour
import com.krakozaybr.todolist.presentation.theme.AppTheme
import com.krakozaybr.todolist.presentation.timePeriodString
import com.krakozaybr.todolist.presentation.toHourFormatString
import java.time.LocalDateTime
import java.util.PriorityQueue


@OptIn(ExperimentalFoundationApi::class)
@Composable
fun TasksList(
    tasks: List<Task>,
    showItemDescription: (Task) -> Unit,
    changeDoneState: (Task) -> Unit,
    deleteItem: (Task) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        // "Click on task for detail" text
        item {
            Box(
                modifier = Modifier
                    .fillMaxWidth(),
            ) {
                Row(
                    modifier = Modifier
                        .align(Alignment.Center),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Text(
                        text = stringResource(id = R.string.click_for_detail),
                        style = MaterialTheme.typography.bodyMedium,
                        textAlign = TextAlign.Center
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Icon(
                        imageVector = Icons.Outlined.Info,
                        contentDescription = stringResource(id = R.string.click_for_detail)
                    )
                }
            }
        }

        for ((hour, list) in tasks.groupByHour()) {

            // Key must be string; Int can cause crashes (maybe because TaskItem keys are Int too)
            stickyHeader(key = hour.toString()) {
                StickyHourHeader(
                    hour,
                    modifier = Modifier
                        .animateItemPlacement()
                )
            }

            items(list, key = { it.id }) {
                TaskItem(
                    modifier = Modifier
                        .animateItemPlacement(),
                    task = it,
                    onClick = {
                        showItemDescription(it)
                    },
                    onLongClick = {
                        changeDoneState(it)
                    },
                    onDismiss = {
                        deleteItem(it)
                    }
                )
                Spacer(modifier = Modifier.height(2.dp))
            }
        }
    }

}

@Preview
@Composable
fun TaskItemsPreview() {
    val tasksArray = remember {
        PriorityQueue<Task> { t1, t2 -> t1.id.compareTo(t2.id) }.apply {
            repeat(30) {
                add(
                    Task(
                        dateStart = LocalDateTime.now().minusHours(it.toLong() / 3 + 1L)
                            .plusMinutes(it.toLong()),
                        dateFinish = LocalDateTime.now().minusHours(it.toLong() / 3),
                        name = "Task $it",
                        description = "Description of task $it",
                        done = it % 3 == 0,
                        id = it,
                    )
                )
            }
        }
    }
    val tasks = remember { mutableStateOf<List<Task>>(tasksArray.toList()) }

    AppTheme {
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .padding(8.dp)
        ) {
            TasksList(
                tasks = tasks.value,
                showItemDescription = {},
                changeDoneState = { task ->
                    tasksArray.remove(task)
                    tasksArray.add(task.copy(done = !task.done))
                    tasks.value = tasksArray.toList()
                },
                deleteItem = { task ->
                    tasksArray.remove(task)
                    tasks.value = tasksArray.toList()
                },
                modifier = Modifier
                    .fillMaxSize()
            )
        }

    }
}

@Composable
private fun StickyHourHeader(hour: Hour, modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .alpha(0.7f)
    ) {
        Card(
            modifier = Modifier
                .align(Alignment.CenterStart),
            shape = CardDefaults.shape,
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.tertiary,
                contentColor = MaterialTheme.colorScheme.onTertiary
            )
        ) {
            Text(
                modifier = Modifier
                    .padding(vertical = 8.dp, horizontal = 16.dp),
                text = hour.toHourFormatString()
            )
        }
    }
}

@Preview
@Composable
fun StickyHourHeaderPreview() {
    Column {
        AppTheme(
            darkTheme = true
        ) {
            StickyHourHeader(hour = 9)
        }
        Spacer(modifier = Modifier.height(12.dp))
        AppTheme(
            darkTheme = false
        ) {
            StickyHourHeader(hour = 14)
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun TaskItem(
    task: Task,
    onClick: () -> Unit,
    onLongClick: () -> Unit,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier
) {
    val dismissState = rememberDismissState()

    if (dismissState.isDismissed(DismissDirection.StartToEnd)) {
        onDismiss()
    }

    SwipeToDismiss(
        modifier = modifier,
        state = dismissState,
        background = {},
        dismissContent = {
            TaskItemBody(
                task = task,
                onClick = onClick,
                onLongClick = onLongClick,
            )
        },
        directions = setOf(DismissDirection.StartToEnd)
    )
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun TaskItemBody(
    task: Task,
    onClick: () -> Unit,
    onLongClick: () -> Unit
) {
    val elevation by animateDpAsState(
        targetValue = if (task.done) {
            4.dp
        } else {
            2.dp
        }, label = "Elevation animation"
    )

    val alpha by animateFloatAsState(
        targetValue = if (task.done) {
            1f
        } else {
            0.5f
        },
        label = "Task alpha animation"
    )

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .combinedClickable(
                onClick = onClick,
                onLongClick = onLongClick
            )
            .alpha(alpha.coerceIn(0f, 1f)),
        elevation = CardDefaults.cardElevation(elevation),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.tertiaryContainer,
            contentColor = MaterialTheme.colorScheme.onTertiaryContainer
        )
    ) {
        Column(
            modifier = Modifier
                .padding(12.dp)
                .fillMaxWidth()
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Crossfade(
                    targetState = task.done,
                    label = "Task done icon crossfade animation"
                ) {
                    Icon(
                        imageVector = if (it) {
                            Icons.Outlined.CheckCircle
                        } else {
                            Icons.Default.CheckCircle
                        },
                        contentDescription = stringResource(id = R.string.task_done_indicator)
                    )
                }
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = task.name + " " + task.id,
                    style = MaterialTheme.typography.bodyLarge,
                )
            }
            Text(
                text = task.timePeriodString(),
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier
                    .align(Alignment.End)
            )
        }
    }
}

@Preview
@Composable
fun TaskDoneItemPreview() {
    val task = Task(
        dateStart = LocalDateTime.now().minusHours(1L),
        dateFinish = LocalDateTime.now(),
        name = "Task name",
        description = "Description of task",
        done = true,
        id = 1,
    )

    Column {
        AppTheme(darkTheme = false) {
            TaskItem(
                task = task,
                onLongClick = {},
                onClick = {},
                onDismiss = {}
            )
        }
        Spacer(modifier = Modifier.height(12.dp))
        AppTheme(darkTheme = true) {
            TaskItem(
                task = task,
                onLongClick = {},
                onClick = {},
                onDismiss = {}
            )
        }
    }
}

@Preview
@Composable
fun TaskNotDoneItemPreview() {
    val task = Task(
        dateStart = LocalDateTime.now().minusHours(1L),
        dateFinish = LocalDateTime.now(),
        name = "Task name",
        description = "Description of task",
        done = false,
        id = 1,
    )

    Column {
        AppTheme(darkTheme = false) {
            TaskItem(
                task = task,
                onLongClick = {},
                onClick = {},
                onDismiss = {}
            )
        }
        Spacer(modifier = Modifier.height(12.dp))
        AppTheme(darkTheme = true) {
            TaskItem(
                task = task,
                onLongClick = {},
                onClick = {},
                onDismiss = {}
            )
        }
    }
}

@Composable
fun EmptyTasksList(
    modifier: Modifier = Modifier
){
    Box (modifier) {
        Text(
            modifier = Modifier.align(Alignment.Center),
            text = stringResource(id = R.string.list_is_empty),
            style = MaterialTheme.typography.headlineMedium
        )
    }
}

@Preview
@Composable
fun EmptyTasksListPreview() {
    AppTheme {
        Surface {
            EmptyTasksList(modifier = Modifier.fillMaxSize())
        }
    }
}
