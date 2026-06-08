package com.example.dz.screens.authors_details

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.dz.theme.ColorCategoryFantasy
import com.example.dz.theme.ColorSecondary
import com.example.dz.theme.ColorTertiary
import com.example.dz.theme.DZTheme
import dz.shared.generated.resources.Res
import dz.shared.generated.resources.*
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

private data class AuthorBook(
    val title: StringResource,
    val author: StringResource,
    val views: StringResource,
    val firstGenre: StringResource,
    val secondGenre: StringResource,
    val cover: DrawableResource
)

@Composable
fun AuthorsDetailsScreen(
    modifier: Modifier = Modifier,
    onBackClick: () -> Unit = {}
) {
    val colors = MaterialTheme.colorScheme
    val books = listOf(
        AuthorBook(
            title = Res.string.author_details_book_red_bone,
            author = Res.string.author_details_book_red_bone_author,
            views = Res.string.author_details_views_1320,
            firstGenre = Res.string.author_details_genre_horror,
            secondGenre = Res.string.author_details_genre_fantasy,
            cover = Res.drawable.author_detail_red_at_bone
        ),
        AuthorBook(
            title = Res.string.author_details_book_bestiary,
            author = Res.string.author_details_book_bestiary_author,
            views = Res.string.author_details_views_1320,
            firstGenre = Res.string.author_details_genre_horror,
            secondGenre = Res.string.author_details_genre_fantasy,
            cover = Res.drawable.author_detail_bestiary
        ),
        AuthorBook(
            title = Res.string.author_details_book_undocumented,
            author = Res.string.author_details_book_undocumented_author,
            views = Res.string.author_details_views_1320,
            firstGenre = Res.string.author_details_genre_horror,
            secondGenre = Res.string.author_details_genre_fantasy,
            cover = Res.drawable.book_olive_again
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
                .padding(horizontal = 38.dp)
                .padding(top = 56.dp, bottom = 28.dp)
        ) {
            CircleIconButton(onClick = onBackClick)

            Spacer(modifier = Modifier.height(17.dp))

            AuthorHeader()

            Spacer(modifier = Modifier.height(42.dp))

            SectionTitle(text = stringResource(Res.string.author_details_about))

            Spacer(modifier = Modifier.height(22.dp))

            AboutText()

            Spacer(modifier = Modifier.height(25.dp))

            SectionTitle(text = stringResource(Res.string.author_details_book))

            Spacer(modifier = Modifier.height(31.dp))

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
        modifier = Modifier.size(52.dp)
    ) {
        Box(contentAlignment = Alignment.Center) {
            Icon(
                painter = painterResource(Res.drawable.ic_back),
                contentDescription = stringResource(Res.string.author_details_back),
                tint = MaterialTheme.colorScheme.onPrimary,
                modifier = Modifier.size(25.dp)
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
            painter = painterResource(Res.drawable.author_detail_patricia_avatar),
            contentDescription = stringResource(Res.string.author_details_profile_image),
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .size(96.dp)
                .clip(CircleShape)
        )

        Spacer(modifier = Modifier.width(28.dp))

        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = stringResource(Res.string.author_details_name),
                color = colors.onPrimary,
                style = MaterialTheme.typography.headlineLarge.copy(
                    fontSize = 31.sp,
                    lineHeight = 36.sp
                ),
                fontWeight = FontWeight.Bold,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )

            Spacer(modifier = Modifier.height(16.dp))

            Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                HeaderGenreTag(
                    text = stringResource(Res.string.author_details_genre_horror),
                    contentColor = ColorTertiary
                )
                HeaderGenreTag(
                    text = stringResource(Res.string.author_details_genre_romantic),
                    contentColor = ColorSecondary
                )
            }
        }
    }
}

@Composable
private fun HeaderGenreTag(
    text: String,
    contentColor: Color
) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(50))
            .background(MaterialTheme.colorScheme.onPrimary)
            .padding(horizontal = 16.dp, vertical = 4.dp)
    ) {
        Text(
            text = text,
            color = contentColor,
            style = MaterialTheme.typography.labelSmall.copy(fontSize = 12.sp, lineHeight = 14.sp),
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
private fun SectionTitle(text: String) {
    Text(
        text = text,
        color = MaterialTheme.colorScheme.onPrimary,
        style = MaterialTheme.typography.titleMedium.copy(
            fontSize = 22.sp,
            lineHeight = 27.sp
        ),
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
            withStyle(boldStyle) { append(stringResource(Res.string.author_details_date_of_birth_label)) }
            withStyle(normalStyle) {
                append(" ")
                append(stringResource(Res.string.author_details_date_of_birth))
                append("\n")
            }
            withStyle(boldStyle) { append(stringResource(Res.string.author_details_nationality_label)) }
            withStyle(normalStyle) {
                append(" ")
                append(stringResource(Res.string.author_details_nationality))
                append("\n")
                append(stringResource(Res.string.author_details_bio))
            }
        },
        style = MaterialTheme.typography.bodyMedium.copy(fontSize = 16.sp),
        lineHeight = 23.sp
    )
}

@Composable
private fun BooksCard(books: List<AuthorBook>) {
    val colors = MaterialTheme.colorScheme

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(28.dp),
        colors = CardDefaults.cardColors(containerColor = colors.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Column(
            modifier = Modifier.padding(horizontal = 27.dp, vertical = 34.dp)
        ) {
            books.forEachIndexed { index, book ->
                AuthorBookRow(book = book)

                if (index != books.lastIndex) {
                    Spacer(modifier = Modifier.height(24.dp))
                    HorizontalDivider(color = colors.outline.copy(alpha = 0.24f))
                    Spacer(modifier = Modifier.height(24.dp))
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
                .width(101.dp)
                .height(139.dp)
                .clip(RoundedCornerShape(8.dp))
        )

        Spacer(modifier = Modifier.width(27.dp))

        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = stringResource(book.title),
                color = colors.onSurface.copy(alpha = 0.72f),
                style = MaterialTheme.typography.titleMedium.copy(
                    fontSize = 22.sp,
                    lineHeight = 26.sp
                ),
                fontWeight = FontWeight.Bold,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )

            Spacer(modifier = Modifier.height(3.dp))

            Text(
                text = stringResource(book.author),
                color = colors.onSurface.copy(alpha = 0.72f),
                style = MaterialTheme.typography.bodyMedium.copy(fontSize = 15.sp, lineHeight = 19.sp)
            )

            Spacer(modifier = Modifier.height(25.dp))

            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    painter = painterResource(Res.drawable.ic_visibility_on),
                    contentDescription = stringResource(Res.string.author_details_views),
                    tint = colors.primary,
                    modifier = Modifier.size(18.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = stringResource(book.views),
                    color = colors.onSurface.copy(alpha = 0.65f),
                    style = MaterialTheme.typography.bodyMedium.copy(fontSize = 16.sp, lineHeight = 19.sp),
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.height(11.dp))

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
            .padding(horizontal = 19.dp, vertical = 5.dp)
    ) {
        Text(
            text = text,
            color = MaterialTheme.colorScheme.onPrimary,
            style = MaterialTheme.typography.labelSmall.copy(fontSize = 12.sp, lineHeight = 14.sp),
            fontWeight = FontWeight.Bold
        )
    }
}

@Preview(showBackground = true, widthDp = 375, heightDp = 820)
@Composable
private fun AuthorsDetailsScreenPreview() {
    DZTheme {
        AuthorsDetailsScreen()
    }
}
