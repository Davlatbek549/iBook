package com.example.dz.features.book.reading

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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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
import com.example.dz.app_components.icons.InkIcons
import com.example.dz.app_components.ink.InkProgressBar
import com.example.dz.theme.inkBodyFontFamily
import com.example.dz.theme.inkColors
import com.example.dz.theme.inkDisplayFontFamily

private const val TOTAL_PAGES = 320

private val paragraphs = listOf(
    "The house appeared out of the mist like something half-remembered from a dream — tall, severe, and utterly silent. Noemí pressed her face to the carriage window and watched the iron gates draw closer.",
    "She had not wanted to come. The city, with its parties and its noise, was where she belonged. Yet the letter had been impossible to ignore.",
    "“We are nearly there,” the driver said, though his voice carried no comfort at all.",
)

@Composable
fun ReadingScreen(
    modifier: Modifier = Modifier,
    onBackClick: () -> Unit = {},
    onMenuClick: () -> Unit = {},
    onCommentsClick: () -> Unit = {},
    onKeepReadingClick: () -> Unit = {}
) {
    val colors = inkColors()
    val displayFont = inkDisplayFontFamily()
    val bodyFont = inkBodyFontFamily()

    var page by remember { mutableIntStateOf(198) }
    var bookmarked by remember { mutableStateOf(false) }

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
                    .clickable(onClick = onBackClick)
            )
            Text(
                text = "MEXICAN GOTHIC",
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
                        .clickable(onClick = onMenuClick)
                )
                Icon(
                    imageVector = InkIcons.Chat,
                    contentDescription = null,
                    tint = colors.muted,
                    modifier = Modifier
                        .size(18.dp)
                        .clickable(onClick = onCommentsClick)
                )
                Icon(
                    imageVector = InkIcons.Bookmark,
                    contentDescription = null,
                    tint = if (bookmarked) colors.accent else colors.muted,
                    modifier = Modifier
                        .size(18.dp)
                        .clickable { bookmarked = !bookmarked }
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
                text = "Chapter Three",
                fontFamily = displayFont,
                fontStyle = FontStyle.Italic,
                fontSize = 13.sp,
                color = colors.accent
            )
            Text(
                text = "The Arrival",
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
                paragraphs.forEach { paragraph ->
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
                    progress = page.toFloat() / TOTAL_PAGES,
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
                            .clickable { if (page > 1) page-- }
                    )
                    Text(
                        text = buildAnnotatedString {
                            append("Page $page of $TOTAL_PAGES · ")
                            withStyle(SpanStyle(color = colors.accent)) {
                                append("${page * 100 / TOTAL_PAGES}%")
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
                            .clickable { if (page < TOTAL_PAGES) page++ }
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
