package com.example.dz.designsystem.components.goal_picker

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.dz.designsystem.theme.DZTheme
import dz.shared.generated.resources.Res
import dz.shared.generated.resources.done
import dz.shared.generated.resources.goal_picker_unit
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.stringResource
import kotlin.math.abs

private data class ReadingGoalPickerMetrics(
    val sheetHeight: Dp,
    val sheetCorner: Dp,
    val sheetHorizontalPadding: Dp,
    val handleTopSpacing: Dp,
    val handleWidth: Dp,
    val handleHeight: Dp,
    val pickerTopSpacing: Dp,
    val optionHeight: Dp,
    val optionTextSize: TextUnit,
    val selectedTextSize: TextUnit,
    val selectedPillHeight: Dp,
    val selectedPillCorner: Dp,
    val unitEndPadding: Dp,
    val doneTopSpacing: Dp,
    val doneHeight: Dp,
    val doneCorner: Dp,
    val doneTextSize: TextUnit,
    val bottomSpacing: Dp
)

@Composable
fun ReadingGoalPickerBottomSheet(
    modifier: Modifier = Modifier,
    selectedMinutes: Int = 40,
    options: List<Int> = (5..120 step 5).toList(),
    onMinuteClick: (Int) -> Unit = {},
    onDoneClick: () -> Unit = {},
    onDismissRequest: () -> Unit = {}
) {
    BoxWithConstraints(modifier = modifier.fillMaxSize()) {
        val metrics = rememberReadingGoalPickerMetrics(maxWidth = maxWidth, maxHeight = maxHeight)
        val colorScheme = MaterialTheme.colorScheme
        val minuteOptions = remember(options, selectedMinutes) {
            options.ifEmpty { listOf(selectedMinutes) }
        }
        val visibleOptionCount = remember(minuteOptions) {
            minuteOptions.size.coerceIn(1, 6)
        }
        val preferredSelectedSlot = minOf(3, visibleOptionCount - 1)
        val selectedIndex = remember(minuteOptions, selectedMinutes) {
            minuteOptions.indexOf(selectedMinutes).takeIf { it >= 0 } ?: 0
        }
        val maxFirstIndex = remember(minuteOptions, visibleOptionCount) {
            (minuteOptions.size - visibleOptionCount).coerceAtLeast(0)
        }
        val initialFirstIndex = remember(selectedIndex, preferredSelectedSlot, maxFirstIndex) {
            (selectedIndex - preferredSelectedSlot).coerceIn(0, maxFirstIndex)
        }
        val selectedSlot = remember(selectedIndex, initialFirstIndex, visibleOptionCount) {
            (selectedIndex - initialFirstIndex).coerceIn(0, visibleOptionCount - 1)
        }
        val pickerHeight = metrics.optionHeight * visibleOptionCount.toFloat()
        val listState = rememberLazyListState(initialFirstVisibleItemIndex = initialFirstIndex)
        val scope = rememberCoroutineScope()
        val sheetInteractionSource = remember { MutableInteractionSource() }
        val centeredIndex by remember(listState, selectedIndex, selectedSlot) {
            derivedStateOf {
                val layoutInfo = listState.layoutInfo
                val itemSize = layoutInfo.visibleItemsInfo.firstOrNull()?.size ?: 0
                val selectedCenter = layoutInfo.viewportStartOffset + ((selectedSlot + 0.5f) * itemSize).toInt()

                layoutInfo.visibleItemsInfo
                    .minByOrNull { item -> abs((item.offset + item.size / 2) - selectedCenter) }
                    ?.index
                    ?: selectedIndex
            }
        }
        val centeredMinutes = minuteOptions.getOrNull(centeredIndex) ?: selectedMinutes

        LaunchedEffect(initialFirstIndex) {
            if (!listState.isScrollInProgress && listState.firstVisibleItemIndex != initialFirstIndex) {
                listState.scrollToItem(initialFirstIndex)
            }
        }

        LaunchedEffect(centeredMinutes) {
            if (centeredMinutes != selectedMinutes) {
                onMinuteClick(centeredMinutes)
            }
        }

        LaunchedEffect(listState.isScrollInProgress) {
            if (!listState.isScrollInProgress && centeredIndex in minuteOptions.indices) {
                val targetFirstIndex = (centeredIndex - selectedSlot).coerceIn(0, maxFirstIndex)
                listState.animateScrollToItem(targetFirstIndex)
            }
        }

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black.copy(alpha = 0.45f))
                .clickable(onClick = onDismissRequest)
        )

        Column(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .height(metrics.sheetHeight)
                .clip(
                    RoundedCornerShape(
                        topStart = metrics.sheetCorner,
                        topEnd = metrics.sheetCorner
                    )
                )
                .background(colorScheme.surface)
                .clickable(
                    interactionSource = sheetInteractionSource,
                    indication = null,
                    onClick = {}
                )
                .navigationBarsPadding()
                .padding(horizontal = metrics.sheetHorizontalPadding),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(metrics.handleTopSpacing))

            Box(
                modifier = Modifier
                    .width(metrics.handleWidth)
                    .height(metrics.handleHeight)
                    .clip(CircleShape)
                    .background(colorScheme.outline.copy(alpha = 0.36f))
            )

            Spacer(modifier = Modifier.height(metrics.pickerTopSpacing))

            ReadingGoalMinuteWheel(
                selectedSlot = selectedSlot,
                selectedMinutes = centeredMinutes,
                centeredIndex = centeredIndex,
                metrics = metrics,
                onMinuteClick = { index ->
                    scope.launch {
                        val targetFirstIndex = (index - selectedSlot).coerceIn(0, maxFirstIndex)
                        listState.animateScrollToItem(targetFirstIndex)
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(pickerHeight),
                listContent = {
                    LazyColumn(
                        state = listState,
                        modifier = Modifier.fillMaxSize()
                    ) {
                        itemsIndexed(minuteOptions) { index, minutes ->
                            ReadingGoalPickerOption(
                                minutes = minutes,
                                selected = index == centeredIndex,
                                selectedMinutes = centeredMinutes,
                                metrics = metrics,
                                onClick = {
                                    scope.launch {
                                        val targetFirstIndex = (index - selectedSlot).coerceIn(0, maxFirstIndex)
                                        listState.animateScrollToItem(targetFirstIndex)
                                    }
                                }
                            )
                        }
                    }
                }
            )

            Spacer(modifier = Modifier.height(metrics.doneTopSpacing))

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(metrics.doneHeight)
                    .clip(RoundedCornerShape(metrics.doneCorner))
                    .background(colorScheme.primary)
                    .clickable(onClick = onDoneClick),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = stringResource(Res.string.done),
                    color = colorScheme.onPrimary,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    style = MaterialTheme.typography.headlineMedium.copy(
                        fontSize = metrics.doneTextSize,
                        lineHeight = metrics.doneTextSize * 1.18f,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 0.sp
                    )
                )
            }

            Spacer(modifier = Modifier.height(metrics.bottomSpacing))
        }
    }
}

@Composable
private fun ReadingGoalMinuteWheel(
    selectedSlot: Int,
    selectedMinutes: Int,
    centeredIndex: Int,
    metrics: ReadingGoalPickerMetrics,
    onMinuteClick: (Int) -> Unit,
    modifier: Modifier = Modifier,
    listContent: @Composable () -> Unit
) {
    val colorScheme = MaterialTheme.colorScheme

    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center
    ) {
        Box(
            modifier = Modifier
                .align(Alignment.TopCenter)
                .padding(top = metrics.optionHeight * selectedSlot.toFloat() + (metrics.optionHeight - metrics.selectedPillHeight) * 0.5f)
                .fillMaxWidth()
                .height(metrics.selectedPillHeight)
                .clip(RoundedCornerShape(metrics.selectedPillCorner))
                .background(colorScheme.outline.copy(alpha = 0.72f))
                .clickable { onMinuteClick(centeredIndex) }
        )

        listContent()

        Box(
            modifier = Modifier
                .align(Alignment.TopCenter)
                .padding(top = metrics.optionHeight * selectedSlot.toFloat() + (metrics.optionHeight - metrics.selectedPillHeight) * 0.5f)
                .fillMaxWidth()
                .height(metrics.selectedPillHeight),
            contentAlignment = Alignment.CenterEnd
        ) {
            Text(
                text = stringResource(Res.string.goal_picker_unit),
                modifier = Modifier.padding(end = metrics.unitEndPadding),
                color = colorScheme.onSurface.copy(alpha = 0.66f),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                style = MaterialTheme.typography.headlineMedium.copy(
                    fontSize = metrics.optionTextSize,
                    lineHeight = metrics.optionTextSize * 1.1f,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 0.sp
                )
            )
        }
    }
}

@Composable
private fun ReadingGoalPickerOption(
    minutes: Int,
    selected: Boolean,
    selectedMinutes: Int,
    metrics: ReadingGoalPickerMetrics,
    onClick: () -> Unit
) {
    val colorScheme = MaterialTheme.colorScheme
    val textAlpha = if (selected) 0.66f else if (minutes < selectedMinutes) 0.18f else 0.28f
    val textStyle = if (selected) MaterialTheme.typography.displayLarge else MaterialTheme.typography.headlineMedium
    val textSize = if (selected) metrics.selectedTextSize else metrics.optionTextSize
    val lineHeight = if (selected) metrics.selectedTextSize * 1.1f else metrics.optionTextSize * 1.12f

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(metrics.optionHeight)
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = minutes.toString(),
            modifier = Modifier.fillMaxWidth(),
            color = colorScheme.onSurface.copy(alpha = textAlpha),
            textAlign = TextAlign.Center,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            style = textStyle.copy(
                fontSize = textSize,
                lineHeight = lineHeight,
                fontWeight = FontWeight.Bold,
                letterSpacing = 0.sp
            )
        )
    }
}

@Composable
private fun rememberReadingGoalPickerMetrics(
    maxWidth: Dp,
    maxHeight: Dp
): ReadingGoalPickerMetrics {
    return remember(maxWidth, maxHeight) {
        val optionTextSize = (maxWidth.value * 0.043f).coerceIn(18f, 22f).sp
        val selectedTextSize = (maxWidth.value * 0.056f).coerceIn(26f, 32f).sp

        ReadingGoalPickerMetrics(
            sheetHeight = (maxHeight * 0.36f).coerceIn(310.dp, 380.dp),
            sheetCorner = (maxWidth * 0.105f).coerceIn(38.dp, 56.dp),
            sheetHorizontalPadding = (maxWidth * 0.075f).coerceIn(28.dp, 44.dp),
            handleTopSpacing = (maxHeight * 0.011f).coerceIn(8.dp, 12.dp),
            handleWidth = (maxWidth * 0.102f).coerceIn(34.dp, 48.dp),
            handleHeight = 6.dp,
            pickerTopSpacing = (maxHeight * 0.014f).coerceIn(10.dp, 16.dp),
            optionHeight = (maxHeight * 0.034f).coerceIn(26.dp, 34.dp),
            optionTextSize = optionTextSize,
            selectedTextSize = selectedTextSize,
            selectedPillHeight = (maxHeight * 0.045f).coerceIn(38.dp, 48.dp),
            selectedPillCorner = (maxWidth * 0.052f).coerceIn(22.dp, 30.dp),
            unitEndPadding = (maxWidth * 0.075f).coerceIn(28.dp, 48.dp),
            doneTopSpacing = (maxHeight * 0.038f).coerceIn(28.dp, 42.dp),
            doneHeight = (maxHeight * 0.079f).coerceIn(66.dp, 82.dp),
            doneCorner = (maxWidth * 0.052f).coerceIn(22.dp, 30.dp),
            doneTextSize = (maxWidth.value * 0.042f).coerceIn(18f, 22f).sp,
            bottomSpacing = (maxHeight * 0.018f).coerceIn(14.dp, 24.dp)
        )
    }
}

@Preview(showBackground = true, widthDp = 390, heightDp = 844)
@Composable
private fun ReadingGoalPickerBottomSheetPreview() {
    DZTheme(darkTheme = false) {
        ReadingGoalPickerBottomSheet()
    }
}
