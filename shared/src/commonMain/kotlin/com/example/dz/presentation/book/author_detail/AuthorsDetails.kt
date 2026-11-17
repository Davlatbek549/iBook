package com.example.dz.presentation.book.author_detail

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.dz.designsystem.components.icons.InkIcons
import com.example.dz.designsystem.components.organic.*
import com.example.dz.designsystem.theme.*
import dz.shared.generated.resources.*
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

/** No author frame exists in the handoff; the sage identity panel follows Friend detail. */
@Composable
fun AuthorsDetailsScreen(
    uiState: AuthorDetailUiState = AuthorDetailUiState(),
    onEvent: (AuthorDetailEvent) -> Unit = {},
    modifier: Modifier = Modifier,
) {
    OrganicScreen(modifier) {
        LazyColumn(Modifier.fillMaxSize().navigationBarsPadding(), contentPadding = PaddingValues(bottom = 28.dp),
            verticalArrangement = Arrangement.spacedBy(22.dp)) {
            item {
                Column(Modifier.fillMaxWidth().clip(RoundedCornerShape(bottomStart = 34.dp, bottomEnd = 34.dp))
                    .background(OrganicColors.accent2_200).padding(horizontal = 24.dp, vertical = 16.dp),
                    verticalArrangement = Arrangement.spacedBy(18.dp)) {
                    OrganicCircleIconButton(InkIcons.Back, { onEvent(AuthorDetailEvent.BackClicked) },
                        stringResource(Res.string.author_details_back), size = 44.dp, background = OrganicColors.bg)
                    Row(horizontalArrangement = Arrangement.spacedBy(16.dp), verticalAlignment = Alignment.CenterVertically) {
                        Box(Modifier.size(76.dp).clip(CircleShape).background(OrganicColors.accent2_700), contentAlignment = Alignment.Center) {
                            val avatar = uiState.avatarRes
                            if (avatar != null) Image(painterResource(avatar), null, Modifier.fillMaxSize(), contentScale = ContentScale.Crop)
                            else Text(uiState.name.take(1).uppercase(), color = OrganicColors.bg, fontFamily = organicHeadingFontFamily(), fontSize = 32.sp)
                        }
                        Column(Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(6.dp)) {
                            OrganicTitle(uiState.name.ifBlank { stringResource(Res.string.organic_author_title) }, fontSize = 26.sp)
                            if (uiState.tagline.isNotBlank()) AuthorBody(uiState.tagline, size = 13)
                        }
                    }
                    if (!uiState.isLoading && uiState.errorMessage == null && uiState.name.isNotBlank()) {
                        OrganicButton(
                            text = stringResource(if (uiState.isFollowing) Res.string.organic_author_following else Res.string.author_follow),
                            onClick = { onEvent(AuthorDetailEvent.FollowClicked) },
                            background = if (uiState.isFollowing) OrganicColors.accent2_700 else OrganicColors.accent,
                        )
                    }
                }
            }
            if (uiState.isLoading) {
                item { Box(Modifier.fillMaxWidth().padding(32.dp), contentAlignment = Alignment.Center) { CircularProgressIndicator(color = OrganicColors.accent) } }
            } else if (uiState.errorMessage != null) {
                item { OrganicCard(Modifier.padding(horizontal = 24.dp).fillMaxWidth(), background = OrganicColors.accent100) { AuthorBody(uiState.errorMessage) } }
            } else {
                item {
                    if (LocalDensity.current.fontScale > 1.3f) {
                        Column(Modifier.padding(horizontal = 24.dp).fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                            AuthorStat(uiState.booksCount, stringResource(Res.string.author_stat_books), Modifier.fillMaxWidth(), horizontal = true)
                            AuthorStat(uiState.readersCount, stringResource(Res.string.author_stat_readers), Modifier.fillMaxWidth(), horizontal = true)
                            AuthorStat(uiState.averageRating, stringResource(Res.string.author_stat_avg), Modifier.fillMaxWidth(), horizontal = true)
                        }
                    } else {
                        Row(Modifier.padding(horizontal = 24.dp).fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                            AuthorStat(uiState.booksCount, stringResource(Res.string.author_stat_books), Modifier.weight(1f))
                            AuthorStat(uiState.readersCount, stringResource(Res.string.author_stat_readers), Modifier.weight(1f))
                            AuthorStat(uiState.averageRating, stringResource(Res.string.author_stat_avg), Modifier.weight(1f))
                        }
                    }
                }
                item {
                    Column(Modifier.padding(horizontal = 24.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                        OrganicSectionHeader(stringResource(Res.string.detail_about))
                        AuthorBody(uiState.about.ifBlank { stringResource(Res.string.organic_author_no_bio) })
                    }
                }
                item {
                    Column(verticalArrangement = Arrangement.spacedBy(14.dp)) {
                        OrganicSectionHeader(stringResource(Res.string.author_by_this), Modifier.padding(horizontal = 24.dp))
                        if (uiState.books.isEmpty()) {
                            AuthorBody(stringResource(Res.string.organic_author_no_books), Modifier.padding(horizontal = 24.dp))
                        } else {
                            LazyRow(contentPadding = PaddingValues(horizontal = 24.dp), horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                                items(uiState.books, key = { it.id }) { book ->
                                    Column(Modifier.width(112.dp).clip(RoundedCornerShape(18.dp))
                                        .clickable(role = Role.Button) { onEvent(AuthorDetailEvent.BookClicked(book.id)) },
                                        verticalArrangement = Arrangement.spacedBy(8.dp)) {
                                        Image(painterResource(book.coverRes), null, Modifier.size(112.dp, 150.dp).clip(RoundedCornerShape(18.dp)), contentScale = ContentScale.Crop)
                                        Text(book.title, fontFamily = organicBodyFontFamily(), fontWeight = FontWeight.SemiBold,
                                            fontSize = 13.sp, color = OrganicColors.text, maxLines = 2, overflow = TextOverflow.Ellipsis)
                                        if (book.rating.isNotBlank()) AuthorBody("★ ${book.rating}", size = 12)
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun AuthorStat(value: String, label: String, modifier: Modifier, horizontal: Boolean = false) {
    OrganicCard(modifier, contentPadding = PaddingValues(14.dp)) {
        if (horizontal) {
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(16.dp), verticalAlignment = Alignment.CenterVertically) {
                Text(value.ifBlank { "—" }, fontFamily = organicHeadingFontFamily(), fontSize = 24.sp, color = OrganicColors.accent700)
                AuthorBody(label, Modifier.weight(1f), size = 12)
            }
        } else {
            Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                Text(value.ifBlank { "—" }, fontFamily = organicHeadingFontFamily(), fontSize = 24.sp, color = OrganicColors.accent700)
                AuthorBody(label, size = 12)
            }
        }
    }
}

@Composable
private fun AuthorBody(text: String, modifier: Modifier = Modifier, size: Int = 15) {
    Text(text, modifier, fontFamily = organicBodyFontFamily(), fontSize = size.sp,
        lineHeight = (size * 1.6f).sp, color = OrganicColors.neutral700)
}

@Preview(showBackground = true, widthDp = 390, heightDp = 844)
@Composable
private fun AuthorsDetailsScreenPreview() { AuthorsDetailsScreen(sampleAuthorDetail("preview")) }
