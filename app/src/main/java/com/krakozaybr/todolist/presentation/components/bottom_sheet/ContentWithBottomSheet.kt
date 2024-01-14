package com.krakozaybr.todolist.presentation.components.bottom_sheet

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.BottomSheetScaffoldState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SheetValue
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.rememberStandardBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.SubcomposeLayout
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.krakozaybr.todolist.presentation.theme.AppTheme
import kotlin.math.roundToInt


@ExperimentalMaterial3Api
@Stable
class ContentWithBottomSheetState(
    val bottomSheetState: SheetState,
    val snackbarHostState: SnackbarHostState
) {
    var sheetPeekHeight by mutableStateOf(0.dp)
}

/**
 * Create and [remember] a [BottomSheetScaffoldState].
 *
 * @param bottomSheetState the state of the standard bottom sheet. See
 * [rememberStandardBottomSheetState]
 * @param snackbarHostState the [SnackbarHostState] used to show snackbars inside the scaffold
 */
@Composable
@ExperimentalMaterial3Api
fun rememberContentWithBottomSheetState(
    bottomSheetState: SheetState = rememberSheetState(
        initialValue = SheetValue.PartiallyExpanded,
        skipHiddenState = true
    ),
    snackbarHostState: SnackbarHostState = remember { SnackbarHostState() }
): ContentWithBottomSheetState {
    return remember(bottomSheetState, snackbarHostState) {
        ContentWithBottomSheetState(
            bottomSheetState = bottomSheetState,
            snackbarHostState = snackbarHostState
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ContentWithBottomSheet(
    content: @Composable (PaddingValues) -> Unit,
    sheetContent: @Composable ColumnScope.() -> Unit,
    modifier: Modifier = Modifier,
    state: ContentWithBottomSheetState = rememberContentWithBottomSheetState(),
    topBar: @Composable (() -> Unit)? = null,
    snackbarHost: @Composable (SnackbarHostState) -> Unit = { SnackbarHost(it) }
) {

    val sheetState = state.bottomSheetState
    val snackbarHostState = state.snackbarHostState

    ContentBottomSheetLayout(
        modifier = modifier,
        topBar = topBar,
        content = content,
        state = state,
        sheetState = sheetState,
        snackbarHost = { snackbarHost(snackbarHostState) },
        bottomSheet = { layoutHeight ->
            BottomSheet(
                modifier = Modifier
                    .background(MaterialTheme.colorScheme.surface),
                state = sheetState,
                dragHandle = null,
                sheetSwipeEnabled = false,
                peekHeight = state.sheetPeekHeight,
                content = sheetContent,
                layoutHeight = layoutHeight.toFloat()
            )
        }
    )
}

@Composable
@OptIn(ExperimentalMaterial3Api::class)
private fun ContentBottomSheetLayout(
    modifier: Modifier,
    topBar: @Composable (() -> Unit)?,
    content: @Composable (PaddingValues) -> Unit,
    state: ContentWithBottomSheetState,
    sheetState: SheetState,
    snackbarHost: @Composable () -> Unit,
    bottomSheet: @Composable (Int) -> Unit
) {
    SubcomposeLayout(modifier) { constraints ->
        val layoutWidth = constraints.maxWidth
        val layoutHeight = constraints.maxHeight
        val looseConstraints = constraints.copy(minWidth = 0, minHeight = 0)

        val topBarPlaceable = topBar?.let {
            subcompose(ContentWithBottomSheetSlot.TopBar) { topBar() }[0]
                .measure(looseConstraints)
        }
        val topBarHeight = topBarPlaceable?.height ?: 0

        val bodyConstraints = looseConstraints.copy(maxHeight = layoutHeight - topBarHeight)
        val bodyPlaceable = subcompose(ContentWithBottomSheetSlot.Body) {
            content(PaddingValues())
        }[0].measure(bodyConstraints)

        val peekHeight = bodyPlaceable.measuredHeight.toDp() - StatusBarHeight
        // Updating state before measuring bottom sheet!!!
        state.sheetPeekHeight = peekHeight

        val sheetPlaceable = subcompose(ContentWithBottomSheetSlot.Sheet) {
            bottomSheet(layoutHeight)
        }[0].measure(looseConstraints)

        // offset can be null after restoring (during navigation)
        val sheetOffsetY = ((sheetState.offset?.roundToInt()
            ?: (layoutHeight - peekHeight.toPx().toInt())))
        val sheetOffsetX = Integer.max(0, (layoutWidth - sheetPlaceable.width) / 2)

        val snackbarPlaceable = subcompose(
            ContentWithBottomSheetSlot.Snackbar, snackbarHost
        )[0].measure(looseConstraints)
        val snackbarOffsetX = (layoutWidth - snackbarPlaceable.width) / 2
        val snackbarOffsetY = when (sheetState.currentValue) {
            SheetValue.PartiallyExpanded -> sheetOffsetY - snackbarPlaceable.height
            SheetValue.Expanded, SheetValue.Hidden -> layoutHeight - snackbarPlaceable.height
        }

        layout(layoutWidth, layoutHeight) {
            // Placement order is important for elevation
            bodyPlaceable.placeRelative(0, topBarHeight)
            topBarPlaceable?.placeRelative(0, 0)
            sheetPlaceable.placeRelative(sheetOffsetX, sheetOffsetY)
            snackbarPlaceable.placeRelative(snackbarOffsetX, snackbarOffsetY)
        }
    }
}

val StatusBarHeight = 24.dp

private enum class ContentWithBottomSheetSlot {
    TopBar,
    Body,
    Sheet,
    Snackbar
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Composable
fun ContentWithBottomSheetPreview() {
    AppTheme {
        Surface {
            ContentWithBottomSheet(
                content = {
                    Text(text = "LOLKEK", modifier = Modifier
                        .fillMaxWidth()
                        .height(400.dp))
                },
                sheetContent = {
                    LazyColumn {
                        items(100) {
                            Text("item $it")
                        }
                    }
                }
            )
        }
    }
}
