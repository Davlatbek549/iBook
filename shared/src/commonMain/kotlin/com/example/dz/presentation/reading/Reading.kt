package com.example.dz.presentation.reading

import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.dz.designsystem.components.downloading.DownloadingPopup
import com.example.dz.designsystem.components.icons.InkIcons
import com.example.dz.designsystem.components.ink.InkButton
import com.example.dz.designsystem.components.ink.InkProgressBar
import com.example.dz.designsystem.components.popups.DeleteBooksPopup
import com.example.dz.designsystem.components.results.SuccessfulDownloadDone
import com.example.dz.designsystem.theme.inkBodyFontFamily
import com.example.dz.designsystem.theme.inkColors
import com.example.dz.designsystem.theme.inkDisplayFontFamily

@Composable
fun ReadingScreen(
    uiState: ReadingUiState = ReadingUiState(),
    onEvent: (ReadingEvent) -> Unit = {},
    modifier: Modifier = Modifier
) {
    val colors = inkColors()
    val displayFont = inkDisplayFontFamily()
    val bodyFont = inkBodyFontFamily()

    Box(modifier = modifier.fillMaxSize()) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(colors.paper)
            .statusBarsPadding()
            .navigationBarsPadding()
    ) {
        // top bar
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 22.dp, end = 22.dp, top = 4.dp, bottom = 10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = InkIcons.Back,
                contentDescription = null,
                tint = colors.muted,
                modifier = Modifier
                    .size(20.dp)
                    .clickable { onEvent(ReadingEvent.BackClicked) }
            )
            Text(
                text = uiState.bookTitle.uppercase(),
                modifier = Modifier.weight(1f),
                fontFamily = bodyFont,
                fontWeight = FontWeight.Medium,
                fontSize = 11.sp,
                letterSpacing = 0.5.sp,
                color = colors.muted,
                textAlign = TextAlign.Center
            )
            Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                Icon(
                    imageVector = if (uiState.isDownloaded) InkIcons.Done else InkIcons.Download,
                    contentDescription = null,
                    tint = if (uiState.isDownloaded) colors.accent else colors.muted,
                    modifier = Modifier
                        .size(19.dp)
                        .alpha(if (uiState.isDownloading) 0.4f else 1f)
                        .clickable(enabled = !uiState.isDownloading) {
                            if (uiState.isDownloaded) {
                                onEvent(ReadingEvent.DeleteDownloadClicked)
                            } else {
                                onEvent(ReadingEvent.DownloadClicked)
                            }
                        }
                )
                Icon(
                    imageVector = InkIcons.Settings,
                    contentDescription = null,
                    tint = colors.muted,
                    modifier = Modifier
                        .size(19.dp)
                        .clickable { onEvent(ReadingEvent.MenuClicked) }
                )
                Icon(
                    imageVector = InkIcons.Chat,
                    contentDescription = null,
                    tint = colors.muted,
                    modifier = Modifier
                        .size(18.dp)
                        .clickable { onEvent(ReadingEvent.CommentsClicked) }
                )
                Icon(
                    imageVector = InkIcons.Bookmark,
                    contentDescription = null,
                    tint = if (uiState.bookmarked) colors.accent else colors.muted,
                    modifier = Modifier
                        .size(18.dp)
                        .clickable { onEvent(ReadingEvent.BookmarkToggled) }
                )
            }
        }

        // page body
        when {
            uiState.isLoading -> Box(
                modifier = Modifier.weight(1f).fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(color = colors.accent)
            }

            uiState.errorMessage != null -> Box(
                modifier = Modifier.weight(1f).fillMaxWidth().padding(horizontal = 32.dp),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = uiState.errorMessage,
                        fontFamily = bodyFont,
                        fontSize = 14.sp,
                        color = colors.muted,
                        textAlign = TextAlign.Center
                    )
                    InkButton(
                        text = "Try Again",
                        onClick = { onEvent(ReadingEvent.RetryClicked) },
                        modifier = Modifier.padding(top = 20.dp),
                        colors = colors
                    )
                }
            }

            else -> Column(
                modifier = Modifier
                    .weight(1f)
                    .verticalScroll(rememberScrollState())
                    .padding(start = 26.dp, end = 26.dp, top = 14.dp)
            ) {
                Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                    uiState.currentPageParagraphs.forEach { paragraph ->
                        Text(
                            text = paragraph,
                            modifier = Modifier.alpha(0.92f),
                            fontFamily = displayFont,
                            fontSize = 16.5.sp,
                            lineHeight = 30.5.sp,
                            color = colors.ink
                        )
                    }
                }
            }
        }

        // bottom progress bar
        if (uiState.pages.isNotEmpty()) {
            Column {
                HorizontalDivider(thickness = 1.dp, color = colors.line)
                Column(modifier = Modifier.padding(start = 22.dp, end = 22.dp, top = 16.dp, bottom = 22.dp)) {
                    InkProgressBar(
                        progress = uiState.progress,
                        modifier = Modifier.fillMaxWidth(),
                        colors = colors
                    )
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = InkIcons.Back,
                            contentDescription = null,
                            tint = colors.muted,
                            modifier = Modifier
                                .size(18.dp)
                                .alpha(if (uiState.canGoToPreviousPage) 1f else 0.3f)
                                .clickable(enabled = uiState.canGoToPreviousPage) {
                                    onEvent(ReadingEvent.PreviousPageClicked)
                                }
                        )
                        Text(
                            text = buildAnnotatedString {
                                append("Page ${uiState.currentPage} of ${uiState.totalPages} · ")
                                withStyle(SpanStyle(color = colors.accent)) {
                                    append("${uiState.progressPercent}%")
                                }
                            },
                            modifier = Modifier.weight(1f),
                            fontFamily = bodyFont,
                            fontSize = 11.sp,
                            color = colors.muted,
                            textAlign = TextAlign.Center
                        )
                        Icon(
                            imageVector = InkIcons.Back,
                            contentDescription = null,
                            tint = colors.muted,
                            modifier = Modifier
                                .size(18.dp)
                                .graphicsLayer { rotationZ = 180f }
                                .alpha(if (uiState.canGoToNextPage) 1f else 0.3f)
                                .clickable(enabled = uiState.canGoToNextPage) {
                                    onEvent(ReadingEvent.NextPageClicked)
                                }
                        )
                    }
                }
            }
        }
    }

        ReadingDownloadOverlays(uiState = uiState, onEvent = onEvent)
    }
}

/**
 * Full-screen overlays for the offline-download flow: the in-progress [DownloadingPopup], the
 * [SuccessfulDownloadDone] confirmation, and the [DeleteBooksPopup] delete confirmation. Rendered
 * above the reader and driven entirely by [ReadingUiState].
 */
@Composable
private fun ReadingDownloadOverlays(
    uiState: ReadingUiState,
    onEvent: (ReadingEvent) -> Unit
) {
    // No byte-level progress from the download use case yet, so animate an indeterminate bar.
    val transition = rememberInfiniteTransition(label = "download-progress")
    val animatedProgress by transition.animateFloat(
        initialValue = 0.08f,
        targetValue = 0.92f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 1100),
            repeatMode = RepeatMode.Reverse
        ),
        label = "download-progress-value"
    )

    when {
        uiState.isDownloading -> DownloadingPopup(
            title = uiState.bookTitle,
            author = "",
            progress = animatedProgress
        )

        uiState.showDownloadSuccess -> Box(
            modifier = Modifier
                .fillMaxSize()
                .clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = null
                ) { onEvent(ReadingEvent.DownloadSuccessDismissed) }
        ) {
            SuccessfulDownloadDone()
        }
    }

    if (uiState.showDeleteDownloadDialog) {
        Box(modifier = Modifier.fillMaxSize()) {
            // Tapping outside the action buttons keeps the download (cancel).
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = null
                    ) { onEvent(ReadingEvent.DismissDeleteDownloadDialog) }
            )
            DeleteBooksPopup(
                onRemoveFromCollectionClick = { onEvent(ReadingEvent.DismissDeleteDownloadDialog) },
                onRemoveEverywhereClick = { onEvent(ReadingEvent.ConfirmDeleteDownload) }
            )
        }
    }
}

@Preview(showBackground = true, widthDp = 375, heightDp = 820)
@Composable
private fun ReadingScreenPreview() {
    ReadingScreen(
        uiState = ReadingUiState(
            bookTitle = "Mexican Gothic",
            pages = listOf(
                "The house appeared out of the mist like something half-remembered from a dream — tall, severe, and utterly silent. Noemí pressed her face to the carriage window and watched the iron gates draw closer.\n\n" +
                    "She had not wanted to come. The city, with its parties and its noise, was where she belonged. Yet the letter had been impossible to ignore.\n\n" +
                    "“We are nearly there,” the driver said, though his voice carried no comfort at all."
            ),
            currentPage = 198,
            totalPages = 320,
            isLoading = false
        )
    )
}

@Preview(showBackground = true, widthDp = 375, heightDp = 820)
@Composable
private fun ReadingScreenLoadingPreview() {
    ReadingScreen(uiState = ReadingUiState(isLoading = true))
}

@Preview(showBackground = true, widthDp = 375, heightDp = 820)
@Composable
private fun ReadingScreenErrorPreview() {
    ReadingScreen(
        uiState = ReadingUiState(
            isLoading = false,
            errorMessage = "Could not load data. Check your connection and try again."
        )
    )
}
