package com.example.ibook.screens.book_detail

import androidx.annotation.StringRes
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Share
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ibook.R
import com.example.ibook.ui.theme.IBookTheme

enum class ReaderTheme {
    Light,
    Dark,
    Sepia
}

private enum class ReaderFont(@param:StringRes val label: Int, val family: FontFamily) {
    Avo(R.string.book_detail_font_avo, FontFamily.SansSerif),
    Roboto(R.string.book_detail_font_roboto, FontFamily.Default),
    SanFrancisco(R.string.book_detail_font_san_francisco, FontFamily.SansSerif),
    Times(R.string.book_detail_font_times, FontFamily.Serif),
    Comic(R.string.book_detail_font_comic, FontFamily.Cursive)
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
    var currentPage by remember { mutableIntStateOf(208) }
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
            modifier = Modifier
                .fillMaxSize()
                .clickable {
                    controlsVisible = !controlsVisible
                    if (!controlsVisible) {
                        actionToolbarVisible = false
                        quickNavigationVisible = false
                        settingsVisible = false
                    }
                }
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
                modifier = Modifier.align(Alignment.Center)
            )
        }

        if (quickNavigationVisible) {
            QuickNavigationBadge(
                palette = palette,
                currentPage = currentPage,
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
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .padding(top = 48.dp, bottom = 36.dp)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = stringResource(R.string.book_detail_logo),
            color = palette.textSecondary,
            style = MaterialTheme.typography.labelSmall,
            fontWeight = FontWeight.SemiBold,
            modifier = Modifier.padding(top = 26.dp)
        )

        Spacer(modifier = Modifier.height(38.dp))

        Text(
            text = stringResource(R.string.book_detail_body),
            color = palette.textPrimary,
            style = MaterialTheme.typography.bodyMedium.copy(
                fontFamily = font.family,
                fontSize = fontSize.sp,
                lineHeight = (fontSize * 1.48f).sp
            ),
            modifier = Modifier.fillMaxWidth()
        )
    }
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
                    imageVector = Icons.Default.ArrowBackIosNew,
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
                    imageVector = Icons.Default.Share,
                    contentDescription = null,
                    tint = palette.toolbar,
                    modifier = Modifier.size(18.dp)
                )
            }
            Surface(onClick = onTextSettingsClick, color = Color.Transparent) {
                Text(
                    text = stringResource(R.string.book_detail_text_size_small) +
                        stringResource(R.string.book_detail_text_size_large),
                    color = palette.toolbar,
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold
                )
            }
            Icon(
                imageVector = Icons.Default.Search,
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
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .width(252.dp)
            .height(120.dp)
    ) {
        Box(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .width(150.dp)
                .height(78.dp)
                .background(palette.selectedText)
        )

        Row(
            modifier = Modifier
                .align(Alignment.TopCenter)
                .width(252.dp)
                .height(42.dp)
                .clip(RoundedCornerShape(9.dp))
                .background(palette.accent),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            SelectAction(R.string.book_detail_copy)
            SelectAction(R.string.book_detail_highlight)
            SelectAction(R.string.book_detail_search)
            SelectAction(R.string.book_detail_share)
        }
    }
}

@Composable
private fun SelectAction(@StringRes label: Int) {
      Text(
        text = stringResource(label),
        color = MaterialTheme.colorScheme.onPrimary,
        style = MaterialTheme.typography.labelSmall.copy(fontSize = 9.sp),
        fontWeight = FontWeight.Bold
    )
}

@Composable
private fun QuickNavigationBadge(
    palette: ReaderPalette, 
    currentPage: Int,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .width(74.dp)
            .height(36.dp)
            .clip(RoundedCornerShape(8.dp))
            .background(palette.accent),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = stringResource(R.string.book_detail_toast_next).replace(
                stringResource(R.string.book_detail_progress_page),
                currentPage.toString()
            ),
            color = MaterialTheme.colorScheme.onPrimary,
            style = MaterialTheme.typography.labelSmall.copy(fontSize = 8.sp, lineHeight = 10.sp),
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center
        )
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
                text = stringResource(R.string.book_detail_text_size_small),
                color = palette.accent,
                style = MaterialTheme.typography.labelSmall,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = stringResource(R.string.book_detail_text_size_large),
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
                    style = MaterialTheme.typography.labelSmall.copy(fontSize = 9.sp)
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
    onPageChange: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(28.dp)
    ) {
        Slider(
            value = currentPage.toFloat(),
            onValueChange = { onPageChange(it.toInt()) },
            valueRange = 1f..320f,
            colors = SliderDefaults.colors(
                thumbColor = palette.accent,
                activeTrackColor = palette.accent,
                inactiveTrackColor = palette.sliderTrack
            ),
            modifier = Modifier.align(Alignment.Center)
        )

        Box(
            modifier = Modifier
                .align(Alignment.TopCenter)
                .offset(x = 54.dp)
                .clip(RoundedCornerShape(50))
                .background(palette.accent)
                .padding(horizontal = 9.dp, vertical = 2.dp)
        ) {
            Text(
                text = currentPage.toString(),
                color = MaterialTheme.colorScheme.onPrimary,
                style = MaterialTheme.typography.labelSmall.copy(fontSize = 7.sp)
            )
        }
    }
}

@Preview(showBackground = true, widthDp = 375, heightDp = 820)
@Composable
private fun BookDetailPreview() {
    IBookTheme {
        BookDetailScreen()
    }
}

@Preview(showBackground = true, widthDp = 375, heightDp = 820)
@Composable
private fun BookSelectPreview() {
    IBookTheme {
        BookDetailScreen(showSelectMenu = true)
    }
}

@Preview(showBackground = true, widthDp = 375, heightDp = 820)
@Composable
private fun BookQuickNextPagePreview() {
    IBookTheme {
        BookDetailScreen(showQuickNextPage = true)
    }
}

@Preview(showBackground = true, widthDp = 375, heightDp = 820)
@Composable
private fun BookDetailSettingPreview() {
    IBookTheme {
        BookDetailScreen(showSettings = true)
    }
}

@Preview(showBackground = true, widthDp = 375, heightDp = 820)
@Composable
private fun BookDetailDarkModePreview() {
    IBookTheme {
        BookDetailScreen(theme = ReaderTheme.Dark)
    }
}

@Preview(showBackground = true, widthDp = 375, heightDp = 820)
@Composable
private fun BookDarkModeSettingPreview() {
    IBookTheme {
        BookDetailScreen(theme = ReaderTheme.Dark, showSettings = true)
    }
}

@Preview(showBackground = true, widthDp = 375, heightDp = 820)
@Composable
private fun BookDetailSepiaModePreview() {
    IBookTheme {
        BookDetailScreen(theme = ReaderTheme.Sepia)
    }
}
