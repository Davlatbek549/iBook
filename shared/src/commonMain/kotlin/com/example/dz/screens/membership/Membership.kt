package com.example.dz.screens.membership

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
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.dz.theme.ColorCategoryFantasy
import com.example.dz.theme.ColorSecondary
import com.example.dz.theme.ColorTertiary
import com.example.dz.theme.DZTheme
import dz.shared.generated.resources.Res
import dz.shared.generated.resources.*
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

private data class MembershipNote(val text: StringResource)

@Composable
fun MembershipScreen(
    modifier: Modifier = Modifier,
    onBackClick: () -> Unit = {}
) {
    val colors = MaterialTheme.colorScheme
    val notes = listOf(
        MembershipNote(Res.string.membership_note_tags),
        MembershipNote(Res.string.membership_note_audio),
        MembershipNote(Res.string.membership_note_cancel)
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
                .padding(horizontal = 23.dp)
                .padding(top = 38.dp, bottom = 24.dp)
        ) {
            BackButton(onClick = onBackClick)

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = stringResource(Res.string.membership_title),
                color = colors.onPrimary,
                style = MaterialTheme.typography.displaySmall,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(24.dp))

            MembershipHeroCard()

            Spacer(modifier = Modifier.height(20.dp))

            SectionTitle(text = stringResource(Res.string.membership_go_premium_title))

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                PremiumPlanCard(
                    modifier = Modifier.weight(1f),
                    title = stringResource(Res.string.membership_month_premium),
                    price = stringResource(Res.string.membership_month_price),
                    period = stringResource(Res.string.membership_per_month),
                    save = stringResource(Res.string.membership_save_10)
                )

                PremiumPlanCard(
                    modifier = Modifier.weight(1f),
                    title = stringResource(Res.string.membership_year_plan),
                    price = stringResource(Res.string.membership_year_price),
                    period = stringResource(Res.string.membership_per_year),
                    save = stringResource(Res.string.membership_save_25),
                    showBestValue = true
                )
            }

            Spacer(modifier = Modifier.height(14.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                PremiumButton(modifier = Modifier.weight(1f))
                PremiumButton(modifier = Modifier.weight(1f))
            }

            Spacer(modifier = Modifier.height(24.dp))

            SectionTitle(text = stringResource(Res.string.membership_you_need_know))

            Spacer(modifier = Modifier.height(16.dp))

            notes.forEach { note ->
                MembershipNoteRow(text = stringResource(note.text))
                Spacer(modifier = Modifier.height(12.dp))
            }
        }
    }
}

@Composable
private fun BackButton(onClick: () -> Unit) {
    Surface(
        onClick = onClick,
        shape = CircleShape,
        color = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.12f),
        tonalElevation = 0.dp,
        shadowElevation = 0.dp,
        modifier = Modifier.size(34.dp)
    ) {
        Box(contentAlignment = Alignment.Center) {
            Icon(
                painter = painterResource(Res.drawable.ic_back),
                contentDescription = stringResource(Res.string.membership_back),
                tint = MaterialTheme.colorScheme.onPrimary,
                modifier = Modifier.size(20.dp)
            )
        }
    }
}

@Composable
private fun MembershipHeroCard() {
    val colors = MaterialTheme.colorScheme

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(142.dp)
            .clip(RoundedCornerShape(16.dp))
            .border(
                width = 1.3.dp,
                color = colors.onPrimary.copy(alpha = 0.48f),
                shape = RoundedCornerShape(16.dp)
            )
            .background(colors.onPrimary.copy(alpha = 0.10f))
    ) {
        Box(
            modifier = Modifier
                .size(110.dp)
                .offset(x = 72.dp, y = 48.dp)
                .clip(CircleShape)
                .background(
                    Brush.radialGradient(
                        colors = listOf(ColorCategoryFantasy.copy(alpha = 0.84f), Color.Transparent)
                    )
                )
        )

        Column(
            modifier = Modifier
                .align(Alignment.BottomStart)
                .padding(start = 15.dp, bottom = 15.dp)
        ) {
            Image(
                painter = painterResource(Res.drawable.book_olive_again),
                contentDescription = stringResource(Res.string.membership_profile_image),
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(44.dp)
                    .clip(CircleShape)
                    .border(1.4.dp, ColorSecondary.copy(alpha = 0.8f), CircleShape)
            )

            Spacer(modifier = Modifier.height(16.dp))

            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = stringResource(Res.string.membership_username),
                    color = colors.onPrimary,
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold
                )
                Icon(
                    painter = painterResource(Res.drawable.ic_premium),
                    contentDescription = null,
                    tint = colors.onPrimary,
                    modifier = Modifier.size(13.dp)
                )
            }

            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(50))
                    .background(colors.onPrimary)
                    .padding(horizontal = 8.dp, vertical = 2.dp)
            ) {
                Text(
                    text = stringResource(Res.string.membership_year_premium),
                    color = colors.primary,
                    style = MaterialTheme.typography.labelSmall.copy(fontSize = 8.sp, lineHeight = 9.sp),
                    fontWeight = FontWeight.Bold
                )
            }
        }

        Column(
            modifier = Modifier
                .align(Alignment.CenterEnd)
                .padding(end = 30.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = stringResource(Res.string.membership_tags_count),
                color = colors.onPrimary,
                style = MaterialTheme.typography.displayLarge.copy(fontSize = 58.sp, lineHeight = 58.sp),
                fontWeight = FontWeight.Bold
            )
            Text(
                text = stringResource(Res.string.membership_tags_book),
                color = colors.onPrimary,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Composable
private fun SectionTitle(text: String) {
    Text(
        text = text,
        color = MaterialTheme.colorScheme.onPrimary,
        style = MaterialTheme.typography.titleMedium,
        fontWeight = FontWeight.Bold
    )
}

@Composable
private fun PremiumPlanCard(
    title: String,
    price: String,
    period: String,
    save: String,
    modifier: Modifier = Modifier,
    showBestValue: Boolean = false
) {
    val colors = MaterialTheme.colorScheme

    Box(
        modifier = modifier
            .height(140.dp)
            .clip(RoundedCornerShape(14.dp))
            .border(
                width = 1.1.dp,
                color = colors.onPrimary.copy(alpha = 0.55f),
                shape = RoundedCornerShape(14.dp)
            )
            .background(colors.onPrimary.copy(alpha = 0.12f))
            .padding(horizontal = 14.dp, vertical = 12.dp)
    ) {
        Box(
            modifier = Modifier
                .size(88.dp)
                .align(Alignment.BottomCenter)
                .offset(y = 28.dp)
                .clip(CircleShape)
                .background(
                    Brush.radialGradient(
                        colors = listOf(ColorCategoryFantasy.copy(alpha = 0.70f), Color.Transparent)
                    )
                )
        )

        Column(modifier = Modifier.fillMaxSize()) {
            Text(
                text = title,
                color = colors.onPrimary,
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Bold,
                maxLines = 1
            )

            Spacer(modifier = Modifier.height(12.dp))

            Row(verticalAlignment = Alignment.Bottom) {
                Text(
                    text = stringResource(Res.string.membership_currency),
                    color = colors.onPrimary,
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier.padding(bottom = 9.dp)
                )
                Text(
                    text = price,
                    color = colors.onPrimary,
                    style = MaterialTheme.typography.displayLarge.copy(fontSize = 50.sp, lineHeight = 48.sp),
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = period,
                    color = colors.onPrimary,
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier.padding(bottom = 7.dp)
                )
            }

            Spacer(modifier = Modifier.weight(1f))

            Text(
                text = save,
                color = colors.onPrimary.copy(alpha = 0.86f),
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )

            if (showBestValue) {
                Spacer(modifier = Modifier.height(3.dp))
                Box(
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally)
                        .clip(RoundedCornerShape(50))
                        .background(colors.onPrimary)
                        .padding(horizontal = 8.dp, vertical = 2.dp)
                ) {
                    Text(
                        text = stringResource(Res.string.membership_best_value),
                        color = colors.primary,
                        style = MaterialTheme.typography.labelSmall.copy(fontSize = 8.sp, lineHeight = 9.sp),
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}

@Composable
private fun PremiumButton(modifier: Modifier = Modifier) {
    Surface(
        onClick = {},
        shape = RoundedCornerShape(14.dp),
        color = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.10f),
        border = androidx.compose.foundation.BorderStroke(
            width = 1.1.dp,
            color = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.55f)
        ),
        tonalElevation = 0.dp,
        shadowElevation = 0.dp,
        modifier = modifier.height(38.dp)
    ) {
        Box(contentAlignment = Alignment.Center) {
            Text(
                text = stringResource(Res.string.membership_go_premium_button),
                color = MaterialTheme.colorScheme.onPrimary,
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
private fun MembershipNoteRow(text: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.Top
    ) {
        Icon(
            painter = painterResource(Res.drawable.ic_premium),
            contentDescription = null,
            tint = MaterialTheme.colorScheme.onPrimary,
            modifier = Modifier.size(14.dp)
        )

        Spacer(modifier = Modifier.width(9.dp))

        Text(
            text = text,
            color = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.88f),
            style = MaterialTheme.typography.bodySmall,
            lineHeight = 16.sp
        )
    }
}

@Preview(showBackground = true, widthDp = 375, heightDp = 820)
@Composable
private fun MembershipScreenPreview() {
    DZTheme {
        MembershipScreen()
    }
}
