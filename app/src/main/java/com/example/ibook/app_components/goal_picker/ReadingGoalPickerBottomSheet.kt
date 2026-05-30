package com.example.ibook.app_components.goal_picker

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ibook.R
import com.example.ibook.ui.theme.IBookTheme

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
    options: List<Int> = listOf(25, 30, 35, 40, 45, 50),
    onMinuteClick: (Int) -> Unit = {},
    onDoneClick: () -> Unit = {},
    onDismissRequest: () -> Unit = {}
) {
    BoxWithConstraints(modifier = modifier.fillMaxSize()) {
        val metrics = rememberReadingGoalPickerMetrics(maxWidth = maxWidth, maxHeight = maxHeight)
        val colorScheme = MaterialTheme.colorScheme

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

            options.forEach { minutes ->
                ReadingGoalPickerOption(
                    minutes = minutes,
                    selected = minutes == selectedMinutes,
                    metrics = metrics,
                    onClick = { onMinuteClick(minutes) }
                )
            }

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
                    text = stringResource(R.string.done),
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
private fun ReadingGoalPickerOption(
    minutes: Int,
    selected: Boolean,
    metrics: ReadingGoalPickerMetrics,
    onClick: () -> Unit
) {
    val colorScheme = MaterialTheme.colorScheme

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(metrics.optionHeight),
        contentAlignment = Alignment.Center
    ) {
        if (selected) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(metrics.selectedPillHeight)
                    .clip(RoundedCornerShape(metrics.selectedPillCorner))
                    .background(colorScheme.outline.copy(alpha = 0.72f))
                    .clickable(onClick = onClick)
                    .padding(horizontal = metrics.sheetHorizontalPadding * 0.5f),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = minutes.toString(),
                    modifier = Modifier.weight(1f),
                    color = colorScheme.onSurface.copy(alpha = 0.66f),
                    textAlign = TextAlign.Center,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    style = MaterialTheme.typography.displayLarge.copy(
                        fontSize = metrics.selectedTextSize,
                        lineHeight = metrics.selectedTextSize * 1.1f,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 0.sp
                    )
                )

                Text(
                    text = stringResource(R.string.goal_picker_unit),
                    modifier = Modifier.weight(1f),
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
        } else {
            Text(
                text = minutes.toString(),
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable(onClick = onClick),
                color = colorScheme.onSurface.copy(alpha = if (minutes < 40) 0.18f else 0.28f),
                textAlign = TextAlign.Center,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                style = MaterialTheme.typography.headlineMedium.copy(
                    fontSize = metrics.optionTextSize,
                    lineHeight = metrics.optionTextSize * 1.12f,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 0.sp
                )
            )
        }
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
            doneTopSpacing = (maxHeight * 0.038f).coerceIn(28.dp, 42.dp),
            doneHeight = (maxHeight * 0.079f).coerceIn(66.dp, 82.dp),
            doneCorner = (maxWidth * 0.052f).coerceIn(22.dp, 30.dp),
            doneTextSize = (maxWidth.value * 0.042f).coerceIn(18f, 22f).sp,
            bottomSpacing = (maxHeight * 0.018f).coerceIn(14.dp, 24.dp)
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun ReadingGoalPickerBottomSheetPreview() {
    IBookTheme {
        ReadingGoalPickerBottomSheet()
    }
}
