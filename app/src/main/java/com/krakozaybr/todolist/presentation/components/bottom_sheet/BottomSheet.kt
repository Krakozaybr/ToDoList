package com.krakozaybr.todolist.presentation.components.bottom_sheet

import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.requiredHeightIn
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SheetValue
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.collapse
import androidx.compose.ui.semantics.dismiss
import androidx.compose.ui.semantics.expand
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.Velocity
import com.krakozaybr.todolist.R
import kotlinx.coroutines.launch
import kotlin.math.roundToInt

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BottomSheet(
    state: SheetState,
    modifier: Modifier = Modifier,
    peekHeight: Dp,
    layoutHeight: Float,
    sheetSwipeEnabled: Boolean = true,
    dragHandle: @Composable (() -> Unit)?,
    content: @Composable ColumnScope.() -> Unit
) {
    val scope = rememberCoroutineScope()
    val peekHeightPx = with(LocalDensity.current) { peekHeight.toPx() }
    val orientation = Orientation.Vertical

    // Callback that is invoked when the anchors have changed.
    val anchorChangeHandler = remember(state, scope) {
        BottomSheetScaffoldAnchorChangeHandler(
            state = state,
            animateTo = { target, velocity ->
                scope.launch {
                    state.swipeableState.animateTo(
                        target, velocity = velocity
                    )
                }
            },
            snapTo = { target ->
                scope.launch { state.swipeableState.snapTo(target) }
            }
        )
    }
    Column(
        modifier = modifier
            .fillMaxWidth()
            .requiredHeightIn(min = peekHeight)
            .nestedScroll(
                remember(state.swipeableState) {
                    ConsumeSwipeWithinBottomSheetBoundsNestedScrollConnection(
                        sheetState = state,
                        orientation = orientation,
                        onFling = { scope.launch { state.settle(it) } }
                    )
                }
            )
            .swipeableV2(
                state = state.swipeableState,
                orientation = orientation,
                enabled = sheetSwipeEnabled
            )
            .swipeAnchors(
                state.swipeableState,
                possibleValues = setOf(
                    SheetValue.Hidden,
                    SheetValue.PartiallyExpanded,
                    SheetValue.Expanded
                ),
                anchorChangeHandler = anchorChangeHandler
            ) { value, sheetSize ->
                when (value) {
                    SheetValue.PartiallyExpanded -> if (state.skipPartiallyExpanded)
                        null else layoutHeight - peekHeightPx

                    SheetValue.Expanded -> if (sheetSize.height == peekHeightPx.roundToInt()) {
                        null
                    } else {
                        maxOf(0f, layoutHeight - sheetSize.height)
                    }

                    SheetValue.Hidden -> if (state.skipHiddenState) null else layoutHeight
                }
            },
    ) {
        Column(
            Modifier.fillMaxSize()
        ){
            if (dragHandle != null) {
                val partialExpandActionLabel = stringResource(id = R.string.partial_expand)
                val dismissActionLabel = stringResource(id = R.string.dismiss_action)
                val expandActionLabel = stringResource(id = R.string.expand)
                Box(
                    Modifier
                        .align(Alignment.CenterHorizontally)
                        .semantics(mergeDescendants = true) {
                            with(state) {
                                // Provides semantics to interact with the bottomsheet if there is more
                                // than one anchor to swipe to and swiping is enabled.
                                if (swipeableState.anchors.size > 1 && sheetSwipeEnabled) {
                                    if (currentValue == SheetValue.PartiallyExpanded) {
                                        if (swipeableState.confirmValueChange(SheetValue.Expanded)) {
                                            expand(expandActionLabel) {
                                                scope.launch { expand() }; true
                                            }
                                        }
                                    } else {
                                        if (swipeableState.confirmValueChange(SheetValue.PartiallyExpanded)) {
                                            collapse(partialExpandActionLabel) {
                                                scope.launch { partialExpand() }; true
                                            }
                                        }
                                    }
                                    if (!state.skipHiddenState) {
                                        dismiss(dismissActionLabel) {
                                            scope.launch { hide() }
                                            true
                                        }
                                    }
                                }
                            }
                        },
                ) {
                    dragHandle()
                }
            }
            content()
        }
    }
}


@ExperimentalMaterial3Api
private fun BottomSheetScaffoldAnchorChangeHandler(
    state: SheetState,
    animateTo: (target: SheetValue, velocity: Float) -> Unit,
    snapTo: (target: SheetValue) -> Unit,
) = AnchorChangeHandler<SheetValue> { previousTarget, previousAnchors, newAnchors ->
    val previousTargetOffset = previousAnchors[previousTarget]
    val newTarget = when (previousTarget) {
        SheetValue.Hidden, SheetValue.PartiallyExpanded -> SheetValue.PartiallyExpanded
        SheetValue.Expanded -> if (newAnchors.containsKey(SheetValue.Expanded)) SheetValue.Expanded else SheetValue.PartiallyExpanded
    }
    val newTargetOffset = newAnchors.getValue(newTarget)
    if (newTargetOffset != previousTargetOffset) {
        if (state.swipeableState.isAnimationRunning) {
            // Re-target the animation to the new offset if it changed
            animateTo(newTarget, state.swipeableState.lastVelocity)
        } else {
            // Snap to the new offset value of the target if no animation was running
            snapTo(newTarget)
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
internal fun ConsumeSwipeWithinBottomSheetBoundsNestedScrollConnection(
    sheetState: SheetState,
    orientation: Orientation,
    onFling: (velocity: Float) -> Unit
): NestedScrollConnection = object : NestedScrollConnection {
    override fun onPreScroll(available: Offset, source: NestedScrollSource): Offset {
        val delta = available.toFloat()
        return if (delta < 0 && source == NestedScrollSource.Drag) {
            sheetState.swipeableState.dispatchRawDelta(delta).toOffset()
        } else {
            Offset.Zero
        }
    }

    override fun onPostScroll(
        consumed: Offset,
        available: Offset,
        source: NestedScrollSource
    ): Offset {
        return if (source == NestedScrollSource.Drag) {
            sheetState.swipeableState.dispatchRawDelta(available.toFloat()).toOffset()
        } else {
            Offset.Zero
        }
    }

    override suspend fun onPreFling(available: Velocity): Velocity {
        val toFling = available.toFloat()
        val currentOffset = sheetState.requireOffset()
        return if (toFling < 0 && currentOffset > sheetState.swipeableState.minOffset) {
            onFling(toFling)
            // since we go to the anchor with tween settling, consume all for the best UX
            available
        } else {
            Velocity.Zero
        }
    }

    override suspend fun onPostFling(consumed: Velocity, available: Velocity): Velocity {
        onFling(available.toFloat())
        return available
    }

    private fun Float.toOffset(): Offset = Offset(
        x = if (orientation == Orientation.Horizontal) this else 0f,
        y = if (orientation == Orientation.Vertical) this else 0f
    )

    @JvmName("velocityToFloat")
    private fun Velocity.toFloat() = if (orientation == Orientation.Horizontal) x else y

    @JvmName("offsetToFloat")
    private fun Offset.toFloat(): Float = if (orientation == Orientation.Horizontal) x else y
}
