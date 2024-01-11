package com.krakozaybr.todolist.presentation.components

import androidx.compose.animation.animateColor
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.updateTransition
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.krakozaybr.todolist.presentation.theme.AppTheme
import kotlin.math.min


data class TaskSwitchColors(
    val activeColor: Color,
    val inactiveColor: Color,
    val strokeActiveColor: Color,
    val strokeInactiveColor: Color,
    val circleColorActive: Color,
    val circleColorInactive: Color
)

object TaskSwitchDefaults {
    @Composable
    fun taskSwitchColors() = TaskSwitchColors(
        activeColor = MaterialTheme.colorScheme.primary,
        inactiveColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.4f),
        strokeActiveColor = MaterialTheme.colorScheme.primaryContainer,
        strokeInactiveColor = MaterialTheme.colorScheme.outline.copy(alpha = 0.4f),
        circleColorActive = MaterialTheme.colorScheme.onSecondary,
        circleColorInactive = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.4f),
    )
}

@Composable
fun TaskSwitch(
    checked: Boolean,
    modifier: Modifier = Modifier,
    colors: TaskSwitchColors = TaskSwitchDefaults.taskSwitchColors(),
    onClick: (Boolean) -> Unit
) {
    val shape = RoundedCornerShape(12.dp)

    val transition = updateTransition(checked, label = "checkbox state")

    val mainColor by transition.animateColor(label = "checkbox color") {
        if (it) {
            colors.activeColor
        } else {
            colors.inactiveColor
        }
    }

    val strokeColor by transition.animateColor(label = "checkbox color") {
        if (it) {
            colors.strokeActiveColor
        } else {
            colors.strokeInactiveColor
        }
    }

    val circleColor by transition.animateColor(label = "checkbox color") {
        if (it) {
            colors.circleColorActive
        } else {
            colors.circleColorInactive
        }
    }

    val progress by transition.animateFloat(
        label = "checkbox circle pos",
        transitionSpec = {
            spring(dampingRatio = 0.75f)
        }
    ) {
        if (it) {
            1f
        } else {
            0f
        }
    }

    Surface(
        modifier = modifier
            .background(mainColor, shape)
            .padding(1.dp)
            .background(strokeColor, shape)
            .padding(1.dp)
            .background(mainColor, shape)
            .drawWithContent {

                val dpCoef = size.height / size.toDpSize().height.value.coerceAtLeast(0.01f)
                val padding = 4f * dpCoef
                val radius = min(size.height / 2, size.width / 3f) - padding

                val leftX = padding + radius
                val rightX = size.width - radius - padding

                val circleX = leftX + (rightX - leftX) * progress

                drawCircle(
                    circleColor,
                    radius,
                    Offset(circleX, size.height / 2)
                )

                if (checked) {
                    val rectPadding = 2 * dpCoef
                    val rectX = padding
                    val height = 2 * dpCoef
                    val width = circleX - radius - rectPadding * 2 - padding

                    val delta = height

                    drawRect(
                        circleColor,
                        Offset(rectX, size.height / 2 - delta),
                        Size(width, height)
                    )

                    drawRect(
                        circleColor,
                        Offset(rectX, size.height / 2 + delta),
                        Size(width, height)
                    )
                }
            }
            .clickable {
                onClick(!checked)
            },
        content = {}
    )
}

@Preview
@Composable
fun RadioPreview() {
    Box (modifier = Modifier.fillMaxSize()) {
        val state = remember { mutableStateOf(true) }
        Column (
            modifier = Modifier.align(Alignment.Center),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {

            val modifier = Modifier
                .size(57.dp, 30.dp)

            AppTheme {
                TaskSwitch(
                    checked = state.value,
                    modifier = modifier
                ) {
                    state.value = it
                }
            }
            AppTheme (darkTheme = true) {
                TaskSwitch(
                    checked = state.value,
                    modifier = modifier
                ) {
                    state.value = it
                }
            }

            AppTheme {
                TaskSwitch(
                    checked = !state.value,
                    modifier = modifier
                ) {
                    state.value = it
                }
            }
            AppTheme (darkTheme = true) {
                TaskSwitch(
                    checked = !state.value,
                    modifier = modifier
                ) {
                    state.value = it
                }
            }
        }
    }
}