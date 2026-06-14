package com.example.dz.features.book.pre_purchase

import androidx.compose.foundation.Image
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
import com.example.dz.app_components.icons.InkIcons
import com.example.dz.app_components.ink.InkChip
import com.example.dz.app_components.ink.InkIconButton
import com.example.dz.app_components.ink.InkLabel
import com.example.dz.app_components.ink.inkCard
import com.example.dz.theme.InkColors
import com.example.dz.theme.InkShape
import com.example.dz.theme.inkBodyFontFamily
import com.example.dz.theme.inkColors
import com.example.dz.theme.inkDisplayFontFamily
import dz.shared.generated.resources.Res
import dz.shared.generated.resources.book_cover
import dz.shared.generated.resources.book_cover_2
import dz.shared.generated.resources.book_cover_3
import dz.shared.generated.resources.book_cover_4
import dz.shared.generated.resources.detail_about
import dz.shared.generated.resources.detail_about_body
import dz.shared.generated.resources.detail_buy_now
import dz.shared.generated.resources.detail_meta_lang
import dz.shared.generated.resources.detail_meta_pages
import dz.shared.generated.resources.detail_meta_rating
import dz.shared.generated.resources.detail_meta_time
import dz.shared.generated.resources.detail_more_like_this
import dz.shared.generated.resources.detail_price
import dz.shared.generated.resources.olive_again_book
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

data class PrePurchaseBookUiState(
    val title: String,
    val author: String,
    val reads: String = "",
    val rating: String = "4.6",
    val reviews: String = "",
    val overview: String = "",
    val price: String = "$12.99",
    val pages: String = "320",
    val readTime: String = "6h 20m",
    val language: String = "EN",
    val tags: List<String> = listOf("Literary", "Gothic"),
    val coverRes: DrawableResource = Res.drawable.book_cover
)

private val moreLikeThisCovers = listOf(
    Res.drawable.book_cover_2,
    Res.drawable.book_cover_3,
    Res.drawable.book_cover_4,
    Res.drawable.olive_again_book
)

@Composable
fun PrePurchaseScreen(
    modifier: Modifier = Modifier,
    showTagsBookAction: Boolean = false,
    onBackClick: () -> Unit = {},
    onShareClick: () -> Unit = {},
    onFavoriteClick: () -> Unit = {},
    onViewSampleClick: () -> Unit = {},
    onPurchaseClick: () -> Unit = {},
    onTagsBookClick: () -> Unit = {},
    onAuthorClick: () -> Unit = {}
) {
    val book = PrePurchaseBookUiState(
        title = "Mexican Gothic",
        author = "Silvia Moreno-Garcia",
        overview = stringResource(Res.string.detail_about_body)
    )

    PrePurchaseScreen(
        book = book,
        modifier = modifier,
        showTagsBookAction = showTagsBookAction,
        onBackClick = onBackClick,
        onShareClick = onShareClick,
        onFavoriteClick = onFavoriteClick,
        onViewSampleClick = onViewSampleClick,
        onPurchaseClick = onPurchaseClick,
        onTagsBookClick = onTagsBookClick,
        onAuthorClick = onAuthorClick
    )
}

@Composable
fun PrePurchaseScreen(
    book: PrePurchaseBookUiState,
    modifier: Modifier = Modifier,
    showTagsBookAction: Boolean = false,
    onBackClick: () -> Unit = {},
    onShareClick: () -> Unit = {},
    onFavoriteClick: () -> Unit = {},
    onViewSampleClick: () -> Unit = {},
    onPurchaseClick: () -> Unit = {},
    onTagsBookClick: () -> Unit = {},
    onAuthorClick: () -> Unit = {}
) {
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
                InkIconButton(icon = InkIcons.Back, onClick = onBackClick, colors = colors)
                Spacer(modifier = Modifier.weight(1f))
                Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                    InkIconButton(icon = InkIcons.Share, onClick = onShareClick, colors = colors)
                    InkIconButton(icon = InkIcons.Bookmark, onClick = onFavoriteClick, colors = colors)
                }
            }

            // hero
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 22.dp, end = 22.dp, top = 18.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Image(
                    painter = painterResource(book.coverRes),
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
                        .clickable(onClick = onAuthorClick),
                    fontFamily = bodyFont,
                    fontSize = 13.sp,
                    color = colors.muted
                )
                Row(
                    modifier = Modifier.padding(top = 14.dp),
                    horizontalArrangement = Arrangement.spacedBy(7.dp)
                ) {
                    book.tags.forEach { tag ->
                        Box(modifier = Modifier.clickable(onClick = onTagsBookClick)) {
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
                    moreLikeThisCovers.forEach { cover ->
                        Image(
                            painter = painterResource(cover),
                            contentDescription = null,
                            contentScale = ContentScale.Crop,
                            modifier = Modifier
                                .size(width = 64.dp, height = 94.dp)
                                .shadow(6.dp, RoundedCornerShape(InkShape.cover), clip = true)
                                .clickable(onClick = onTagsBookClick)
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
                        .clickable(onClick = onPurchaseClick),
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
                        .clickable(onClick = onViewSampleClick),
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
