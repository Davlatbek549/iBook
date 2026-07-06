package com.example.dz.presentation.book.author_detail

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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.dz.designsystem.components.icons.InkIcons
import com.example.dz.designsystem.components.ink.InkIconButton
import com.example.dz.designsystem.components.ink.InkLabel
import com.example.dz.designsystem.components.ink.InkSectionTitle
import com.example.dz.designsystem.components.ink.inkCard
import com.example.dz.designsystem.theme.InkColors
import com.example.dz.designsystem.theme.InkShape
import com.example.dz.designsystem.theme.inkBodyFontFamily
import com.example.dz.designsystem.theme.inkColors
import com.example.dz.designsystem.theme.inkDisplayFontFamily
import dz.shared.generated.resources.Res
import dz.shared.generated.resources.author_by_this
import dz.shared.generated.resources.author_follow
import dz.shared.generated.resources.author_stat_avg
import dz.shared.generated.resources.author_stat_books
import dz.shared.generated.resources.author_stat_readers
import dz.shared.generated.resources.detail_about
import dz.shared.generated.resources.home_see_all
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

@Composable
fun AuthorsDetailsScreen(
    uiState: AuthorDetailUiState = AuthorDetailUiState(),
    onEvent: (AuthorDetailEvent) -> Unit = {},
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
            .verticalScroll(rememberScrollState())
            .padding(bottom = 30.dp)
    ) {
        // top bar
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 22.dp, end = 22.dp, top = 4.dp)
        ) {
            InkIconButton(
                icon = InkIcons.Back,
                onClick = { onEvent(AuthorDetailEvent.BackClicked) },
                colors = colors
            )
            Spacer(modifier = Modifier.weight(1f))
            InkIconButton(
                icon = InkIcons.Share,
                onClick = { onEvent(AuthorDetailEvent.ShareClicked) },
                colors = colors
            )
        }

        // identity
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 26.dp, end = 26.dp, top = 14.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            uiState.avatarRes?.let { avatar ->
                Image(
                    painter = painterResource(avatar),
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .size(96.dp)
                        .shadow(14.dp, RoundedCornerShape(InkShape.radiusSm + 6.dp), clip = true)
                )
            }
            Text(
                text = uiState.name,
                modifier = Modifier.padding(top = 18.dp),
                fontFamily = displayFont,
                fontWeight = FontWeight.Medium,
                fontSize = 26.sp,
                color = colors.ink
            )
            Text(
                text = uiState.tagline,
                modifier = Modifier.padding(top = 8.dp),
                fontFamily = displayFont,
                fontStyle = FontStyle.Italic,
                fontSize = 12.5.sp,
                color = colors.muted
            )
        }

        // stats
        Row(
            modifier = Modifier
                .padding(start = 22.dp, end = 22.dp, top = 22.dp)
                .fillMaxWidth()
                .height(IntrinsicSize.Min)
                .inkCard(colors)
                .padding(vertical = 15.dp)
        ) {
            StatCell(value = uiState.booksCount, label = stringResource(Res.string.author_stat_books), colors = colors, modifier = Modifier.weight(1f))
            VerticalDivider(modifier = Modifier.fillMaxHeight(), thickness = 1.dp, color = colors.line)
            StatCell(value = uiState.readersCount, label = stringResource(Res.string.author_stat_readers), colors = colors, modifier = Modifier.weight(1f))
            VerticalDivider(modifier = Modifier.fillMaxHeight(), thickness = 1.dp, color = colors.line)
            StatCell(value = uiState.averageRating, label = stringResource(Res.string.author_stat_avg), colors = colors, modifier = Modifier.weight(1f))
        }

        // actions
        Row(
            modifier = Modifier.padding(start = 22.dp, end = 22.dp, top = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Box(
                modifier = Modifier
                    .weight(1f)
                    .height(46.dp)
                    .clip(RoundedCornerShape(InkShape.radiusSm + 2.dp))
                    .background(if (uiState.isFollowing) colors.alt else colors.accent)
                    .clickable { onEvent(AuthorDetailEvent.FollowClicked) },
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = if (uiState.isFollowing) "Following" else stringResource(Res.string.author_follow),
                    fontFamily = bodyFont,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 14.sp,
                    color = if (uiState.isFollowing) colors.ink else colors.onAccent
                )
            }
            Box(
                modifier = Modifier
                    .size(46.dp)
                    .clip(RoundedCornerShape(InkShape.radiusSm + 2.dp))
                    .border(1.dp, colors.line, RoundedCornerShape(InkShape.radiusSm + 2.dp))
                    .clickable { onEvent(AuthorDetailEvent.MessageClicked) },
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = InkIcons.Message,
                    contentDescription = null,
                    tint = colors.ink,
                    modifier = Modifier.size(18.dp)
                )
            }
        }

        // about
        Column(modifier = Modifier.padding(start = 22.dp, end = 22.dp, top = 24.dp)) {
            InkLabel(text = stringResource(Res.string.detail_about), colors = colors)
            Text(
                text = uiState.about,
                modifier = Modifier.padding(top = 11.dp),
                fontFamily = bodyFont,
                fontSize = 13.5.sp,
                lineHeight = 23.sp,
                maxLines = 3,
                overflow = TextOverflow.Ellipsis,
                color = colors.inkSoft
            )
        }

        // by this author
        Column(modifier = Modifier.padding(top = 22.dp)) {
            InkSectionTitle(
                text = stringResource(Res.string.author_by_this),
                modifier = Modifier.padding(horizontal = 22.dp),
                action = stringResource(Res.string.home_see_all),
                onActionClick = { onEvent(AuthorDetailEvent.SeeAllClicked) },
                colors = colors
            )
            Row(
                modifier = Modifier
                    .padding(top = 14.dp)
                    .horizontalScroll(rememberScrollState())
                    .padding(horizontal = 22.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                uiState.books.forEach { book ->
                    Column(modifier = Modifier.width(104.dp).clickable { onEvent(AuthorDetailEvent.BookClicked(book.id)) }) {
                        Image(
                            painter = painterResource(book.coverRes),
                            contentDescription = null,
                            contentScale = ContentScale.Crop,
                            modifier = Modifier
                                .size(width = 104.dp, height = 152.dp)
                                .shadow(8.dp, RoundedCornerShape(InkShape.cover), clip = true)
                        )
                        Text(
                            text = book.title,
                            modifier = Modifier.padding(top = 9.dp),
                            fontFamily = displayFont,
                            fontWeight = FontWeight.Medium,
                            fontSize = 13.sp,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                            color = colors.ink
                        )
                        Row(
                            modifier = Modifier.padding(top = 5.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(5.dp)
                        ) {
                            Icon(
                                imageVector = InkIcons.Star,
                                contentDescription = null,
                                tint = colors.accent,
                                modifier = Modifier.size(11.dp)
                            )
                            Text(
                                text = book.rating,
                                fontFamily = bodyFont,
                                fontWeight = FontWeight.SemiBold,
                                fontSize = 11.sp,
                                color = colors.inkSoft
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun StatCell(
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
            fontSize = 17.sp,
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
private fun AuthorsDetailsScreenPreview() {
    AuthorsDetailsScreen(uiState = sampleAuthorDetail("preview"))
}
