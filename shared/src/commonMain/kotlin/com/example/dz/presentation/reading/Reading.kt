package com.example.dz.presentation.reading

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.dz.core.platform.StatusBarAppearance
import com.example.dz.designsystem.components.icons.OrganicIcons
import com.example.dz.designsystem.components.organic.OrganicCard
import com.example.dz.designsystem.components.organic.OrganicPageColors
import com.example.dz.designsystem.components.organic.OrganicPrimaryButton
import com.example.dz.designsystem.components.organic.OrganicSkeleton
import com.example.dz.designsystem.components.organic.OrganicSuccessOverlay
import com.example.dz.designsystem.theme.OrganicColors
import com.example.dz.designsystem.theme.OrganicShape
import com.example.dz.designsystem.theme.organicBodyFontFamily
import com.example.dz.designsystem.theme.organicSerifFontFamily
import com.example.dz.domain.model.PageTheme
import dz.shared.generated.resources.Res
import dz.shared.generated.resources.book_download
import dz.shared.generated.resources.book_downloaded
import dz.shared.generated.resources.download_completed_coffee_message
import dz.shared.generated.resources.download_completed_title
import dz.shared.generated.resources.nav_back
import dz.shared.generated.resources.popup_delete_download
import dz.shared.generated.resources.popup_delete_download_message
import dz.shared.generated.resources.popup_keep_download
import dz.shared.generated.resources.reader_bookmark
import dz.shared.generated.resources.reader_bookmarked
import dz.shared.generated.resources.reader_comments
import dz.shared.generated.resources.reader_display_options
import dz.shared.generated.resources.reader_page_of
import dz.shared.generated.resources.reader_try_again
import kotlinx.coroutines.delay
import org.jetbrains.compose.resources.stringResource

/**
 * The reader.
 *
 * Everything here serves one thing: the page. The chrome fades away on a tap so the text is all
 * that is left, and the reader keeps their place in the words while it goes — the column grows into
 * the space the chrome leaves without re-wrapping a line, so nothing on the page is anywhere new
 * except further up the screen.
 *
 * Geometry from `dz-all-screens.html`.
 */
@Composable
fun ReadingScreen(
    uiState: ReadingUiState = ReadingUiState(),
    onEvent: (ReadingEvent) -> Unit = {},
    modifier: Modifier = Modifier,
) {
    val page = uiState.preferences.pageTheme.colors()
    val chromeFade = tween<Float>(CHROME_FADE_MILLIS)

    // The clock and battery are the system's to draw; on the night ground they have to be told to
    // come out light, or they stay dark ink on a dark page.
    StatusBarAppearance(darkBackground = uiState.preferences.pageTheme == PageTheme.NIGHT)

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(page.ground)
    ) {
        Column(modifier = Modifier.fillMaxSize().statusBarsPadding()) {
            AnimatedVisibility(
                visible = uiState.chromeVisible,
                enter = fadeIn(chromeFade),
                exit = fadeOut(chromeFade),
            ) {
                ReaderHeader(uiState = uiState, page = page, onEvent = onEvent)
            }

            Box(modifier = Modifier.weight(1f)) {
                when {
                    uiState.isLoading -> PageSkeleton()
                    uiState.errorMessage != null -> PageError(
                        message = uiState.errorMessage,
                        page = page,
                        onRetry = { onEvent(ReadingEvent.RetryClicked) },
                    )

                    else -> PageBody(uiState = uiState, page = page, onEvent = onEvent)
                }
            }

            // Nothing to be a fraction of, and nothing to save offline, until the text has landed.
            AnimatedVisibility(
                visible = uiState.chromeVisible && uiState.pages.isNotEmpty(),
                enter = fadeIn(chromeFade),
                exit = fadeOut(chromeFade),
            ) {
                Column {
                    ProgressRow(uiState = uiState, page = page, onEvent = onEvent)
                    ReaderActions(uiState = uiState, page = page, onEvent = onEvent)
                }
            }
        }

        if (uiState.showDisplaySheet) {
            DisplaySheet(
                uiState = uiState,
                onEvent = onEvent,
                modifier = Modifier.align(Alignment.BottomCenter),
            )
        }

        DownloadOverlays(uiState = uiState, onEvent = onEvent)
    }
}

/** Back, the book's name, and the pin for the page you are on. */
@Composable
private fun ReaderHeader(
    uiState: ReadingUiState,
    page: OrganicPageColors,
    onEvent: (ReadingEvent) -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 24.dp, end = 24.dp, top = 12.dp),
        horizontalArrangement = Arrangement.spacedBy(14.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        ReaderCircleButton(
            icon = OrganicIcons.ChevronLeft,
            contentDescription = stringResource(Res.string.nav_back),
            background = page.chrome,
            tint = page.chromeInk,
            onClick = { onEvent(ReadingEvent.BackClicked) },
        )
        Text(
            text = uiState.bookTitle,
            modifier = Modifier.weight(1f),
            fontFamily = organicBodyFontFamily(),
            fontSize = 12.sp,
            color = page.ink.copy(alpha = 0.7f),
            maxLines = 1,
            textAlign = TextAlign.Center
        )
        // The ribbon fills in once the page is pinned, so the button says which state it is in
        // rather than only what tapping it would do. There is nothing to pin until the text has
        // landed, so until then the space is only held, not filled.
        if (uiState.pages.isEmpty()) {
            Spacer(modifier = Modifier.size(38.dp))
        } else {
            ReaderCircleButton(
                icon = if (uiState.bookmarked) OrganicIcons.BookmarkFilled else OrganicIcons.Bookmark,
                contentDescription = stringResource(
                    if (uiState.bookmarked) Res.string.reader_bookmarked else Res.string.reader_bookmark
                ),
                background = if (uiState.bookmarked) OrganicColors.accent200 else page.chrome,
                tint = if (uiState.bookmarked) OrganicColors.accent800 else page.chromeInk,
                onClick = { onEvent(ReadingEvent.BookmarkToggled) },
            )
        }
    }
}

/**
 * The page.
 *
 * A page is a fixed number of characters, which at reading size is taller than any phone — so the
 * page scrolls, and the taps have to share the same surface as the scroll. One tap detector on the
 * scrolling column does that: a drag belongs to the scroll, which consumes it, and only a real tap
 * ever reaches here. The sides then turn pages and the middle shows or hides the chrome, which is
 * the model the handoff describes when it says a tap on the *centre* toggles it.
 *
 * No zone gives a ripple: a flash of ink across the words is worse than no feedback at all when
 * the response is the whole page changing.
 */
@Composable
private fun PageBody(
    uiState: ReadingUiState,
    page: OrganicPageColors,
    onEvent: (ReadingEvent) -> Unit,
) {
    val face = if (uiState.preferences.useSerif) organicSerifFontFamily() else organicBodyFontFamily()
    val bodySize = uiState.preferences.bodySizeSp.sp
    val scroll = rememberScrollState()

    // A new page starts at its top. Carrying the old offset across drops the reader into the
    // middle of a page they have not read a word of.
    LaunchedEffect(uiState.currentPage) { scroll.scrollTo(0) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .pointerInput(uiState.canGoToPreviousPage, uiState.canGoToNextPage) {
                detectTapGestures { tap ->
                    val edge = size.width * PAGE_TURN_ZONE
                    when {
                        tap.x < edge && uiState.canGoToPreviousPage ->
                            onEvent(ReadingEvent.PreviousPageClicked)

                        tap.x > size.width - edge && uiState.canGoToNextPage ->
                            onEvent(ReadingEvent.NextPageClicked)

                        else -> onEvent(ReadingEvent.PageTapped)
                    }
                }
            }
            .verticalScroll(scroll)
            .padding(horizontal = 28.dp)
            .padding(top = 26.dp, bottom = 24.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        uiState.currentPageParagraphs.forEach { paragraph ->
            Text(
                text = paragraph,
                fontFamily = face,
                fontSize = bodySize,
                lineHeight = bodySize * LINE_HEIGHT_RATIO,
                color = page.ink
            )
        }
    }
}

/** The book's text could not be fetched — said on the page's own ground, with a way back in. */
@Composable
private fun PageError(
    message: String,
    page: OrganicPageColors,
    onRetry: () -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 36.dp),
        verticalArrangement = Arrangement.spacedBy(22.dp, Alignment.CenterVertically),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = message,
            fontFamily = organicBodyFontFamily(),
            fontSize = 15.sp,
            lineHeight = 22.sp,
            color = page.ink.copy(alpha = 0.8f),
            textAlign = TextAlign.Center
        )
        OrganicPrimaryButton(
            text = stringResource(Res.string.reader_try_again),
            onClick = onRetry,
            fullWidth = false,
        )
    }
}

/**
 * How far through, as a bar and as a count.
 *
 * The handoff calls the bar a scrubber, so it is one: dragging it moves through the book, and the
 * pinned page sits on it as a tick, which is what keeps a bookmark from being somewhere you can
 * only put things and never go.
 */
@Composable
private fun ProgressRow(
    uiState: ReadingUiState,
    page: OrganicPageColors,
    onEvent: (ReadingEvent) -> Unit,
) {
    val bookmarkAt = uiState.bookmarkProgress

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 28.dp)
            .padding(top = 18.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .weight(1f)
                // The bar draws at 8dp; the target around it is finger-sized.
                .height(SCRUB_TARGET_HEIGHT)
                .pointerInput(uiState.totalPages) {
                    detectTapGestures { tap ->
                        onEvent(ReadingEvent.ProgressScrubbed(tap.x / size.width))
                        onEvent(ReadingEvent.ProgressScrubFinished)
                    }
                }
                .pointerInput(uiState.totalPages) {
                    detectHorizontalDragGestures(
                        onDragEnd = { onEvent(ReadingEvent.ProgressScrubFinished) },
                        onDragCancel = { onEvent(ReadingEvent.ProgressScrubFinished) },
                    ) { change, _ ->
                        onEvent(ReadingEvent.ProgressScrubbed(change.position.x / size.width))
                    }
                },
            contentAlignment = Alignment.Center
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(8.dp)
                    .clip(RoundedCornerShape(OrganicShape.pill))
                    .background(page.chrome)
                    .drawBehind {
                        drawRect(
                            color = OrganicColors.accent,
                            size = Size(size.width * uiState.progress, size.height),
                        )
                        bookmarkAt?.let { at -> drawBookmarkTick(at) }
                    }
            )
        }
        Text(
            text = stringResource(Res.string.reader_page_of, uiState.currentPage, uiState.totalPages),
            fontFamily = organicBodyFontFamily(),
            fontSize = 12.sp,
            color = page.ink.copy(alpha = 0.7f)
        )
    }
}

/** The pinned page, marked on the bar — dark enough to be seen on the fill and on the track. */
private fun androidx.compose.ui.graphics.drawscope.DrawScope.drawBookmarkTick(at: Float) {
    val width = 3.dp.toPx()
    val x = (size.width * at - width / 2f).coerceIn(0f, size.width - width)
    drawRoundRect(
        color = OrganicColors.accent800,
        topLeft = Offset(x, 0f),
        size = Size(width, size.height),
        cornerRadius = CornerRadius(width / 2f, width / 2f),
    )
}

/**
 * The reader's own tools: type, notes, and keeping the book for offline.
 *
 * The design puts a "Listen" pill here and a magnifier beside the other two. There is no audio
 * anywhere in the app — no narration, no player, no field on a book that says one exists — and
 * nothing searches inside a book's text, so neither is drawn as a control that cannot do anything.
 * The download in their place is real, and says which of its three states it is in.
 */
@Composable
private fun ReaderActions(
    uiState: ReadingUiState,
    page: OrganicPageColors,
    onEvent: (ReadingEvent) -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .navigationBarsPadding()
            .padding(horizontal = 28.dp)
            .padding(top = 18.dp, bottom = 30.dp),
        horizontalArrangement = Arrangement.spacedBy(10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        ReaderCircleButton(
            icon = OrganicIcons.TextSize,
            contentDescription = stringResource(Res.string.reader_display_options),
            background = page.chrome,
            tint = page.chromeInk,
            size = ACTION_SIZE,
            onClick = { onEvent(ReadingEvent.MenuClicked) },
        )
        ReaderCircleButton(
            icon = OrganicIcons.Chat,
            contentDescription = stringResource(Res.string.reader_comments),
            background = page.chrome,
            tint = page.chromeInk,
            size = ACTION_SIZE,
            onClick = { onEvent(ReadingEvent.CommentsClicked) },
        )
        DownloadAction(uiState = uiState, page = page, onEvent = onEvent)
    }
}

/** Save the book for offline, then say it is saved, then offer to let it go. */
@Composable
private fun DownloadAction(
    uiState: ReadingUiState,
    page: OrganicPageColors,
    onEvent: (ReadingEvent) -> Unit,
) {
    val downloaded = uiState.isDownloaded
    Box(
        modifier = Modifier
            .size(ACTION_SIZE)
            .clip(CircleShape)
            .background(if (downloaded) OrganicColors.accent2_200 else page.chrome)
            .clickable(
                enabled = !uiState.isDownloading,
                role = Role.Button,
            ) {
                onEvent(
                    if (downloaded) ReadingEvent.DeleteDownloadClicked
                    else ReadingEvent.DownloadClicked
                )
            },
        contentAlignment = Alignment.Center
    ) {
        if (uiState.isDownloading) {
            CircularProgressIndicator(
                modifier = Modifier.size(19.dp),
                color = page.chromeInk,
                strokeWidth = 2.dp
            )
        } else {
            Icon(
                imageVector = if (downloaded) OrganicIcons.Check else OrganicIcons.Download,
                contentDescription = stringResource(
                    if (downloaded) Res.string.book_downloaded else Res.string.book_download
                ),
                tint = if (downloaded) OrganicColors.accent2_900 else page.chromeInk,
                modifier = Modifier.size(ACTION_SIZE * 0.41f)
            )
        }
    }
}

/** Everything the download has to say for itself, over the page it belongs to. */
@Composable
private fun DownloadOverlays(
    uiState: ReadingUiState,
    onEvent: (ReadingEvent) -> Unit,
) {
    if (uiState.showDownloadSuccess) {
        // The overlay swallows taps on purpose, so it has to take itself away again.
        LaunchedEffect(Unit) {
            delay(SUCCESS_DWELL_MILLIS)
            onEvent(ReadingEvent.DownloadSuccessDismissed)
        }
        OrganicSuccessOverlay(
            title = stringResource(Res.string.download_completed_title),
            body = stringResource(Res.string.download_completed_coffee_message),
        )
    }

    if (uiState.showDeleteDownloadDialog) {
        ConfirmDeleteDownload(
            onKeep = { onEvent(ReadingEvent.DismissDeleteDownloadDialog) },
            onDelete = { onEvent(ReadingEvent.ConfirmDeleteDownload) },
        )
    }

    uiState.downloadErrorMessage?.let { message ->
        LaunchedEffect(message) {
            delay(ERROR_DWELL_MILLIS)
            onEvent(ReadingEvent.DownloadErrorDismissed)
        }
        Box(
            modifier = Modifier
                .fillMaxSize()
                .navigationBarsPadding()
                .padding(horizontal = 24.dp, vertical = 28.dp),
            contentAlignment = Alignment.BottomCenter
        ) {
            Text(
                text = message,
                modifier = Modifier
                    .clip(RoundedCornerShape(OrganicShape.pill))
                    .background(OrganicColors.neutral900)
                    .padding(horizontal = 20.dp, vertical = 14.dp),
                fontFamily = organicBodyFontFamily(),
                fontSize = 13.sp,
                color = OrganicColors.neutral100,
                textAlign = TextAlign.Center
            )
        }
    }
}

/**
 * Losing a download is worth a question, because getting it back costs the whole book again.
 *
 * Keeping it is the filled answer: the destructive one should never be the easiest thing to hit.
 */
@Composable
private fun ConfirmDeleteDownload(
    onKeep: () -> Unit,
    onDelete: () -> Unit,
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            // Same scrim as every other thing that covers a screen in this system.
            .background(OrganicColors.neutral900.copy(alpha = 0.42f))
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null,
                onClick = onKeep,
            )
            .padding(horizontal = 28.dp),
        contentAlignment = Alignment.Center
    ) {
        OrganicCard(
            modifier = Modifier
                .fillMaxWidth()
                // A tap inside the card is not a tap outside it — swallowed here rather than
                // given to the card, which would ripple for something that does nothing.
                .clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = null,
                ) {},
            background = OrganicColors.bg,
            cornerRadius = OrganicShape.radiusLg,
            elevated = true,
            contentPadding = PaddingValues(24.dp),
        ) {
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(18.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = stringResource(Res.string.popup_delete_download_message),
                    fontFamily = organicBodyFontFamily(),
                    fontSize = 15.sp,
                    lineHeight = 22.sp,
                    color = OrganicColors.text,
                    textAlign = TextAlign.Center
                )
                OrganicPrimaryButton(
                    text = stringResource(Res.string.popup_keep_download),
                    onClick = onKeep,
                )
                Text(
                    text = stringResource(Res.string.popup_delete_download),
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(OrganicShape.pill))
                        .clickable(role = Role.Button, onClick = onDelete)
                        .padding(vertical = 10.dp),
                    fontFamily = organicBodyFontFamily(),
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 15.sp,
                    color = OrganicColors.danger,
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}

@Composable
private fun ReaderCircleButton(
    icon: ImageVector,
    contentDescription: String,
    background: Color,
    tint: Color,
    onClick: () -> Unit,
    size: Dp = 38.dp,
) {
    Box(
        modifier = Modifier
            .size(size)
            .clip(CircleShape)
            .background(background)
            .clickable(role = Role.Button, onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            imageVector = icon,
            contentDescription = contentDescription,
            tint = tint,
            modifier = Modifier.size(size * ICON_FRACTION)
        )
    }
}

/** A page's worth of grey lines while the book's text is still being fetched. */
@Composable
private fun PageSkeleton() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 28.dp)
            .padding(top = 26.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        repeat(12) { line ->
            OrganicSkeleton(
                modifier = Modifier
                    .fillMaxWidth(if (line % 4 == 3) 0.6f else 1f)
                    .height(14.dp),
                cornerRadius = OrganicShape.radiusSm,
            )
        }
    }
}

/** Line height 1.8 at every size — the design's setting, and what makes a long page readable. */
private const val LINE_HEIGHT_RATIO = 1.8f
private const val CHROME_FADE_MILLIS = 180

/** How much of each edge turns a page; the rest of the width belongs to the chrome toggle. */
private const val PAGE_TURN_ZONE = 0.28f

private val ACTION_SIZE = 46.dp

/** How much of a round button its glyph fills, at every size the reader draws one. */
private const val ICON_FRACTION = 0.45f
private val SCRUB_TARGET_HEIGHT = 24.dp

private const val SUCCESS_DWELL_MILLIS = 1_800L
private const val ERROR_DWELL_MILLIS = 3_500L

/** The four grounds the display sheet offers. */
@Composable
private fun PageTheme.colors(): OrganicPageColors = when (this) {
    PageTheme.CREAM -> OrganicPageColors(
        ground = OrganicColors.bg,
        ink = OrganicColors.neutral900,
        chrome = OrganicColors.neutral200,
        chromeInk = OrganicColors.neutral800,
    )

    PageTheme.PAPER -> OrganicPageColors(
        ground = OrganicColors.neutral100,
        ink = OrganicColors.neutral900,
        chrome = OrganicColors.neutral200,
        chromeInk = OrganicColors.neutral800,
    )

    PageTheme.SAGE -> OrganicPageColors(
        ground = OrganicColors.accent2_200,
        ink = OrganicColors.accent2_900,
        chrome = OrganicColors.accent2_300,
        chromeInk = OrganicColors.accent2_900,
    )

    // The one inversion in the app: at night the page is the dark thing and the words are light.
    PageTheme.NIGHT -> OrganicPageColors(
        ground = OrganicColors.neutral900,
        ink = OrganicColors.neutral200,
        chrome = OrganicColors.neutral800,
        chromeInk = OrganicColors.neutral200,
    )
}

@Preview
@Composable
fun ReadingScreenPreview() {
    ReadingScreen()
}
