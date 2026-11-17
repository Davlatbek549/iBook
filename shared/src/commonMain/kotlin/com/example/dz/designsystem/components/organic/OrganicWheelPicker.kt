package com.example.dz.designsystem.components.organic

import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.snapping.rememberSnapFlingBehavior
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.dz.designsystem.theme.OrganicColors
import com.example.dz.designsystem.theme.organicBodyFontFamily
import kotlin.math.abs
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.launch

/**
 * One column of a scroll wheel — day, month or year. It snaps to a row and reports the row that
 * settles in the middle; the caller draws the selection band behind all its columns at once.
 *
 * Rows fade with their distance from the middle, the middle one bold, as on the date sheet.
 */
@Composable
fun OrganicWheelPicker(
    items: List<String>,
    selectedIndex: Int,
    onSelectedIndexChange: (Int) -> Unit,
    modifier: Modifier = Modifier,
    itemHeight: Dp = OrganicWheelDefaults.ItemHeight,
    visibleItems: Int = OrganicWheelDefaults.VisibleItems,
    alignment: Alignment = Alignment.Center,
) {
    val state = rememberLazyListState(initialFirstVisibleItemIndex = selectedIndex.coerceIn(0, (items.size - 1).coerceAtLeast(0)))
    val itemHeightPx = with(LocalDensity.current) { itemHeight.toPx() }
    val centred by remember(state, itemHeightPx) { derivedStateOf { state.centredIndex(itemHeightPx) } }
    val latestOnChange by rememberUpdatedState(onSelectedIndexChange)
    val latestItemCount by rememberUpdatedState(items.size)
    val scope = rememberCoroutineScope()

    // Report a row only once the wheel has come to rest on it.
    LaunchedEffect(state) {
        snapshotFlow { state.isScrollInProgress }
            .filter { !it }
            .collect {
                val index = state.centredIndex(itemHeightPx).coerceIn(0, (latestItemCount - 1).coerceAtLeast(0))
                latestOnChange(index)
            }
    }

    // Follow a selection the caller changed — a day clamped when the month got shorter.
    LaunchedEffect(selectedIndex, items.size) {
        snapshotFlow { state.isScrollInProgress }.filter { !it }.distinctUntilChanged().collect {
            val wanted = selectedIndex.coerceIn(0, (items.size - 1).coerceAtLeast(0))
            if (state.centredIndex(itemHeightPx) != wanted) state.scrollToItem(wanted)
        }
    }

    val padding = itemHeight * (visibleItems / 2)
    val font = organicBodyFontFamily()
    LazyColumn(
        state = state,
        modifier = modifier.height(itemHeight * visibleItems),
        contentPadding = PaddingValues(vertical = padding),
        flingBehavior = rememberSnapFlingBehavior(lazyListState = state),
    ) {
        items(items.size) { index ->
            val distance = abs(index - centred)
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(itemHeight)
                    .clickable(role = Role.Button) { scope.launch { state.animateScrollToItem(index) } }
                    .padding(horizontal = 6.dp),
                contentAlignment = alignment,
            ) {
                Text(
                    text = items[index],
                    fontFamily = font,
                    fontWeight = if (distance == 0) FontWeight.Bold else FontWeight.Normal,
                    fontSize = if (distance == 0) 17.sp else 15.sp,
                    maxLines = 1,
                    color = when (distance) {
                        0 -> OrganicColors.text
                        1 -> OrganicColors.neutral700
                        else -> OrganicColors.neutral500
                    },
                )
            }
        }
    }
}

object OrganicWheelDefaults {
    val ItemHeight: Dp = 38.dp
    const val VisibleItems: Int = 5
}

/** With the list padded by half its height, the first visible row is the one in the middle. */
private fun LazyListState.centredIndex(itemHeightPx: Float): Int =
    firstVisibleItemIndex + if (firstVisibleItemScrollOffset > itemHeightPx / 2) 1 else 0
