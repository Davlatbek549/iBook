package com.example.ibook.screens.authorsDetatil

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material.icons.filled.RemoveRedEye
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ibook.R
import com.example.ibook.ui.theme.ColorCategoryFantasy
import com.example.ibook.ui.theme.ColorSecondary
import com.example.ibook.ui.theme.ColorTertiary
import com.example.ibook.ui.theme.IBookTheme

private data class AuthorBook(
    @param:StringRes val title: Int,
    @param:StringRes val author: Int,
    @param:StringRes val views: Int,
    @param:StringRes val firstGenre: Int,
    @param:StringRes val secondGenre: Int,
    @param:DrawableRes val cover: Int
)

@Composable
fun AuthorsDetailsScreen(
    modifier: Modifier = Modifier,
    onBackClick: () -> Unit = {}
) {
    val colors = MaterialTheme.colorScheme
    val books = listOf(
        AuthorBook(
            title = R.string.author_details_book_red_bone,
            author = R.string.author_details_book_red_bone_author,
            views = R.string.author_details_views_1320,
            firstGenre = R.string.author_details_genre_horror,
            secondGenre = R.string.author_details_genre_fantasy,
            cover = R.drawable.olive_again_book
        ),
        AuthorBook(
            title = R.string.author_details_book_bestiary,
            author = R.string.author_details_book_bestiary_author,
            views = R.string.author_details_views_1320,
            firstGenre = R.string.author_details_genre_horror,
            secondGenre = R.string.author_details_genre_fantasy,
            cover = R.drawable.app_img
        ),
        AuthorBook(
            title = R.string.author_details_book_undocumented,
            author = R.string.author_details_book_undocumented_author,
            views = R.string.author_details_views_1320,
            firstGenre = R.string.author_details_genre_horror,
            secondGenre = R.string.author_details_genre_fantasy,
            cover = R.drawable.book_olive_again
        )
    )

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(colors.primary)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding()
                .navigationBarsPadding()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 16.dp)
                .padding(top = 28.dp, bottom = 0.dp)
        ) {
            CircleIconButton(onClick = onBackClick)

            Spacer(modifier = Modifier.height(10.dp))

            AuthorHeader()

            Spacer(modifier = Modifier.height(22.dp))

            SectionTitle(text = stringResource(R.string.author_details_about))

            Spacer(modifier = Modifier.height(12.dp))

            AboutText()

            Spacer(modifier = Modifier.height(18.dp))

            SectionTitle(text = stringResource(R.string.author_details_book))

            Spacer(modifier = Modifier.height(16.dp))

            BooksCard(books = books)
        }
    }
}

@Composable
private fun CircleIconButton(onClick: () -> Unit) {
    Surface(
        onClick = onClick,
        shape = CircleShape,
        color = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.12f),
        tonalElevation = 0.dp,
        shadowElevation = 0.dp,
        modifier = Modifier.size(24.dp)
    ) {
        Box(contentAlignment = Alignment.Center) {
            Icon(
                imageVector = Icons.Default.ArrowBackIosNew,
                contentDescription = stringResource(R.string.author_details_back),
                tint = MaterialTheme.colorScheme.onPrimary,
                modifier = Modifier.size(15.dp)
            )
        }
    }
}

@Composable
private fun AuthorHeader() {
    val colors = MaterialTheme.colorScheme

    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            painter = painterResource(R.drawable.book_olive_again),
            contentDescription = stringResource(R.string.author_details_profile_image),
                contentScale = ContentScale.Crop,
                modifier = Modifier
                .size(64.dp)
                .clip(CircleShape)
                .border(3.dp, colors.onPrimary, CircleShape)
        )

        Spacer(modifier = Modifier.width(12.dp))

        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = stringResource(R.string.author_details_name),
                color = colors.onPrimary,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )

            Spacer(modifier = Modifier.height(6.dp))

            Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                SmallGenreTag(
                    text = stringResource(R.string.author_details_genre_horror),
                    color = ColorTertiary
                )
                SmallGenreTag(
                    text = stringResource(R.string.author_details_genre_romantic),
                    color = ColorSecondary
                )
            }
        }
    }
}

@Composable
private fun SectionTitle(text: String) {
    Text(
        text = text,
        color = MaterialTheme.colorScheme.onPrimary,
        style = MaterialTheme.typography.labelLarge,
        fontWeight = FontWeight.Bold
    )
}

@Composable
private fun AboutText() {
    val colors = MaterialTheme.colorScheme
    val boldStyle = SpanStyle(
        color = colors.onPrimary,
        fontWeight = FontWeight.Bold
    )
    val normalStyle = SpanStyle(color = colors.onPrimary.copy(alpha = 0.88f))

    Text(
        text = buildAnnotatedString {
            withStyle(boldStyle) { append(stringResource(R.string.author_details_date_of_birth_label)) }
            withStyle(normalStyle) {
                append(" ")
                append(stringResource(R.string.author_details_date_of_birth))
                append("\n")
            }
            withStyle(boldStyle) { append(stringResource(R.string.author_details_nationality_label)) }
            withStyle(normalStyle) {
                append(" ")
                append(stringResource(R.string.author_details_nationality))
                append("\n")
                append(stringResource(R.string.author_details_bio))
            }
        },
        style = MaterialTheme.typography.labelSmall.copy(fontSize = 9.sp),
        lineHeight = 13.sp
    )
}

@Composable
private fun BooksCard(books: List<AuthorBook>) {
    val colors = MaterialTheme.colorScheme

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp),
        colors = CardDefaults.cardColors(containerColor = colors.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Column(
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 18.dp)
        ) {
            books.forEachIndexed { index, book ->
                AuthorBookRow(book = book)

                if (index != books.lastIndex) {
                    Spacer(modifier = Modifier.height(12.dp))
                    HorizontalDivider(color = colors.outline.copy(alpha = 0.24f))
                    Spacer(modifier = Modifier.height(12.dp))
                }
            }
        }
    }
}

@Composable
private fun AuthorBookRow(book: AuthorBook) {
    val colors = MaterialTheme.colorScheme

    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.Top
    ) {
        Image(
            painter = painterResource(book.cover),
            contentDescription = stringResource(book.title),
                contentScale = ContentScale.Crop,
                modifier = Modifier
                .width(48.dp)
                .height(73.dp)
                .clip(RoundedCornerShape(6.dp))
        )

        Spacer(modifier = Modifier.width(14.dp))

        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = stringResource(book.title),
                color = colors.onSurface,
                style = MaterialTheme.typography.labelLarge,
                fontWeight = FontWeight.Bold,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )

            Text(
                text = stringResource(book.author),
                color = colors.onSurface.copy(alpha = 0.72f),
                style = MaterialTheme.typography.labelSmall.copy(fontSize = 9.sp)
            )

            Spacer(modifier = Modifier.height(8.dp))

            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.RemoveRedEye,
                    contentDescription = stringResource(R.string.author_details_views),
                    tint = colors.primary,
                    modifier = Modifier.size(12.dp)
                )
                Spacer(modifier = Modifier.width(3.dp))
                Text(
                    text = stringResource(book.views),
                    color = colors.onSurface.copy(alpha = 0.65f),
                    style = MaterialTheme.typography.labelSmall.copy(fontSize = 9.sp)
                )
            }

            Spacer(modifier = Modifier.height(5.dp))

            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                SmallGenreTag(text = stringResource(book.firstGenre), color = ColorTertiary)
                SmallGenreTag(text = stringResource(book.secondGenre), color = ColorCategoryFantasy)
            }
        }
    }
}

@Composable
private fun SmallGenreTag(
    text: String,
    color: Color
) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(50))
            .background(color)
            .padding(horizontal = 9.dp, vertical = 2.dp)
    ) {
        Text(
            text = text,
            color = MaterialTheme.colorScheme.onPrimary,
            style = MaterialTheme.typography.labelSmall.copy(fontSize = 7.sp, lineHeight = 9.sp),
            fontWeight = FontWeight.Bold
        )
    }
}

@Preview(showBackground = true, widthDp = 375, heightDp = 820)
@Composable
private fun AuthorsDetailsScreenPreview() {
    IBookTheme {
        AuthorsDetailsScreen()
    }
}
