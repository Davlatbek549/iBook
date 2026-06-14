package com.example.dz.features.book.detail

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextLayoutResult
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.dz.theme.DZTheme
import dz.shared.generated.resources.Res
import dz.shared.generated.resources.*
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import kotlin.math.roundToInt

private const val ReaderInitialPage = 208
private const val ReaderTotalPages = 337

enum class ReaderTheme {
    Light,
    Dark,
    Sepia
}

private val AvoReaderFontFamily = FontFamily.SansSerif
private val RobotoReaderFontFamily = FontFamily.SansSerif
private val SanFranciscoReaderFontFamily = FontFamily.SansSerif

private enum class ReaderFont(val label: StringResource, val family: FontFamily) {
    Avo(Res.string.book_detail_font_avo, AvoReaderFontFamily),
    Roboto(Res.string.book_detail_font_roboto, RobotoReaderFontFamily),
    SanFrancisco(Res.string.book_detail_font_san_francisco, SanFranciscoReaderFontFamily),
    Times(Res.string.book_detail_font_times, FontFamily.Serif),
    Comic(Res.string.book_detail_font_comic, FontFamily.Cursive)
}

private data class ReaderPalette(
    val background: Color,
    val textPrimary: Color,
    val textSecondary: Color,
    val toolbar: Color,
    val accent: Color,
    val sliderTrack: Color,
    val modalBackground: Color,
    val selectedText: Color
)

@Composable
fun BookDetailScreen(
    modifier: Modifier = Modifier,
    theme: ReaderTheme = ReaderTheme.Light,
    showSelectMenu: Boolean = false,
    showQuickNextPage: Boolean = false,
    showSettings: Boolean = false,
    onBackClick: () -> Unit = {}
) {
    var readerTheme by remember { mutableStateOf(theme) }
    var selectedFont by remember { mutableStateOf(ReaderFont.SanFrancisco) }
    var fontSize by remember { mutableFloatStateOf(15f) }
    var currentPage by remember { mutableIntStateOf(ReaderInitialPage) }
    var controlsVisible by remember { mutableStateOf(true) }
    var actionToolbarVisible by remember { mutableStateOf(showSelectMenu) }
    var quickNavigationVisible by remember { mutableStateOf(showQuickNextPage) }
    var settingsVisible by remember { mutableStateOf(showSettings) }

    val palette = readerPalette(readerTheme)

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(palette.background)
            .padding(horizontal = 24.dp, vertical = 40.dp)
    ) {
        ReaderView(
            palette = palette,
            font = selectedFont,
            fontSize = fontSize,
            onTap = {
                controlsVisible = !controlsVisible
                if (!controlsVisible) {
                    actionToolbarVisible = false
                    quickNavigationVisible = false
                    settingsVisible = false
                }
            },
            onSelectionStarted = {
                controlsVisible = true
                actionToolbarVisible = false
                quickNavigationVisible = false
                settingsVisible = false
            },
            modifier = Modifier
                .fillMaxSize()
        )

        if (controlsVisible) {
            TopBar(
                palette = palette,
                onBackClick = onBackClick,
                onShareClick = { actionToolbarVisible = !actionToolbarVisible },
                onTextSettingsClick = { settingsVisible = !settingsVisible },
                modifier = Modifier.align(Alignment.TopCenter)
            )

            BottomProgressBar(
                palette = palette,
                currentPage = currentPage,
                totalPages = ReaderTotalPages,
                onPageChange = {
                    currentPage = it
                    quickNavigationVisible = true
                },
                modifier = Modifier.align(Alignment.BottomCenter)
            )
        }

        if (actionToolbarVisible) {
            ActionToolbar(
                palette = palette,
                onActionClick = { actionToolbarVisible = false },
                modifier = Modifier.align(Alignment.Center)
            )
        }

        if (quickNavigationVisible) {
            QuickNavigationBadge(
                palette = palette,
                currentPage = currentPage,
                totalPages = ReaderTotalPages,
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .offset(y = (-42).dp)
            )
        }

        if (settingsVisible) {
            SettingsModal(
                palette = palette,
                theme = readerTheme,
                selectedFont = selectedFont,
                fontSize = fontSize,
                onThemeChange = { readerTheme = it },
                onFontChange = { selectedFont = it },
                onFontSizeChange = { fontSize = it },
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(top = 52.dp)
            )
        }
    }
}

@Composable
private fun readerPalette(theme: ReaderTheme): ReaderPalette {
    val colors = MaterialTheme.colorScheme
    return when (theme) {
        ReaderTheme.Light -> ReaderPalette(
            background = Color.White,
            textPrimary = Color(0xFF45424A),
            textSecondary = Color(0xFFB9B6C4),
            toolbar = Color(0xFFB9B6C4),
            accent = colors.primary,
            sliderTrack = Color(0xFFC7C3D1),
            modalBackground = Color.White,
            selectedText = colors.primary.copy(alpha = 0.22f)
        )

        ReaderTheme.Dark -> ReaderPalette(
            background = Color(0xFF545454),
            textPrimary = Color.White.copy(alpha = 0.88f),
            textSecondary = Color.White.copy(alpha = 0.35f),
            toolbar = Color.White.copy(alpha = 0.56f),
            accent = colors.primary,
            sliderTrack = Color.White.copy(alpha = 0.22f),
            modalBackground = Color(0xFF202026),
            selectedText = colors.primary.copy(alpha = 0.28f)
        )

        ReaderTheme.Sepia -> ReaderPalette(
            background = Color(0xFFFFE3B7),
            textPrimary = Color(0xFF5C5145),
            textSecondary = Color(0xFFAA9479),
            toolbar = Color(0xFFBBA17C),
            accent = colors.primary,
            sliderTrack = Color(0xFFBCA684),
            modalBackground = Color(0xFFFFF7E9),
            selectedText = colors.primary.copy(alpha = 0.22f)
        )
    }
}

@Composable
private fun ReaderView(
    palette: ReaderPalette,
    font: ReaderFont,
    fontSize: Float,
    onTap: () -> Unit,
    onSelectionStarted: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .padding(top = 48.dp, bottom = 36.dp)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = stringResource(Res.string.book_detail_logo),
            color = palette.textSecondary,
            style = MaterialTheme.typography.labelSmall,
            fontWeight = FontWeight.SemiBold,
            modifier = Modifier.padding(top = 26.dp)
        )

        Spacer(modifier = Modifier.height(38.dp))

        SelectableReaderText(
            text = stringResource(Res.string.book_detail_body),
            palette = palette,
            font = font,
            fontSize = fontSize,
            onTap = onTap,
            onSelectionStarted = onSelectionStarted,
            modifier = Modifier.fillMaxWidth()
        )
    }
}

@Composable
private fun SelectableReaderText(
    text: String,
    palette: ReaderPalette,
    font: ReaderFont,
    fontSize: Float,
    onTap: () -> Unit,
    onSelectionStarted: () -> Unit,
    modifier: Modifier = Modifier
) {
    var textLayout by remember(text) { mutableStateOf<TextLayoutResult?>(null) }
    var selectionRange by remember(text) { mutableStateOf<TextRange?>(null) }
    val selectedRange = selectionRange?.normalized()
    val annotatedText = remember(text, selectedRange, palette.selectedText) {
        buildAnnotatedString {
            append(text)
            if (selectedRange != null && selectedRange.start < selectedRange.end) {
                addStyle(
                    style = SpanStyle(background = palette.selectedText),
                    start = selectedRange.start,
                    end = selectedRange.end
                )
            }
        }
    }

    Box(modifier = modifier) {
        Text(
            text = annotatedText,
            color = palette.textPrimary,
            style = MaterialTheme.typography.bodyMedium.copy(
                fontFamily = font.family,
                fontSize = fontSize.sp,
                lineHeight = (fontSize * 1.48f).sp
            ),
            onTextLayout = { textLayout = it },
            modifier = Modifier
                .fillMaxWidth()
                .pointerInput(text) {
                    detectTapGestures(
                        onTap = {
                            selectionRange = null
                            onTap()
                        },
                        onLongPress = { pressOffset ->
                            val layout = textLayout ?: return@detectTapGestures
                            val offset = layout.getOffsetForPosition(pressOffset)
                            selectionRange = text.wordRangeAt(offset)
                            onSelectionStarted()
                        }
                    )
                }
        )

        if (selectedRange != null && selectedRange.start < selectedRange.end && textLayout != null) {
            ReaderTextSelectionOverlay(
                text = text,
                palette = palette,
                selectionRange = selectedRange,
                textLayout = textLayout!!,
                onSelectionChange = { selectionRange = it.normalized() },
                onActionClick = { selectionRange = null },
                modifier = Modifier.matchParentSize()
            )
        }
    }
}

@Composable
private fun ReaderTextSelectionOverlay(
    text: String,
    palette: ReaderPalette,
    selectionRange: TextRange,
    textLayout: TextLayoutResult,
    onSelectionChange: (TextRange) -> Unit,
    onActionClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val density = LocalDensity.current
    val toolbarHeightPx = with(density) { 42.dp.roundToPx() }
    val toolbarGapPx = with(density) { 12.dp.roundToPx() }
    val touchSizePx = with(density) { 34.dp.roundToPx() }
    val startBox = textLayout.getBoundingBox(selectionRange.start.coerceIn(0, text.lastIndex))
    val endBox = textLayout.getBoundingBox((selectionRange.end - 1).coerceIn(0, text.lastIndex))
    val toolbarTop = (startBox.top - toolbarHeightPx - toolbarGapPx).roundToInt().coerceAtLeast(0)
    val startHandle = Offset(startBox.left, startBox.bottom)
    val endHandle = Offset(endBox.right, endBox.bottom)

    Box(modifier = modifier) {
        SelectionActionPopup(
            palette = palette,
            onActionClick = onActionClick,
            modifier = Modifier
                .offset { IntOffset(0, toolbarTop) }
                .fillMaxWidth()
        )

        SelectionHandle(
            position = startHandle,
            touchSizePx = touchSizePx,
            palette = palette,
            isStart = true,
            textLength = text.length,
            selectionRange = selectionRange,
            textLayout = textLayout,
            onSelectionChange = onSelectionChange
        )

        SelectionHandle(
            position = endHandle,
            touchSizePx = touchSizePx,
            palette = palette,
            isStart = false,
            textLength = text.length,
            selectionRange = selectionRange,
            textLayout = textLayout,
            onSelectionChange = onSelectionChange
        )
    }
}

@Composable
private fun SelectionActionPopup(
    palette: ReaderPalette,
    onActionClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .height(42.dp)
            .clip(RoundedCornerShape(10.dp))
            .background(palette.accent),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        SelectAction(Res.string.book_detail_copy, onClick = onActionClick)
        SelectDivider()
        SelectAction(Res.string.book_detail_highlight, onClick = onActionClick)
        SelectDivider()
        SelectAction(Res.string.book_detail_search, onClick = onActionClick)
        SelectDivider()
        SelectAction(Res.string.book_detail_share, onClick = onActionClick)
    }
}

@Composable
private fun SelectionHandle(
    position: Offset,
    touchSizePx: Int,
    palette: ReaderPalette,
    isStart: Boolean,
    textLength: Int,
    selectionRange: TextRange,
    textLayout: TextLayoutResult,
    onSelectionChange: (TextRange) -> Unit
) {
    var dragPosition by remember { mutableStateOf<Offset?>(null) }

    Box(
        modifier = Modifier
            .offset {
                IntOffset(
                    x = (position.x - touchSizePx / 2f).roundToInt(),
                    y = (position.y - touchSizePx / 2f).roundToInt()
                )
            }
            .size(34.dp)
            .pointerInput(position, selectionRange) {
                detectDragGestures(
                    onDragStart = { dragPosition = position },
                    onDragCancel = { dragPosition = null },
                    onDragEnd = { dragPosition = null },
                    onDrag = { _, dragAmount ->
                        val nextPosition = (dragPosition ?: position) + dragAmount
                        dragPosition = nextPosition
                        val offset = textLayout.getOffsetForPosition(nextPosition)

                        if (isStart) {
                            val nextStart = offset.coerceIn(0, selectionRange.end - 1)
                            onSelectionChange(TextRange(nextStart, selectionRange.end))
                        } else {
                            val nextEnd = (offset + 1).coerceIn(selectionRange.start + 1, textLength)
                            onSelectionChange(TextRange(selectionRange.start, nextEnd))
                        }
                    }
                )
            },
        contentAlignment = Alignment.Center
    ) {
        Box(
            modifier = Modifier
                .width(2.dp)
                .height(30.dp)
                .background(palette.accent)
        )
        Box(
            modifier = Modifier
                .align(if (isStart) Alignment.TopCenter else Alignment.BottomCenter)
                .size(11.dp)
                .clip(CircleShape)
                .background(palette.accent)
        )
    }
}

private fun TextRange.normalized(): TextRange {
    return if (start <= end) this else TextRange(end, start)
}

private fun String.wordRangeAt(offset: Int): TextRange {
    if (isEmpty()) return TextRange(0, 0)

    var index = offset.coerceIn(0, lastIndex)
    if (!this[index].isReaderWordCharacter()) {
        index = when {
            index > 0 && this[index - 1].isReaderWordCharacter() -> index - 1
            index < lastIndex && this[index + 1].isReaderWordCharacter() -> index + 1
            else -> return TextRange(index, (index + 1).coerceAtMost(length))
        }
    }

    var start = index
    while (start > 0 && this[start - 1].isReaderWordCharacter()) {
        start--
    }

    var end = index + 1
    while (end < length && this[end].isReaderWordCharacter()) {
        end++
    }

    return TextRange(start, end)
}

private fun Char.isReaderWordCharacter(): Boolean {
    return isLetterOrDigit() || this == '\'' || this == '’' || this == '-'
}

@Composable
private fun TopBar(
    palette: ReaderPalette,
    onBackClick: () -> Unit,
    onShareClick: () -> Unit,
    onTextSettingsClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Surface(
            onClick = onBackClick,
            shape = CircleShape,
            color = palette.accent.copy(alpha = 0.10f),
            modifier = Modifier.size(36.dp)
        ) {
            Box(contentAlignment = Alignment.Center) {
                Icon(
                    painter = painterResource(Res.drawable.ic_back),
                    contentDescription = null,
                    tint = palette.accent.copy(alpha = 0.62f),
                    modifier = Modifier.size(19.dp)
                )
            }
        }

        Row(
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButtonSurface(onClick = onShareClick) {
                Icon(
                    painter = painterResource(Res.drawable.ic_share),
                    contentDescription = null,
                    tint = palette.toolbar,
                    modifier = Modifier.size(18.dp)
                )
            }
            Surface(onClick = onTextSettingsClick, color = Color.Transparent) {
                Text(
                    text = stringResource(Res.string.book_detail_text_size_small) +
                        stringResource(Res.string.book_detail_text_size_large),
                    color = palette.toolbar,
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold
                )
            }
            Icon(
                painter = painterResource(Res.drawable.ic_search),
                contentDescription = null,
                tint = palette.toolbar,
                modifier = Modifier.size(18.dp)
            )
        }
    }
}

@Composable
private fun IconButtonSurface(
    onClick: () -> Unit,
    content: @Composable () -> Unit
) {
    Surface(onClick = onClick, color = Color.Transparent) {
        Box(contentAlignment = Alignment.Center, modifier = Modifier.size(22.dp)) {
            content()
        }
    }
}

@Composable
private fun ActionToolbar(
    palette: ReaderPalette,
    onActionClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(170.dp)
            .padding(horizontal = 22.dp)
    ) {
        Box(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth(0.78f)
                .height(88.dp)
                .background(palette.selectedText)
        )

        Box(
            modifier = Modifier
                .align(Alignment.Center)
                .width(2.dp)
                .height(98.dp)
                .background(palette.accent)
        )

        Row(
            modifier = Modifier
                .align(Alignment.TopCenter)
                .fillMaxWidth()
                .height(42.dp)
                .clip(RoundedCornerShape(10.dp))
                .background(palette.accent),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            SelectAction(Res.string.book_detail_copy, onClick = onActionClick)
            SelectDivider()
            SelectAction(Res.string.book_detail_highlight, onClick = onActionClick)
            SelectDivider()
            SelectAction(Res.string.book_detail_search, onClick = onActionClick)
            SelectDivider()
            SelectAction(Res.string.book_detail_share, onClick = onActionClick)
        }

        Box(
            modifier = Modifier
                .align(Alignment.Center)
                .offset(y = (-4).dp)
                .size(8.dp)
                .clip(CircleShape)
                .background(palette.accent)
        )

        Box(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .offset(y = (-37).dp)
                .size(8.dp)
                .clip(CircleShape)
                .background(palette.accent)
        )
    }
}

@Composable
private fun SelectDivider() {
    Text(
        text = "|",
        color = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.78f),
        style = MaterialTheme.typography.bodyMedium.copy(fontSize = 17.sp),
        fontWeight = FontWeight.Normal
    )
}

@Composable
private fun SelectAction(
    label: StringResource,
    onClick: () -> Unit
) {
    Surface(
        onClick = onClick,
        color = Color.Transparent
    ) {
        Box(
            modifier = Modifier
                .height(42.dp)
                .padding(horizontal = 10.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = stringResource(label),
                color = MaterialTheme.colorScheme.onPrimary,
                style = MaterialTheme.typography.bodyMedium.copy(fontSize = 16.sp),
                fontWeight = FontWeight.Normal
            )
        }
    }
}

@Composable
private fun QuickNavigationBadge(
    palette: ReaderPalette,
    currentPage: Int,
    totalPages: Int,
    modifier: Modifier = Modifier
) {
    BoxWithConstraints(
        modifier = modifier
            .fillMaxWidth()
            .height(36.dp)
    ) {
        val badgeWidth = 74.dp
        val pageProgress = remember(currentPage, totalPages) {
            ((currentPage - 1).toFloat() / (totalPages - 1).toFloat()).coerceIn(0f, 1f)
        }

        Box(
            modifier = Modifier
                .align(Alignment.CenterStart)
                .offset(x = (maxWidth - badgeWidth) * pageProgress)
                .width(badgeWidth)
                .height(36.dp)
                .clip(RoundedCornerShape(8.dp))
                .background(palette.accent),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = stringResource(Res.string.book_detail_toast_next).replace(
                    stringResource(Res.string.book_detail_progress_page),
                    currentPage.toString()
                ),
                color = MaterialTheme.colorScheme.onPrimary,
                style = MaterialTheme.typography.labelSmall.copy(fontSize = 8.sp, lineHeight = 10.sp),
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
private fun SettingsModal(
    palette: ReaderPalette,
    theme: ReaderTheme,
    selectedFont: ReaderFont,
    fontSize: Float,
    onThemeChange: (ReaderTheme) -> Unit,
    onFontChange: (ReaderFont) -> Unit,
    onFontSizeChange: (Float) -> Unit,
    modifier: Modifier = Modifier
) {
    val panelText = if (theme == ReaderTheme.Dark) {
        Color.White.copy(alpha = 0.88f)
    } else {
        Color(0xFF6D6878)
    }

    Column(
        modifier = modifier
            .width(188.dp)
            .clip(RoundedCornerShape(16.dp))
            .border(1.5.dp, palette.accent, RoundedCornerShape(16.dp))
            .background(palette.modalBackground)
            .padding(horizontal = 16.dp, vertical = 14.dp)
    ) {
        Slider(
            value = fontSize,
            onValueChange = onFontSizeChange,
            valueRange = 12f..22f,
            colors = SliderDefaults.colors(
                thumbColor = palette.accent,
                activeTrackColor = palette.accent,
                inactiveTrackColor = palette.sliderTrack
            )
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceAround,
            verticalAlignment = Alignment.Bottom
        ) {
            Text(
                text = stringResource(Res.string.book_detail_text_size_small),
                color = palette.accent,
                style = MaterialTheme.typography.labelSmall,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = stringResource(Res.string.book_detail_text_size_large),
                color = palette.accent,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
        }

        Spacer(modifier = Modifier.height(14.dp))

        Row(horizontalArrangement = Arrangement.spacedBy(24.dp)) {
            ThemeSwatch(Color.White, selected = theme == ReaderTheme.Light, onClick = { onThemeChange(ReaderTheme.Light) })
            ThemeSwatch(Color(0xFFFFE3B7), selected = theme == ReaderTheme.Sepia, onClick = { onThemeChange(ReaderTheme.Sepia) })
            ThemeSwatch(Color(0xFF4C4C4C), selected = theme == ReaderTheme.Dark, onClick = { onThemeChange(ReaderTheme.Dark) })
        }

        Spacer(modifier = Modifier.height(12.dp))

        ReaderFont.entries.forEach { option ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(28.dp)
                    .clickable { onFontChange(option) },
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(9.dp)
                        .clip(CircleShape)
                        .background(if (option == selectedFont) palette.accent else panelText.copy(alpha = 0.45f))
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = stringResource(option.label),
                    color = panelText,
                    style = MaterialTheme.typography.labelSmall.copy(
                        fontFamily = option.family,
                        fontSize = 9.sp
                    )
                )
            }
        }
    }
}

@Composable
private fun ThemeSwatch(
    color: Color,
    selected: Boolean,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .size(25.dp)
            .clip(RoundedCornerShape(6.dp))
            .background(color)
            .border(
                width = if (selected) 1.4.dp else 0.dp,
                color = MaterialTheme.colorScheme.primary,
                shape = RoundedCornerShape(6.dp)
            )
            .clickable(onClick = onClick)
    )
}

@Composable
private fun BottomProgressBar(
    palette: ReaderPalette,
    currentPage: Int,
    totalPages: Int,
    onPageChange: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    BoxWithConstraints(
        modifier = modifier
            .fillMaxWidth()
            .height(34.dp)
    ) {
        val badgeWidth = 76.dp
        val pageProgress = remember(currentPage, totalPages) {
            ((currentPage - 1).toFloat() / (totalPages - 1).toFloat()).coerceIn(0f, 1f)
        }

        Slider(
            value = currentPage.toFloat(),
            onValueChange = { onPageChange(it.roundToInt().coerceIn(1, totalPages)) },
            valueRange = 1f..totalPages.toFloat(),
            colors = SliderDefaults.colors(
                thumbColor = Color.Transparent,
                activeTrackColor = palette.accent,
                inactiveTrackColor = palette.sliderTrack
            ),
            modifier = Modifier.align(Alignment.Center)
        )

        Box(
            modifier = Modifier
                .align(Alignment.CenterStart)
                .offset(x = (maxWidth - badgeWidth) * pageProgress)
                .width(badgeWidth)
                .clip(RoundedCornerShape(50))
                .background(palette.accent)
                .padding(horizontal = 10.dp, vertical = 4.dp)
        ) {
            Text(
                text = "$currentPage/$totalPages",
                color = MaterialTheme.colorScheme.onPrimary,
                style = MaterialTheme.typography.labelSmall.copy(fontSize = 12.sp),
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

@Preview(showBackground = true, widthDp = 375, heightDp = 820)
@Composable
private fun BookDetailPreview() {
    DZTheme {
        BookDetailScreen()
    }
}

@Preview(showBackground = true, widthDp = 375, heightDp = 820)
@Composable
private fun BookSelectPreview() {
    DZTheme {
        BookDetailScreen(showSelectMenu = true)
    }
}

@Preview(showBackground = true, widthDp = 375, heightDp = 820)
@Composable
private fun BookQuickNextPagePreview() {
    DZTheme {
        BookDetailScreen(showQuickNextPage = true)
    }
}

@Preview(showBackground = true, widthDp = 375, heightDp = 820)
@Composable
private fun BookDetailSettingPreview() {
    DZTheme {
        BookDetailScreen(showSettings = true)
    }
}

@Preview(showBackground = true, widthDp = 375, heightDp = 820)
@Composable
private fun BookDetailDarkModePreview() {
    DZTheme {
        BookDetailScreen(theme = ReaderTheme.Dark)
    }
}

@Preview(showBackground = true, widthDp = 375, heightDp = 820)
@Composable
private fun BookDarkModeSettingPreview() {
    DZTheme {
        BookDetailScreen(theme = ReaderTheme.Dark, showSettings = true)
    }
}

@Preview(showBackground = true, widthDp = 375, heightDp = 820)
@Composable
private fun BookDetailSepiaModePreview() {
    DZTheme {
        BookDetailScreen(theme = ReaderTheme.Sepia)
    }
}
