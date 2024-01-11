package com.krakozaybr.todolist.presentation.components

import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material3.BottomSheetScaffold
import androidx.compose.material3.BottomSheetScaffoldState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.rememberBottomSheetScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

val INITIAL_BOTTOM_SHEET_PEEK_HEIGHT = 0.dp

@OptIn(ExperimentalMaterial3Api::class)
class ContentWithBottomSheetState(
    val bottomSheetScaffoldState: BottomSheetScaffoldState,
    initialSheetPeekHeight: Dp,
) {
    var sheetPeekHeight by mutableStateOf(initialSheetPeekHeight)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun rememberContentWithBottomSheetState(
    bottomSheetScaffoldState: BottomSheetScaffoldState = rememberBottomSheetScaffoldState(),
    initialSheetPeekHeight: Dp = INITIAL_BOTTOM_SHEET_PEEK_HEIGHT,
): ContentWithBottomSheetState {
    return remember {
        ContentWithBottomSheetState(
            bottomSheetScaffoldState = bottomSheetScaffoldState,
            initialSheetPeekHeight = initialSheetPeekHeight,
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
    val localConfiguration = LocalConfiguration.current
    val density = LocalDensity.current

    val screenHeight = localConfiguration.screenHeightDp.dp

    BottomSheetScaffold(
        modifier = modifier.onGloballyPositioned {
            if (state.sheetPeekHeight == INITIAL_BOTTOM_SHEET_PEEK_HEIGHT) {
                state.sheetPeekHeight = with(density) {
                    screenHeight - it.size.height.toDp()
                }
            }
        },
        content = content,
        sheetShape = RectangleShape,
        sheetDragHandle = null,
        sheetPeekHeight = state.sheetPeekHeight,
        sheetContent = sheetContent,
        scaffoldState = state.bottomSheetScaffoldState,
        topBar = topBar,
        snackbarHost = snackbarHost,
    )
}