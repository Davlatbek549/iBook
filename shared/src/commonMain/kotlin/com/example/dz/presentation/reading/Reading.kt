package com.example.dz.presentation.reading

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.dz.designsystem.components.icons.InkIcons
import com.example.dz.designsystem.components.ink.InkProgressBar
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

    Column(
        modifier = modifier
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
                textAlign = androidx.compose.ui.text.style.TextAlign.Center
            )
            Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
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
        Column(
            modifier = Modifier
                .weight(1f)
                .verticalScroll(rememberScrollState())
                .padding(start = 26.dp, end = 26.dp, top = 14.dp)
        ) {
            Text(
                text = uiState.chapterLabel,
                fontFamily = displayFont,
                fontStyle = FontStyle.Italic,
                fontSize = 13.sp,
                color = colors.accent
            )
            Text(
                text = uiState.chapterTitle,
                modifier = Modifier.padding(top = 14.dp),
                fontFamily = displayFont,
                fontWeight = FontWeight.Medium,
                fontSize = 22.sp,
                color = colors.ink
            )
            Column(
                modifier = Modifier.padding(top = 20.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                uiState.paragraphs.forEach { paragraph ->
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

        // bottom progress bar
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
                            .clickable { onEvent(ReadingEvent.PreviousPageClicked) }
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
                        textAlign = androidx.compose.ui.text.style.TextAlign.Center
                    )
                    Icon(
                        imageVector = InkIcons.Back,
                        contentDescription = null,
                        tint = colors.muted,
                        modifier = Modifier
                            .size(18.dp)
                            .graphicsLayer { rotationZ = 180f }
                            .clickable { onEvent(ReadingEvent.NextPageClicked) }
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true, widthDp = 375, heightDp = 820)
@Composable
private fun ReadingScreenPreview() {
    ReadingScreen()
}
