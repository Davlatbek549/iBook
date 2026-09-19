package com.example.dz.presentation.reading

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
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
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Constraints
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
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.drop
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.jetbrains.compose.resources.stringResource

/**
 * The reader.
 *
 * A page is exactly what fits: the book's text is measured against this screen at the size the
 * reader chose and cut where the last line that fits ends. Nothing scrolls. Pages are turned by
 * swiping, and the whole book is cut again whenever the type changes, so a page always fills the
 * screen and never spills off the bottom of it.
 *
 * The chrome keeps its space when it fades, rather than the page growing into it. If the page
 * grew, hiding the chrome would change how much text fits and re-cut the book underneath someone
 * mid-sentence.
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
    val chromeAlpha by animateFloatAsState(
        targetValue = if (uiState.chromeVisible) 1f else 0f,
        animationSpec = tween(CHROME_FADE_MILLIS),
        label = "reader-chrome",
    )

    // The clock and battery are the system's to draw; on the night ground they have to be told to
    // come out light, or they stay dark ink on a dark page.
    StatusBarAppearance(darkBackground = uiState.preferences.pageTheme == PageTheme.NIGHT)

    // Cut fresh for each book: a pagination belongs to one text at one size on one screen.
    var pagination by remember(uiState.text) { mutableStateOf(ReaderPagination.Empty) }
    val pagerState = rememberPagerState(pageCount = { pagination.pageCount })
    val pinnedHere = pagination.holds(uiState.bookmarkOffset, pagerState.currentPage)

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(page.ground)
    ) {
        Column(modifier = Modifier.fillMaxSize().statusBarsPadding()) {
            ReaderHeader(
                uiState = uiState,
                page = page,
                pinnedHere = pinnedHere,
                alpha = chromeAlpha,
                onEvent = onEvent,
            )

            Box(modifier = Modifier.weight(1f)) {
                when {
                    uiState.isLoading -> PageSkeleton()
                    uiState.errorMessage != null -> PageError(
                        message = uiState.errorMessage,
                        page = page,
                        onRetry = { onEvent(ReadingEvent.RetryClicked) },
                    )

                    else -> ReaderPages(
                        uiState = uiState,
                        page = page,
                        pagination = pagination,
                        pagerState = pagerState,
                        onPaginated = { pagination = it },
                        onEvent = onEvent,
                    )
                }
            }

            // Nothing to be a fraction of, and nothing to save offline, until the text has landed.
            if (uiState.hasText) {
                ProgressRow(
                    uiState = uiState,
                    page = page,
                    pagination = pagination,
                    pagerState = pagerState,
                    alpha = chromeAlpha,
                )
                ReaderActions(
                    uiState = uiState,
                    page = page,
                    alpha = chromeAlpha,
                    onEvent = onEvent,
                )
            }
        }

        // With the chrome gone the page is left on its own, and a book is never quite on its own:
        // it has a number at the foot of every page. It takes the chrome's place as that fades.
        if (uiState.hasText && pagination !== ReaderPagination.Empty) {
            Text(
                text = (pagerState.currentPage + 1).toString(),
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .navigationBarsPadding()
                    .padding(bottom = 34.dp)
                    .alpha(1f - chromeAlpha),
                fontFamily = organicBodyFontFamily(),
                fontSize = 12.sp,
                color = page.ink.copy(alpha = 0.45f)
            )
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

/**
 * The pages themselves.
 *
 * Cutting the book takes about as long as it takes to read this sentence, so it happens off the
 * main thread and behind a short delay — a reader dragging the size slider would otherwise start a
 * pagination for every pixel, and only the one they stop on is worth finishing.
 */
@Composable
private fun ReaderPages(
    uiState: ReadingUiState,
    page: OrganicPageColors,
    pagination: ReaderPagination,
    pagerState: PagerState,
    onPaginated: (ReaderPagination) -> Unit,
    onEvent: (ReadingEvent) -> Unit,
) {
    val density = LocalDensity.current
    // Its own measurer, uncached: every measurement during a pagination is of different text, so a
    // cache would only hold pages nobody will ask for again.
    val measurer = rememberTextMeasurer(cacheSize = 0)

    val face = if (uiState.preferences.useSerif) organicSerifFontFamily() else organicBodyFontFamily()
    val bodySize = uiState.preferences.bodySizeSp.sp
    val style = TextStyle(
        fontFamily = face,
        fontSize = bodySize,
        lineHeight = bodySize * LINE_HEIGHT_RATIO,
        color = page.ink,
    )

    BoxWithConstraints(modifier = Modifier.fillMaxSize().clipToBounds()) {
        val textWidth = with(density) { (maxWidth - PAGE_GUTTER * 2).roundToPx() }
        val textHeight = with(density) { (maxHeight - PAGE_TOP - PAGE_BOTTOM).roundToPx() }

        LaunchedEffect(
            uiState.text,
            uiState.preferences.fontScale,
            uiState.preferences.useSerif,
            textWidth,
            textHeight,
        ) {
            delay(REPAGINATE_DELAY_MILLIS)
            onPaginated(
                withContext(Dispatchers.Default) {
                    paginateForViewport(
                        text = uiState.text,
                        measurer = measurer,
                        style = style,
                        constraints = Constraints(maxWidth = textWidth, maxHeight = textHeight),
                    )
                }
            )
        }

        // Put the reader back where they were — on first load, and again every time the book is
        // cut anew, which is what keeps a size change from also being a jump. Only then is the
        // pager listened to: a freshly cut book sits on page one until it is told otherwise, and
        // reporting that as a place the reader went would write away the place they were at.
        LaunchedEffect(pagination) {
            val target = pagination.pageOf(uiState.offset)
            if (pagerState.currentPage != target) pagerState.scrollToPage(target)
            snapshotFlow { pagerState.settledPage }
                .drop(1)
                .collect { index -> onEvent(ReadingEvent.PageSettled(pagination.startOf(index))) }
        }

        if (pagination === ReaderPagination.Empty) {
            PageSkeleton()
            return@BoxWithConstraints
        }

        HorizontalPager(
            state = pagerState,
            modifier = Modifier
                .fillMaxSize()
                // A tap anywhere shows or hides the chrome. Turning a page is a swipe now, so the
                // tap does not have to be shared out between three parts of the screen.
                .pointerInput(Unit) {
                    detectTapGestures { onEvent(ReadingEvent.PageTapped) }
                },
            key = { it },
        ) { index ->
            Text(
                text = pagination.pageText(index),
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = PAGE_GUTTER)
                    .padding(top = PAGE_TOP, bottom = PAGE_BOTTOM),
                style = style,
            )
        }
    }
}

/** Back, the book's name, and the pin for the page you are on. */
@Composable
private fun ReaderHeader(
    uiState: ReadingUiState,
    page: OrganicPageColors,
    pinnedHere: Boolean,
    alpha: Float,
    onEvent: (ReadingEvent) -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 24.dp, end = 24.dp, top = 12.dp)
            .alpha(alpha),
        horizontalArrangement = Arrangement.spacedBy(14.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        ReaderCircleButton(
            icon = OrganicIcons.ChevronLeft,
            contentDescription = stringResource(Res.string.nav_back),
            background = page.chrome,
            tint = page.chromeInk,
            enabled = uiState.chromeVisible,
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
        if (!uiState.hasText) {
            Spacer(modifier = Modifier.size(38.dp))
        } else {
            ReaderCircleButton(
                icon = if (pinnedHere) OrganicIcons.BookmarkFilled else OrganicIcons.Bookmark,
                contentDescription = stringResource(
                    if (pinnedHere) Res.string.reader_bookmarked else Res.string.reader_bookmark
                ),
                background = if (pinnedHere) OrganicColors.accent200 else page.chrome,
                tint = if (pinnedHere) OrganicColors.accent800 else page.chromeInk,
                enabled = uiState.chromeVisible,
                onClick = { onEvent(ReadingEvent.BookmarkToggled(pinned = !pinnedHere)) },
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
 * pinned place sits on it as a tick, which is what keeps a bookmark from being somewhere you can
 * only put things and never go.
 */
@Composable
private fun ProgressRow(
    uiState: ReadingUiState,
    page: OrganicPageColors,
    pagination: ReaderPagination,
    pagerState: PagerState,
    alpha: Float,
) {
    val scope = rememberCoroutineScope()
    val pageCount = pagination.pageCount
    val current = pagerState.currentPage
    val bookmarkAt = uiState.bookmarkOffset
        ?.takeIf { pageCount > 1 }
        ?.let { pagination.pageOf(it).toFloat() / (pageCount - 1) }

    fun scrubTo(fraction: Float) {
        val target = (fraction.coerceIn(0f, 1f) * (pageCount - 1)).toInt().coerceIn(0, pageCount - 1)
        if (target != pagerState.currentPage) scope.launch { pagerState.scrollToPage(target) }
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 28.dp)
            .padding(top = 18.dp)
            .alpha(alpha),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .weight(1f)
                // The bar draws at 8dp; the target around it is finger-sized.
                .height(SCRUB_TARGET_HEIGHT)
                .pointerInput(pageCount, uiState.chromeVisible) {
                    if (!uiState.chromeVisible) return@pointerInput
                    detectTapGestures { tap -> scrubTo(tap.x / size.width) }
                }
                .pointerInput(pageCount, uiState.chromeVisible) {
                    if (!uiState.chromeVisible) return@pointerInput
                    detectHorizontalDragGestures { change, _ ->
                        scrubTo(change.position.x / size.width)
                    }
                },
            contentAlignment = Alignment.Center
        ) {
            val filled = if (pageCount > 1) (current + 1).toFloat() / pageCount else 1f
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(8.dp)
                    .clip(RoundedCornerShape(OrganicShape.pill))
                    .background(page.chrome)
                    .drawBehind {
                        drawRect(
                            color = OrganicColors.accent,
                            size = Size(size.width * filled, size.height),
                        )
                        bookmarkAt?.let { at -> drawBookmarkTick(at) }
                    }
            )
        }
        Text(
            text = stringResource(Res.string.reader_page_of, current + 1, pageCount),
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
    alpha: Float,
    onEvent: (ReadingEvent) -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .navigationBarsPadding()
            .padding(horizontal = 28.dp)
            .padding(top = 18.dp, bottom = 30.dp)
            .alpha(alpha),
        horizontalArrangement = Arrangement.spacedBy(10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        ReaderCircleButton(
            icon = OrganicIcons.TextSize,
            contentDescription = stringResource(Res.string.reader_display_options),
            background = page.chrome,
            tint = page.chromeInk,
            size = ACTION_SIZE,
            enabled = uiState.chromeVisible,
            onClick = { onEvent(ReadingEvent.MenuClicked) },
        )
        ReaderCircleButton(
            icon = OrganicIcons.Chat,
            contentDescription = stringResource(Res.string.reader_comments),
            background = page.chrome,
            tint = page.chromeInk,
            size = ACTION_SIZE,
            enabled = uiState.chromeVisible,
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
                enabled = uiState.chromeVisible && !uiState.isDownloading,
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
    enabled: Boolean = true,
) {
    Box(
        modifier = Modifier
            .size(size)
            .clip(CircleShape)
            .background(background)
            // A faded button is not a button: while the chrome is hidden these still hold their
            // place in the layout, and a tap where the back arrow used to be must not leave.
            .clickable(enabled = enabled, role = Role.Button, onClick = onClick),
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

/** A page's worth of grey lines while the book's text is still being fetched, or cut. */
@Composable
private fun PageSkeleton() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = PAGE_GUTTER)
            .padding(top = PAGE_TOP),
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

/** The margins a page is set in, and therefore what the text is measured against. */
private val PAGE_GUTTER = 28.dp
private val PAGE_TOP = 26.dp
private val PAGE_BOTTOM = 24.dp

private val ACTION_SIZE = 46.dp
private val SCRUB_TARGET_HEIGHT = 24.dp

/** How much of a round button its glyph fills, at every size the reader draws one. */
private const val ICON_FRACTION = 0.45f

/** Long enough that dragging the size slider starts one pagination, not forty. */
private const val REPAGINATE_DELAY_MILLIS = 180L

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
