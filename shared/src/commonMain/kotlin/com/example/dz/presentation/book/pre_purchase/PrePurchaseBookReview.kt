package com.example.dz.presentation.book.pre_purchase

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.dz.designsystem.components.icons.InkIcons
import com.example.dz.designsystem.components.ink.InkChip
import com.example.dz.designsystem.components.ink.InkIconButton
import com.example.dz.designsystem.components.ink.InkLabel
import com.example.dz.designsystem.components.ink.inkCard
import com.example.dz.designsystem.components.remote.RemoteBookCover
import com.example.dz.designsystem.theme.InkColors
import com.example.dz.designsystem.theme.InkShape
import com.example.dz.designsystem.theme.inkBodyFontFamily
import com.example.dz.designsystem.theme.inkColors
import com.example.dz.designsystem.theme.inkDisplayFontFamily
import dz.shared.generated.resources.Res
import dz.shared.generated.resources.detail_about
import dz.shared.generated.resources.detail_available_offline
import dz.shared.generated.resources.detail_buy_now
import dz.shared.generated.resources.detail_meta_lang
import dz.shared.generated.resources.detail_meta_pages
import dz.shared.generated.resources.detail_meta_rating
import dz.shared.generated.resources.detail_meta_time
import dz.shared.generated.resources.detail_more_like_this
import dz.shared.generated.resources.detail_price
import org.jetbrains.compose.resources.stringResource

private val previewUiState = PrePurchaseUiState(
    bookId = "mexican-gothic",
    title = "Mexican Gothic",
    author = "Silvia Moreno-Garcia",
    overview = "Placeholder overview for preview only.",
)

@Composable
fun PrePurchaseScreen(
    uiState: PrePurchaseUiState = previewUiState,
    onEvent: (PrePurchaseEvent) -> Unit = {},
    modifier: Modifier = Modifier
) {
    val book = uiState
    val colors = inkColors()
    val displayFont = inkDisplayFontFamily()
    val bodyFont = inkBodyFontFamily()

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(colors.paper)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding()
                .verticalScroll(rememberScrollState())
                .padding(bottom = 140.dp)
        ) {
            // top bar
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 22.dp, end = 22.dp, top = 4.dp, bottom = 8.dp)
            ) {
                InkIconButton(icon = InkIcons.Back, onClick = { onEvent(PrePurchaseEvent.BackClicked) }, colors = colors)
                Spacer(modifier = Modifier.weight(1f))
                Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                    InkIconButton(icon = InkIcons.Share, onClick = { onEvent(PrePurchaseEvent.ShareClicked) }, colors = colors)
                    InkIconButton(
                        icon = InkIcons.Bookmark,
                        onClick = { onEvent(PrePurchaseEvent.FavoriteClicked) },
                        colors = colors
                    )
                }
            }

            // hero
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 22.dp, end = 22.dp, top = 18.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                RemoteBookCover(
                    coverUrl = book.coverUrl,
                    fallback = book.coverRes,
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .size(width = 132.dp, height = 196.dp)
                        .shadow(16.dp, RoundedCornerShape(InkShape.cover + 2.dp), clip = true)
                )
                Text(
                    text = book.title,
                    modifier = Modifier.padding(top = 22.dp),
                    fontFamily = displayFont,
                    fontWeight = FontWeight.Medium,
                    fontSize = 25.sp,
                    lineHeight = 29.sp,
                    textAlign = TextAlign.Center,
                    color = colors.ink
                )
                Text(
                    text = book.author,
                    modifier = Modifier
                        .padding(top = 7.dp)
                        .clickable { onEvent(PrePurchaseEvent.AuthorClicked) },
                    fontFamily = bodyFont,
                    fontSize = 13.sp,
                    color = colors.muted
                )
                Row(
                    modifier = Modifier.padding(top = 14.dp),
                    horizontalArrangement = Arrangement.spacedBy(7.dp)
                ) {
                    book.tags.forEach { tag ->
                        Box(modifier = Modifier.clickable { onEvent(PrePurchaseEvent.TagClicked) }) {
                            InkChip(text = tag, solid = true, colors = colors)
                        }
                    }
                }
            }

            // meta strip
            Row(
                modifier = Modifier
                    .padding(start = 22.dp, end = 22.dp, top = 24.dp)
                    .fillMaxWidth()
                    .height(IntrinsicSize.Min)
                    .inkCard(colors)
                    .padding(vertical = 16.dp)
            ) {
                MetaCell(value = "${book.rating} ★", label = stringResource(Res.string.detail_meta_rating), colors = colors, modifier = Modifier.weight(1f))
                VerticalDivider(modifier = Modifier.fillMaxHeight(), thickness = 1.dp, color = colors.line)
                MetaCell(value = book.pages, label = stringResource(Res.string.detail_meta_pages), colors = colors, modifier = Modifier.weight(1f))
                VerticalDivider(modifier = Modifier.fillMaxHeight(), thickness = 1.dp, color = colors.line)
                MetaCell(value = book.readTime, label = stringResource(Res.string.detail_meta_time), colors = colors, modifier = Modifier.weight(1f))
                VerticalDivider(modifier = Modifier.fillMaxHeight(), thickness = 1.dp, color = colors.line)
                MetaCell(value = book.language, label = stringResource(Res.string.detail_meta_lang), colors = colors, modifier = Modifier.weight(1f))
            }

            // about
            Column(modifier = Modifier.padding(start = 22.dp, end = 22.dp, top = 24.dp)) {
                InkLabel(text = stringResource(Res.string.detail_about), colors = colors)
                Text(
                    text = book.overview,
                    modifier = Modifier.padding(top = 12.dp),
                    fontFamily = bodyFont,
                    fontSize = 14.sp,
                    lineHeight = 24.5.sp,
                    color = colors.inkSoft
                )
            }

            // more like this
            Column(modifier = Modifier.padding(top = 22.dp)) {
                Text(
                    text = stringResource(Res.string.detail_more_like_this),
                    modifier = Modifier.padding(horizontal = 22.dp),
                    fontFamily = displayFont,
                    fontWeight = FontWeight.Medium,
                    fontSize = 16.sp,
                    color = colors.ink
                )
                Row(
                    modifier = Modifier
                        .padding(top = 12.dp)
                        .horizontalScroll(rememberScrollState())
                        .padding(horizontal = 22.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    book.relatedBooks.forEach { related ->
                        RemoteBookCover(
                            coverUrl = related.coverUrl,
                            fallback = related.coverRes,
                            contentDescription = null,
                            contentScale = ContentScale.Crop,
                            modifier = Modifier
                                .size(width = 64.dp, height = 94.dp)
                                .shadow(6.dp, RoundedCornerShape(InkShape.cover), clip = true)
                                .clickable { onEvent(PrePurchaseEvent.RelatedBookClicked(related.id)) }
                        )
                    }
                }
            }
        }

        // buy bar
        Column(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .background(colors.paper)
        ) {
            if (book.isDownloaded) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(colors.alt)
                        .padding(vertical = 8.dp),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = InkIcons.Done,
                        contentDescription = null,
                        tint = colors.accent,
                        modifier = Modifier.size(13.dp)
                    )
                    Text(
                        text = stringResource(Res.string.detail_available_offline),
                        modifier = Modifier.padding(start = 6.dp),
                        fontFamily = bodyFont,
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 11.sp,
                        letterSpacing = 0.4.sp,
                        color = colors.accent
                    )
                }
            }
            HorizontalDivider(thickness = 1.dp, color = colors.line)
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .navigationBarsPadding()
                    .padding(start = 22.dp, end = 22.dp, top = 14.dp, bottom = 18.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                Column {
                    Text(
                        text = stringResource(Res.string.detail_price).uppercase(),
                        fontFamily = bodyFont,
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 10.sp,
                        letterSpacing = 0.6.sp,
                        color = colors.muted
                    )
                    Text(
                        text = book.price,
                        modifier = Modifier.padding(top = 4.dp),
                        fontFamily = displayFont,
                        fontWeight = FontWeight.Medium,
                        fontSize = 22.sp,
                        color = colors.ink
                    )
                }
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .height(52.dp)
                        .clip(RoundedCornerShape(InkShape.radiusSm + 2.dp))
                        .background(colors.accent)
                        .clickable { onEvent(PrePurchaseEvent.PurchaseClicked) },
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = stringResource(Res.string.detail_buy_now),
                        fontFamily = bodyFont,
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 15.sp,
                        color = colors.onAccent
                    )
                }
                Box(
                    modifier = Modifier
                        .size(52.dp)
                        .clip(RoundedCornerShape(InkShape.radiusSm + 2.dp))
                        .border(1.dp, colors.line, RoundedCornerShape(InkShape.radiusSm + 2.dp))
                        .clickable { onEvent(PrePurchaseEvent.ViewSampleClicked) },
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = InkIcons.BookOpen,
                        contentDescription = null,
                        tint = colors.ink,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }
        }
    }
}

@Composable
private fun MetaCell(
    value: String,
    label: String,
    colors: InkColors,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = value,
            fontFamily = inkDisplayFontFamily(),
            fontWeight = FontWeight.Medium,
            fontSize = 16.sp,
            color = colors.ink
        )
        Text(
            text = label.uppercase(),
            modifier = Modifier.padding(top = 5.dp),
            fontFamily = inkBodyFontFamily(),
            fontWeight = FontWeight.Medium,
            fontSize = 10.sp,
            letterSpacing = 0.8.sp,
            color = colors.muted
        )
    }
}

@Preview(showBackground = true, widthDp = 375, heightDp = 820)
@Composable
fun PrePurchaseScreenPreview() {
    PrePurchaseScreen()
}
