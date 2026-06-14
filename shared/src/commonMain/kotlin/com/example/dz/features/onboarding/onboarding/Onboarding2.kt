package com.example.dz.features.onboarding.onboarding

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.dz.app_components.icons.InkIcons
import com.example.dz.app_components.ink.InkChip
import com.example.dz.app_components.ink.InkLabel
import com.example.dz.app_components.ink.inkCard
import com.example.dz.theme.InkShape
import com.example.dz.theme.inkBodyFontFamily
import com.example.dz.theme.inkColors
import dz.shared.generated.resources.Res
import dz.shared.generated.resources.book_cover_2
import dz.shared.generated.resources.book_cover_4
import dz.shared.generated.resources.chip_finished
import dz.shared.generated.resources.chip_loved
import dz.shared.generated.resources.chip_to_read
import dz.shared.generated.resources.continue_btn
import dz.shared.generated.resources.my_shelf
import dz.shared.generated.resources.new_collection
import dz.shared.generated.resources.olive_again_book
import dz.shared.generated.resources.onb2_copy
import dz.shared.generated.resources.onb2_title
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

@Composable
fun OnboardingScreenTwo(
    onNextClick: () -> Unit = {},
    onSkipClick: () -> Unit = {}
) {
    OnboardingScaffold(
        title = stringResource(Res.string.onb2_title),
        copy = stringResource(Res.string.onb2_copy),
        pageIndex = 1,
        buttonText = stringResource(Res.string.continue_btn),
        onButtonClick = onNextClick,
        onSkipClick = onSkipClick,
        hero = { ShelfHero() }
    )
}

@Composable
private fun ShelfHero() {
    val colors = inkColors()

    Column(modifier = Modifier.width(250.dp)) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .inkCard(colors)
                .padding(16.dp)
        ) {
            InkLabel(text = stringResource(Res.string.my_shelf), colors = colors)

            Row(
                modifier = Modifier.padding(top = 12.dp),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                ShelfCover(Res.drawable.olive_again_book)
                ShelfCover(Res.drawable.book_cover_4)
                ShelfCover(Res.drawable.book_cover_2)
            }

            Spacer(modifier = Modifier.height(14.dp))
            HorizontalDivider(thickness = 1.dp, color = colors.line)

            Row(
                modifier = Modifier.padding(top = 12.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Icon(
                    imageVector = InkIcons.Plus,
                    contentDescription = null,
                    tint = colors.accent,
                    modifier = Modifier.size(15.dp)
                )
                Text(
                    text = stringResource(Res.string.new_collection),
                    fontFamily = inkBodyFontFamily(),
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 12.sp,
                    color = colors.accent
                )
            }
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 14.dp),
            horizontalArrangement = Arrangement.spacedBy(7.dp, Alignment.CenterHorizontally)
        ) {
            InkChip(text = stringResource(Res.string.chip_to_read), solid = true, colors = colors)
            InkChip(text = stringResource(Res.string.chip_finished), colors = colors)
            InkChip(text = stringResource(Res.string.chip_loved), colors = colors)
        }
    }
}

@Composable
private fun ShelfCover(cover: DrawableResource) {
    Image(
        painter = painterResource(cover),
        contentDescription = null,
        contentScale = ContentScale.Crop,
        modifier = Modifier
            .size(width = 62.dp, height = 92.dp)
            .shadow(6.dp, RoundedCornerShape(InkShape.cover), clip = true)
    )
}

@Preview(showBackground = true, widthDp = 375, heightDp = 820)
@Composable
fun OnboardingScreenTwpPreview() {
    OnboardingScreenTwo()
}
